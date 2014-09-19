package com.example.testtask;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.Bitmap;
import android.os.AsyncTask;

public class HttpConection {
	
	private String address= "http://abc.qqq.ee/rr";
	
	private  String POST(String url, String json){
		InputStream inputSream = null;
		String result ="";
		try{
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);
			StringEntity se = new StringEntity(json);
			httpPost.setEntity(se);
			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Content-type", "application/json");
			HttpResponse httpResponse = httpClient.execute(httpPost);
			inputSream = httpResponse.getEntity().getContent();
			if(inputSream != null){
				BufferedReader bufferdReader = new BufferedReader(new InputStreamReader(inputSream) );
				String line = "";
				while((line = bufferdReader.readLine()) != null)
					result += line;
				inputSream.close();
			}
		} catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
	
	public void SendImage(Bitmap bitmap){
		HttpAsyncTask task = new HttpAsyncTask();
		Utils utils = new Utils();
		task.execute(utils.getJSON(bitmap));
	}
	
	
	public class HttpAsyncTask extends AsyncTask<String, Void, String>{

		@Override
		protected String doInBackground(String... params) {
			return POST(address,params[0]);
		}
		
		@Override
		protected void onPostExecute(String result) {
			//Here we can do something with result
			}
	}
}
