package com.amereco.json;

import com.google.gson.JsonObject;

public class NoteObject {
    private FormData formData;
    private String notebookGuid;
    private String noteGuid;
    private JsonObject noteJsonObject;

    public FormData getFormData() {
        return formData;
    }

    public void setFormData(FormData formData) {
        this.formData = formData;
    }

    public String getNotebookGuid() {
        return notebookGuid;
    }

    public void setNotebookGuid(String notebookGuid) {
        this.notebookGuid = notebookGuid;
    }

    public String getNoteGuid() {
        return noteGuid;
    }

    public void setNoteGuid(String noteGuid) {
        this.noteGuid = noteGuid;
    }

    public JsonObject getNoteJsonObject() {
        return noteJsonObject;
    }

    public void setNoteJsonObject(JsonObject noteJsonObject) {
        this.noteJsonObject = noteJsonObject;
    }

    @Override
    public String toString() {
        return "NoteObject{" +
                "formData=" + formData +
                ", notebookGuid='" + notebookGuid + '\'' +
                ", noteGuid='" + noteGuid + '\'' +
                ", noteJsonObject=" + noteJsonObject +
                '}';
    }
}
