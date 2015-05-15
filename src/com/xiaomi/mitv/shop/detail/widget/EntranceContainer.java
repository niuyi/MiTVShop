package com.xiaomi.mitv.shop.detail.widget;

import android.content.Context;
import android.widget.RelativeLayout;

public class EntranceContainer extends RelativeLayout {
	private int mWidth;
	private int mHeight;
	
	public EntranceContainer(Context context, int width, int height) {
		super(context);
		this.mWidth = width;
		this.mHeight = height;
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(mWidth, mHeight);
	}
}
