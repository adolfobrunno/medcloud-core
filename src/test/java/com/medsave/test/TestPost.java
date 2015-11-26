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

// Token DEV: 98c2f9c9-69bf-4c1b-95cb-895e2c453124


public class TestPost {

	final static String URL = "http://159.203.70.250/api/image/new";
	final static String URL_DEV = "http://localhost:8080/api/image/new";
//	final static String fileName = "C:\\Users\\Adolfo\\Pictures\\raiox.jpg"; // <<<<  Diretório da imagem
	final static String USER_TOKEN = "98c2f9c9-69bf-4c1b-95cb-895e2c453124";
	final static String USER_TOKEN_DEV = "b66eadee-fe7f-462d-8444-c160dd738ef9";
	final static File directory = new File("/home/adolfo/Dev/W0001/W0001/1.2.826.0.1.3680043.2.656.1.136/S02A01");
	static int count = 1;
	static int total = directory.listFiles().length;
	static long totalTime = 0;
	
	public static void doPost(final File file){
		
		final DefaultHttpClient httpclient = new DefaultHttpClient();
		final HttpPost httpPost = new HttpPost(URL_DEV);
		long initMillis;
		
		initMillis = System.currentTimeMillis();
		String boundary = "-------------" + initMillis;
    	String pid = "W0001";
    	String sid = "1.2.826.0.1.3680043.2.656.1.136";
    	String sno = "S02A01";
    	String iid = file.getName().substring(0, file.getName().lastIndexOf('.'));
		String jsonBody = "{ \"modality\": \"CT\", "+
    			"\"iid\" : \""+iid+"\","+
    			"\"pid\" : \""+pid+"\","+
    			"\"sid\" : \""+sid+"\","+
    			"\"sno\" : \""+sno+"\","+
    			"\"creation_date\" : \"20151124\","+
    			"\"size\" : \""+file.length()+"\","+
    			"\"cid\" : \"001\","+
    			"\"user_token\": \""+USER_TOKEN_DEV+"\"}";
		
		System.out.println(jsonBody);
		StringBody json = new StringBody(jsonBody, ContentType.APPLICATION_JSON);
		FileBody fileBody = new FileBody(file);
		HttpEntity entity = MultipartEntityBuilder.create()
				.setBoundary(boundary)
				.addPart("file", fileBody)
				.addPart("properties", json)
				.build();
		
		httpPost.setEntity(entity);
		HttpResponse response;
		try {
			response = httpclient.execute(httpPost);
			System.out.println(response.getStatusLine());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			long elapsedTime = System.currentTimeMillis() - initMillis;
			TestPost.totalTime += elapsedTime;
			System.out.println("Feito " + TestPost.count + " de " + TestPost.total + ". Tempo: " + elapsedTime);
			System.out.println("Tempo Total: " + TestPost.totalTime);
			TestPost.count++;
			httpclient.close();
		}
			
		
	}

	public static void main(String[] args) throws IllegalStateException, IOException {
		
		final File[] files = directory.listFiles();
		final int total = TestPost.total;
		
		new Thread(){
			@Override
			public void run() {
				for (int j = 0; j < total/2; j++){
					TestPost.doPost(files[j]);
				}
			};
		}.start();
		new Thread(){
			@Override
			public void run() {
				for (int j = total/2; j < total; j++){
					TestPost.doPost(files[j]);
				}
			};
		}.start();
	
	}
	
}
