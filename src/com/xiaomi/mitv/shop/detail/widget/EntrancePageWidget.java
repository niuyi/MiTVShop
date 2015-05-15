package com.xiaomi.mitv.shop.detail.widget;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.xiaomi.mitv.shop.detail.BackPage;
import com.xiaomi.mitv.shop.detail.BaseApplication;
import com.xiaomi.mitv.shop.detail.Constants;
import com.xiaomi.mitv.shop.detail.ForwardPage;
import com.xiaomi.mitv.shop.R;
import com.xiaomi.mitv.shop.detail.ui.PlaySoundRelativeLayout;

public class EntrancePageWidget extends PlaySoundRelativeLayout implements BackPage{
	private static final String TAG = "EntrancePageWidget";
	
	private static final int ANIMATOR_DURATION_FOCUS_MOVE = 200;
	public static final int ANIMATOR_DURATION_SELECTED_CENTER_POSTER_IMAGEVIEW = 600;
	public static final int ANIMATOR_DURATION_SCATTER_POSTER_IMAGEVIEW = 100;
	
//	private static final int ENTRANCE_IMAGEVIEW_MARGIN = 5;
//	private static final int ENTRANCE_LEFT_IMAGEVIEW_MARGIN_LEFT = 231;
//	private static final int ENTRANCE_IMAGEVIEW_WIDTH = 480;
//	private static final int ENTRANCE_GROUP_TOP_MARGIN = 275;
//	private static final int IMAGEVIEW_FORCUS_OFFSET = 30;
	
	private int mThresholdX; 
	private int mImageViewForcusOffset;
	
	private int mEntranceImageViewMargin;
	
	private ImageView mBorderFocusImageView;
	private ImageView[] mEntranceImageViews = new ImageView[BaseApplication.getInstance().getResConstants().getFragments().length];
	private Point[] mEntranceImageViewsOriginLocation = new Point[BaseApplication.getInstance().getResConstants().getFragments().length];
	
	private int mCurrentSelectedEntranceViewId = -1; // ID 从1开始
	
	private ForwardPage mForwardPageWidget;

	private Context mContext;
	
	private boolean isAnimatoring = false;
	
	private View mTitleGroupView;
	
//	private Bitmap[] mEntranceBitmaps = new Bitmap[Constants.CATEGORY_FRAGMENTS.length];
	
	private AssetManager mAssetManager;
	private EntranceContainer mEntranceContainer; //
	
	private boolean mLoadingCoverBitmaps = false;
	
	private ImageView mReflectImageView;
	
	private int ENTRANCE_LEFT_IMAGEVIEW_MARGIN_LEFT;
	
	private int mCardWidth;
	
	private boolean mEntranceAnimatoring = false;
	
	private Handler mHandler = new Handler();
	
	public EntrancePageWidget(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		mAssetManager = mContext.getAssets();
		attachContent();
	}
	
