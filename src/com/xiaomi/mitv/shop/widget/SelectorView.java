package com.xiaomi.mitv.shop.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.xiaomi.mitv.api.util.MILog;
import com.xiaomi.mitv.shop.R;

import static android.widget.ImageView.ScaleType.*;

public class SelectorView extends ImageView {

	private int delta = 15;
	private ValueAnimator mValueAnimator;
	private boolean mInit = false;


	public SelectorView(Context context) {
		super(context);
		setScaleType(FIT_XY);
//		setImageResource(R.drawable.icon_on);
		setImageResource(R.drawable.mitv_highlight_bar);
	}

	public void initView(View focusView, ViewGroup container){
		mInit = true;

		FrameLayout.LayoutParams para = new FrameLayout.LayoutParams(focusView.getWidth() + delta * 2, focusView.getHeight() + delta * 2);
		container.addView(this, para);

		int[] coord = new int[2];
		focusView.getLocationOnScreen(coord);

		int newX = coord[0] - delta;
		int newY = coord[1] - delta;

		setX(newX);
		setY(newY);
	}

	public void move(View focusView) {

		final FrameLayout.LayoutParams animStartLayout = (FrameLayout.LayoutParams)getLayoutParams();

		if (mValueAnimator != null) {
			mValueAnimator.cancel();
			mValueAnimator = null;
		}

		int[] coord = new int[2];
		focusView.getLocationInWindow(coord);

		final int newX = coord[0] - delta;
		final int newY = coord[1] - delta;

		coord = new int[2];
		getLocationInWindow(coord);

		final int oldX = coord[0];
		final int oldY = coord[1];

		final int newHeight = focusView.getHeight() + delta * 2;

		mValueAnimator = ValueAnimator.ofFloat(0f, 1f);

		mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(animStartLayout);
				float percent = (Float) animation.getAnimatedValue();

				setX(newX - (newX - oldX) * (1 - percent));
				setY(newY - (newY - oldY) * (1 - percent));

				lp.height = (int) (lp.height + (newHeight - lp.height) * percent);
				setLayoutParams(lp);
			}
		});

		mValueAnimator.setDuration(150);
		mValueAnimator.start();
	}

	public boolean isInit(){
		return mInit;
	}

//	@Override
//	public void setTranslationX(float x){
//		super.setTranslationX(x);
//		if(this.getHeight() != mTargetHeight){
//			LayoutParams layoutParams = getLayoutParams();
//			if(layoutParams != null){
//				float diff = (mTargetHeight - mOriginHeight) * getRate(x);
//				layoutParams.height = mOriginHeight + (int)diff;
//				this.requestLayout();
//			}
//		}
//	}
//
//	private float getRate(float x) {
//		if(mTargetX != mOriginX){
//			float r = (float)(x - mOriginX) /(float)(mTargetX - mOriginX);
//			return r;
//		}
//		return 0;
//	}
//
//	public void setTranslationY(float y){
//		super.setTranslationY(y);
//	}
//
//	public void setTargetHeight(int height){
//		this.mOriginHeight = mTargetHeight;
//		this.mTargetHeight = height;
//	}
//
//	public int getTargetHeight(){
//		return mTargetHeight;
//	}
//
//	public void setTargetX(float x){
//		this.mTargetX = x;
//	}
//
//	public void setOriginX(float x){
//		this.mOriginX  = x;
//	}
}
