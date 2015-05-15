package com.xiaomi.mitv.shop.detail.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.xiaomi.mitv.shop.R;


public class PageProgressMarker extends LinearLayout {
	private TextView mTotalTextView;
	private TextView mCurrentTextView;
	
	public PageProgressMarker(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		attachContent(context);
		setupViews();
	}

	private void attachContent(Context context) {
		LinearLayout container = new LinearLayout(context);
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//		lp.gravity = Gravity.CENTER_VERTICAL;
		lp.topMargin = 20;
		lp.leftMargin = 45;
		this.addView(container, lp);
		
		mTotalTextView = new TextView(context);
		mTotalTextView.setTextAppearance(context, R.style.handbook_second_page_marker_total_textview_style);
		
		TextView seperateTextView = new TextView(context);
		seperateTextView.setTextAppearance(context, R.style.handbook_second_page_marker_total_textview_style);
		seperateTextView.setText("/");
		seperateTextView.setVisibility(View.GONE);
		
		mCurrentTextView = new TextView(context);
		mCurrentTextView.setTextAppearance(context, R.style.handbook_second_page_marker_total_textview_style);
		mCurrentTextView.setText("1");
		mCurrentTextView.setVisibility(View.GONE);
		
//		lp.addRule(RelativeLayout.CENTER_IN_PARENT);
		container.addView(mCurrentTextView, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		container.addView(seperateTextView, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		container.addView(mTotalTextView, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
	}

	private void setupViews() {
//		mCurrentIndexTextView = (TextView) this.findViewById(R.id.handbook_second_page_center_imageview_marker_current_textview);
//		Log.e("@@@@@@@@@@@@@@@@@@@@: ", "mCurrentIndexTextView: " + mCurrentIndexTextView);
//		mTotalTextView = (TextView) this.findViewById(R.id.handbook_second_page_center_imageview_marker_total_textview);
//		Log.e("@@@@@@@@@@@@@@@@@@@@: ", "mTotalTextView: " + mTotalTextView);

	}
	
	public void setCurrentIndex(int index) {
		mCurrentTextView.setText(index + "");
	}
	
	public void setTotal(int total) {
		mTotalTextView.setText(total + "");
	}
	
	public void cleanup() {
		mTotalTextView.setText("");
	}
}
