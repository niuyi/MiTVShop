package com.xiaomi.mitv.shop;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.xiaomi.mitv.shop.model.CheckoutResponse;
import com.xiaomi.mitv.shop.model.CheckoutResponse.Invoice;
import com.xiaomi.mitv.shop.widget.SelectorView;

/**
 * Created by niuyi on 2015/5/21.
 */
public class InvoiceActivity extends Activity implements View.OnFocusChangeListener {

    private static final String TAG = "InvoiceActivity";
    private TextView mDesc;
    private RadioGroup mGroup;
//    private SelectorView mSelectorView;
    private ImageView mSelectorView;
    private ViewGroup mRootView;
    private RadioButton mCurrentButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invoice_activity);

        TextView title = (TextView)findViewById(R.id.title_text);
        title.setText("发票信息");

        mDesc = (TextView)findViewById(R.id.title_text);
        mGroup = (RadioGroup)findViewById(R.id.radio_group);
        mRootView = (ViewGroup)findViewById(android.R.id.content);

        String id = getIntent().getStringExtra("invoice_id");

        if(id == null)
            id = Invoice.COMPANY_ID;

        for(int i = 0 ; i < mGroup.getChildCount(); i++){
            View child = mGroup.getChildAt(i);

            if(child instanceof RadioButton){
                RadioButton button = (RadioButton)child;
                button.setOnFocusChangeListener(this);

                if(id.equals(child.getTag())){
                    Log.i(TAG, "find child for " + id);
                    mCurrentButton = button;
                    button.setChecked(true);
//                    button.requestFocus();
                }
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        mCurrentButton.requestFocus();
    }

    int delta = 42;

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(hasFocus){
            mCurrentButton = (RadioButton)v;

            final RadioButton button = (RadioButton)v;

            v.post(new Runnable() {
                @Override
                public void run() {
                    Log.i(TAG, String.format("focu view mw(%d) mh(%d)", button.getWidth(), button.getHeight()));
                    if (mSelectorView == null) {
                        mSelectorView = new ImageView(InvoiceActivity.this);
                        mSelectorView.setScaleType(ImageView.ScaleType.FIT_XY);
                        mSelectorView.setBackgroundResource(R.drawable.focus_bar);
                        FrameLayout.LayoutParams para = new FrameLayout.LayoutParams(button.getWidth() + delta*2, button.getHeight() + delta*2);

                        mRootView.addView(mSelectorView, para);
                    }

                    moveSelector(button);
                }


            });


        }
    }


    private ObjectAnimator mMoveXAnimator;
    private ObjectAnimator mMoveYAnimator;

    private void moveSelector(View focusView) {
        int[] coord = new int[2];
        focusView.getLocationInWindow(coord);

        if(mMoveXAnimator != null){
            mMoveXAnimator.cancel();
        }

        if(mMoveYAnimator != null){
            mMoveYAnimator.cancel();
        }

        int newX = coord[0] - delta;
        int newY = coord[1] - delta;

        mMoveXAnimator = ObjectAnimator.ofFloat(mSelectorView, "x", mSelectorView.getX(), newX);
        mMoveYAnimator = ObjectAnimator.ofFloat(mSelectorView, "y", mSelectorView.getY(), newY);

        mMoveXAnimator.setDuration(300);
        mMoveYAnimator.setDuration(300);

        mMoveXAnimator.start();
        mMoveYAnimator.start();

//        Log.i(TAG, String.format("mSelectorView view x(%f) y(%f)", mSelectorView.getX(), mSelectorView.getY()));
    }
}