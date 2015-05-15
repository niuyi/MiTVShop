package com.xiaomi.mitv.shop.detail.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;

import org.apache.http.util.ByteArrayBuffer;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;

public class Utils {
	public static boolean isAssetBackgroundFile(AssetManager assetManager, String path) {
		if (path != null && path.endsWith("bg.png")) {
			return true;
		}
//		try {
//			String[] contents = assetManager.list(path);
//			if (contents != null && contents.length == 0) {
//				return true;
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		return false;	
	}
	
	public static Bitmap getCoverPosterBitmap(Bitmap bitmap) {
		  Matrix matrix = new Matrix(); 
		  matrix.postScale(0.5f,0.5f);
		  Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
//		  Log.e("!!!!!!!!!!!!!!!!!!!!: ", "width : " + resizeBmp.getWidth() + " height: " + resizeBmp.getHeight());
		  return resizeBmp;
	}
	
	public static Bitmap loadAssetBitmap(AssetManager assetManager, String path, int sampleSize){
		InputStream is = null;
		Bitmap bitmap = null;
		try {
			is = assetManager.open(path);
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = sampleSize;
			bitmap = BitmapFactory.decodeStream(is, null, options);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return bitmap;
	}
	
	public static Bitmap loadAssetBitmap(AssetManager assetManager, String path) {
		return loadAssetBitmap(assetManager, path, 1);
	}
	
	private static final String ENCODING = "UTF-8";
	
	private static final int BUFFER_SIZE = 1024;
	private static final byte[] BUFFER = new byte[BUFFER_SIZE];

	public static String inputStream2String(InputStream inputStream) {
		try {
			BufferedInputStream bis = new BufferedInputStream(inputStream);
			ByteArrayBuffer arrayBuffer = new ByteArrayBuffer(BUFFER_SIZE);
			int sizeRead = 0;
			while ((sizeRead = bis.read(BUFFER, 0, BUFFER_SIZE)) > 0) {
				arrayBuffer.append(BUFFER, 0, sizeRead);
			}
			return new String(arrayBuffer.toByteArray(), ENCODING);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void writeToFile(String data) {
	    try {
	    	File file = new File("/data/duokancache/poster.index");
	    	FileOutputStream fo = new FileOutputStream(file);
	        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fo);
	        outputStreamWriter.write(data);
	        outputStreamWriter.close();
	    }
	    catch (IOException e) {
	        Log.e("Exception", "File write failed: " + e.toString());
	    } 
	}
}
