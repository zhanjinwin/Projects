package com.android.volley.toolbox;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;

import android.os.Environment;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyLog;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.FiledownloadListener;

public class FileDownloadRequest extends Request<Object> {
	
	private FiledownloadListener mFiledownloadListener;
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
	
	public FileDownloadRequest(int method, String url, FiledownloadListener processListener,
			Listener<Object> listener, ErrorListener errorListener){
		super(method, url, errorListener);
		mFiledownloadListener = processListener;
		mListener = listener;
	}
	
	public FileDownloadRequest(FileDownloadRequest request){
		super(request.getMethod(), request.getUrl(), request.getErrorListener());
		mFiledownloadListener = request.mFiledownloadListener;
		mListener = request.mListener;
		mTotalFileSize = request.mTotalFileSize;
		mDownloadSize = request.mDownloadSize;
		Log.e("test", "FileDownloadRequest,mDownloadSize="+mDownloadSize);
	}

	@Override
	protected Response<Object> parseNetworkResponse(NetworkResponse response) {
		Log.e("test", "before parseNetworkResponse");
		String filePath = Environment.getExternalStorageDirectory().getPath()
				+File.separator+"VolleyDownloadFile";
//		String fileName = "test";
//		byte[] buf = response.data;
//		VolleyUtils.byte2File(buf , filePath, fileName);
		Log.e("test", "after parseNetworkResponse");
		return Response.success(new Object(),null);
	}

	@Override
	protected void deliverResponse(Object response) {
		mListener.onResponse(response);
		
	}
	
	public void deliverProcess(long downloadSize) {
		mDownloadSize = downloadSize;
		mFiledownloadListener.onProcess(mTotalFileSize, downloadSize);
	}

	public long getDownloadedSize() {
		// TODO Auto-generated method stub
		return mDownloadSize;
	}
	
	public void setDownloadedSize(long downloadedSize){
		mDownloadSize = downloadedSize;
	}
	
	public void setTotalSize(long totalSize){
		mTotalFileSize = totalSize;
	}
	
	public byte[] handleResponseDataBySkip(HttpResponse response) throws ServerError, IOException {
		Log.e("test", "handleResponseData,entity="+response.getEntity());
		if (response.getEntity() != null) {
			HttpEntity entity = response.getEntity();
			ByteArrayPool pool = new ByteArrayPool(4096);
			PoolingByteArrayOutputStream bytes =
		            new PoolingByteArrayOutputStream(pool, (int) entity.getContentLength());
		    byte[] buffer = null;
		    try {
		        InputStream in = entity.getContent();
		        if (in == null) {
		            throw new ServerError();
		        }
		        buffer = pool.getBuf(1024);
		        int count;
//		        if(isContinueDownload()){
		        	long download = getDownloadedSize();
//		        	long at = download;
//		        	while (at >0) {
//						long amt = in.skip(at);
//						Log.e("test", "handleResponseData,real skiped ="+amt+" ,true skip="+at);
//						if(amt == -1){
//							Log.e("test", "skip input error");
//						}
//						at -=amt;
//					}
		        	Log.e("test", "inputsream.size="+entity.getContentLength());
		        	long skiped = in.skip(download); //skip maybe unsafe,the result may not equal demand
		        	Log.e("test", "handleResponseData,skiped ="+skiped+" ,downloaded="+download);
//		        }
		        long lastTimeSize = 0;
		        while (!isCanceled() && (count = in.read(buffer)) != -1) {
		            bytes.write(buffer, 0, count);
		            setDownloadedSize(getDownloadedSize() + count);
		            if(getDownloadedSize() - lastTimeSize >= 0.01*entity.getContentLength()){
		            	lastTimeSize =getDownloadedSize();
//		            	deliverProcess(entity.getContentLength(), getDownloadedSize() + count);
		            }
		        }
		        byte[] array = bytes.toByteArray();
		        bytes.flush();
		        return array;
		    } finally {
		        try {
		            // Close the InputStream and release the resources by "consuming the content".
		            entity.consumeContent();
		        } catch (IOException e) {
		            // This can happen if there was an exception above that left the entity in
		            // an invalid state.
		            VolleyLog.v("Error occured when calling consumingContent");
		        }
		        pool.returnBuf(buffer);
		        bytes.close();
		    }
		} else {
			return new byte[0];
		}
    }

    private boolean isContinueDownload(File file){
    	File parent = file.getParentFile();
    	Log.e("test", "isContinueDownload,parentFile isDirectory ="+parent.isDirectory()+" ,isExists="+parent.exists());
        if (!file.getParentFile().exists()) {  
            file.getParentFile().mkdirs();       
        } 
//    	Log.e("test", "isContinueDownload,fileSize="+file.length()+" ,downloadedSize="+mDownloadSize);
    	return  file.exists();
    }
    
    public static String sFilePath = Environment.getExternalStorageDirectory().getPath()
			+File.separator+"VolleyDownloadFile"
			+File.separator+ "test"+".apk";
    
	public byte[] handleResponseData(HttpResponse response) throws ServerError, IOException {
		Log.e("test", "handleResponseData,entity="+response.getEntity());
		if (response.getEntity() != null) {
			HttpEntity entity = response.getEntity();
			
			FileOutputStream fos ;
			File file = new File(sFilePath);
			if(isContinueDownload(file)){
				fos = new FileOutputStream(file,true); 
			} else{
				setTotalSize(entity.getContentLength());
				fos = new FileOutputStream(file);
			}
			
			mFiledownloadListener.onStart();
		    byte[] buffer = new byte[1024*100];
		    try {
		        InputStream in = entity.getContent();
		        if (in == null) {
		            throw new ServerError();
		        }
		        int count;
		        Log.e("test", "inputsream.size="+entity.getContentLength());
		        long lastTimeSize = 0;
		        while (!isCanceled() && (count = in.read(buffer)) != -1) {
		            fos.write(buffer, 0, count);
		            setDownloadedSize(getDownloadedSize() + count);
		            if(getDownloadedSize() - lastTimeSize >= 0.01 * mTotalFileSize){
		            	lastTimeSize =getDownloadedSize();
		            	deliverProcess(getDownloadedSize() + count);
		            }
		        }
		        if(getDownloadedSize() == mTotalFileSize){
		        	mFiledownloadListener.onComplete();
		        } 
		        return null;
		    } finally {
		        try {
		            // Close the InputStream and release the resources by "consuming the content".
		            entity.consumeContent();
		        } catch (IOException e) {
		            // This can happen if there was an exception above that left the entity in
		            // an invalid state.
		            VolleyLog.v("Error occured when calling consumingContent");
		        }
		    }
		} else {
			return new byte[0];
		}
    }

	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		File file = new File(sFilePath);
        if (!file.getParentFile().exists()) {  
            file.getParentFile().mkdirs();       
        } 
		long fileSize =0;
		if(file.exists()){
			fileSize = file.length();
		}
		Log.e("test", "getHeaders,fileSize="+fileSize+" ,downloadedSize="+getDownloadedSize());
		if(fileSize != 0){
			Map<String, String> map = new HashMap<String, String>();
			map.put("RANGE", "bytes="+fileSize+"-");
			return map;
		} else{
			return Collections.emptyMap();
		}
	}
	
	public static void deleteFile(String path){
		File file = new File(path);
		file.delete();
	}
}
