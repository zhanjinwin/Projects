package com.example.testforcetouchpreviewwidget;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class FroceTouchTestService extends Service {

	private static final String TAG = "FroceTouchTestService";

	@Override
	public IBinder onBind(Intent intent) {
		Log.e(TAG, "onBind");
		if(intent.getData()!= null){
			Toast.makeText(getApplicationContext(), intent.getDataString(), 1000).show();;
		}
		return null;
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		Log.e(TAG, "onStart");
		if(intent.getData()!= null){
			Toast.makeText(getApplicationContext(), intent.getDataString(), 1000).show();;
		}
	}

}
