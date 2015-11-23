package com.medsave.test;

import java.io.File;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;


public class TestPost {

	final static String URL = "http://www.medasave.com.br/api/image/new";
	final static String URL_DEV = "http://localhost:8080/api/image/new";
	final static String fileName = "C:\\Users\\Adolfo\\Pictures\\raiox.jpg"; // <<<<  Diretório da imagem
	final static String USER_TOKEN = "b7c81e02-9b0c-4a6e-b4ef-2417ed8d0657";  // <<<< Token do usuário de testes adolfobrunno
	
	
	public static void main(String[] args) throws IllegalStateException, IOException {

		DefaultHttpClient httpclient = new DefaultHttpClient();
        	HttpPost httpPost = new HttpPost(URL);

        	String boundary = "-------------" + System.currentTimeMillis();
	
		// Editar as propriedades da imagem:     <<<<<<<<<<	
        	String jsonBody = "{ \"modality\": \"CT\", "+
			 	"\"iid\" : \"32.3242.1231584.1035\","+
			 	"\"pid\" : \"2.5496.1231584.0003\","+
			 	"\"sid\" : \"3.5496.1231584.0002\","+
			 	"\"sno\" : \"4.8391.0392340.0002\","+
			 	"\"creation_date\" : \"20150827\","+
			 	"\"size\" : \"950032\","+
				"\"cid\" : \"001\","+
			  	"\"user_token\": \""+USER_TOKEN+"\"}";
		
		StringBody json = new StringBody(jsonBody, ContentType.APPLICATION_JSON);
		FileBody file = new FileBody(new File(fileName));
		
		HttpEntity entity = MultipartEntityBuilder.create()
                .setBoundary(boundary)
                .addPart("file", file)
                .addPart("properties", json)
                .build();
		
		httpPost.setEntity(entity);
		
		HttpResponse response = httpclient.execute(httpPost);
		
		System.out.println(response.getStatusLine());

	}
	

	
}
