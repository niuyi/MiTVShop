package com.xiaomi.mitv.shop.detail.widget;

import java.util.ArrayList;
import java.util.List;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.squareup.picasso.Picasso;
import com.xiaomi.mitv.shop.App;
import com.xiaomi.mitv.shop.R;
import com.xiaomi.mitv.shop.detail.BackPage;
import com.xiaomi.mitv.shop.detail.Constants;
import com.xiaomi.mitv.shop.detail.ForwardPage;
import com.xiaomi.mitv.shop.detail.ui.PlaySoundRelativeLayout;
import com.xiaomi.mitv.shop.detail.utils.Utils;

//import com.xiaomi.mitv.handbook.common.BackPage;
//import com.xiaomi.mitv.handbook.common.Constants;
//import com.xiaomi.mitv.handbook.common.ForwardPage;
//import com.xiaomi.mitv.handbook.common.R;
//import com.xiaomi.mitv.handbook.common.ui.PlaySoundRelativeLayout;
//import com.xiaomi.mitv.handbook.common.utils.Utils;
//import com.xiaomi.mitv.handbook.common.widget.PostersControlWidget.IssueAdapterData;

public class ShowFullScreenImageWidget extends PlaySoundRelativeLayout implements ForwardPage {
	private static final String TAG = "ShowFullScreenImageWidget";
	private static final int ANIMATOR_DURATION_FULL_SCREEN_POSTER_SLIDE = 500;
	
	private ImageView[] mImageViews = new ImageView[2];
	
	private int mTopImageViewIndex = 0;
	private int mCurrentSelectedIndex = 0;
	
	private String[] mPosters;
//	private Bitmap[] mBitmaps;
	private Handler mHandler = new Handler();
	private AssetManager mAssetManager;
	private Context mContext;
	
	private boolean mPosterLoading = false;
	private boolean mAnimatoring = false;
	private boolean mBackPageAnimatoring = false;
	
	private BackPage mBackPageWidget;
	private IndicatorGroup mIndicatorGroup;
	
	private ViewGroup mIndicatorContainer;
	
	private ImageView mLeftArrowImageView;
	private ImageView mRightArrowImageView;
	
	private int mIssueIndex;
	private PostersControlWidget.IssueAdapterData[] mIssueAdapterDatas;
	
	private boolean mShaking = false;
	
	public ShowFullScreenImageWidget(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		mAssetManager = context.getAssets();
		
		attachContent();
	}
	
	public void setIndicatorContainer(ViewGroup indicatorContainer) {
		this.mIndicatorContainer = indicatorContainer;
	}
	
	private void attachContent() {
		for (int i = 0; i < 2; i++) {
			ImageView imageview = new ImageView(mContext);
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			this.addView(imageview, lp);
			imageview.setVisibility(View.INVISIBLE);
			mImageViews[i] = imageview;
		}
		mImageViews[0].bringToFront();
	}

	public void setLeftArrowImageView(ImageView leftArrowImageView) {
		this.mLeftArrowImageView = leftArrowImageView;
	}

	public void setRightArrowImageView(ImageView rightArrowImageView) {
		this.mRightArrowImageView = rightArrowImageView;
	}
	
