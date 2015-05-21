package com.xiaomi.mitv.shop.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;
import com.xiaomi.mitv.shop.R;

/**
 * Created by linniu on 2015/5/21.
 */
public class InvoiceButton extends CheckableButton{
    private ImageView mIcon;
    private TextView mTitle;

    public InvoiceButton(Context context) {
        super(context);
    }

    public InvoiceButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InvoiceButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setTitle(String title){
        mTitle.setText(title);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mTitle = (TextView)findViewById(R.id.item_title);
        mIcon = (ImageView)findViewById(R.id.iv_icon);
    }

    @Override
    public void setChecked(boolean status) {
        super.setChecked(status);
        if(status){
            mIcon.setImageResource(R.drawable.radio_on);
        }else{
            mIcon.setImageResource(R.drawable.radio_off);
        }
    }
}
