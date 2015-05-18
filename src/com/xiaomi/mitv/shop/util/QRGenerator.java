/**
 * @author Liyc
 * 
 * uses zxing 2.1 (http://code.google.com/p/zxing/) 
 */
package com.xiaomi.mitv.shop.util;

import java.io.FileOutputStream;
import java.util.Hashtable;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

public class QRGenerator {
	private static final String TAG = "QRGenerator";
	private static final int WHITE = 0xFFFFFFFF;
	private static final int BLACK = 0xFF000000;
	
	public static Bitmap create2DCode(String content, int dimension) {
//		Log.v(TAG, "create2DCode enter, content = " + content + ", dimension = " + dimension);
		
		Hashtable<EncodeHintType, Integer> hints = new Hashtable<EncodeHintType, Integer>();
		hints.put(EncodeHintType.MARGIN, 1);
		
		//
		// generate pixels
		//
	    MultiFormatWriter writer = new MultiFormatWriter();
	    BitMatrix result;
	    try {
			result = writer.encode(content, BarcodeFormat.QR_CODE, dimension, dimension, hints);
		} catch (Exception e) {
			Log.e(TAG, "error to encode: ", e);
	    	return null;
	    }
	    
	    //
	    // create bitmap
	    //
	    int width = result.getWidth();
	    int height = result.getHeight();
	    int[] pixels = new int[width * height];
	    for (int y = 0; y < height; y++) {
	    	int offset = y * width;
	    	for (int x = 0; x < width; x++) {
	    		pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
	    	}
	    }

	    Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
	    bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
	    
	    return bitmap;
	}
}
