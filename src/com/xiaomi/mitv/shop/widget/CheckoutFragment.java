package com.xiaomi.mitv.shop.widget;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.xiaomi.mitv.shop.DeliverTimeActivity;
import com.xiaomi.mitv.shop.InvoiceActivity;
import com.xiaomi.mitv.shop.R;
import com.xiaomi.mitv.shop.model.CheckoutResponse;
import com.xiaomi.mitv.shop.model.ProductManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by linniu on 2015/5/23.
 */
public class CheckoutFragment extends Fragment implements View.OnFocusChangeListener, SwitcherItem.OnSelectChangeListener {

    private static final String TAG = "CheckoutFragment";
    private ViewGroup mRootView;
    private SelectorView mSelectorView;

    private Button mAlipayButton;
    private Button mXiaomiPayButton;
    private SwitcherItem mDeliverTimeItem;
    private SwitcherItem mInvoiceItem;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.checkout_widget, container, false);

        TextView title = (TextView)view.findViewById(R.id.title_text);
        title.setText("订单确认");

        mRootView = (ViewGroup)view.findViewById(R.id.root_container);

        SelectorView selectorView = new SelectorView(getActivity());
        SelectorViewListener listener = new SelectorViewListener(mRootView, selectorView);

        CheckoutResponse res = getResponse();
        ProductManager.INSTSNCE.setCurrentCheckoutResponse(res);

        mDeliverTimeItem = (SwitcherItem)view.findViewById(R.id.deliver_time_item);
        setupDeliverItem(res, mDeliverTimeItem);
        mDeliverTimeItem.setOnFocusChangeListener(listener);
        mDeliverTimeItem.setOnSelectChangeListener(this);

        mInvoiceItem = (SwitcherItem)view.findViewById(R.id.invoice_item);
        setupInvoiceItem(res, mInvoiceItem);
        mInvoiceItem.setOnFocusChangeListener(listener);
        mInvoiceItem.setOnSelectChangeListener(this);

        final View addressView = view.findViewById(R.id.address_item);
        addressView.setOnFocusChangeListener(listener);

        mAlipayButton = (Button)view.findViewById(R.id.ali_pay);
        mAlipayButton.setOnFocusChangeListener(this);
        mAlipayButton.requestFocus();

        mXiaomiPayButton = (Button)view.findViewById(R.id.xiaomi_pay);
        mXiaomiPayButton.setOnFocusChangeListener(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mDeliverTimeItem.setSelectedValue(ProductManager.INSTSNCE.getCurrentCheckoutResponse().getSelectedDeliverTimeValue());
        mInvoiceItem.setSelectedValue(ProductManager.INSTSNCE.getCurrentCheckoutResponse().getSelectedInvoiceValue());
    }

    private void setupDeliverItem(CheckoutResponse res, SwitcherItem item) {
        item.setTitle("送货时间");

        List<Pair<String, String>> values = new ArrayList<Pair<String, String>>();

        String checkValue = "";
        for(int i = 0; i < res.body.deliverTimeList.size(); i++){
            CheckoutResponse.DeliverTime time =  res.body.deliverTimeList.get(i);

            String value = String.valueOf(time.value);
            values.add(new Pair<String, String>(value, time.desc));
            if(time.checked){
                checkValue = value;
            }
        }

        item.setValues(values, checkValue);

        item.setClickable(true);
        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "click me");
                Intent in = new Intent();
                in.setClass(getActivity(), DeliverTimeActivity.class);
                startActivity(in);
            }
        });
    }

    private void setupInvoiceItem(CheckoutResponse res, SwitcherItem item) {
        item.setTitle("发票信息");

        List<Pair<String, String>> values = new ArrayList<Pair<String, String>>();

        String checkValue = "";
        for(int i = 0; i < res.body.invoiceList.size(); i++){
            CheckoutResponse.Invoice time =  res.body.invoiceList.get(i);

            String value = String.valueOf(time.value);
            values.add(new Pair<String, String>(value, time.desc));
            if(time.checked){
                checkValue = value;
            }
        }

        item.setValues(values, checkValue);
        item.setClickable(true);
        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "click me");
                Intent in = new Intent();
                in.setClass(getActivity(), InvoiceActivity.class);
                startActivity(in);
            }
        });
    }

    private static CheckoutResponse getResponse() {
        CheckoutResponse response = new CheckoutResponse();

        CheckoutResponse.DeliverTime time = new CheckoutResponse.DeliverTime();
        time.value = Integer.valueOf(CheckoutResponse.DeliverTime.ON_LIMITED_ID);
        time.desc = "不限送货时间\r\n周一至周日";
        time.checked = true;
        response.body.deliverTimeList.add(time);

        time = new CheckoutResponse.DeliverTime();
        time.value = Integer.valueOf(CheckoutResponse.DeliverTime.HOLIDAY_ID);
        time.desc = "双休日、节假日送货\r\n周六至周日";
        time.checked = false;
        response.body.deliverTimeList.add(time);

        time = new CheckoutResponse.DeliverTime();
        time.value = Integer.valueOf(CheckoutResponse.DeliverTime.WORKING_DAY_ID);
        time.desc = "工作日送货\r\n周一至周五";
        time.checked = false;
        response.body.deliverTimeList.add(time);

        CheckoutResponse.Invoice invoice = new CheckoutResponse.Invoice();
        invoice.value = Integer.valueOf(CheckoutResponse.Invoice.ELECTRON_ID);
        invoice.desc = "电子发票";
        invoice.checked = true;
        response.body.invoiceList.add(invoice);

        invoice = new CheckoutResponse.Invoice();
        invoice.value = Integer.valueOf(CheckoutResponse.Invoice.PERSONAL_ID);
        invoice.desc = "个人发票";
        invoice.checked = false;
        response.body.invoiceList.add(invoice);

        invoice = new CheckoutResponse.Invoice();
        invoice.value = Integer.valueOf(CheckoutResponse.Invoice.COMPANY_ID);
        invoice.desc = "单位发票";
        invoice.checked = false;
        response.body.invoiceList.add(invoice);

        return response;
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if(hasFocus){

            if(view == mAlipayButton || view == mXiaomiPayButton){
                if(mSelectorView != null){
                    mSelectorView.setVisibility(View.GONE);
                }

                return;
            }

            if(mSelectorView != null){
                mSelectorView.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onSelectChange(View view, String value) {
        if(view == mDeliverTimeItem){
            ProductManager.INSTSNCE.getCurrentCheckoutResponse().setDeliverTimeSelectedByValue(value);
        }else if(view == mInvoiceItem){
            ProductManager.INSTSNCE.getCurrentCheckoutResponse().setInvoiceSelectedByValue(value);
        }
    }
}