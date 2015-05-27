package com.xiaomi.mitv.shop;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.xiaomi.mitv.shop.model.CheckoutResponse.Invoice;
import com.xiaomi.mitv.shop.model.ProductManager;

/**
 * Created by niuyi on 2015/5/21.
 */
public class InvoiceActivity extends Activity implements View.OnClickListener, View.OnFocusChangeListener {

    private static final String TAG = "InvoiceActivity";
    public static final int REQUEST_CODE = 0;
    public static final String TITLE_KEY = "title";

    private TextView mDesc;

    private Button mElecButton;
    private Button mPersonalButton;
    private Button mCompanyButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invoice_activity);

        TextView title = (TextView)findViewById(R.id.title_text);
        title.setText(R.string.invoice_title);

        mDesc = (TextView)findViewById(R.id.tv_desc);

        mElecButton = (Button)findViewById(R.id.btn_electron);
        mElecButton.setOnClickListener(this);
        mElecButton.setOnFocusChangeListener(this);

        mPersonalButton = (Button)findViewById(R.id.btn_personal);
        mPersonalButton.setOnClickListener(this);

        mCompanyButton = (Button)findViewById(R.id.btn_company);
        mCompanyButton.setOnClickListener(this);

        String value = ProductManager.INSTANCE.getCurrentCheckoutResponse().getSelectedInvoiceValue();

        if(Invoice.ELECTRON_ID.equalsIgnoreCase(value)){
            mElecButton.requestFocus();
        }else if(Invoice.PERSONAL_ID.equalsIgnoreCase(value)){
            mPersonalButton.requestFocus();
        }else if(Invoice.COMPANY_ID.equalsIgnoreCase(value)){
            mCompanyButton.requestFocus();
        }
    }

    @Override
    public void onClick(View view) {
        if(view == mElecButton){
            ProductManager.INSTANCE.getCurrentCheckoutResponse().setInvoiceSelectedByValue(Invoice.ELECTRON_ID);
            finish();
        }else if(view == mPersonalButton){
            ProductManager.INSTANCE.getCurrentCheckoutResponse().setInvoiceSelectedByValue(Invoice.PERSONAL_ID);
            finish();
        }else if(view == mCompanyButton){
            Intent in = new Intent();
            in.setClass(this, InputInvoiceTitleActivity.class);
            startActivityForResult(in, REQUEST_CODE);
        }
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if(view == mElecButton){
            if(hasFocus){
                mDesc.setVisibility(View.VISIBLE);
            }else{
                mDesc.setVisibility(View.GONE);
            }
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            String address = data.getStringExtra(TITLE_KEY);
            ProductManager.INSTANCE.getCurrentCheckoutResponse().setInvoiceSelectedByValue(Invoice.COMPANY_ID);
            ProductManager.INSTANCE.getCurrentCheckoutResponse().body.invoice_title = address;
            finish();
        }
    }
}