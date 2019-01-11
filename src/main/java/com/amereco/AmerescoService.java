package com.amereco;

import com.amereco.json.EdgeFormData;
import com.amereco.json.FormData;
import com.amereco.json.NoteObject;
import com.amereco.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.logging.Logger;

public class AmerescoService extends ServerCommunication {
    private AmerescoDAO amerescoDAO;
    private Properties properties;
    private Gson gson;
    private Logger logger = Logger.getLogger(AmerescoService.class.getName());
    private JsonParser jsonParser;
    private String baseUrl;

    public AmerescoService() {
        gson = new Gson();
        jsonParser  = new JsonParser();
        properties = PropertiesReader.getProperties();
        baseUrl = properties.getProperty("com.edge.baseurl");
        amerescoDAO = new AmerescoDAO();
    }

    public void start(){
        logger.info("***** start *****");
        System.out.println("***** start *****");
        List<String> titles = amerescoDAO.getNoteWrongNoteTitles(59177263,59185826);
        logger.info("total note to be processed"+titles.size());
        System.out.println("total note to be processed"+titles.size());
        int processedCount =0;
        for(String title : titles){
            processNote(title,processedCount);
        }
        System.out.println(titles);
        System.out.println("***** end *****");
        logger.info("***** end *****");
    }
    protected List<EdgeFormData> getEdgeFormData(String formDefJson) {
        try {
            List<EdgeFormData> edgeFormDatas = gson.fromJson(formDefJson, new TypeToken<List<EdgeFormData>>() {
            }.getType());
            return edgeFormDatas;
        } catch (Exception e) {
            formDefJson = formDefJson.substring(1, formDefJson.length() - 1);
            List<EdgeFormData> edgeFormDatas = gson.fromJson(formDefJson, new TypeToken<List<EdgeFormData>>() {
            }.getType());
            return edgeFormDatas;
        }
    }

    private FormData getFormData(List<EdgeFormData> formDataList){
        FormData formData = new FormData();
        formData.setAction(Utils.getValue(formDataList,"Action"));
        formData.setPhysicalInstall(Utils.getValue(formDataList,"Physical Install"));
        formData.setReason(Utils.getValue(formDataList,"Reason"));
        formData.setNewQrCode(Utils.getValue(formDataList,"SELC QR Code"));
        formData.setNewScan(Utils.getValue(formDataList,"Luminaire Scan"));

        formData.setRemoveReason(Utils.getValue(formDataList,"Reason for removal"));

        formData.setReplaceNewQr(Utils.getValue(formDataList,"New SELC QR Code"));
        formData.setReplaceExistingQr(Utils.getValue(formDataList,"Existing SELC QR Code"));
        return formData;
    }

    private void updateForm(List<EdgeFormData> formDataList,FormData formData){
        String action = formData.getAction();
        Utils.updateValue(formDataList,"Action",action);
        if(action.equals("New Streetlight")){
            Utils.updateValue(formDataList,"Physical Install",formData.getPhysicalInstall());
            Utils.updateValue(formDataList,"Reason",formData.getReason());
            Utils.updateValue(formDataList,"SELC QR Code",formData.getNewQrCode());
            Utils.updateValue(formDataList,"Luminaire Scan", formData.getNewScan());
        }else if(action.equals("Update Streetlight")){
            Utils.updateValue(formDataList,"New SELC QR Code",formData.getReplaceNewQr());
            Utils.updateValue(formDataList,"Existing SELC QR Code ", formData.getReplaceExistingQr());
        }else if(action.equals("Remove Streetlight")){
            Utils.updateValue(formDataList,"Reason for removal",formData.getRemoveReason());
        }
    }

    private void processNote(String title,int processedCount){
        try {
            String oldNoteGuid = amerescoDAO.getNoteGuidByTitle(title, 59177263);
            String currentNoteGuid = amerescoDAO.getNoteGuidByTitle(title, 59185826);
            String oldNoteurl = baseUrl + "/rest/notes/" + oldNoteGuid;
            String newNoteurl = baseUrl + "/rest/notes/" + currentNoteGuid;
            ResponseEntity<String> oldNoteresponseEntity = call(oldNoteurl, null, HttpMethod.GET);
            ResponseEntity<String> newNoteresponseEntity = call(newNoteurl, null, HttpMethod.GET);
            NoteObject oldNoteObject = null;
            if (oldNoteresponseEntity.getStatusCode().is2xxSuccessful()) {
                oldNoteObject = processOldNote(oldNoteresponseEntity,oldNoteGuid);
            } else {
                logger.info("Error while fetching note from deleted notebook noteGuid is: " + oldNoteGuid);
            }

            if (newNoteresponseEntity.getStatusCode().is2xxSuccessful()) {
                if(oldNoteObject != null) {
                    processNewNote(newNoteresponseEntity, currentNoteGuid, oldNoteObject);
                }
            } else {
                logger.info("Error while fetching note from correct notebook noteGuid is: " + currentNoteGuid);
            }
            processedCount++;
            logger.info("note processsed" + processedCount);
            System.out.println("note processsed" + processedCount);
        }catch (Exception e){
            e.printStackTrace();
            logger.info("Error while processing note:"+title+" err: "+e.getMessage());
        }
    }

