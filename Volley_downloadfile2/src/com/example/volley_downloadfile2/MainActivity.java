package com.example.volley_downloadfile2;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.FiledownloadListener;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.FileDownloadRequest;
import com.android.volley.toolbox.Volley;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.volley_downloadfile2.R;

public class MainActivity extends Activity {
	private TextView mTips;
	private Button mStartBtn;
	private Button mPauseBtn;
	private Button mFreshBtn;
	private RequestQueue mQueue;
	private FileDownloadRequest mFileRequest;
	private ProgressBar mProcessBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);
		mQueue = new Volley().newRequestQueue(getApplicationContext());
		mTips = (TextView) findViewById(R.id.tips);
		mProcessBar = (ProgressBar) findViewById(R.id.bar);
		initialRequest();
		initialPauseBtn();
		initialStartBtn();
		initialFreshBtn();
	}

	private void initialFreshBtn() {
		mFreshBtn = (Button) findViewById(R.id.fresh_btn);
		mFreshBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				mFileRequest.setDownloadedSize(0);
				mFileRequest.setTotalSize(0);
				mProcessBar.setProgress(0);
				mQueue.cancelAll(MainActivity.this);
				FileDownloadRequest.deleteFile(FileDownloadRequest.sFilePath);
				
			}
		});
		
	}

	public void initialRequest(){
		String url = "http://raf-admin.nubia.cn/apps/apks/703c4bdd-4eb9-43be-b79c-0b428ffc6401.apk";
		FiledownloadListener processListener = new FiledownloadListener() {

			@Override
			public void onProcess(long fileSize, long downloadSize) {
				float process = (float)downloadSize / (float)fileSize;
				mFileRequest.setDownloadedSize(downloadSize);
				mFileRequest.setTotalSize(fileSize);
				Log.e("test", "onProcess, process ="+process);
				int progress =0;
				if(0<=process && process <=1){
					progress = (int) (process * 100);
				} else{
					progress = 100;
				}
				mProcessBar.setProgress(progress);
			}

			@Override
			public void onStart() {
				Log.e("test", "onStart");
				mProcessBar.setProgress(0);
			}

			@Override
			public void onComplete() {
				Log.e("test", "onComplete");
				mProcessBar.setProgress(100);
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
	    mFileRequest = new FileDownloadRequest(Method.GET, 
				url, processListener, listener, errorListener);
	    mFileRequest.setShouldCache(false);
	    mFileRequest.setTag(MainActivity.this);
	}
	public void initialStartBtn(){
		mStartBtn = (Button) findViewById(R.id.start_btn);
		mStartBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				FileDownloadRequest request = new FileDownloadRequest(mFileRequest);
				request.setTag(MainActivity.this);
				mQueue.add(request);
				
			}
		});
	}
	public void initialPauseBtn(){
		mPauseBtn = (Button) findViewById(R.id.pause_btn);
		mPauseBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				mQueue.cancelAll(MainActivity.this);
			}
		});
	}
	
	
	@Override
	protected void onResume() {
		super.onResume();
	}

	

}
