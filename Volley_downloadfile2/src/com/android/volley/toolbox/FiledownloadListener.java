package com.android.volley.toolbox;

public interface FiledownloadListener {
	
	public void onStart();
	
	public void onProcess(long fileSize, long downloadSize);
	
	public void onComplete();
}