    private NoteObject processOldNote(ResponseEntity<String> oldNoteresponseEntity,String oldNoteGuid){
        NoteObject noteObject = new NoteObject();
        String body = oldNoteresponseEntity.getBody();
        JsonObject oldEdgeJsonObject = (JsonObject) jsonParser.parse(body);
        String notebookGuid = oldEdgeJsonObject.has("edgeNotebook") ? oldEdgeJsonObject.get("edgeNotebook").getAsJsonObject().get("notebookGuid").getAsString() : null;
        noteObject.setNotebookGuid(notebookGuid);
        noteObject.setNoteGuid(oldNoteGuid);
        String geometry = oldEdgeJsonObject.has("geometry") ? oldEdgeJsonObject.get("geometry").getAsString() : null;
        if(geometry != null){
            JsonArray serverEdgeFormJsonArray = oldEdgeJsonObject.get("formData").getAsJsonArray();
            int size = serverEdgeFormJsonArray.size();
            for (int i = 0; i < size; i++) {
                JsonObject serverEdgeForm = serverEdgeFormJsonArray.get(i).getAsJsonObject();
                String formTemplateGuid = serverEdgeForm.get("formTemplateGuid").getAsString();
                String formDefJson = serverEdgeForm.get("formDef").toString();
                formDefJson = formDefJson.replaceAll("\\\\", "");
                List<EdgeFormData> formDataList = getEdgeFormData(formDefJson);
                if (formTemplateGuid.equals(properties.getProperty("com.edge.oldTemplateGuid"))) {
                    FormData formData = getFormData(formDataList);
                    noteObject.setFormData(formData);
                }
                serverEdgeForm.add("formDef", gson.toJsonTree(formDataList));
                serverEdgeForm.addProperty("formGuid", UUID.randomUUID().toString());
            }
            oldEdgeJsonObject.add("formData", serverEdgeFormJsonArray);
            oldEdgeJsonObject.addProperty("isDeleted", true);
            oldEdgeJsonObject.addProperty("syncTime",System.currentTimeMillis());
            oldEdgeJsonObject.addProperty("createdDateTime", System.currentTimeMillis());
            oldEdgeJsonObject.addProperty("noteGuid", UUID.randomUUID().toString());
            noteObject.setNoteJsonObject(oldEdgeJsonObject);
        }else{
            logger.info("Geometry is empty for this note: " + oldNoteGuid);
        }
        return noteObject;
    }

    private void processNewNote(ResponseEntity<String> newNoteresponseEntity,String currentNoteGuid,NoteObject noteObject){
        FormData formData = noteObject.getFormData();
        JsonObject oldEdgeJsonObject = noteObject.getNoteJsonObject();
        String oldNotebookGuid = noteObject.getNotebookGuid();
        String oldNoteGuid = noteObject.getNoteGuid();
        String body = newNoteresponseEntity.getBody();
        JsonObject edgeJsonObject = (JsonObject) jsonParser.parse(body);
        String notebookGuid = edgeJsonObject.has("edgeNotebook") ? edgeJsonObject.get("edgeNotebook").getAsJsonObject().get("notebookGuid").getAsString() : null;
        String geometry = edgeJsonObject.has("geometry") ? edgeJsonObject.get("geometry").getAsString() : null;
        if (geometry != null){
            JsonArray serverEdgeFormJsonArray = edgeJsonObject.get("formData").getAsJsonArray();
            int size = serverEdgeFormJsonArray.size();
            for (int i = 0; i < size; i++) {
                JsonObject serverEdgeForm = serverEdgeFormJsonArray.get(i).getAsJsonObject();
                String formTemplateGuid = serverEdgeForm.get("formTemplateGuid").getAsString();
                String formDefJson = serverEdgeForm.get("formDef").toString();
                formDefJson = formDefJson.replaceAll("\\\\", "");
                List<EdgeFormData> formDataList = getEdgeFormData(formDefJson);
                if (formTemplateGuid.equals(properties.getProperty("com.edge.newTemplateGuid"))) {
                    if (formData != null) {
                        updateForm(formDataList, formData);
                    }
                }
                serverEdgeForm.add("formDef", gson.toJsonTree(formDataList));
                serverEdgeForm.addProperty("formGuid", UUID.randomUUID().toString());
            }
            if(formData != null) {
                edgeJsonObject.add("formData", serverEdgeFormJsonArray);
                edgeJsonObject.addProperty("createdBy", oldEdgeJsonObject.get("createdBy").getAsString());
                edgeJsonObject.addProperty("createdDateTime", oldEdgeJsonObject.get("createdDateTime").getAsString());
                edgeJsonObject.addProperty("noteGuid", UUID.randomUUID().toString());
                String updateNoteUrl = baseUrl + "/rest/notebooks/" + notebookGuid + "/notes/" + currentNoteGuid;
                ResponseEntity<String> responseEntity1 = call(updateNoteUrl, edgeJsonObject.toString(), HttpMethod.PUT);
                if (responseEntity1.getStatusCode().is2xxSuccessful()) {
                    logger.info("new note successfully updated: " + responseEntity1.getBody());

                    String oldNoteUpdateNoteUrl = baseUrl + "/rest/notebooks/" + oldNotebookGuid + "/notes/" + oldNoteGuid;
                    ResponseEntity<String> responseEntity2 = call(oldNoteUpdateNoteUrl, oldEdgeJsonObject.toString(), HttpMethod.PUT);
                    if (responseEntity2.getStatusCode().is2xxSuccessful()) {
                        logger.info("Old note successfully updated: " + responseEntity2.getBody());
                    } else {
                        logger.info("Old note failed to update: " + oldNoteGuid);
                    }

                } else {
                    logger.info("new note failed to update: " + responseEntity1.getBody());
                }
            }
        }else{
            logger.info("Geometry is empty for this note: " + currentNoteGuid);
        }
    }
}
