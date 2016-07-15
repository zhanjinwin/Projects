package com.android.volley.toolbox;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;

import com.android.volley.ServerError;
import com.android.volley.VolleyLog;

public class VolleyUtils {

	public static byte[] entityToBytes(HttpEntity entity) throws IOException, ServerError {
		ByteArrayPool pool = new ByteArrayPool(1024 * 10);
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
	        while ((count = in.read(buffer)) != -1) {
	            bytes.write(buffer, 0, count);
	        }
	        return bytes.toByteArray();
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
	}
}
