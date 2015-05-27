package com.xiaomi.mitv.shop;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.xiaomi.mitv.shop.model.CheckoutResponse;
import com.xiaomi.mitv.shop.model.ProductManager;
import com.xiaomi.mitv.shop.widget.CheckedButtonGroup;
import com.xiaomi.mitv.shop.widget.DeliverTimeButton;

/**
 * Created by niuyi on 2015/5/22.
 */
public class DeliverTimeActivity extends Activity {

    private CheckedButtonGroup mGroup;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deliver_time_activity);

        TextView title = (TextView)findViewById(R.id.title_text);
        title.setText(R.string.deliver_time);

        String value = ProductManager.INSTANCE.getCurrentCheckoutResponse().getSelectedDeliverTimeValue();

        DeliverTimeButton button = (DeliverTimeButton)findViewById(R.id.btn_1);
        button.init(this, R.string.deliver_time_no_limit, R.string.deliver_time_no_limit_2, CheckoutResponse.DeliverTime.ON_LIMITED_ID);
        if(CheckoutResponse.DeliverTime.ON_LIMITED_ID.equalsIgnoreCase(value)){
            button.requestFocus();
        }

        button = (DeliverTimeButton)findViewById(R.id.btn_2);
        button.init(this, R.string.deliver_time_working_day, R.string.deliver_time_working_day_2, CheckoutResponse.DeliverTime.WORKING_DAY_ID);
        if(CheckoutResponse.DeliverTime.WORKING_DAY_ID.equalsIgnoreCase(value)){
            button.requestFocus();
        }
        LinearLayout.LayoutParams para = (LinearLayout.LayoutParams)button.getLayoutParams();
        para.leftMargin = 90;

        button = (DeliverTimeButton)findViewById(R.id.btn_3);
        button.init(this, R.string.deliver_time_holiday_day, R.string.deliver_time_holiday_day_2, CheckoutResponse.DeliverTime.HOLIDAY_ID);
        if(CheckoutResponse.DeliverTime.HOLIDAY_ID.equalsIgnoreCase(value)){
            button.requestFocus();
        }
        para = (LinearLayout.LayoutParams)button.getLayoutParams();
        para.leftMargin = 90;


//        mGroup = (CheckedButtonGroup)findViewById(R.id.radio_group);
//
//        ViewGroup rootView = (ViewGroup)findViewById(android.R.id.content);
//        SelectorView selectorView = new SelectorView(this);
//        SelectorViewListener listener = new SelectorViewListener(rootView, selectorView);
//
//        DeliverTimeButton button = (DeliverTimeButton)getLayoutInflater().inflate(R.layout.deliver_time_button_widget, null);
//        button.setTitle("不限送货时间");
//        button.setTitle2("周一至周日");
//        button.setValue(CheckoutResponse.DeliverTime.ON_LIMITED_ID);
//        button.setOnFocusChangeListener(listener);
//
//        LinearLayout.LayoutParams para = new LinearLayout.LayoutParams(400, 200);
//        para.gravity = Gravity.CENTER_VERTICAL;
//
//        mGroup.addView(button, 0, para);
//
//        button = (DeliverTimeButton)getLayoutInflater().inflate(R.layout.deliver_time_button_widget, null);
//        button.setTitle("工作日送货");
//        button.setTitle2("周一至周五");
//        button.setValue(CheckoutResponse.DeliverTime.WORKING_DAY_ID);
//        button.setOnFocusChangeListener(listener);
//
//        para = new LinearLayout.LayoutParams(400, 200);
//        para.gravity = Gravity.CENTER_VERTICAL;
//        para.leftMargin = 50;
//
//        mGroup.addView(button, 1, para);
//
//        button = (DeliverTimeButton)getLayoutInflater().inflate(R.layout.deliver_time_button_widget, null);
//        button.setTitle("双休日、假日送货");
//        button.setTitle2("周六至周日");
//        button.setValue(CheckoutResponse.DeliverTime.HOLIDAY_ID);
//        button.setOnFocusChangeListener(listener);
//
//        mGroup.addView(button, 2, para);
//
//        String value = ProductManager.INSTSNCE.getCurrentCheckoutResponse().getSelectedDeliverTimeValue();
//        mGroup.setCheckedByValue(value);
    }
}