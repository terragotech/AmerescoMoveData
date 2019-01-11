package com.amereco;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;



public class ServerCommunication {
    private Logger logger = Logger.getLogger(ServerCommunication.class);
    protected ResponseEntity<String> call(String url, String body, HttpMethod httpMethod){
        logger.info("Request Url : "+url);
        logger.info("Request Data : "+body);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = getHeaders();

        HttpEntity request = null;
        if(body != null){
            headers.add("Content-Type", "application/json");
            request = new HttpEntity<String>(body, headers);
        }else{
            request = new HttpEntity<String>(headers);
        }

        ResponseEntity<String> responseEntity = restTemplate.exchange(url, httpMethod, request, String.class);
        logger.info("------------ Response ------------------");

        logger.info("Response Code:" + responseEntity.getStatusCode().toString());
        /*if( responseEntity.getBody() != null){
//            logger.info("Response Data:" + responseEntity.getBody());
        }*/
        return responseEntity;
    }
    private HttpHeaders getHeaders() {
        String  userName = PropertiesReader.getProperties().getProperty("com.edge.username");
        String  password = PropertiesReader.getProperties().getProperty("com.edge.password");
        HttpHeaders headers = new HttpHeaders();

        String plainCreds = userName + ":" + password;

        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);
        System.out.println(base64CredsBytes);

        headers.add("Authorization", "Basic " + base64Creds);
        return headers;
    }
}
