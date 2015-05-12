package com.xiaomi.mitv.shop.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.*;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;
import com.xiaomi.mitv.shop.R;

import java.util.List;

/**
 * Created by niuyi on 2015/5/12.
 */
public class DialogButtonView extends LinearLayout {

    final private String TAG = "DialogButtonView";

    private ButtonOnClickListener mButtonOnClickListener;
    private OnItemClickListener mListener;
    private List<? extends CharSequence> mButtonNames;
    private CharSequence mTitle;

    private LinearLayout mButtonContainer;
    private int mSelectedIndex = -1;

    public DialogButtonView(Context context) {
        super(context);
    }

    public DialogButtonView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DialogButtonView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void inflate() {
        if (TextUtils.isEmpty(mTitle) || mButtonNames == null) {
            Log.i(TAG, "title || button names is null!");
            return;
        }
        LayoutInflater.from(getContext()).inflate(R.layout.device_shop_dialog_item, this);
        init();
    }

    private void init() {
        Log.i(TAG, "init!");

        TextView title = (TextView) findViewById(R.id.item_title);
        title.setText(mTitle);

        mButtonContainer = (LinearLayout) findViewById(R.id.item_container);
        mButtonOnClickListener = new ButtonOnClickListener();

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.rightMargin = 32;
        params.gravity = Gravity.CENTER_VERTICAL;

        mButtonContainer.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.i(TAG, "mButtonContainer onFocusChange: view: " + v.getTag() + " ,hasFocus:" + hasFocus);
                if(hasFocus){

                }
            }
        });

        for (int i = 0; i < mButtonNames.size(); ++i) {
            ToggleButton btn = new ToggleButton(getContext());
            btn.setTag(i);

            btn.setText(mButtonNames.get(i));
            btn.setTextOff(mButtonNames.get(i));
            btn.setTextOn(mButtonNames.get(i));

            btn.setBackgroundResource(R.drawable.device_shop_dialog_btn_selector);
            mButtonContainer.addView(btn, params);

            btn.setOnFocusChangeListener(new OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    Log.i(TAG, "onFocusChange: view: " + v.getTag() + " ,hasFocus:" + hasFocus);
                    ToggleButton button = (ToggleButton)v;
                    if(hasFocus){
                        mSelectedIndex = ((Integer)v.getTag()).intValue();

                        for (int i = 0; i < mButtonContainer.getChildCount(); ++i) {
                            ToggleButton b = (ToggleButton)mButtonContainer.getChildAt(i);
                            b.setChecked(false);
                        }

                        button.setChecked(true);
                    }
                }
            });
        }
    }

//    @Override
//    public boolean dispatchKeyEvent(KeyEvent event) {
//        Log.i(TAG, this.getTag() + " , dispatchKeyEvent: " + event);
//
////        if(event.getAction() == KeyEvent.ACTION_DOWN
////                && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT){
////            if(mSelectedIndex > 0){
////                mSelectedIndex --;
////                updateSelection();
////            }
////        }else if(event.getAction() == KeyEvent.ACTION_DOWN
////                && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT){
////            if(mSelectedIndex < mButtonContainer.getChildCount() - 1){
////                mSelectedIndex ++;
////                updateSelection();
////            }
////        }
//
//        return super.dispatchKeyEvent(event);
//    }

    private void updateSelection() {
        Log.i(TAG, "updateSelection: " + mSelectedIndex);

        Button b = getCurrentButton();
        if(b != null){
            b.setSelected(true);
        }
    }

    public void setSelected(){
        Log.i(TAG, "setSelected: " + mSelectedIndex);
        Button b = getCurrentButton();
        if(b != null){
           b.setSelected(true);
        }
    }

    private ToggleButton getCurrentButton() {
        if(mSelectedIndex >= 0 && mSelectedIndex <= mButtonContainer.getChildCount() - 1){
            return (ToggleButton)mButtonContainer.getChildAt(mSelectedIndex);
        }

        return null;
    }

    public void setFocus(){
        mButtonContainer.requestFocus();
        ToggleButton b = getCurrentButton();
        if(b != null){
           b.requestFocus();
        }
    }

    public String getSelected(){
        ToggleButton b = getCurrentButton();
        if(b != null){
            return b.getTag().toString();
        }

        return "null";
    }

    public void setItemTitle(CharSequence title) {
        Log.i(TAG, "setItemTitle");
        mTitle = title;
    }

    public void setBtnItemNames(List<? extends CharSequence> names) {
        mButtonNames = names;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public int getBtnCount() {
        return mButtonContainer.getChildCount();
    }

    public void setAllBtnEnable(boolean b) {
        for (int i = 0; i < mButtonContainer.getChildCount(); ++i) {
            mButtonContainer.getChildAt(i).setEnabled(b);
        }
    }

    public void setBtnEnable(int p, boolean b) {
        if (p >= 0 && p < mButtonContainer.getChildCount()) {
            mButtonContainer.getChildAt(p).setEnabled(b);
        }
    }

    public boolean isBtnEnable(int p) {
        if (p >= 0 && p < mButtonContainer.getChildCount()) {
            return mButtonContainer.getChildAt(p).isEnabled();
        }
        throw new IllegalArgumentException("position out of child count!");
    }

    public void setAllBtnSelected(boolean b) {
        for (int i = 0; i < mButtonContainer.getChildCount(); ++i) {
            mButtonContainer.getChildAt(i).setSelected(b);
        }
    }

    public void setBtnSelected(int p, boolean b) {
        if (p >= 0 && p < mButtonContainer.getChildCount()) {
            mButtonContainer.getChildAt(p).setSelected(b);
        }
    }

    public boolean isBtnSelected(int p) {
        if (p >= 0 && p < mButtonContainer.getChildCount()) {
            return mButtonContainer.getChildAt(p).isSelected();
        }
        throw new IllegalArgumentException("position out of child count!");
    }

    public int getFirstBtnSelected() {
        for (int i = 0; i < mButtonContainer.getChildCount(); ++i) {
            if (mButtonContainer.getChildAt(i).isSelected()) {
                return i;
            }
        }
        return -1;
    }

    public interface OnItemClickListener {
        public void onClick(DialogButtonView view, int pos);
    }

    private class ButtonOnClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            int pos = (Integer) v.getTag();
            mListener.onClick(DialogButtonView.this, pos);
        }
    }
}
