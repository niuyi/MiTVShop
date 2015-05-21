package com.xiaomi.mitv.shop.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by linniu on 2015/5/21.
 */
public class CheckedButtonGroup extends LinearLayout implements View.OnClickListener {


    public CheckedButtonGroup(Context context) {
        super(context);
    }

    public CheckedButtonGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CheckedButtonGroup(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setCheckedByValue(String value){
        int count = getChildCount();
        for(int i = 0 ; i < count ; i++){
            CheckableButton child = (CheckableButton)getChildAt(i);
            if(value.equals(child.getValue())){
                child.setChecked(true);
            }else{
                child.setChecked(false);
            }
        }
    }

    public String getCheckedValue(){
        int count = getChildCount();
        for(int i = 0 ; i < count ; i++){
            CheckableButton child = (CheckableButton)getChildAt(i);
            if(child.checked()){
                return (String)child.getValue();
            }
        }

        return null;
    }

    @Override
    public void onClick(View view) {
        if(view instanceof CheckableButton){
            setCheckedByValue(((CheckableButton)view).getValue());
        }
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (child instanceof CheckableButton) {
            final CheckableButton button = (CheckableButton) child;
            button.setOnClickListener(this);
        }

        super.addView(child, index, params);
    }
}
