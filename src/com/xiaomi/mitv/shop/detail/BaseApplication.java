package com.xiaomi.mitv.shop.detail;

import android.app.Application;

public class BaseApplication {
	private static BaseApplication sInstance = new BaseApplication();

	public static BaseApplication getInstance() {
		return sInstance;
	}

//	@Override
//	public void onCreate() {
//		super.onCreate();
//		sInstance = this;
//	}
	
	public BaseResConstants getResConstants(){
		return new ResConstants();
	}
}
