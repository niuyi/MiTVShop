package com.xiaomi.mitv.shop;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import com.xiaomi.mitv.shop.com.xiaomi.mitv.shop.db.ShopDBManager;

public class MyActivity extends Activity {
    private static final String TAG = "MyActivity";

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        new Thread(){
            public void run(){
                for(int i = 0 ; i < 1000 ; i++){
                    ShopDBManager.INSTANCE.addValue(String.valueOf(i), String.valueOf(i));
                }

                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Log.i(TAG, "get 0: " + ShopDBManager.INSTANCE.getValue("0"));
                Log.i(TAG, "get 100: " + ShopDBManager.INSTANCE.getValue("100"));
                Log.i(TAG, "get 499: " + ShopDBManager.INSTANCE.getValue("499"));
                Log.i(TAG, "get 899: " + ShopDBManager.INSTANCE.getValue("899"));
            }
        }.start();
    }
}
