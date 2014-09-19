package com.example.testtask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.testtask.HttpConection.HttpAsyncTask;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

	private int REQUEST_CODE_PHOTO = 1;
	private File directory;
	private Uri currentUri;
	private ScreenView screenView;
	public static Bitmap screenBitmap;
	private Bitmap newBitmap;
	private SimpleDateFormat format;
	private int i=1;
	
	private String TAG_LOG = "myLogs";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		screenView = new ScreenView(this);
		setContentView(screenView);
		createDirectory();
		screenBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.background);
		format = new SimpleDateFormat("dd-MM-yyyy HH.mm.ss");
		
		Utils utils = new Utils();
		utils.getJSON(screenBitmap);
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == REQUEST_CODE_PHOTO){
			if(resultCode == RESULT_OK){
			Matrix mat = new Matrix();
			mat.postRotate(90);
			Bitmap b= getBitmap();
			Bitmap bMapRotate = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), mat, false);	
			screenBitmap = bMapRotate;
			
			}
		}
	}
	
	private void createDirectory() {
		directory  = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"myPicture");
		if(!directory.exists())
			directory.mkdirs();
	}
	
	private Uri getUri(){
		File file = new File(directory.getPath()+"/"+format.format(new Date())+".jpg");
		currentUri = Uri.fromFile(file);
		return currentUri;
	}
	
	private Bitmap getBitmap(){
		Bitmap bitmap = null;
		try {
		bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), currentUri);
		} catch (IOException e) {
			e.printStackTrace();
			Log.d(TAG_LOG,"file is not found");
		}
		return bitmap;
	}
	
	private void cutNewImage(Bitmap bitmap){
		
		newBitmap=null;
		try{
		newBitmap = Bitmap.createBitmap(bitmap,screenView.get_x() , screenView.get_y(), screenView.get_side(),screenView.get_side());
			} catch(Exception e){
				Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
				return;
			}
		File file = new File(directory.getPath()+"/"+i+".jpg");
		if(file.exists())
			file.delete();
		i +=1;
		OutputStream fOut= null; 
		try {
			fOut = new FileOutputStream(file);
			newBitmap = addBorder(newBitmap, 15);
			newBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
			fOut.flush();
			fOut.close();
			Toast.makeText(this, getResources().getString(R.string.save_message)+"("+i+".jpg"+")",Toast.LENGTH_SHORT).show();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private Bitmap addBorder(Bitmap bmp, int borderSize){
		Bitmap bmpWithBorder = Bitmap.createBitmap(bmp.getWidth() + borderSize*2
				, bmp.getHeight() + borderSize*2, bmp.getConfig());
		Canvas canvas = new Canvas(bmpWithBorder);
		canvas.drawColor(Color.YELLOW);
		canvas.drawBitmap(bmp, borderSize, borderSize, null);
		return bmpWithBorder;
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.action_menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.GetImage:
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, getUri());
			startActivityForResult(intent, REQUEST_CODE_PHOTO);
		return true;
		case R.id.CutImage:
			cutNewImage(screenBitmap);
		return true;
		case R.id.Post:
			if(isConnected()){
			if(newBitmap != null){
			HttpConection c = new HttpConection();
			c.SendImage(newBitmap);
			}
			} else{
				Toast.makeText(this, "No Internet access", Toast.LENGTH_SHORT).show();
			}
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private boolean isConnected(){
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo  info = cm.getActiveNetworkInfo();
		if(info != null && info.isConnected())
			return true;
		else 
			return false;
	}
	
}



	