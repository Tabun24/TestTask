package com.example.testtask;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

public class Utils {

	SimpleDateFormat formatDate = new SimpleDateFormat("dd-MM-yyyy");
	SimpleDateFormat formatTime = new SimpleDateFormat("HH.mm.ss");
	Date date = new Date();
	
	public  String getJSON(Bitmap bitmap){
		String message;
		JSONObject json = new JSONObject();
		try {
			json.put("image", BitmapToString(bitmap));
			//json.put("image", "image");
			json.put("date",formatDate.format(date));
			json.put("time",formatTime.format(date));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		message = json.toString();
		return message;
	}
	
	public String BitmapToString (Bitmap bitmap){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
		byte [] b = baos.toByteArray();
		String s = Base64.encodeToString(b,Base64.DEFAULT);
		return s;
	}
	
	public Bitmap StringToBitMap(String encodedString){
	try{
		byte[]  b = Base64.decode(encodedString, Base64.DEFAULT);
		Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
		return bitmap;
	} catch(Exception e){
		return null;
	}
	}
}
