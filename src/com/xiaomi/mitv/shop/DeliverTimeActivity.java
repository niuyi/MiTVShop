package com.xiaomi.mitv.shop;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.xiaomi.mitv.shop.model.CheckoutResponse;
import com.xiaomi.mitv.shop.widget.CheckedButtonGroup;
import com.xiaomi.mitv.shop.widget.DeliverTimeButton;

/**
 * Created by niuyi on 2015/5/22.
 */
public class DeliverTimeActivity extends BaseSelectorActivity {

    private CheckedButtonGroup mGroup;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deliver_time_activity);

        TextView title = (TextView)findViewById(R.id.title_text);
        title.setText("送货时间");

        mGroup = (CheckedButtonGroup)findViewById(R.id.radio_group);

        String id = getIntent().getStringExtra("delivertime_id");

        if(id == null)
            id = CheckoutResponse.DeliverTime.ON_LIMITED_ID;

        DeliverTimeButton button = (DeliverTimeButton)getLayoutInflater().inflate(R.layout.deliver_time_button_widget, null);
        button.setTitle("不限送货时间");
        button.setTitle2("周一至周日");
        button.setValue(CheckoutResponse.DeliverTime.ON_LIMITED_ID);
        button.setOnFocusChangeListener(this);

        LinearLayout.LayoutParams para = new LinearLayout.LayoutParams(400, 200);
        para.gravity = Gravity.CENTER_VERTICAL;

        mGroup.addView(button, 0, para);

        button = (DeliverTimeButton)getLayoutInflater().inflate(R.layout.deliver_time_button_widget, null);
        button.setTitle("工作日送货");
        button.setTitle2("周一至周五");
        button.setValue(CheckoutResponse.DeliverTime.WORKING_DAY_ID);
        button.setOnFocusChangeListener(this);

        para = new LinearLayout.LayoutParams(400, 200);
        para.gravity = Gravity.CENTER_VERTICAL;
        para.leftMargin = 50;

        mGroup.addView(button, 1, para);

        button = (DeliverTimeButton)getLayoutInflater().inflate(R.layout.deliver_time_button_widget, null);
        button.setTitle("双休日、假日送货");
        button.setTitle2("周六至周日");
        button.setValue(CheckoutResponse.DeliverTime.HOLIDAY_ID);
        button.setOnFocusChangeListener(this);

        mGroup.addView(button, 2, para);

        mGroup.setCheckedByValue(id);
    }
}