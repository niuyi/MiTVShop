package com.xiaomi.mitv.shop.detail;

import android.os.Bundle;

public interface ForwardPage {
	void load(Bundle bundle, LoadCallback callBack, Object object);
	void setShowOrHide(boolean show);
	void setBackPageAnimatoring(boolean animatoring);
	
	public static interface LoadCallback {
		void onLoadStarted();
		void onLoadVisibleBitmapsFinished();
		void onLoadFinished();
		
		public static class Stub implements LoadCallback {
			@Override
			public void onLoadStarted() {}
			@Override
			public void onLoadVisibleBitmapsFinished() {}
			@Override
			public void onLoadFinished() {}
		}
	}
	

}
