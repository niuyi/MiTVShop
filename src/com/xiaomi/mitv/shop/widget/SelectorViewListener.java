package com.xiaomi.mitv.shop.widget;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

/**
 * Created by linniu on 5/24/2015.
 */
public class SelectorViewListener implements View.OnFocusChangeListener, AdapterView.OnItemSelectedListener {

    private ViewGroup mRootView;
    private SelectorView mSelectorView;

    public SelectorViewListener(ViewGroup rootView, SelectorView selectorView){
        mRootView = rootView;
        mSelectorView = selectorView;
    }


    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if(hasFocus){

            handleMove(view);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        handleMove(view);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void handleMove(View view) {
        final View focusView = view;

        if(!mSelectorView.isInit()){
            view.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mSelectorView.initView(focusView, mRootView);
                }
            }, 100);
        }else {
            mSelectorView.move(focusView);
        }
    }

}
