package com.example.testbundle;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.widget.ImageView;

public class TargetActivity extends Activity {
	private ImageView mImag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.target_activity_main);
		mImag = (ImageView) findViewById(R.id.result);
		
		Bundle bundle = getIntent().getExtras();
		if(bundle != null){
			Bitmap bitmap = bundle.getParcelable("bitmap");
			if(bitmap != null){
				mImag.setImageBitmap(bitmap.copy(Config.ARGB_8888, false));
			}
		}
	}

}
