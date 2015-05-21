package com.xiaomi.mitv.shop.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import com.xiaomi.mitv.shop.R;

/**
 * Created by linniu on 2015/5/21.
 */
public abstract class CheckableButton extends LinearLayout {

    private boolean mChecked = false;
    private String mValue;

    public CheckableButton(Context context) {
        super(context);
        initView();
    }

    public CheckableButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public CheckableButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    private void initView(){
        setOrientation(VERTICAL);
        setBackgroundResource(R.drawable.invoice_btn_selector);
        setFocusable(true);
        setClickable(true);
    }

    public void setChecked(boolean status){
        mChecked = status;
    }

    public boolean checked(){
        return mChecked;
    }

    public String getValue() {
        return mValue;
    }

    public void setValue(String value){
        this.mValue = value;
    }
}
