package com.xiaomi.mitv.shop.widget;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.xiaomi.mitv.shop.R;

import java.util.List;

/**
 * Created by niuyi on 2015/5/12.
 */
public class DialogButtonView extends LinearLayout {

    final private String TAG = "DialogButtonView";

    private OnItemClickListener mListener;
    private List<? extends CharSequence> mButtonNames;
    private CharSequence mTitle;

    private RadioGroup mButtonContainer;
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

        mButtonContainer = (RadioGroup) findViewById(R.id.item_container);

        RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.rightMargin = 60;
        params.gravity = Gravity.CENTER_VERTICAL;

        for (int i = 0; i < mButtonNames.size(); ++i) {
            RadioButton btn = new RadioButton(getContext());
            btn.setTag(i);

            btn.setText(mButtonNames.get(i));
            btn.setTextColor(Color.BLACK);
            btn.setButtonDrawable(android.R.color.transparent);
            btn.setBackgroundResource(R.drawable.device_shop_dialog_btn_selector);
            mButtonContainer.addView(btn, params);
//            btn.setOnFocusChangeListener(new OnFocusChangeListener() {
//                @Override
//                public void onFocusChange(View v, boolean hasFocus) {
//                    Log.i(TAG, "onFocusChange: view: " + v.getTag() + " ,hasFocus:" + hasFocus);
//
//                    ToggleButton button = (ToggleButton)v;
//                    if(hasFocus){
//                        mSelectedIndex = ((Integer)v.getTag()).intValue();
//
//                        for (int i = 0; i < mButtonContainer.getChildCount(); ++i) {
//                            ToggleButton b = (ToggleButton)mButtonContainer.getChildAt(i);
//                            b.setChecked(false);
//                        }
//
//                        button.setChecked(true);
//                    }
//                }
//            });
        }
    }

    private RadioButton getCurrentButton() {
//        if(mSelectedIndex >= 0 && mSelectedIndex <= mButtonContainer.getChildCount() - 1){
//            return (ToggleButton)mButtonContainer.getChildAt(mSelectedIndex);
//        }

        return null;
    }

    public String getSelected(){
        RadioButton b = getCurrentButton();
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
