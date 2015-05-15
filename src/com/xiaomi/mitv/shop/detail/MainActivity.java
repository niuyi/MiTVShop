package com.xiaomi.mitv.shop.detail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.xiaomi.mitv.shop.R;
import com.xiaomi.mitv.shop.detail.widget.EntrancePageWidget;
import com.xiaomi.mitv.shop.detail.widget.PostersControlWidget;
import com.xiaomi.mitv.shop.detail.widget.ShowFullScreenImageWidget;


public class MainActivity extends Activity {
	private static final String TAG = "MainActivity";
	
//	private EntrancePageWidget mEntrancePageWidget;
	private PostersControlWidget mPostersControlWidget;
	private ShowFullScreenImageWidget mFullScreenWidget;
	
	private ImageView mBackgroundImageView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setupViews();
		
	}
	
	private void setupViews() {
//		mEntrancePageWidget = (EntrancePageWidget) this.findViewById(R.id.handbook_first_page_widget);
		mPostersControlWidget = (PostersControlWidget) this.findViewById(R.id.handbook_second_page_widget);
		mFullScreenWidget = (ShowFullScreenImageWidget) this.findViewById(R.id.handbook_third_page_widget);
		
		mBackgroundImageView = (ImageView) this.findViewById(R.id.handbook_main_background_imageview);
		
//		mEntrancePageWidget.setForwardPageWidget(mPostersControlWidget);
//
//		mPostersControlWidget.setBackPageWidget(mEntrancePageWidget);
		mPostersControlWidget.setForwardPageWidget(mFullScreenWidget);
		mPostersControlWidget.setBackgroundImageView(mBackgroundImageView);
		
		mFullScreenWidget.setBackPageWidget(mPostersControlWidget);
		
		
		ViewGroup fullScreenIndicatorContainer = (ViewGroup) this.findViewById(R.id.handbook_forth_page_container);
		mFullScreenWidget.setIndicatorContainer(fullScreenIndicatorContainer);
		mFullScreenWidget.setLeftArrowImageView((ImageView) this.findViewById(R.id.handbook_forth_page_left_arrow_imageview));
		mFullScreenWidget.setRightArrowImageView((ImageView) this.findViewById(R.id.handbook_forth_page_right_arrow_imageview));
		
//		mEntrancePageWidget.startEntranceAnimation();
//		Intent intent = getIntent();
//		if (intent != null) {
//			boolean showAnimator = intent.getBooleanExtra("show_animator", false);
//			if (showAnimator) {
//				mEntrancePageWidget.startEntranceAnimation();
//			}
//		}

		Bundle bundle = new Bundle();
		bundle.putInt(Constants.BUNDLE_KEY_ENTRANCE_PAGE_CLICK_INDEX, 0);

		mPostersControlWidget.load(bundle, new ForwardPage.LoadCallback() {

			@Override
			public void onLoadFinished() {
				Log.i(TAG, "PostersControlWidget onLoadFinished");
//				mPostersControlWidget.setVisibility(View.INVISIBLE);
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
	}

	
	
	@Override
	public void onBackPressed() {
		Log.i(TAG, "onBackPressed");
//		Log.i(TAG, "mEntrancePageWidget.getVisibility(): " + mEntrancePageWidget.getVisibility());
		Log.i(TAG, "mFullScreenWidget.getVisibility(): " + mFullScreenWidget.getVisibility());
		Log.i(TAG, "mPostersControlWidget.getVisibility(): " + mPostersControlWidget.getVisibility());
		if (mPostersControlWidget.getVisibility() == View.VISIBLE) {
			super.onBackPressed();
		} else {
			if (mPostersControlWidget.getVisibility() == View.INVISIBLE && mFullScreenWidget.getVisibility() == View.INVISIBLE) {
				super.onBackPressed();
			}
		}
	}

//	@Override
//	protected void onStart() {
//		mEntrancePageWidget.onActivityStart();
//		super.onStart();
//	}
//
//	@Override
//	protected void onStop() {
//		mEntrancePageWidget.onActivityStop();
//		super.onStop();
//	}
	
}
