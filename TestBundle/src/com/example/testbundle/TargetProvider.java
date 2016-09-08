package com.example.testbundle;

import android.annotation.SuppressLint;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

public class TargetProvider extends ContentProvider {

	@Override
	public int delete(Uri arg0, String arg1, String[] arg2) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getType(Uri arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri arg0, ContentValues arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Cursor query(Uri arg0, String[] arg1, String arg2, String[] arg3,
			String arg4) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(Uri arg0, ContentValues arg1, String arg2, String[] arg3) {
		// TODO Auto-generated method stub
		return 0;
	}

	@SuppressLint("NewApi")
	@Override
	public Bundle call(String method, String arg, Bundle extras) {
		Bundle bundle = extras;
		if(bundle != null){
			Bitmap result = bundle.getParcelable("bitmap");
			if(result != null){
				Log.e("test", "bitmap.with="+result.getWidth()+" ,height="+result.getHeight()+" ,allocateSize ="+result.getAllocationByteCount());		
//				mImag.setImageBitmap(bitmap.copy(Config.ARGB_8888, false));
			}
		}
		return super.call(method, arg, extras);
	}

}
