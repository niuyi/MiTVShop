package com.xiaomi.mitv.shop.detail.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.xiaomi.mitv.shop.R;


public class IndicatorGroup extends LinearLayout {
	private int mTotal;
	private int mCurrentIndex = 0;//by default;
	private Context mContext;
	private ImageView[] mChildren;
	private Drawable mDotNormalDrawable;
	private Drawable mDotSelectedDrawable;
	
	
	public IndicatorGroup(Context context, int total) {
		super(context);
		this.mTotal = total;
		this.mContext = context;
		this.mChildren = new ImageView[total];
		
		mDotNormalDrawable = context.getResources().getDrawable(R.drawable.dot_normal);
		mDotSelectedDrawable = context.getResources().getDrawable(R.drawable.dot_selected);
		
		attachContent();
	}

	private void attachContent() {
		for (int i = 0; i < mTotal; i++) {
			ImageView imageView = new ImageView(mContext);
			imageView.setImageDrawable(mDotNormalDrawable);
			LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			mChildren[i] = imageView;
			if (i != 0) {
				lp.leftMargin = 50;
			} else {
				imageView.setImageDrawable(mDotSelectedDrawable);
			}
			this.addView(imageView, lp);
		}
	}
	
	public void setCurrentIndex(int index) {
		if (index == mCurrentIndex) {
			return;
		}
		if (mCurrentIndex >= 0) {
			mChildren[mCurrentIndex].setImageDrawable(mDotNormalDrawable);
		}
		mChildren[index].setImageDrawable(mDotSelectedDrawable);
		mCurrentIndex = index;
	}
}
