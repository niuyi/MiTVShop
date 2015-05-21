package com.xiaomi.mitv.shop;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import com.xiaomi.mitv.shop.model.CheckoutResponse.Invoice;
import com.xiaomi.mitv.shop.widget.CheckedButtonGroup;
import com.xiaomi.mitv.shop.widget.InvoiceButton;

/**
 * Created by niuyi on 2015/5/21.
 */
public class InvoiceActivity extends BaseSelectorActivity {

    private static final String TAG = "InvoiceActivity";

    private TextView mDesc;
    private CheckedButtonGroup mGroup;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invoice_activity);

        TextView title = (TextView)findViewById(R.id.title_text);
        title.setText("发票信息1");

        mDesc = (TextView)findViewById(R.id.title_text);
        mGroup = (CheckedButtonGroup)findViewById(R.id.radio_group);

        String id = getIntent().getStringExtra("invoice_id");

        if(id == null)
            id = Invoice.PERSONAL_ID;

        InvoiceButton button = (InvoiceButton)getLayoutInflater().inflate(R.layout.invoice_button_widget, null);
        button.setTitle("电子发票");
        button.setValue(Invoice.ELECTRON_ID);
        button.setOnFocusChangeListener(this);

        LinearLayout.LayoutParams para = new LinearLayout.LayoutParams(400, 200);
        para.gravity = Gravity.CENTER_VERTICAL;

        mGroup.addView(button, 0, para);

        button = (InvoiceButton)getLayoutInflater().inflate(R.layout.invoice_button_widget, null);
        button.setTitle("个人发票");
        button.setValue(Invoice.PERSONAL_ID);
        button.setOnFocusChangeListener(this);

        para = new LinearLayout.LayoutParams(400, 200);
        para.gravity = Gravity.CENTER_VERTICAL;
        para.leftMargin = 50;

        mGroup.addView(button, 1, para);

        button = (InvoiceButton)getLayoutInflater().inflate(R.layout.invoice_button_widget, null);
        button.setTitle("单位发票");
        button.setValue(Invoice.COMPANY_ID);
        button.setOnFocusChangeListener(this);

        mGroup.addView(button, 2, para);

        mGroup.setCheckedByValue(id);

//        for(int i = 0 ; i < mGroup.getChildCount(); i++){
//            View child = mGroup.getChildAt(i);
//
//            if(child instanceof RadioButton){
//                RadioButton button = (RadioButton)child;
//                button.setOnFocusChangeListener(this);
//
//                if(id.equals(child.getTag())){
//                    Log.i(TAG, "find child for " + id);
//                    mCurrentButton = button;
//                    button.setChecked(true);
//                    button.requestFocus();
//                }
//            }
//        }
    }
}