	public void show() {
		mPosterLoading = true;
		mAnimatoring = false;
		mTopImageViewIndex = 0;
//		mCurrentSelectedIndex = 0;
		mImageViews[0].bringToFront();
	    new Thread(new Runnable() {

			@Override
			public void run() {
				Log.i(TAG, "load first bitmap");
//				mBitmaps[mCurrentSelectedIndex] = Utils.loadAssetBitmap(mAssetManager, mPosters[mCurrentSelectedIndex]);
				Log.i(TAG, "end load first bitmap");
				mHandler.post(new Runnable() {

					@Override
					public void run() {
//						mImageViews[mTopImageViewIndex].setImageBitmap(mBitmaps[mCurrentSelectedIndex]);
						Picasso.with(App.getInstance().getApplicationContext()).load(mPosters[mCurrentSelectedIndex]).into(mImageViews[mTopImageViewIndex]);
						ShowFullScreenImageWidget.this.setVisibility(View.VISIBLE);
						resetIndicatorGroup(mPosters.length);
						updateArrowImageViewStatus();
					}

				});
				Log.i(TAG, "load other bitmaps");
//				for (int i = 0 ; i < mBitmaps.length; i++) {
//					if (i != mCurrentSelectedIndex) {
//						mBitmaps[i] = Utils.loadAssetBitmap(mAssetManager, mPosters[i]);
//					}
//				}
				Log.i(TAG, "end load other bitmaps");
				mHandler.post(new Runnable() {

					@Override
					public void run() {
						ShowFullScreenImageWidget.this.setFocusable(true);
						mPosterLoading = false;
					}
					
				});
			}
	    	
	    }).start();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT || keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
			handleLeftRightPressed(keyCode == KeyEvent.KEYCODE_DPAD_LEFT);
			return true;
		}
		if (keyCode == KeyEvent.KEYCODE_DPAD_UP || keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
			playErrorSound();
			return true;
		}
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (event.getRepeatCount() == 0 && !mBackPageAnimatoring && !mPosterLoading && !mAnimatoring && !mShaking) {
				Log.i(TAG, "KEYCODE_BACK");
				onBackPressed(true, false);
			} 
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private void onBackPressed(boolean userCall, boolean leftPressed) {
		this.setVisibility(View.INVISIBLE);
//		Bitmap coverBitmap = Utils.getCoverPosterBitmap(mBitmaps[mCurrentSelectedIndex]);
		cleanup();
		Log.i(TAG, "on Back to page, mCurrentSelectedIndex: " + mCurrentSelectedIndex);
		Bundle bundle = new Bundle();
		bundle.putBoolean(Constants.BUNDLE_KEY_POSTERS_PAGE_USER_CALL_BACK, userCall);
		bundle.putInt(Constants.BUNDLE_KEY_POSTERS_PAGE_POSTER_ISSUE_INDEX, mIssueIndex);
		bundle.putInt(Constants.BUNDLE_KEY_POSTERS_PAGE_POSTER_SELECTED_INDEX, mCurrentSelectedIndex);
		bundle.putBoolean(Constants.BUNDLE_KEY_POSTERS_PAGE_LEFT_PRESSED, leftPressed);
		
		mBackPageWidget.onBackToPage(null, bundle);
	}

	private void updateArrowImageViewStatus() {
		if (mCurrentSelectedIndex == 0) {
			mLeftArrowImageView.setVisibility(View.INVISIBLE);
		} else {
			mLeftArrowImageView.setVisibility(View.VISIBLE);
		}
		if (mCurrentSelectedIndex == mPosters.length - 1) {
			mRightArrowImageView.setVisibility(View.INVISIBLE);
		} else {
			mRightArrowImageView.setVisibility(View.VISIBLE);
		}
	}
	
	private AnimatorSet getShakingAnimator(int distance, View imageView) {
		final int currentX = (int) imageView.getX();
		AnimatorSet set = new AnimatorSet();
		ObjectAnimator oa = ObjectAnimator.ofFloat(imageView, "x", currentX + distance);
		oa.setDuration(100);
		ObjectAnimator ob = ObjectAnimator.ofFloat(imageView, "x", currentX);
		ob.setDuration(300);
		set.playSequentially(oa, ob);
		return set;
	}
	
	private void startShakeAnimator(boolean leftPressed) {
		AnimatorSet as = new AnimatorSet();
		int distance = leftPressed ? 50 : -50;
		List<Animator> children = new ArrayList<Animator>();
		children.add(getShakingAnimator(distance, mImageViews[mTopImageViewIndex]));
		
		as.playTogether(children);

		as.addListener(new AnimatorListenerAdapter() {

			@Override
			public void onAnimationStart(Animator animation) {
				mShaking = true;
				mImageViews[(mTopImageViewIndex + 1) % 2].setVisibility(View.INVISIBLE);
			}

			@Override
			public void onAnimationEnd(Animator animation) {
				mImageViews[(mTopImageViewIndex + 1) % 2].setVisibility(View.VISIBLE);
				mShaking = false;
			}
		});
		as.start();
	}
	
