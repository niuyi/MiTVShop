package com.xiaomi.mitv.shop.detail.widget;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.xiaomi.mitv.shop.App;
import com.xiaomi.mitv.shop.detail.AssertFileManager;
import com.xiaomi.mitv.shop.detail.BackPage;
import com.xiaomi.mitv.shop.detail.BaseApplication;
import com.xiaomi.mitv.shop.detail.Constants;
import com.xiaomi.mitv.shop.detail.ForwardPage;
import com.xiaomi.mitv.shop.R;
import com.xiaomi.mitv.shop.detail.ui.PlaySoundRelativeLayout;
import com.xiaomi.mitv.shop.detail.utils.Utils;

public class PostersControlWidget extends PlaySoundRelativeLayout implements BackPage, ForwardPage{
	private static final String TAG = "PostersControlWidget";
	
	private static final int IMAGEVIEW_COUNT = 3;
	private static final int ANIMATOR_DURATION_FOLDER_UNFOLDER = 300;
	private static final int ANIMATOR_DURATION_POSTER_SLIDE = 350;
	private static final int ANIMATOR_DURATION_CENTER_POSTER_CLICKED = 500;
	private static final int ANIMATOR_DURATION_CENTER_POSTER_ALPHA = 600;
	
	private int[] mPosterImageViewX; 
//	public static final int[] POSTER_IMAGEVIEWS_X = new int[]{-710, 480, 1670};
	public static final float[] POSTER_IMAGEVIEWS_SCALE = new float[]{1.0f, 1.1f, 1.0f};
	private int mPosterImageViewY;
	
	private TextView mTitleTextView; 
	private ImageView mBackgroundImageView;
	
	private PageProgressMarker mPageProgressMarker;
	private IndicatorGroup mIndicatorGroup;
	
	private Context mContext;

	private ImageView[] mPosterImageViews = new ImageView[IMAGEVIEW_COUNT];
	
	private int mCenterViewPosition;
	private int[] mViewPositionArray = new int[IMAGEVIEW_COUNT];
	
	private IssueAdapterData[] mInput;
	private int mCenterAdapterDataPosition;
	private int[] mAdapterDataPositionArray;
	
	private boolean mAnimatoring;
	private ImageView mFocusImageView;
	
	private AssetManager mAssetManager;
	
//	private Bitmap mBackgroundBitmap;
	
	private Handler mHandle = new Handler();
	
	private ViewGroup mPosterViewsContainer;
	private View mMaskContainer;
	private View mOtherContainer;

	//todo: delete this
	private View mPageBookContainer;
	private ImageView[] mPageBookImageViews = new ImageView[4];
	
	private boolean mShaking = false;
	
	private ForwardPage mForwardPageWidget;
	private BackPage mBackPageWidget;
	
	private ObjectAnimator mCenterPosterAlphaAnimator;
	
	private int mPosterWidth = 0;
	private final Detail mDetail;

	public PostersControlWidget(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		
		attachContents(context);
	
		this.setFocusable(false);
		this.mContext = context;
		this.mAssetManager = context.getAssets();

		mDetail = new Detail();

		Category category = new Category();
		category.title = "功能介绍";
		category.images.add("http://c1.mifile.cn/f/i/2014/cn/goods/mi4/md/gallery/gallery-list-a.png");
		category.images.add("http://c1.mifile.cn/f/i/2014/cn/goods/mi4/md/gallery/gallery-list-d_2x.jpg");
		mDetail.categories.add(category);

		category = new Category();
		category.title = "技术规格";
		category.images.add("http://c1.mifile.cn/f/i/2014/cn/goods/mi4/md/specs/specs-img-bs.jpg");
		category.images.add("http://c1.mifile.cn/f/i/2014/cn/goods/mi4/md/specs/space-img-04.jpg?140730");
		mDetail.categories.add(category);


		category = new Category();
		category.title = "包装参数";
		category.images.add("http://c1.mifile.cn/f/i/2014/cn/goods/mi4/md/gallery/gallery-list-e_2x.png");
		mDetail.categories.add(category);

		setupViews();

	}

	private void setupViews() {
		mOtherContainer = this.findViewById(R.id.handbook_second_page_other_container);
		mMaskContainer = this.findViewById(R.id.handbook_second_page_mask_container);
		
		mPageBookContainer = this.findViewById(R.id.handbook_second_page_pagebook_container);
		mPageProgressMarker = (PageProgressMarker) this.findViewById(R.id.handbook_second_page_pageprogressmarker);
		mFocusImageView = (ImageView) this.findViewById(R.id.handbook_second_page_center_focus_imageview);
		mTitleTextView = (TextView) this.findViewById(R.id.handbook_second_page_title_textview);
//		mBackgroundImageView = (ImageView) this.findViewById(R.id.handbook_second_page_background_imageview);
		
		mPageBookImageViews[0] = (ImageView) mPageBookContainer.findViewById(R.id.handbook_second_page_first_pagebook);
		mPageBookImageViews[1] = (ImageView) mPageBookContainer.findViewById(R.id.handbook_second_page_second_pagebook);
		mPageBookImageViews[2] = (ImageView) mPageBookContainer.findViewById(R.id.handbook_second_page_third_pagebook);
		mPageBookImageViews[3] = (ImageView) mPageBookContainer.findViewById(R.id.handbook_second_page_last_pagebook);
	}

