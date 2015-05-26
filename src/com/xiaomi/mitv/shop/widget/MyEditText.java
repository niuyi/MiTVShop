package com.xiaomi.mitv.shop.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import com.xiaomi.mitv.shop.R;

/**
 * Created by linniu on 5/26/2015.
 */
public class MyEditText extends EditText implements View.OnFocusChangeListener {

    private String mDefaultText;

    public MyEditText(Context context) {
        super(context);
    }

    public MyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mDefaultText = this.getText().toString();
        Log.i("MyEditText", "get default text: " + mDefaultText);
        setOnFocusChangeListener(this);
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if(hasFocus){
            this.setTextColor(getContext().getResources().getColor(R.color.white));
            if(getText().toString().equalsIgnoreCase(mDefaultText)){
                this.setText("");
            }
        }else{
            this.setTextColor(getContext().getResources().getColor(R.color.white_60_transparent));
            if(getText().toString().trim().length() == 0){
                setText(mDefaultText);
            }
        }
    }

    public boolean isEmpty(){
        return getText().toString().trim().length() == 0 || getText().toString().trim().equalsIgnoreCase(mDefaultText);
    }

    public void showError(){
        setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.input_error));

        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.shake);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.input_selector));
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        this.startAnimation(animation);
    }
}
