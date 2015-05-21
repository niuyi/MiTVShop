package com.xiaomi.mitv.shop.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import com.xiaomi.mitv.api.util.MILog;
import com.xiaomi.mitv.shop.R;

import static android.widget.ImageView.ScaleType.*;

public class SelectorView extends ImageView {

	public SelectorView(Context context) {
		super(context);
		setScaleType(FIT_XY);
		setImageResource(R.drawable.focus_bar);
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