	private OnFocusChangeListener  mFocusChangeListener = new OnFocusChangeListener(){
		//通过相对屏幕位置来维护焦点
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if (mEntranceAnimatoring) {
				return;
			}
			if (hasFocus) {
				int id = v.getId();
				int realX = (int) v.getX();
				int relativeX = realX + (int) mEntranceContainer.getX();
				Log.i(TAG, "has focus: " + v.getId() + " real x: " + v.getX() + " relative x: " + relativeX);
				AnimatorSet root = new AnimatorSet();
				if (relativeX > mThresholdX || relativeX < 0) {
					int targetX = relativeX > 0 ? (int) mEntranceContainer.getX() -(v.getWidth() + mEntranceImageViewMargin) : (int) mEntranceContainer.getX() + (v.getWidth() + mEntranceImageViewMargin);
					ObjectAnimator boa = ObjectAnimator.ofFloat(mEntranceContainer, "x", targetX);
					boa.setDuration(ANIMATOR_DURATION_FOCUS_MOVE);
					if ((id == mEntranceImageViews.length) && (mEntranceImageViews.length % 2 == 1) && (mBorderFocusImageView.getY() > mEntranceImageViews[0].getY())) {
						ObjectAnimator bob = ObjectAnimator.ofFloat(mBorderFocusImageView, "y", mEntranceImageViews[0].getY() - mImageViewForcusOffset);
						root.playTogether(boa, bob);
					} else {
						root.playTogether(boa);
					}
				} else {
					ObjectAnimator boa = ObjectAnimator.ofFloat(mBorderFocusImageView, "x", relativeX - mImageViewForcusOffset);
					ObjectAnimator bob = ObjectAnimator.ofFloat(mBorderFocusImageView, "y", v.getY() - mImageViewForcusOffset);
					boa.setDuration(ANIMATOR_DURATION_FOCUS_MOVE);
					bob.setDuration(ANIMATOR_DURATION_FOCUS_MOVE);
					root.playTogether(boa, bob);
				}
				root.addListener(new AnimatorListenerAdapter(){
					
					@Override
					public void onAnimationStart(Animator animation) {
						isAnimatoring = true;
					}

					@Override
					public void onAnimationEnd(Animator animation) {
						isAnimatoring = false;
					}
					
				});
				root.start();
			}
		}
	};
	
	private OnKeyListener mOnKeyListener = new OnKeyListener() {

		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			if (event.getAction() == KeyEvent.ACTION_DOWN) {
				if (mEntranceAnimatoring) {
					return true;
				}
				if (keyCode == KeyEvent.KEYCODE_DPAD_UP && v.getId() % 2 == 1) {
					playErrorSound();
					return true;
				}
				if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN && v.getId() % 2 == 0) {
					playErrorSound();
					return true;
				}
				if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
					if (isAnimatoring) {
						return true;
					}
					if (v.getId() == 1 || v.getId() == 2) {
						playErrorSound();
						return true;
					}
				}
				
				if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
					if (isAnimatoring) {
						return true;
					}
					if ((v.getId() == mEntranceImageViews.length) || (mEntranceImageViews.length % 2 == 0 && v.getId() == mEntranceImageViews.length - 1)) {
						playErrorSound();
						return true;
					}
				}
			}
			return false;
		}
	};
	
	
	private OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (mLoadingCoverBitmaps) {
				return;
			}
			mCurrentSelectedEntranceViewId = v.getId();
			setEntranceImageViewsFocusable(false);
			saveEntranceImageViewLocationInfo();
