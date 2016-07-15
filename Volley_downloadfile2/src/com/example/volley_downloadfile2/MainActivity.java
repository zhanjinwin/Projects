package com.example.volley_downloadfile2;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.Response.ProcessListener;
import com.android.volley.toolbox.FileDownloadRequest;
import com.android.volley.toolbox.Volley;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.example.volley_downloadfile2.R;

public class MainActivity extends Activity {
	private TextView mTips;
	private Button mStartBtn;
	private RequestQueue mQueue;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);
		mQueue = new Volley().newRequestQueue(getApplicationContext());
		mTips = (TextView) findViewById(R.id.tips);
		mStartBtn = (Button) findViewById(R.id.start_btn);
		mStartBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String url = "http://raf-admin.nubia.cn/apps/apks/703c4bdd-4eb9-43be-b79c-0b428ffc6401.apk";
				ProcessListener<Float> processListener = new ProcessListener<Float>() {

					@Override
					public void onProcess(Float response) {
						Log.e("test", "onProcess, process ="+response);
//						mTips.setText(Float.toString(response));
					}
				};
				Listener<Object> listener = new Listener<Object>() {

					@Override
					public void onResponse(Object response) {
						Log.e("test", "onResponse,response="+response);
						
					}
				};
				ErrorListener errorListener = new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e("test","onErrorResponse,error="+error);
						
					}
				};
				FileDownloadRequest fileRequest = new FileDownloadRequest(Method.GET, 
						url, processListener, listener, errorListener);
				mQueue.add(fileRequest);
				
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	

}
