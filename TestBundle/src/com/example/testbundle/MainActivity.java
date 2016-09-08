package com.example.testbundle;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.Surface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

	private Button mBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mBtn = (Button) findViewById(R.id.jump);
		mBtn.setOnClickListener(new OnClickListener() {
			
			@SuppressLint("NewApi")
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), TargetActivity.class);
				Bundle bundle = new Bundle();
				
//				Bitmap bitmap = Bitmap.createBitmap(1024, 1024, Config.ARGB_8888);
				
//				BitmapDrawable drawable = (BitmapDrawable)getResources().getDrawable(R.drawable.bundle_test);
//				Bitmap bitmap = drawable.getBitmap();
				
				Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bundle_test);
				Bitmap result = reflectCreateAshmemBitmap(bitmap);
				
//				Bitmap result = reflectScreenShort();
				
//				Bitmap result = GetandSaveCurrentImage();
				
				Log.e("test", "bitmap.with="+result.getWidth()+" ,height="+result.getHeight()+" ,allocateSize ="+result.getAllocationByteCount());		
				bundle.putParcelable("bitmap", result);
				boolean hasFd = bundle.hasFileDescriptors();
				int contents = bundle.describeContents();
				Log.e("test", "hasFd="+hasFd+" ,contents="+contents);
//				intent.putExtras(bundle);
				callProvider(bundle);
				startActivity(intent);
				
			}
		});
	}
	
	@SuppressLint("NewApi")
	public void callProvider(Bundle bundle){
//		getContentResolver().call(Uri.parse("content://com.examp.testbundle.testcall"), 
//				"bitmap", "bitmap", bundle);
		getContentResolver().call(Uri.parse("content://cn.nubia.launcher.settings"), 
				"launcherFit", "initBitmap", bundle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	
	@SuppressLint("NewApi")
	public static Bitmap reflectScreenShot(Context context){
		Bitmap result = null;

		float dimes[] =getDimens(context);
		try {
			Class<?> surfaceControl = Class.forName("android.view.SurfaceControl");
			Method screenShot = surfaceControl.getMethod("screenshot", int.class, int.class);
			result = (Bitmap) screenShot.invoke(surfaceControl, dimes[0], dimes[1]);
 		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return result;
	}
	
//	public static Bitmap screenShot(Context context){
//		float dimens[] =getDimens(context);
//		
////		SystemAccessManager sam = context.getSystemService(ExtraContext.)
//	}
	
	@SuppressLint("NewApi")
	public static float[] getDimens(Context context){
		DisplayMetrics dm = new DisplayMetrics();
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		display.getRealMetrics(dm);
		float dimes[] = {dm.widthPixels ,dm.heightPixels};
		float degress = getDegressForRotation(display.getRotation());
		boolean requireRotation = (degress > 0);
		if(requireRotation){
			Matrix mx = new Matrix();
			mx.reset();
			mx.setRotate(-degress);
			mx.mapPoints(dimes);
			dimes[0] = Math.abs(dimes[0]);
			dimes[1] = Math.abs(dimes[1]);
		}
		
		return dimes;
	}
	
	public static float getDegressForRotation(int roatation){
		switch (roatation) {
		case Surface.ROTATION_90:
			return 360f - 90f;
		case Surface.ROTATION_180:
			return 360f - 180f;
		case Surface.ROTATION_270:
			return 360f - 270f;
		}
		
		return 0f;
	}
	
	public static Bitmap reflectCreateAshmemBitmap(Bitmap bitmap){
		Bitmap result = null;
		
		Class<?> bitmapClass = Bitmap.class;
		try {
			Method createAshem = bitmapClass.getMethod("createAshmemBitmap");
			createAshem.setAccessible(true);
			result = (Bitmap) createAshem.invoke(bitmap);
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
	
    private Bitmap GetandSaveCurrentImage()
    {
    	//1.构建Bitmap
    	WindowManager windowManager = getWindowManager();
    	Display display = windowManager.getDefaultDisplay();
    	int w = display.getWidth();
    	int h = display.getHeight();
    	
    	Bitmap Bmp = Bitmap.createBitmap( w, h, Config.ARGB_8888 );    
    	
    	//2.获取屏幕
    	View decorview = this.getWindow().getDecorView(); 
    	decorview.setDrawingCacheEnabled(true); 
    	Bmp = decorview.getDrawingCache(); 

    	//3.保存Bitmap 
    	try {
    		String SavePATH = Environment.getExternalStorageDirectory().getPath()+"/"+"myBitmap";
			File path = new File(SavePATH);
    		//文件
    	    String filepath = SavePATH + "/Screen_1.png";
    		File file = new File(filepath);
    		if(!path.exists()){
    			path.mkdirs();
    		}
    		if (!file.exists()) {
    			file.createNewFile();
    		}
    		
    		FileOutputStream fos = null;
    		fos = new FileOutputStream(file);
    		if (null != fos) {
    			Bmp.compress(Bitmap.CompressFormat.PNG, 90, fos);
        		fos.flush();
        		fos.close();  
        		
            	Toast.makeText(getApplicationContext(), "截屏文件已保存至SDCard/ADASiteMaps/ScreenImage/下", Toast.LENGTH_LONG).show();
    		}

    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	
    	return Bmp;
    }
}
