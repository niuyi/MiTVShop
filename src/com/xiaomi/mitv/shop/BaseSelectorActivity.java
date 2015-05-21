package com.xiaomi.mitv.shop;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import com.xiaomi.mitv.shop.widget.SelectorView;

/**
 * Created by linniu on 2015/5/21.
 */
public class BaseSelectorActivity extends Activity implements View.OnFocusChangeListener {

    private SelectorView mSelectorView;
    protected ViewGroup mRootView;

    private ObjectAnimator mMoveXAnimator;
    private ObjectAnimator mMoveYAnimator;

    private int delta = 42;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRootView = (ViewGroup)findViewById(android.R.id.content);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(hasFocus){

            final View focusView = v;

            v.post(new Runnable() {
                @Override
                public void run() {
                    if (mSelectorView == null) {
                        mSelectorView = new SelectorView(BaseSelectorActivity.this);

                        FrameLayout.LayoutParams para = new FrameLayout.LayoutParams(focusView.getWidth() + delta*2, focusView.getHeight() + delta*2);
                        mRootView.addView(mSelectorView, para);

                        int[] coord = new int[2];
                        focusView.getLocationInWindow(coord);

                        int newX = coord[0] - delta;
                        int newY = coord[1] - delta;

                        mSelectorView.setX(newX);
                        mSelectorView.setY(newY);

                    }else {
                        moveSelector(focusView);
                    }
                }


            });
        }
    }

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
    }
}