	private void attachContents(Context context) {
		mPosterImageViewX = this.getResources().getIntArray(R.array.poster_imageview_x);
		mPosterImageViewY = (int) this.getResources().getDimension(R.dimen.second_page_poster_imageview_margin_top);
		ViewGroup root = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.widget_posters, null);
		LayoutParams lp = new LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
		this.addView(root, lp);
		
		mPosterViewsContainer = (ViewGroup) root.findViewById(R.id.handbook_second_page_posters_container);
		
		mPosterWidth = (int) context.getResources().getDimension(R.dimen.second_page_poster_imageview_width);
		int height = (int) context.getResources().getDimension(R.dimen.second_page_poster_imageview_height);
		int marginTop = (int) context.getResources().getDimension(R.dimen.second_page_poster_imageview_margin_top);
		for (int i = 0; i < 3; i++) {
			ImageView imageView = new ImageView(context);
			RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(mPosterWidth, height);
			layoutParams.topMargin = marginTop;
			mPosterViewsContainer.addView(imageView, layoutParams);
			
			int targetIndex = (i + 1) % IMAGEVIEW_COUNT;
			imageView.setX(mPosterImageViewX[1]);
			imageView.setScaleX(POSTER_IMAGEVIEWS_SCALE[targetIndex]);
			imageView.setScaleY(POSTER_IMAGEVIEWS_SCALE[targetIndex]);
			mPosterImageViews[i] = imageView;
		}
		mPosterImageViews[2].setVisibility(View.INVISIBLE);
//		mPosterImageViews[1].setVisibility(View.INVISIBLE);
		mPosterImageViews[0].bringToFront();

