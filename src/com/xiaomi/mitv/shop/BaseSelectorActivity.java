package com.xiaomi.mitv.shop;

import android.animation.AnimatorSet;
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

    private AnimatorSet mMovieAnimator;

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

        if(mMovieAnimator != null){
            mMovieAnimator.cancel();
            mMovieAnimator = null;
        }

        mMovieAnimator = new AnimatorSet();

        int[] coord = new int[2];
        focusView.getLocationInWindow(coord);

        int newX = coord[0] - delta;
        int newY = coord[1] - delta;

        ObjectAnimator moveXAnimator = ObjectAnimator.ofFloat(mSelectorView, "x", mSelectorView.getX(), newX);
        ObjectAnimator moveYAnimator = ObjectAnimator.ofFloat(mSelectorView, "y", mSelectorView.getY(), newY);

        moveXAnimator.setDuration(300);
        moveYAnimator.setDuration(300);

        int newHeight = focusView.getHeight() + delta*2;

        if(mSelectorView.getHeight() != newHeight){
//            ObjectAnimator sizeAnimator = ObjectAnimator.ofFloat(mSelectorView, "height", mSelectorView.getHeight(), newHeight);
//            sizeAnimator.setDuration(300);
//            mMovieAnimator.playTogether(moveXAnimator, moveYAnimator, sizeAnimator);
        }else{
            mMovieAnimator.playTogether(moveXAnimator, moveYAnimator);
        }

        mMovieAnimator.start();


////        moveXAnimator.setDuration(300);
////        mMoveYAnimator.setDuration(300);
////
////        moveXAnimator.start();
////        mMoveYAnimator.start();
//
//        int newHeight = focusView.getHeight() + delta*2;
//
//        AnimatorSet set = new AnimatorSet();

    }
}