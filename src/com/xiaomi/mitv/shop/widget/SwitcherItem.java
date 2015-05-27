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
import com.xiaomi.mitv.shop.model.CheckoutResponse;

import java.util.List;

/**
 * Created by linniu on 2015/5/23.
 */
public class SwitcherItem extends LinearLayout {
    private static final String TAG = "SwitcherItem";
    private List<String> values;
    private TextView mTitleView;
    private ImageView mLeftIcon;
    private ImageView mRightIcon;
    private TextView mValue;
    private TextView mValue2;
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
        mValue = (TextView)findViewById(R.id.tv_deliver_value);
        mValue2 = (TextView)findViewById(R.id.tv_deliver_value_2);
    }

    public void setTitle(String title){
        mTitleView.setText(title);
    }
    public void setOnSelectChangeListener(OnSelectChangeListener listener){
        mListener = listener;
    }

    public void setValues(List<String> values, String selectedId){
        this.values = values;
        setSelectedValue(selectedId);
    }

    public void setSelectedValue(String selectedId) {
        for(int i = 0 ; i < values.size(); i++){
            String p = values.get(i);
            if(p.equals(selectedId)){
                mCurrentIndex = i;
                break;
            }
        }

        updateValue();
    }

    private void updateValue() {
        String p = values.get(mCurrentIndex);

        if(CheckoutResponse.DeliverTime.HOLIDAY_ID.equals(p)){
            mValue.setText(R.string.deliver_time_holiday_day);
            mValue2.setText(R.string.deliver_time_holiday_day_2);
        }else if(CheckoutResponse.DeliverTime.WORKING_DAY_ID.equals(p)){
            mValue.setText(R.string.deliver_time_working_day);
            mValue2.setText(R.string.deliver_time_working_day_2);
        }else{
            mValue.setText(R.string.deliver_time_no_limit);
            mValue2.setText(R.string.deliver_time_no_limit_2);
        }

        if(mListener != null){
            mListener.onSelectChange(this, p);
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