	private void handleLeftRightPressed(boolean leftPressed) {
		if (mPosterLoading || mAnimatoring || mShaking || mBackPageAnimatoring) {
			return;
		}
		final int targetIndex = leftPressed ? mCurrentSelectedIndex - 1 : mCurrentSelectedIndex + 1;
		if (targetIndex < 0) { //到达一个问题的最左侧
			if (mIssueIndex == 0) { //是第一个问题
				startShakeAnimator(leftPressed);
				playErrorSound();
			} else {
				handleIssueSwitch(leftPressed);
			}
			return;
		}
		if (targetIndex > mPosters.length - 1) {//到达一个问题的最右侧
			if (mIssueIndex == mIssueAdapterDatas.length - 1) {//是最后一个问题
				startShakeAnimator(leftPressed);
				playErrorSound();
			} else {
				handleIssueSwitch(leftPressed);
			}
			return;
		}
		startPosterSwitchAnimator(leftPressed, targetIndex);
	}

	private void handleIssueSwitch(boolean leftPressed) {
		onBackPressed(false, leftPressed);
	}
	
//	private void handleIssueSwitch(final boolean leftPressed) {
//		mPosterLoading = true;
//		
//		mIssueIndex = leftPressed ? mIssueIndex - 1: mIssueIndex + 1;
//		
//		mPosters = mIssueAdapterDatas[mIssueIndex].mPosterChilren;
//		mBitmaps = new Bitmap[mPosters.length];
//		
//		if (mIndicatorGroup != null) {
//			mIndicatorContainer.removeView(mIndicatorGroup);
//		}
//		mCurrentSelectedIndex = leftPressed? mPosters.length - 1 : 0;
//		
//		new Thread(new Runnable() {
//
//			@Override
//			public void run() {
//				Log.i(TAG, "IssueSwitch load first bitmap");
//				mBitmaps[mCurrentSelectedIndex] = Utils.loadAssetBitmap(mAssetManager, mPosters[mCurrentSelectedIndex]);
//				Log.i(TAG, "IssueSwitch end load first bitmap");
//				mHandler.post(new Runnable() {
//
//					@Override
//					public void run() {
//						startPosterSwitchAnimator(leftPressed, mCurrentSelectedIndex);
//						ShowFullScreenImageWidget.this.setVisibility(View.VISIBLE);
//						resetIndicatorGroup(mPosters.length);
//						updateArrowImageViewStatus();
//					}
//
//				});
//				Log.i(TAG, "load other bitmaps");
//				for (int i = 0 ; i < mBitmaps.length; i++) {
//					if (i != mCurrentSelectedIndex) {
//						mBitmaps[i] = Utils.loadAssetBitmap(mAssetManager, mPosters[i]);
//					}
//				}
//				Log.i(TAG, "end load other bitmaps");
//				mHandler.post(new Runnable() {
//
//					@Override
//					public void run() {
//						ShowFullScreenImageWidget.this.setFocusable(true);
//						mPosterLoading = false;
//					}
//					
//				});
//			}
//			
//		}).start();
//		
//		return;
//	}

	private void resetIndicatorGroup(int count) {
		mIndicatorGroup = new IndicatorGroup(mContext, count);
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.topMargin = 1000;
		lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
		mIndicatorContainer.addView(mIndicatorGroup, lp);
		mIndicatorGroup.setCurrentIndex(mCurrentSelectedIndex);
	}
	
