package com.xiaomi.mitv.shop.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.xiaomi.mitv.shop.R;

import java.util.List;

/**
 * Created by linniu on 2015/5/23.
 */
public class SwitcherItem extends LinearLayout {
    private static final String TAG = "SwitcherItem";
    private List<Pair<String, String>> values;
    private TextView mTitleView;
    private ImageView mLeftIcon;
    private ImageView mRightIcon;
    private TextView mValue;
    private int mCurrentIndex = 0;
    private OnSelectChangeListener mListener;

    public SwitcherItem(Context context) {
        super(context);
    }

    public SwitcherItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SwitcherItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mTitleView = (TextView)findViewById(R.id.item_title);
        mLeftIcon = (ImageView)findViewById(R.id.iv_left);
        mRightIcon = (ImageView)findViewById(R.id.iv_right);
        mValue = (TextView)findViewById(R.id.tv_value);

        this.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    setBackgroundColor(getContext().getResources().getColor(android.R.color.holo_blue_bright));
                } else {
                    setBackgroundColor(getContext().getResources().getColor(android.R.color.transparent));
                }
            }
        });
    }

    public void setTitle(String title){
        mTitleView.setText(title);
    }
    public void setOnSelectChangeListener(OnSelectChangeListener listener){
        mListener = listener;
    }

    public void setValues(List<Pair<String, String>> values, String selectedId){
        this.values = values;
        setSelectedValue(selectedId);
    }

    public void setSelectedValue(String selectedId) {
        for(int i = 0 ; i < values.size(); i++){
            Pair<String, String> p = values.get(i);
            if(p.first.equals(selectedId)){
                mCurrentIndex = i;
                break;
            }
        }

        updateValue();
    }

    private void updateValue() {
        Pair<String, String> p = values.get(mCurrentIndex);
        mValue.setText(p.second);
        if(mListener != null){
            mListener.onSelectChange(this, p.first);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d(TAG, "onKeyDown keyCode=" + keyCode + ", repeat=" + event.getRepeatCount());

        if(keyCode == KeyEvent.KEYCODE_ENTER && event.getRepeatCount() == 0){
            this.callOnClick();
        }

        if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT && event.getRepeatCount() == 0) {
            mLeftIcon.setImageResource(R.drawable.triangle_left_focus);

            --mCurrentIndex;
            mCurrentIndex = mCurrentIndex < 0 ? values.size() - 1 : mCurrentIndex;

            updateValue();
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT && event.getRepeatCount() == 0) {
            mRightIcon.setImageResource(R.drawable.triangle_right_focus);

            ++mCurrentIndex;
            mCurrentIndex = (mCurrentIndex > values.size() - 1) ? 0 : mCurrentIndex;

            updateValue();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        Log.d(TAG, "onKeyUp keyCode=" + keyCode);
        if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
            mLeftIcon.setImageResource(R.drawable.triangle_left_normal);
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
            mRightIcon.setImageResource(R.drawable.triangle_right_normal);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    interface OnSelectChangeListener{
        public void onSelectChange(View view, String value);
    }
}
