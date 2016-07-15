package com.android.volley.toolbox;

import java.io.UnsupportedEncodingException;

import org.json.JSONArray;
import org.json.JSONException;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.Response.ProcessListener;

public class FileDownloadRequest extends Request<Object> {
	
	private ProcessListener<Float> mProcessListener;
	private Listener<Object> mListener;
	private long mTotalFileSize;
	private long mDownloadSize;

	public FileDownloadRequest(String url, ErrorListener listener) {
		super(url, listener);
		// TODO Auto-generated constructor stub
	}

	public FileDownloadRequest(int method, String url, ErrorListener listener) {
		super(method, url, listener);
		// TODO Auto-generated constructor stub
	}
	
	public FileDownloadRequest(int method, String url, ProcessListener<Float> processListener,
			Listener<Object> listener, ErrorListener errorListener){
		super(method, url, errorListener);
		mProcessListener = processListener;
		mListener = listener;
		
	}

	@Override
	protected Response<Object> parseNetworkResponse(NetworkResponse response) {
		return Response.success(new Object(),
		        HttpHeaderParser.parseCacheHeaders(response));
	}

	@Override
	protected void deliverResponse(Object response) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	@Override
	public void deliverProcess(long fileSize, long downloadSize) {
		mTotalFileSize = fileSize;
		mDownloadSize = downloadSize;
		float process = (float)downloadSize / (float)fileSize;
		mProcessListener.onProcess(process);
	}

	@Override
	public long getDownloadedSize() {
		// TODO Auto-generated method stub
		return mDownloadSize;
	}
}