	private void startPosterSwitchAnimator(boolean leftPressed, final int targetIndex) {
		playKeyStoneSound();
//		mImageViews[(mTopImageViewIndex + 1) % 2].setImageBitmap(mBitmaps[targetIndex]);
		Picasso.with(App.getInstance().getApplicationContext()).load(mPosters[targetIndex]).into(mImageViews[(mTopImageViewIndex + 1) % 2]);
//		Log.e(TAG, "mTopImageViewIndex: " + mTopImageViewIndex);
		if (!leftPressed) {
			ObjectAnimator oa = ObjectAnimator.ofFloat(mImageViews[mTopImageViewIndex], "x", -1920f);
			oa.setDuration(ANIMATOR_DURATION_FULL_SCREEN_POSTER_SLIDE);
			mRightArrowImageView.setImageResource(R.drawable.icon_arrow_active);
			oa.addListener(new AnimatorListenerAdapter() {

				@Override
				public void onAnimationEnd(Animator animation) {
					mImageViews[mTopImageViewIndex].setVisibility(View.INVISIBLE);
					mImageViews[mTopImageViewIndex].setX(0);
					mImageViews[(mTopImageViewIndex + 1) % 2].bringToFront();
					mImageViews[mTopImageViewIndex].setVisibility(View.VISIBLE);
					mTopImageViewIndex = (mTopImageViewIndex + 1) % 2;
					mCurrentSelectedIndex = targetIndex;
					mIndicatorGroup.setCurrentIndex(mCurrentSelectedIndex);
					updateArrowImageViewStatus();
					mRightArrowImageView.setImageResource(R.drawable.icon_arrow_normal);
					mAnimatoring = false;
				}

				@Override
				public void onAnimationStart(Animator animation) {
					mAnimatoring = true;
				}
				
			});
			oa.start();
		} else {
			mLeftArrowImageView.setImageResource(R.drawable.icon_arrow_left_active);
			mImageViews[(mTopImageViewIndex + 1) % 2].setVisibility(View.INVISIBLE);
			mImageViews[(mTopImageViewIndex + 1) % 2].setX(-1920);
			mImageViews[(mTopImageViewIndex + 1) % 2].bringToFront();
			mImageViews[(mTopImageViewIndex + 1) % 2].setVisibility(View.VISIBLE);
			ObjectAnimator oa = ObjectAnimator.ofFloat(mImageViews[(mTopImageViewIndex + 1) % 2], "x", 0);
			oa.setDuration(ANIMATOR_DURATION_FULL_SCREEN_POSTER_SLIDE);
			oa.addListener(new AnimatorListenerAdapter() {

				@Override
				public void onAnimationStart(Animator animation) {
					mAnimatoring = true;
				}

				@Override
				public void onAnimationEnd(Animator animation) {
					mTopImageViewIndex = (mTopImageViewIndex + 1) % 2;
					mCurrentSelectedIndex = targetIndex;
					mIndicatorGroup.setCurrentIndex(mCurrentSelectedIndex);
					updateArrowImageViewStatus();
					mLeftArrowImageView.setImageResource(R.drawable.icon_arrow_left_normal);
					mAnimatoring = false;
				}

			});
			oa.start();
		}
	}

	private void cleanup() {
		if (mIndicatorGroup != null) {
			mIndicatorContainer.removeView(mIndicatorGroup);
		}
		if (mLeftArrowImageView != null) {
			mLeftArrowImageView.setVisibility(View.INVISIBLE);
		}
		if (mRightArrowImageView != null) {
			mRightArrowImageView.setVisibility(View.INVISIBLE);
		}
		if (mImageViews != null) {
			for (ImageView imageView : mImageViews) {
				imageView.setVisibility(View.INVISIBLE);
				imageView.setImageBitmap(null);
			}
		}
//		if (mBitmaps != null) {
//			for (int i = 0; i < mBitmaps.length; i++) {
//				if (mBitmaps[i] != null) {
//					Log.i(TAG, "recycle fullscreen poster bitmap " + i);
//					mBitmaps[i].recycle();
//					mBitmaps[i] = null;
//				}
//			}
//		}
	}

	public void setImageViewsVisible(boolean visible) {
		for (ImageView imageView : mImageViews) {
			imageView.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
		}
	}
	
	public boolean isImageViewVisible() {
		return mImageViews[0].getVisibility() == View.VISIBLE || mImageViews[1].getVisibility() == View.VISIBLE;
	}
	
	public void setBackPageWidget(BackPage backPageWidget) {
		this.mBackPageWidget = backPageWidget;
	}

	@Override
	public void load(Bundle bundle, LoadCallback callBack, Object object) {
		String[] posters = bundle.getStringArray(Constants.BUNDLE_KEY_POSTERS_PAGE_POSTER_PATH);
		
		mPosters = posters;
//		mBitmaps = new Bitmap[posters.length];
		
		mCurrentSelectedIndex = bundle.getInt(Constants.BUNDLE_KEY_POSTERS_PAGE_POSTER_SELECTED_INDEX);

		mIssueIndex = bundle.getInt(Constants.BUNDLE_KEY_POSTERS_PAGE_POSTER_ISSUE_INDEX);
		mIssueAdapterDatas = (PostersControlWidget.IssueAdapterData[]) object;
		
		show();
		requestFocus();
	}

	@Override
	public void setShowOrHide(boolean show) {
		setImageViewsVisible(show);
	}

	@Override
	public void setBackPageAnimatoring(boolean animatoring) {
		mBackPageAnimatoring = animatoring;
	}
}