//			mTitleGroupView = EntrancePageWidget.this.findViewById(R.id.handbook_first_page_title_group);
			mTitleGroupView.setVisibility(View.INVISIBLE);
			mBorderFocusImageView.setVisibility(View.INVISIBLE);
			Bundle bundle = new Bundle();
			bundle.putInt(Constants.BUNDLE_KEY_ENTRANCE_PAGE_CLICK_INDEX, mCurrentSelectedEntranceViewId - 1);
			mForwardPageWidget.load(bundle, new ForwardPage.LoadCallback() {

				@Override
				public void onLoadFinished() {
					Log.i(TAG, "PostersControlWidget onLoadFinished");
					EntrancePageWidget.this.setVisibility(View.INVISIBLE);
				}

				@Override
				public void onLoadStarted() {
					Log.i(TAG, "PostersControlWidget onLoadStarted");
				}

				@Override
				public void onLoadVisibleBitmapsFinished() {
					Log.i(TAG, "PostersControlWidget onLoadVisibleBitmapsFinished");
//					EntrancePageWidget.this.setVisibility(View.INVISIBLE);
				}
//			}, mEntranceBitmaps[mCurrentSelectedEntranceViewId - 1]);
			}, null);
			startScatterAnimator();
		}

		private void startScatterAnimator() {
			AnimatorSet[] scatterAnimatorSet = new AnimatorSet[mEntranceImageViews.length];
			for (int i = 0; i < mEntranceImageViews.length; i++) {
				int selectedIndex = mCurrentSelectedEntranceViewId - 1;
				if (i == selectedIndex) {
					scatterAnimatorSet[i] = getSelectedEntranceViewScaleAnimator(mEntranceImageViews[i], true);
					scatterAnimatorSet[i].addListener(new AnimatorListenerAdapter() {
						
						@Override
						public void onAnimationStart(Animator animation) {
							super.onAnimationStart(animation);
							mReflectImageView.setVisibility(View.INVISIBLE);
//							EntrancePageWidget.this.setBackgroundDrawable(null);
						}

						@Override
						public void onAnimationEnd(Animator animation) {
							EntrancePageWidget.this.setVisibility(View.INVISIBLE);
						}
						
					});
					continue;
				}
				scatterAnimatorSet[i] = getExitViewAnimator(mEntranceImageViews[i], i < selectedIndex);
			}
			AnimatorSet as = new AnimatorSet();
			as.playTogether(scatterAnimatorSet);
			as.addListener(new AnimatorListenerAdapter() {

				@Override
				public void onAnimationEnd(Animator animation) {
					mBorderFocusImageView.setVisibility(View.INVISIBLE);
				}

			});
			as.start();
		}

		private AnimatorSet getExitViewAnimator(ImageView entranceImageView, boolean left) {
			int targetX = left ? -1000 : mEntranceContainer.getWidth(); 
			AnimatorSet as = new AnimatorSet();
			ObjectAnimator oa = ObjectAnimator.ofFloat(entranceImageView, "x", targetX);
			oa.setDuration(ANIMATOR_DURATION_SCATTER_POSTER_IMAGEVIEW);
			as.playTogether(oa);
			return as;
		}
		
	};
	
	private AnimatorSet getSelectedEntranceViewScaleAnimator(View v, boolean zoomIn) {
		int posterWidth = (int) mContext.getResources().getDimension(R.dimen.second_page_poster_imageview_width);
		int posterHeight = (int) mContext.getResources().getDimension(R.dimen.second_page_poster_imageview_height);
		
		int mainEnterImageViewWidth = (int) mContext.getResources().getDimension(R.dimen.first_page_imageview_width);
		int[] postersControlImageViewX = mContext.getResources().getIntArray(R.array.poster_imageview_x);
		int posterControlImageViewY = (int) mContext.getResources().getDimension(R.dimen.second_page_poster_imageview_margin_top);
		
		int offset = - (int) mEntranceContainer.getX();
		int targetX = zoomIn? postersControlImageViewX[1] + posterWidth / 4 + offset : mEntranceImageViewsOriginLocation[mCurrentSelectedEntranceViewId - 1].x;
		int targetY = zoomIn? posterControlImageViewY + posterHeight / 4: mEntranceImageViewsOriginLocation[mCurrentSelectedEntranceViewId - 1].y;
		
		float targetScale = zoomIn ? posterWidth * 1.1f / mainEnterImageViewWidth: 1.0f;
		float targetAlpha = zoomIn ? 0f : 1f;
//		float targetAlpha = zoomIn ?  1f : 1f;
		
		AnimatorSet as = new AnimatorSet();
		ObjectAnimator oa = ObjectAnimator.ofFloat(v, "x", targetX);
		ObjectAnimator ob = ObjectAnimator.ofFloat(v, "y", targetY);
		ObjectAnimator oc = ObjectAnimator.ofFloat(v, "scaleX", targetScale);
		ObjectAnimator od = ObjectAnimator.ofFloat(v, "scaleY", targetScale);
		ObjectAnimator oe = ObjectAnimator.ofFloat(v, "alpha", targetAlpha);
		oa.setDuration(ANIMATOR_DURATION_SELECTED_CENTER_POSTER_IMAGEVIEW);
		ob.setDuration(ANIMATOR_DURATION_SELECTED_CENTER_POSTER_IMAGEVIEW);
		oc.setDuration(ANIMATOR_DURATION_SELECTED_CENTER_POSTER_IMAGEVIEW);
		od.setDuration(ANIMATOR_DURATION_SELECTED_CENTER_POSTER_IMAGEVIEW);
		oe.setDuration(ANIMATOR_DURATION_SELECTED_CENTER_POSTER_IMAGEVIEW);
		as.playTogether(oa, ob, oc, od, oe);
		as.start();
		return as;
	}
	
	private void setEntranceImageViewsFocusable(boolean focusable) {
//		Log.i(TAG, "mCurrentSelectedEntranceViewId: " + mCurrentSelectedEntranceViewId);
		for (int i = 0; i < mEntranceImageViews.length; i++) {
			if (!focusable && (i == mCurrentSelectedEntranceViewId - 1)) {
				continue;
			}
			mEntranceImageViews[i].setFocusable(focusable);
			mEntranceImageViews[i].setFocusableInTouchMode(focusable);
		}
		if (!focusable && (mCurrentSelectedEntranceViewId > 0)) {
			mEntranceImageViews[mCurrentSelectedEntranceViewId - 1].setFocusable(focusable);
			mEntranceImageViews[mCurrentSelectedEntranceViewId - 1].setFocusableInTouchMode(focusable);
		}
	}
	
	private void saveEntranceImageViewLocationInfo() {
		for (int i = 0; i < mEntranceImageViews.length; i++) {
			Point point = new Point((int) mEntranceImageViews[i].getX(), (int) mEntranceImageViews[i].getY());
			mEntranceImageViewsOriginLocation[i] = point;
		}
	}
	
	private void attachContent() {
		mEntranceImageViewMargin = (int) this.getResources().getDimension(R.dimen.entrance_imageview_margin);
		mImageViewForcusOffset = (int) this.getResources().getDimension(R.dimen.focus_offset);
		
		mThresholdX = (int) this.getResources().getDimension(R.dimen.mThresholdX);
		mCardWidth = this.getResources().getDimensionPixelSize(R.dimen.first_page_imageview_width);
		int height = this.getResources().getDimensionPixelSize(R.dimen.first_page_imageview_height);
		
		int column = mEntranceImageViews.length / 2 + mEntranceImageViews.length % 2 + 1;
		ENTRANCE_LEFT_IMAGEVIEW_MARGIN_LEFT = (int) this.getResources().getDimension(R.dimen.entrance_left_imageview_margin_left);
		
		int entranceContainerWidth = ENTRANCE_LEFT_IMAGEVIEW_MARGIN_LEFT + column * (mEntranceImageViewMargin + mCardWidth);
		Log.i(TAG, "Entrance imageview container width: " + entranceContainerWidth + " column: " + column);
		
		int screenHeight = this.getResources().getDisplayMetrics().heightPixels;
		Log.e(TAG, "screenHeight: " + screenHeight);
		
		mEntranceContainer = new EntranceContainer(mContext, entranceContainerWidth, screenHeight);
		this.addView(mEntranceContainer, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		
		int ENTRANCE_GROUP_TOP_MARGIN = (int) this.getResources().getDimension(R.dimen.entrance_group_top_margin);
		
		String[] categoryFragments = BaseApplication.getInstance().getResConstants().getFragments();
		int[] entranceImageviewDrawable = BaseApplication.getInstance().getResConstants().getEntranceResIds();
		for (int i = 0; i < categoryFragments.length; i++) {
			ImageView imageView = new ImageView(mContext);
			imageView.setId(i + 1);
			imageView.setFocusable(true);
			imageView.setFocusableInTouchMode(true);
			imageView.setOnFocusChangeListener(mFocusChangeListener);
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(mCardWidth, height);
			imageView.setOnClickListener(mOnClickListener);
			imageView.setOnKeyListener(mOnKeyListener);
			imageView.setImageResource(entranceImageviewDrawable[i]);
			mEntranceImageViews[i] = imageView;
			if (i != 0 && i != 1) {
				lp.leftMargin = mEntranceImageViewMargin;
				lp.addRule(RelativeLayout.RIGHT_OF, i - 2 + 1);
			} else {
				lp.leftMargin = ENTRANCE_LEFT_IMAGEVIEW_MARGIN_LEFT;
			}
			if (i % 2 == 0) {
				lp.topMargin = ENTRANCE_GROUP_TOP_MARGIN;
			} else {
				lp.topMargin = mEntranceImageViewMargin;
				lp.addRule(RelativeLayout.BELOW, i - 1 + 1);
			}
			mEntranceContainer.addView(imageView, lp);
			
		}
		mReflectImageView = new ImageView(mContext);
		RelativeLayout.LayoutParams reflectLP = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		reflectLP.addRule(RelativeLayout.BELOW, 2);
		reflectLP.leftMargin = ENTRANCE_LEFT_IMAGEVIEW_MARGIN_LEFT;
//		mReflectImageView.setImageResource(R.drawable.reflection);
		mEntranceContainer.addView(mReflectImageView, reflectLP);
		
		mBorderFocusImageView = new ImageView(mContext);
		mBorderFocusImageView.setImageResource(R.drawable.home_page_focus);
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.leftMargin = ENTRANCE_LEFT_IMAGEVIEW_MARGIN_LEFT - mImageViewForcusOffset;
		lp.topMargin = ENTRANCE_GROUP_TOP_MARGIN - mImageViewForcusOffset;
		this.addView(mBorderFocusImageView, lp);
	}

	public void onBackToPage(Object object, Bundle bundle) {
		this.setVisibility(View.VISIBLE);
		mTitleGroupView.setVisibility(View.VISIBLE);
		startEntranceGatherAnimator();
	}
	
	private void startEntranceGatherAnimator() {
		AnimatorSet root = new AnimatorSet();
		AnimatorSet[] children = new AnimatorSet[mEntranceImageViews.length];
		for (int i = 0; i < mEntranceImageViews.length; i++) {
			AnimatorSet as = new AnimatorSet();
			ObjectAnimator oa = ObjectAnimator.ofFloat(mEntranceImageViews[i], "x", mEntranceImageViewsOriginLocation[i].x);
			ObjectAnimator ob = ObjectAnimator.ofFloat(mEntranceImageViews[i], "y", mEntranceImageViewsOriginLocation[i].y);
			ObjectAnimator oc = ObjectAnimator.ofFloat(mEntranceImageViews[i], "scaleX", 1f);
			ObjectAnimator od = ObjectAnimator.ofFloat(mEntranceImageViews[i], "scaleY", 1f);
			ObjectAnimator oe = ObjectAnimator.ofFloat(mEntranceImageViews[i], "alpha", 1f);
			as.playTogether(oa, ob, oc, od, oe);
			children[i] = as;
		}
		root.playTogether(children);
		root.addListener(new AnimatorListenerAdapter() {

			@Override
			public void onAnimationEnd(Animator animation) {
				EntrancePageWidget.this.setBackgroundResource(R.drawable.entrance_bg);
				mReflectImageView.setVisibility(View.VISIBLE);
				mBorderFocusImageView.setVisibility(View.VISIBLE);
				mEntranceImageViews[mCurrentSelectedEntranceViewId - 1].setFocusable(true);
				mEntranceImageViews[mCurrentSelectedEntranceViewId - 1].requestFocus();
				EntrancePageWidget.this.setEntranceImageViewsFocusable(true);
			}
		});
		root.start();
	}
	
	public void setForwardPageWidget(ForwardPage forwardPageWidget) {
		this.mForwardPageWidget = forwardPageWidget;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_DPAD_UP && mCurrentSelectedEntranceViewId % 2 == 1) {
			playErrorSound();
		}
		if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN && mCurrentSelectedEntranceViewId % 2 == 0) {
			playErrorSound();
		}
		return super.onKeyDown(keyCode, event);
	}

	public void onActivityStart() {
//		mLoadingCoverBitmaps = true;
//		new Thread(new Runnable() {
//
//			@Override
//			public void run() {
//				try {
//					loadCoverBitmapToMemory();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//
//		}).start();
	}