		Button b = (Button)root.findViewById(R.id.button_buy);
		b.requestFocus();
		b.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i(TAG, "buy");
			}
		});
	}

	private void loadDrawables(final String typeFragmentString, final LoadCallback callBack, Object object) throws IOException {
		Log.i(TAG, "load drawable start");
		mHandle.post(new Runnable() {

			@Override
			public void run() {
				PostersControlWidget.this.setFocusable(false);
				PostersControlWidget.this.setFocusableInTouchMode(false);
				
				PostersControlWidget.this.setVisibility(View.VISIBLE);
				mFocusImageView.setVisibility(View.INVISIBLE);
				mPageBookContainer.setVisibility(View.INVISIBLE);
				
				if (callBack != null) {
					callBack.onLoadStarted();
				}
			}
			
		});
		
//		String[] contents = mAssetManager.list(folderName);
		final List<IssueAdapterData> contentBitmapList = AssertFileManager.getTypeAdapterData(mAssetManager, typeFragmentString);

		for(IssueAdapterData data : contentBitmapList){
			Log.i(TAG, "get IssueAdapterData: " + data);
		}
//		Log.i(TAG, "cosume: " + (System.currentTimeMillis() - curr));
		Log.i(TAG, "start load center bitmap");
//		long time = System.currentTimeMillis();
//		final Bitmap centerShowPosterBitmap = Utils.loadAssetBitmap(mAssetManager, contentBitmapList.get(0).mPosterChilren[0], 2);
////		final Bitmap centerShowPosterBitmap = (Bitmap) object;
//		contentBitmapList.get(0).mBitmap = centerShowPosterBitmap;
//		Log.i(TAG, "load center bitmap finished, consume: " + (System.currentTimeMillis() - time) + " ms");
		mHandle.post(new Runnable() {

			@Override
			public void run() {
//				mPosterImageViews[0].setImageBitmap(centerShowPosterBitmap);
				contentBitmapList.get(0).mUrl = "http://c1.mifile.cn/f/i/2014/cn/goods/mi4/md/gallery/gallery-list-a.png";
				contentBitmapList.get(0).loadImage(mPosterImageViews[0]);

				mPosterImageViews[0].setAlpha(0f);
				mPosterImageViews[0].setScaleX(0.5f);
				mPosterImageViews[0].setScaleY(0.5f);
				mPosterImageViews[1].setVisibility(View.INVISIBLE);

				AnimatorSet mCenterPosterAnimatorSet = new AnimatorSet();
				mCenterPosterAlphaAnimator = ObjectAnimator.ofFloat(mPosterImageViews[0], "alpha", 1.0f);
				ObjectAnimator ob = ObjectAnimator.ofFloat(mPosterImageViews[0], "scaleX", 1.1f);
				ObjectAnimator oc = ObjectAnimator.ofFloat(mPosterImageViews[0], "scaleY", 1.1f);
				mCenterPosterAnimatorSet.playTogether(mCenterPosterAlphaAnimator, ob, oc);

				mCenterPosterAlphaAnimator.setDuration(ANIMATOR_DURATION_CENTER_POSTER_ALPHA);
				ob.setDuration(ANIMATOR_DURATION_CENTER_POSTER_ALPHA);
				oc.setDuration(ANIMATOR_DURATION_CENTER_POSTER_ALPHA);

				mCenterPosterAlphaAnimator.addListener(new AnimatorListenerAdapter() {

					@Override
					public void onAnimationStart(Animator animation) {
						super.onAnimationStart(animation);
						Log.i(TAG, "start center poster alpha animator~");
					}

					@Override
					public void onAnimationEnd(Animator animation) {
						mPosterImageViews[1].setVisibility(View.VISIBLE);
						mBackgroundImageView.setImageResource(R.drawable.detail_bg);
					}

				});
				PostersControlWidget.this.setVisibility(View.VISIBLE);
				mCenterPosterAnimatorSet.start();
				if (callBack != null) {
					callBack.onLoadVisibleBitmapsFinished();
				}
			}
		});
		Log.i(TAG, "start load background");
		Log.i(TAG, "load background finish");
//		mHandle.post(new Runnable() {
//
//			@Override
//			public void run() {
////				mBackgroundImageView.setImageBitmap(mBackgroundBitmap);
////				mBackgroundImageView.setAlpha(0.2f);
////				ObjectAnimator oa = ObjectAnimator.ofFloat(mBackgroundImageView, "alpha", 1.0f);
////				oa.setDuration(300);
////				oa.start();
//			}
//
//		});
//		for (int i = 1; i < contentBitmapList.size(); i++) {
//			contentBitmapList.get(i).mBitmap = Utils.loadAssetBitmap(mAssetManager, contentBitmapList.get(i).mPosterChilren[0], 2);
//		}
//		Log.e(TAG, "load " + contents.length + " images, consume: " + (System.currentTimeMillis() - current));
		final IssueAdapterData[] mContentBitmaps = new IssueAdapterData[contentBitmapList.size()];
		contentBitmapList.toArray(mContentBitmaps);
		mHandle.post(new Runnable() {

			@Override
			public void run() {
				setInput(mContentBitmaps);
				AnimatorSet as = getFolderUnfolderAnimator(true);
				as.addListener(new AnimatorListenerAdapter() {

					@Override
					public void onAnimationStart(Animator animation) {
					}

					@Override
					public void onAnimationEnd(Animator animation) {
						PostersControlWidget.this.setFocusable(true);
						PostersControlWidget.this.setFocusableInTouchMode(true);
						PostersControlWidget.this.requestFocus();
						mFocusImageView.setVisibility(View.VISIBLE);
						int total = mInput[mCenterAdapterDataPosition].mChildCount;
						if (total == 1) {
							mPageProgressMarker.setVisibility(View.INVISIBLE);
						} else {
							mPageProgressMarker.setVisibility(View.VISIBLE);
						}
						mPageProgressMarker.setCurrentIndex(1);
						mPageProgressMarker.setTotal(total);

						int index = getTypeFragmentIndex(typeFragmentString);
						Log.i(TAG, "typeFragmentString: " + typeFragmentString + " index: " + index);
						int[] pagebookResIds = BaseApplication.getInstance().getResConstants().getPageBookResIds()[index];
						long curr = System.currentTimeMillis();
						for (int i = 0; i < mPageBookImageViews.length; i ++) {
							mPageBookImageViews[i].setImageResource(pagebookResIds[i]); //need about 20ms
						}
						Log.i(TAG, "set pagebook image res, consume " + (System.currentTimeMillis() - curr) + " ms");
						updatePageBooksVisible(total);
						mPageBookContainer.setVisibility(View.GONE);

						if (callBack != null) {
							callBack.onLoadFinished();
						}
					}

				});
				Log.i(TAG, "alpha: " + mPosterImageViews[0].getAlpha());
				if (mCenterPosterAlphaAnimator != null) {
					long currentPlayTime = mCenterPosterAlphaAnimator.getCurrentPlayTime();
					long duration = mCenterPosterAlphaAnimator.getDuration();
					if (currentPlayTime > 0 && currentPlayTime < duration) {
						as.setStartDelay(duration - currentPlayTime);
						Log.i(TAG, "start delay : " + (duration - currentPlayTime));
					}
					Log.i(TAG, "currentPlayTime: " + currentPlayTime + " duration:" + mCenterPosterAlphaAnimator.getDuration());
				}
				Log.i(TAG, "as.Start");
				as.start();
			}
		});
	}

	private int getTypeFragmentIndex(String typeFragmentString) {
		String[] fragments = BaseApplication.getInstance().getResConstants().getFragments();
		for (int i = 0; i < fragments.length; i++) {
			if (typeFragmentString.equals(fragments[i])) {
				return i;
			}
		}
		return 0;
	}

	private AnimatorSet getFolderUnfolderAnimator(boolean unfolder) {
		AnimatorSet as = new AnimatorSet();
		int rightTargetX = unfolder ?  mPosterImageViewX[2] : mPosterImageViewX[1];
		int leftTargetX = unfolder ?  mPosterImageViewX[0] : mPosterImageViewX[1];
		ImageView leftPosterImageView = mPosterImageViews[mViewPositionArray[0]];
		ImageView rightPosterImageView = mPosterImageViews[mViewPositionArray[2]];
//		rightPosterImageView.setVisibility(View.VISIBLE);
		
		ObjectAnimator ob = ObjectAnimator.ofFloat(rightPosterImageView, "x", rightTargetX);
		ObjectAnimator oc = ObjectAnimator.ofFloat(leftPosterImageView, "x", leftTargetX);
		
		ob.setDuration(ANIMATOR_DURATION_FOLDER_UNFOLDER);
		oc.setDuration(ANIMATOR_DURATION_FOLDER_UNFOLDER);
		
		as.playTogether(ob, oc);
		return as;
	}
	
	public void load(final Bundle bundle, final LoadCallback callBack, final Object object) {
		final int entranceIndex = bundle.getInt(Constants.BUNDLE_KEY_ENTRANCE_PAGE_CLICK_INDEX);
		mTitleTextView.setAlpha(0f);
//		mTitleTextView.setText(BaseApplication.getInstance().getResConstants().getTitleResIds()[entranceIndex]);
		mTitleTextView.setText(mDetail.name + ":" + mDetail.categories.get(0).title);
		ObjectAnimator oa = ObjectAnimator.ofFloat(mTitleTextView, "alpha", 1.0f);
		oa.setDuration(700);
		oa.start();
		new Thread(new Runnable(){

			@Override
			public void run() {
				try {
					loadDrawables(BaseApplication.getInstance().getResConstants().getFragments()[entranceIndex], callBack, object);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		}).start();
	}
	
	public void setInput(IssueAdapterData[] input) {
		mAnimatoring = false;
		mInput = input;

		//for test
		mInput[0].mUrl = "http://c1.mifile.cn/f/i/2014/cn/goods/mi4/md/gallery/gallery-list-a.png";
		mInput[0].mPosterChilren = new String[]{"http://c1.mifile.cn/f/i/2014/cn/goods/mi4/md/gallery/gallery-list-a.png"};

		mInput[1].mUrl = "http://c1.mifile.cn/f/i/2014/cn/goods/mi4/md/specs/specs-img-bs.jpg";
		mInput[1].mPosterChilren = new String[]{"http://c1.mifile.cn/f/i/2014/cn/goods/mi4/md/specs/specs-img-bs.jpg"};

		mInput[2].mUrl = "http://c1.mifile.cn/f/i/2014/cn/goods/mi4/md/gallery/gallery-list-e_2x.png";
		mInput[2].mPosterChilren = new String[]{"http://c1.mifile.cn/f/i/2014/cn/goods/mi4/md/gallery/gallery-list-e_2x.png"};
		mInput[2].mPosterChilren = new String[]{"http://c1.mifile.cn/f/i/2014/cn/goods/mi4/md/specs/space-img-04.jpg?140730"};
		mInput[2].mChildCount = 2;

		mAdapterDataPositionArray = new int[mDetail.categories.size()];
		
		mIndicatorGroup = new IndicatorGroup(mContext, mDetail.categories.size());
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.topMargin = 900;
		lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
		mPosterViewsContainer.addView(mIndicatorGroup, lp);
		mIndicatorGroup.setCurrentIndex(0);
		
		mCenterViewPosition = 0;
		mCenterAdapterDataPosition = 0;
//		mPosterImageViews[1].setImageBitmap(mInput[1].mBitmap);

		mInput[1].loadImage(mPosterImageViews[1]);
		mInput[mAdapterDataPositionArray.length - 1].loadImage(mPosterImageViews[2]);
//		mPosterImageViews[2].setImageBitmap(mInput[mAdapterDataPositionArray.length - 1].mBitmap);
//		Picasso.with(getContext()).load(mDetail.categories.get(mDetail.categories.size() - 1).images.get(0)).into(mPosterImageViews[2]);
		mPosterImageViews[2].setVisibility(View.INVISIBLE);
		onCenterIndexChanged();
	}

	private void resetPosterImageViews() {
		Log.i(TAG, "reset Poster ImageViews");
		for (int i = 0; i < mPosterImageViews.length; i++) {
			int targetIndex = (i + 1) % IMAGEVIEW_COUNT;
			mPosterImageViews[i].setX(mPosterImageViewX[1]);
			mPosterImageViews[i].setScaleX(POSTER_IMAGEVIEWS_SCALE[targetIndex]);
			mPosterImageViews[i].setScaleY(POSTER_IMAGEVIEWS_SCALE[targetIndex]);
		}
		mPosterImageViews[0].bringToFront();
	}
	
	private void updateLocalArrarys() {
		mViewPositionArray[1] = mCenterViewPosition;
		mViewPositionArray[0] = mCenterViewPosition - 1 >= 0 ? mCenterViewPosition - 1: IMAGEVIEW_COUNT - 1;
		mViewPositionArray[2] = mCenterViewPosition + 1 == IMAGEVIEW_COUNT ? 0 : mCenterViewPosition + 1;
	
		mAdapterDataPositionArray[1] = mCenterAdapterDataPosition;
		mAdapterDataPositionArray[0] = mCenterAdapterDataPosition - 1 >= 0 ? mCenterAdapterDataPosition - 1 : mAdapterDataPositionArray.length - 1;
		for (int i = 2; i < mAdapterDataPositionArray.length; i++) {
			mAdapterDataPositionArray[i] = mAdapterDataPositionArray[i - 1] + 1 > mAdapterDataPositionArray.length - 1 ? (mAdapterDataPositionArray[i - 1] + 1) % mAdapterDataPositionArray.length : mAdapterDataPositionArray[i - 1] + 1;  
		}
	}

	private void handleLeftRightPressed(final boolean leftPressed, final boolean open) {
		if (mAnimatoring || mShaking) {
			return;
		}
		if (leftPressed && mCenterAdapterDataPosition == 0) {
			//到达最左边
			playErrorSound();
			startShakeAnimator(leftPressed);
			return;
		}
		if (!leftPressed && mCenterAdapterDataPosition == mAdapterDataPositionArray.length - 1) {
			//到达最右边
			startShakeAnimator(leftPressed);
			playErrorSound();
			return;
		}
		playKeyStoneSound();
		AnimatorSet root = new AnimatorSet();
		AnimatorSet slideAs =  getPosterSlideAnimatorSet(leftPressed);
		root.playTogether(slideAs);
		root.addListener(new AnimatorListenerAdapter() {

			@Override
			public void onAnimationEnd(Animator animation) {
				Log.i(TAG, "onAnimationEnd");
				if (leftPressed) {
					mCenterViewPosition = mCenterViewPosition - 1 >= 0 ? mCenterViewPosition - 1 : IMAGEVIEW_COUNT - 1;
					mCenterAdapterDataPosition = mCenterAdapterDataPosition - 1 >= 0 ? mCenterAdapterDataPosition - 1 : mAdapterDataPositionArray.length - 1;
				} else {
					mCenterViewPosition = (mCenterViewPosition + 1) % IMAGEVIEW_COUNT;
					mCenterAdapterDataPosition = (mCenterAdapterDataPosition + 1) % mAdapterDataPositionArray.length;
				}
				onCenterIndexChanged();
				mFocusImageView.setVisibility(View.VISIBLE);
//				mFocusImageView.bringToFront();
				if (mInput[mCenterAdapterDataPosition].mChildCount == 1) {
					mPageProgressMarker.setVisibility(View.INVISIBLE);
				} else {
					mPageProgressMarker.setCurrentIndex(mInput[mCenterAdapterDataPosition].mCurrentSelectedPosterIndex + 1);
					mPageProgressMarker.setTotal(mInput[mCenterAdapterDataPosition].mChildCount);
					mPageProgressMarker.setVisibility(View.VISIBLE);
				}
				mPosterImageViews[mCenterViewPosition].bringToFront();
				updatePageBooksVisible(mInput[mCenterAdapterDataPosition].mChildCount);
				mPageBookContainer.setVisibility(View.GONE);
				mAnimatoring = false;
				if (open) {
					okPressed(false);
				}
			}

			@Override
			public void onAnimationStart(Animator animation) {
				mAnimatoring = true;
				Log.i(TAG, "onAnimationStart");
				mFocusImageView.setVisibility(View.INVISIBLE);
				mPageProgressMarker.setVisibility(View.INVISIBLE);
				mPageBookContainer.setVisibility(View.INVISIBLE);
			}
			
		});
		root.start();
	}

	private AnimatorSet getPosterSlideAnimatorSet(final boolean leftPressed) {
		AnimatorSet root = new AnimatorSet();
		List<AnimatorSet> children = new ArrayList<AnimatorSet>();
		for (int i = 0; i < IMAGEVIEW_COUNT; i++) {
			int currentIndex = getCurrentIndex(i);
			int targetIndex = getTargetIndex(currentIndex, leftPressed);
			final ImageView animatorImageView = mPosterImageViews[i];
			if ((targetIndex == 0 && leftPressed) || (targetIndex == 2 && !leftPressed)) { 
				AnimatorSet as = getEnterExitAnimatorSet(currentIndex, targetIndex, animatorImageView, leftPressed);
				children.add(as);
			} else {
				AnimatorSet as = new AnimatorSet();
				ObjectAnimator oa = ObjectAnimator.ofFloat(mPosterImageViews[i], "x", mPosterImageViewX[targetIndex]);
				ObjectAnimator ob = ObjectAnimator.ofFloat(mPosterImageViews[i], "scaleX", POSTER_IMAGEVIEWS_SCALE[targetIndex]);
				ObjectAnimator oc = ObjectAnimator.ofFloat(mPosterImageViews[i], "scaleY", POSTER_IMAGEVIEWS_SCALE[targetIndex]);
				int duration = ANIMATOR_DURATION_POSTER_SLIDE;
				oa.setDuration(duration);
				ob.setDuration(duration);
				oc.setDuration(duration);
				as.playTogether(oa, ob, oc);
				children.add(as);
			}
		}
		AnimatorSet[] chidrenArray = new AnimatorSet[children.size()];
		children.toArray(chidrenArray);
		root.playTogether(chidrenArray);
		return root;
	}

	private AnimatorSet getEnterExitAnimatorSet(final int currentIndex, int targetIndex, final ImageView animatorImageView, final boolean leftPressed) {
		AnimatorSet as = new AnimatorSet();
		
		int exitTargetX = leftPressed ? 1920 : -1920;
		final int enterStartX = leftPressed ? -mPosterWidth: 1920; 
		
		ObjectAnimator exitAnimator = ObjectAnimator.ofFloat(animatorImageView, "x", exitTargetX); 
		int exitDuration = ANIMATOR_DURATION_POSTER_SLIDE / 2 - 50;
		exitAnimator.setDuration(exitDuration);
		exitAnimator.addListener(new AnimatorListenerAdapter() {

			@Override
			public void onAnimationEnd(Animator animation) {
				int targetBitmapPosition = 0;
				int targetCenterPosition = 0;
				if (leftPressed) {
					targetBitmapPosition = mCenterAdapterDataPosition - 2 >= 0 ? mCenterAdapterDataPosition - 2 :  mCenterAdapterDataPosition - 2 + mAdapterDataPositionArray.length;
					targetCenterPosition = mCenterAdapterDataPosition - 1;
				} else {
					targetBitmapPosition = (mCenterAdapterDataPosition + 2) % mAdapterDataPositionArray.length; 
					targetCenterPosition = mCenterAdapterDataPosition + 1;
				}
//				Log.e(TAG, "target bitmap position : " + targetBitmapPosition + " mCenterAdapterDataPosition: " + mCenterAdapterDataPosition);
				//换图
//				animatorImageView.setImageBitmap(mInput[targetBitmapPosition].mBitmap);
				mInput[targetBitmapPosition].loadImage(animatorImageView);
				//是否显示
				if (targetCenterPosition == 0 && targetBitmapPosition == mAdapterDataPositionArray.length - 1) {
					animatorImageView.setVisibility(View.INVISIBLE);
				} else if (targetCenterPosition == mAdapterDataPositionArray.length - 1 && targetBitmapPosition == 0) {
					animatorImageView.setVisibility(View.INVISIBLE);
				} else {
					animatorImageView.setVisibility(View.VISIBLE);
				}
				//定位
				animatorImageView.setX(enterStartX);
			}
		});
		ObjectAnimator enterAnimator = ObjectAnimator.ofFloat(animatorImageView, "x", mPosterImageViewX[targetIndex]); 
		int enterDuration = ANIMATOR_DURATION_POSTER_SLIDE / 2;
		enterAnimator.setDuration(enterDuration);
		int startDelay = 50;
		enterAnimator.setStartDelay(startDelay);
		as.playSequentially(exitAnimator, enterAnimator);
		return as;
	}

	private void startShakeAnimator(boolean leftPressed) {
		AnimatorSet as = new AnimatorSet();
		int distance = leftPressed ? 50 : -50;
		List<Animator> children = new ArrayList<Animator>();
		for (int i = 0; i < mPosterImageViews.length; i++) {
			ImageView imageView = mPosterImageViews[i];
			children.add(getShakingAnimator(distance, imageView));
		}
		children.add(getShakingAnimator(distance, mFocusImageView));
		children.add(getShakingAnimator(distance, mPageProgressMarker));
		children.add(getShakingAnimator(distance, mPageBookContainer));
		
		as.playTogether(children);

		as.addListener(new AnimatorListenerAdapter() {

			@Override
			public void onAnimationStart(Animator animation) {
				mShaking = true;
			}

			@Override
			public void onAnimationEnd(Animator animation) {
				mShaking = false;
			}
		});
		as.start();
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

	private void onCenterIndexChanged() {
		updateLocalArrarys();
		Log.i(TAG, "mCenterAdapterDataPosition: " + mCenterAdapterDataPosition);
		mIndicatorGroup.setCurrentIndex(mCenterAdapterDataPosition);
		mTitleTextView.setText(mDetail.name + " : " + mDetail.categories.get(mCenterAdapterDataPosition).title);
	}

	private int getTargetIndex(int currentIndex, boolean leftPressed) {
//		Log.e(TAG, "current index: " + currentIndex);
		int targetIndex = !leftPressed ? currentIndex - 1: currentIndex + 1;
		if (targetIndex < 0) {
			targetIndex = 2;
		}
		if (targetIndex > IMAGEVIEW_COUNT - 1) {
			targetIndex = 0;
		}
		return targetIndex;
	}

	private int getCurrentIndex(int viewPosition) {
		for (int i = 0; i < mViewPositionArray.length; i++) {
			if (mViewPositionArray[i] == viewPosition) {
				return i;
			}
		}
		return 0;
	}

	public static class IssueAdapterData {
		public String mParentFolderName;
		public String mName;
		private Bitmap mBitmap;
		public String[] mPosterChilren;
		public String mUrl;
		public int mChildCount = 1;
		public int mCurrentSelectedPosterIndex = 0;


		public String toString(){
			StringBuilder sb = new StringBuilder();

			sb.append("mParentFolderName: ").append(mParentFolderName).append("\r\n");
			sb.append("mName: ").append(mName).append("\r\n");
			sb.append("mPosterChilren: ").append(Arrays.asList(mPosterChilren)).append("\r\n");

			return sb.toString();
		}

		public void loadImage(ImageView imageView){
			if(mBitmap != null){
				Log.i(TAG, "mBitmap not null: " + mUrl);
				imageView.setImageBitmap(mBitmap);
			}else{
				Picasso.with(App.getInstance().getApplicationContext()).load(mUrl).into(imageView, new Callback(){
					@Override
					public void onSuccess()
					{
						Log.i(TAG, "load success for: " + mUrl);
//						try {
//							mBitmap = Picasso.with(App.getInstance().getApplicationContext()).load(mUrl).get();
//						} catch (IOException e) {
//							e.printStackTrace();
//						}
					}

					@Override
					public void onError() { }
				});
			}
		}
	}

	private void okPressed(boolean userCall) {
		Log.i(TAG, "ok pressed, user call? " + userCall);
		this.setFocusable(false);
		playKeyStoneSound();
		mFocusImageView.setVisibility(View.INVISIBLE);
		mIndicatorGroup.setVisibility(View.INVISIBLE);
		
		mPageProgressMarker.setVisibility(View.INVISIBLE);
		mOtherContainer.setVisibility(View.INVISIBLE);
		mMaskContainer.setVisibility(View.INVISIBLE);
		
		ImageView centerImageView = mPosterImageViews[mCenterViewPosition];
		centerImageView.bringToFront();
		
		IssueAdapterData selectedAdapterData = mInput[mCenterAdapterDataPosition];
//		Log.i(TAG, "ok press mInput[mCenterAdapterDataPosition]: " + mInput[mCenterAdapterDataPosition] + " selectedAdapterData: " + selectedAdapterData + " mCurrentSelectedPosterIndex: " + selectedAdapterData.mCurrentSelectedPosterIndex);
		
		Bundle bundle = new Bundle();
		bundle.putStringArray(Constants.BUNDLE_KEY_POSTERS_PAGE_POSTER_PATH, selectedAdapterData.mPosterChilren);
		bundle.putInt(Constants.BUNDLE_KEY_POSTERS_PAGE_POSTER_SELECTED_INDEX, selectedAdapterData.mCurrentSelectedPosterIndex);
		bundle.putInt(Constants.BUNDLE_KEY_POSTERS_PAGE_POSTER_ISSUE_INDEX, mCenterAdapterDataPosition);
		
		mForwardPageWidget.load(bundle, new LoadCallback.Stub(), mInput);
		
		AnimatorSet as = getCenterClickPosterImageViewAnimator(true, userCall);
		as.addListener(new AnimatorListenerAdapter() {
			
			@Override
			public void onAnimationStart(Animator animation) {
				Log.i(TAG, "Center Click Poster animator start~~");
				mForwardPageWidget.setBackPageAnimatoring(true);
			}

			@Override
			public void onAnimationEnd(Animator animation) {
				Log.i(TAG, "Center Click Poster animator end~~");
				mForwardPageWidget.setShowOrHide(true);
				PostersControlWidget.this.setVisibility(View.INVISIBLE);
				mBackgroundImageView.setVisibility(View.INVISIBLE);
				mForwardPageWidget.setBackPageAnimatoring(false);
			}
		});
		as.start();
	}

	private AnimatorSet getCenterClickPosterImageViewAnimator(boolean zoomIn, boolean userCall) {
		ImageView centerImageView = mPosterImageViews[mCenterViewPosition];
		float targetScale = zoomIn ? 2f: 1.1f;
		int zoomInTargetY = (int) this.getResources().getDimension(R.dimen.sencond_page_center_imageview_scale_targetY);
//		float targetY = zoomIn ? 270f : POSTER_IMAGEVIEWS_Y;
		float targetY = zoomIn ? zoomInTargetY : mPosterImageViewY;
		AnimatorSet as = new AnimatorSet();
		ObjectAnimator oa = ObjectAnimator.ofFloat(centerImageView, "scaleX", targetScale);
		ObjectAnimator ob = ObjectAnimator.ofFloat(centerImageView, "scaleY", targetScale);
		ObjectAnimator oc = ObjectAnimator.ofFloat(centerImageView, "y", targetY);
		int duration = userCall ? ANIMATOR_DURATION_CENTER_POSTER_CLICKED : 300;
		oa.setDuration(duration);
		ob.setDuration(duration);
		oc.setDuration(duration);
		as.playTogether(oa, ob, oc);
		return as;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT || keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
			handleLeftRightPressed(keyCode == KeyEvent.KEYCODE_DPAD_LEFT, false);
			return true;
		}
		if (keyCode == KeyEvent.KEYCODE_ENTER && !mAnimatoring && !mShaking && event.getRepeatCount() == 0) {
			okPressed(true);
			return true;
		}
		if (keyCode == KeyEvent.KEYCODE_DPAD_UP || keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
			playErrorSound();
			return true;
		}
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (event.getRepeatCount() == 0) {
				Log.i(TAG, "KEYCODE_BACK");
				AnimatorSet as = getFolderUnfolderAnimator(false);
				mPosterImageViews[mCenterViewPosition].bringToFront();
				mPosterViewsContainer.invalidate();
				as.addListener(new AnimatorListenerAdapter() {

					@Override
					public void onAnimationEnd(Animator animation) {
						PostersControlWidget.this.setVisibility(View.INVISIBLE);
						cleanup();
						mBackPageWidget.onBackToPage(null, null);
					}
					
				});
				as.start();
			} 
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void cleanup() {
		Log.i(TAG, "cleanup");
		resetPosterImageViews();
		if (mFocusImageView != null) {
			mFocusImageView.bringToFront();
		}
		if (mIndicatorGroup != null) {
			mPosterViewsContainer.removeView(mIndicatorGroup);
		}
		if (mPageProgressMarker != null) {
			mPageProgressMarker.cleanup();
			mPageProgressMarker.setVisibility(View.INVISIBLE);
		}
		if (mPosterImageViews != null) {
			for (ImageView imageView : mPosterImageViews) {
				imageView.setImageBitmap(null);
				imageView.setVisibility(View.VISIBLE);
			}
		}
		if (mInput != null) {
			for (int i = 0; i < mInput.length; i++) {
				IssueAdapterData data  = mInput[i];
//				if (data != null && data.mBitmap != null && i != 0) {
				if (data != null && data.mBitmap != null) {
					Log.i(TAG, "recycle poster bitmap");
					data.mBitmap.recycle();
					data.mBitmap = null;
				}
			}
		}
		if (mBackgroundImageView != null) {
			mBackgroundImageView.setImageBitmap(null);
		}
//		if (mBackgroundBitmap != null) {
//			Log.i(TAG, "recycle background bitmap");
//			mBackgroundBitmap.recycle();
//			mBackgroundBitmap = null;
//		}
//
	}

	public void onBackToPage(Object object, Bundle bundle) {
		mTitleTextView.setAlpha(0);
		ObjectAnimator oa = ObjectAnimator.ofFloat(mTitleTextView, "alpha", 1.0f);
		oa.setDuration(700);
		oa.start();
		
		final boolean userCall = bundle.getBoolean(Constants.BUNDLE_KEY_POSTERS_PAGE_USER_CALL_BACK, true);
		int issueIndex = bundle.getInt(Constants.BUNDLE_KEY_POSTERS_PAGE_POSTER_ISSUE_INDEX, 0);
		int currentSelectedIndex = bundle.getInt(Constants.BUNDLE_KEY_POSTERS_PAGE_POSTER_SELECTED_INDEX, 0);
		final boolean leftPressed = bundle.getBoolean(Constants.BUNDLE_KEY_POSTERS_PAGE_LEFT_PRESSED, false);
		
		mInput[issueIndex].mCurrentSelectedPosterIndex = currentSelectedIndex;
		Log.i(TAG, "mCenterAdapterDataPosition: " + mCenterAdapterDataPosition + "  mCurrentSelectedPosterIndex: " + currentSelectedIndex + " object: " + mInput[mCenterAdapterDataPosition]);
//		if (mCenterAdapterDataPosition != arg[1]) { //全屏时切换了问题
//			mCenterAdapterDataPosition = arg[1];
//			Log.e(TAG, "full screen change the issues!!!!!!!, mCenterAdapterDataPosition: " + mCenterAdapterDataPosition);
//			onCenterIndexChanged();
//			int leftViewPosition = mCenterViewPosition - 1 >= 0 ? mCenterViewPosition - 1 : mCenterViewPosition - 1 + IMAGEVIEW_COUNT; 
//			int rightViewPosition = mCenterViewPosition + 1 <= IMAGEVIEW_COUNT - 1 ? mCenterViewPosition + 1 : (mCenterViewPosition + 1) % IMAGEVIEW_COUNT; 
//			for (int i = 0; i < IMAGEVIEW_COUNT; i++) {
//				mPosterImageViews[i].setVisibility(View.VISIBLE);
//			}
//			Log.i(TAG, "center view position: " + mCenterViewPosition + " left :" + leftViewPosition + " right: " + rightViewPosition);
//			//换左右两边图
//			if (mCenterAdapterDataPosition == 0) {
//				mPosterImageViews[leftViewPosition].setVisibility(View.INVISIBLE);
//			} else {
//				mPosterImageViews[leftViewPosition].setImageBitmap(mInput[mCenterAdapterDataPosition - 1].mBitmap);
//			}
//			if (mCenterAdapterDataPosition == mInput.length - 1) {
//				mPosterImageViews[rightViewPosition].setVisibility(View.INVISIBLE);
//			} else {
//				mPosterImageViews[rightViewPosition].setImageBitmap(mInput[mCenterAdapterDataPosition + 1].mBitmap);
//			}
//		}
		updatePageBooksVisible(mInput[mCenterAdapterDataPosition].mChildCount);
		
		this.setVisibility(View.VISIBLE);
		mOtherContainer.setVisibility(View.VISIBLE);
		mBackgroundImageView.setVisibility(View.VISIBLE);
		
		AnimatorSet as = getCenterClickPosterImageViewAnimator(false, userCall);
		if (object != null && object instanceof Bitmap) {
			mInput[mCenterAdapterDataPosition].mBitmap = (Bitmap) object;
			mPosterImageViews[mCenterViewPosition].setImageBitmap((Bitmap) object);
		}
//		
		as.addListener(new AnimatorListenerAdapter() {

			@Override
			public void onAnimationEnd(Animator animation) {
				mFocusImageView.setVisibility(View.VISIBLE);
				mIndicatorGroup.setVisibility(View.VISIBLE);
				mMaskContainer.setVisibility(View.VISIBLE);
				IssueAdapterData centerAdapterData = mInput[mCenterAdapterDataPosition];
				if (centerAdapterData.mChildCount == 1) {
					mPageProgressMarker.setVisibility(View.INVISIBLE);
				} else {
					mPageProgressMarker.setCurrentIndex(centerAdapterData.mCurrentSelectedPosterIndex + 1);
					mPageProgressMarker.setVisibility(View.VISIBLE);
				}
				PostersControlWidget.this.setFocusable(true);
				if (!userCall) {
					handleLeftRightPressed(leftPressed, true);
				}
			}
			
		});
		as.start();
	}
	
	public void setForwardPageWidget(ForwardPage forwardPageWidget) {
		this.mForwardPageWidget = forwardPageWidget;
	}
	
	public void setBackPageWidget(BackPage backPageWidget) {
		this.mBackPageWidget = backPageWidget;
	}

	@Override
	public void setShowOrHide(boolean show) {
	}
	
	public void setBackgroundImageView(ImageView backgroundImageView) {
		this.mBackgroundImageView = backgroundImageView;
	}

	@Override
	public void setBackPageAnimatoring(boolean animatoring) {
	}
	
	public void updatePageBooksVisible(int pageCount) {

//		return;
		if (pageCount < 1) {
			return;
		}
		if (pageCount > 5) {
			pageCount = 5;
		}
		ObjectAnimator[] alphaAnimators = new ObjectAnimator[pageCount - 1];
		for (int i = 0; i < pageCount - 1; i++) {
			if (i > (mPageBookImageViews.length - 1)) {
				break;
			}
			mPageBookImageViews[i].setAlpha(0f);
			mPageBookImageViews[i].setVisibility(View.VISIBLE);
			alphaAnimators[i] = ObjectAnimator.ofFloat(mPageBookImageViews[i], "alpha", 1.0f);
			alphaAnimators[i].setDuration(60);
		}
		if (alphaAnimators.length != 0) {
			AnimatorSet as = new AnimatorSet();
			as.playSequentially(alphaAnimators);
			as.start();
		}
		for (int i = pageCount - 1; i < mPageBookImageViews.length; i ++) {
			mPageBookImageViews[i].setVisibility(View.INVISIBLE);
		}
	}

	class Detail{
		String name = "小米手机4";
		String price = "¥1999";
		List<Category> categories = new ArrayList<Category>();
	}

	class Category{
		String title;
		List<String> images = new ArrayList<String>();
		List<Integer> images2 = new ArrayList<Integer>();
	}
}
