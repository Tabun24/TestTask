package com.example.testtask;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.view.MotionEvent;
import android.view.View;

public class ScreenView extends View {

	private Paint paint;
	private Bitmap b;
	
	private boolean drag = false;
	private float x;
	private float y;
	private float dragX;
	private float dragY;
	
	private float scaleX;
	private float scaleY;
	private int side = 150;
	
	public ScreenView(Context context) {
		super(context);
		paint = new Paint();
		paint.setColor(Color.YELLOW);
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(10);
	}

	
	@Override
	protected void onDraw(Canvas canvas) {
		b = MainActivity.screenBitmap;
		if(b !=null){
			scaleX = Float.valueOf(b.getWidth())/getMeasuredWidth();
			scaleY = Float.valueOf(b.getHeight())/getMeasuredHeight();
			b = Bitmap.createScaledBitmap(b, getMeasuredWidth(), getMeasuredHeight(), false);	
			canvas.drawBitmap(b, 0, 0, null);}
			canvas.drawRect(x, y, x+side, y+side, paint);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float evX = event.getX();
		float evY = event.getY();
	switch(event.getAction()){
	case MotionEvent.ACTION_DOWN:
		if(evX > x && evX < x+side && evY > y && evY < y+side){
			dragX = evX - x;
			dragY = evY - y;
			drag = true;
		}
		break;
	case MotionEvent.ACTION_MOVE:
		if(drag){
			x = evX-dragX <= 0 ? 0 :evX-dragX;
			x = (x+side)>= getMeasuredWidth() ? getMeasuredWidth()-side : x;
			y = evY-dragY <= 0 ? 0 :evY-dragY;
			y = (y+side)>= getMeasuredHeight() ? getMeasuredHeight()-side : y;
			invalidate();
		}
		break;
	case MotionEvent.ACTION_UP:
		drag = false;
		break;
	
	}
	return true;
	}
	
	public int get_x(){
		return (int) (x*scaleX);
	}
	
	public int get_y(){
		return (int) (y*scaleY);
	}
	
	public int get_side(){
		return (int) (side*(scaleY>scaleX?scaleX:scaleY));
	}
	
}