//	private void loadCoverBitmapToMemory() throws IOException {
//		for (int i = 0; i < Constants.CATEGORY_FRAGMENTS.length; i++) {
//			String typeFragment = Constants.CATEGORY_FRAGMENTS[i];
//			String folderName = Constants.sScreenFragment + Constants.SEPERATE + typeFragment;
//			String[] contents = mContext.getAssets().list(folderName);
//			String coverPosterPath = null;
//			for (int j = 0; j < contents.length; j++ ) {
//				String s = contents[j];
//				if (s == null) {
//					return;
//				}
//				String path = folderName + File.separator + s;
//				if (!Utils.isAssetBackgroundFile(mAssetManager, path)) {
//					String[] posters = mAssetManager.list(path);
//					coverPosterPath = path + Constants.SEPERATE + posters[0];
//				}
//			}
//			Log.e(TAG, "load cover bitmap : " + coverPosterPath);
//			if (coverPosterPath != null) {
//				mEntranceBitmaps[i] = Utils.loadAssetBitmap(mAssetManager, coverPosterPath, 2);
//			}
//		}
//		mHandler.post(new Runnable() {
//
//			@Override
//			public void run() {
//				mLoadingCoverBitmaps = false;
//			}
//			
//		});
//	}
	
	public void onActivityStop() {
//		if (mEntranceBitmaps != null) {
//			for (int i = 0; i < mEntranceBitmaps.length; i++) {
//				Bitmap bitmap = mEntranceBitmaps[i];
//				if (bitmap != null) {
//					Log.i(TAG, "recycle cover bitmap");
//					bitmap.recycle();
//				}
//				mEntranceBitmaps[i] = null;
//			}
//		}
	}
	
	
	
	public void startEntranceAnimation() {
		AnimatorSet root = new AnimatorSet();
		AnimatorSet[] animators = new AnimatorSet[mEntranceImageViews.length + 1];
//		int[] durations = new int[mEntranceImageViews.length];
		Log.e(TAG, "container x: " + mEntranceContainer.getX());
		int[] initCardXs = new int[mEntranceImageViews.length];
		int[] cardXs = new int[mEntranceImageViews.length];
		for (int i = 0; i < animators.length - 1; i++) {
			AnimatorSet cardAS = new AnimatorSet();
			ImageView view = mEntranceImageViews[i];
			if (i == 0 || i == 1) {
				cardXs[i] = ENTRANCE_LEFT_IMAGEVIEW_MARGIN_LEFT;
				initCardXs[i] = 1920;
			} else {
				cardXs[i] = cardXs[i - 2] + mEntranceImageViewMargin + mCardWidth;
				initCardXs[i] = initCardXs[i - 2] + mEntranceImageViewMargin + mCardWidth;;
			}
			view.setX(initCardXs[i]);
			int column = i / 2;
			int startDelay = (i % 2 == 0) ? column * 100 : column * 100 + 50;
			ObjectAnimator oa = ObjectAnimator.ofFloat(view, "x", cardXs[i]);
			oa.setStartDelay(startDelay);
			oa.setDuration(1000);
			
			int delta = 10;
			ObjectAnimator ob = ObjectAnimator.ofFloat(view, "x", cardXs[i] + delta);
			ObjectAnimator oc = ObjectAnimator.ofFloat(view, "x", cardXs[i]);
			if (i == 0) {
				ObjectAnimator od = ObjectAnimator.ofFloat(mBorderFocusImageView, "x", cardXs[i] - mImageViewForcusOffset + delta);
				ObjectAnimator oe = ObjectAnimator.ofFloat(mBorderFocusImageView, "x", cardXs[i] - mImageViewForcusOffset);
				od.addListener(new AnimatorListener() {

					@Override
					public void onAnimationCancel(Animator animation) {
					}

					@Override
					public void onAnimationEnd(Animator animation) {
					}

					@Override
					public void onAnimationRepeat(Animator animation) {
					}

					@Override
					public void onAnimationStart(Animator animation) {
						mBorderFocusImageView.setVisibility(View.VISIBLE);
					}
					
				});
				
				AnimatorSet set1 = new AnimatorSet();
				set1.playSequentially(ob, oc);
				AnimatorSet set2 = new AnimatorSet();
				set2.playSequentially(od, oe);
			
				AnimatorSet set3 = new AnimatorSet();
				set3.playTogether(set1, set2);
				
				cardAS.playSequentially(oa, set3);
			} else {
				cardAS.playSequentially(oa, ob, oc);
			}
			
			animators[i] = cardAS;
		}
		if (mTitleGroupView == null) {
//			mTitleGroupView = EntrancePageWidget.this.findViewById(R.id.handbook_first_page_title_group);
		}
		mTitleGroupView.setAlpha(0f);
		AnimatorSet titleAs = new AnimatorSet();
		ObjectAnimator titleOa = ObjectAnimator.ofFloat(mTitleGroupView, "alpha", 1.0f);
		titleOa.setDuration(2000);
		titleAs.play(titleOa);
//		root.playSequentially(animators);
		animators[animators.length - 1] = titleAs;
		root.playTogether(animators);
		root.addListener(new AnimatorListener() {

			@Override
			public void onAnimationCancel(Animator animation) {
			}

			@Override
			public void onAnimationEnd(Animator animation) {
				mEntranceAnimatoring = false;
				EntrancePageWidget.this.setEnabled(true);
				mReflectImageView.setVisibility(View.VISIBLE);
				mBorderFocusImageView.setVisibility(View.VISIBLE);
			}

			@Override
			public void onAnimationRepeat(Animator animation) {
			}

			@Override
			public void onAnimationStart(Animator animation) {
				mEntranceAnimatoring = true;
				EntrancePageWidget.this.setEnabled(false);
				mReflectImageView.setVisibility(View.INVISIBLE);
				mBorderFocusImageView.setVisibility(View.INVISIBLE);
			}
			
		});
		root.start();
	}
	
	
	
	public boolean isEntranceAnimatoring() {
		return mEntranceAnimatoring;
	}
}
