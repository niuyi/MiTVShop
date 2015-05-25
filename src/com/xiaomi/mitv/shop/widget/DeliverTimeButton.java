package com.xiaomi.mitv.shop.widget;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.xiaomi.mitv.shop.R;
import com.xiaomi.mitv.shop.model.ProductManager;

/**
 * Created by linniu on 2015/5/21.
 */
public class DeliverTimeButton extends LinearLayout{

    private TextView mTitle;
    private TextView mTitle2;

    public DeliverTimeButton(Context context) {
        super(context);
    }

    public DeliverTimeButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DeliverTimeButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void init(final Activity activity, int text1, int text2, final String value){
        mTitle.setText(text1);
        mTitle2.setText(text2);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ProductManager.INSTSNCE.getCurrentCheckoutResponse().setDeliverTimeSelectedByValue(value);
                activity.finish();
            }
        });
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mTitle = (TextView)findViewById(R.id.item_title);
        mTitle2 = (TextView)findViewById(R.id.item_title_2);
    }
}
