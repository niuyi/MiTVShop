package com.xiaomi.mitv.shop;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.xiaomi.mitv.shop.model.CheckoutResponse.Invoice;
import com.xiaomi.mitv.shop.model.ProductManager;
import com.xiaomi.mitv.shop.widget.CheckedButtonGroup;
import com.xiaomi.mitv.shop.widget.InvoiceButton;
import com.xiaomi.mitv.shop.widget.SelectorView;
import com.xiaomi.mitv.shop.widget.SelectorViewListener;

/**
 * Created by niuyi on 2015/5/21.
 */
public class InvoiceActivity extends Activity {

    private static final String TAG = "InvoiceActivity";

    private TextView mDesc;
    private CheckedButtonGroup mGroup;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invoice_activity);

        TextView title = (TextView)findViewById(R.id.title_text);
        title.setText("发票信息");

        mDesc = (TextView)findViewById(R.id.title_text);
        mGroup = (CheckedButtonGroup)findViewById(R.id.radio_group);

        ViewGroup rootView = (ViewGroup)findViewById(android.R.id.content);
        SelectorView selectorView = new SelectorView(this);
        SelectorViewListener listener = new SelectorViewListener(rootView, selectorView);

        InvoiceButton button = (InvoiceButton)getLayoutInflater().inflate(R.layout.invoice_button_widget, null);
        button.setTitle("电子发票");
        button.setValue(Invoice.ELECTRON_ID);
        button.setOnFocusChangeListener(listener);

        LinearLayout.LayoutParams para = new LinearLayout.LayoutParams(400, 200);
        para.gravity = Gravity.CENTER_VERTICAL;

        mGroup.addView(button, 0, para);

        button = (InvoiceButton)getLayoutInflater().inflate(R.layout.invoice_button_widget, null);
        button.setTitle("个人发票");
        button.setValue(Invoice.PERSONAL_ID);
        button.setOnFocusChangeListener(listener);

        para = new LinearLayout.LayoutParams(400, 200);
        para.gravity = Gravity.CENTER_VERTICAL;
        para.leftMargin = 50;

        mGroup.addView(button, 1, para);

        button = (InvoiceButton)getLayoutInflater().inflate(R.layout.invoice_button_widget, null);
        button.setTitle("单位发票");
        button.setValue(Invoice.COMPANY_ID);
        button.setOnFocusChangeListener(listener);

        mGroup.addView(button, 2, para);

        String value = ProductManager.INSTSNCE.getCurrentCheckoutResponse().getSelectedInvoiceValue();
        mGroup.setCheckedByValue(value);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ProductManager.INSTSNCE.getCurrentCheckoutResponse().setInvoiceSelectedByValue(mGroup.getCheckedValue());
    }
}