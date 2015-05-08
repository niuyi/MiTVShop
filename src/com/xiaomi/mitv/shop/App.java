package com.xiaomi.mitv.shop;

import android.app.Application;

/**
 * Created by niuyi on 2015/5/8.
 */
public class App extends Application {

    private static App mInstance = null;

    public App(){
        mInstance = this;
    }

    public static App getInstance(){
        return mInstance;
    }
	
}
