package com.xiaomi.mitv.shop.widget;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.xiaomi.mitv.shop.AddressActivity;
import com.xiaomi.mitv.shop.DeliverTimeActivity;
import com.xiaomi.mitv.shop.InvoiceActivity;
import com.xiaomi.mitv.shop.R;
import com.xiaomi.mitv.shop.model.Address;
import com.xiaomi.mitv.shop.model.CheckoutResponse;
import com.xiaomi.mitv.shop.model.ProductDetail;
import com.xiaomi.mitv.shop.model.ProductManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by linniu on 2015/5/23.
 */
public class CheckoutFragment extends Fragment implements SwitcherItem.OnSelectChangeListener, View.OnFocusChangeListener {

    private static final String TAG = "CheckoutFragment";
    private ViewGroup mRootView;
//    private SelectorView mSelectorView;

    private Button mAlipayButton;
    private Button mXiaomiPayButton;
    private SwitcherItem mDeliverTimeItem;
    private View mInvoiceItem;
    private TextView mNameTextView;
    private TextView mCityTextView;
    private TextView mAddressTextView;
    private TextView mInvoiceTV;
    private String mGid;
    private String mPid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.checkout_widget, container, false);

        TextView title = (TextView)view.findViewById(R.id.title_text);
        title.setText(R.string.order_detail);

        mRootView = (ViewGroup)view.findViewById(R.id.root_container);

//        mSelectorView = new SelectorView(getActivity());
//        SelectorViewListener listener = new SelectorViewListener(mRootView, mSelectorView);
//
        CheckoutResponse res = getResponse();

        mGid = "2";
        mPid = "1";

        ProductManager.INSTANCE.setCurrentCheckoutResponse(res);
        ProductDetail productDetail = ProductManager.INSTANCE.getProductDetail(mPid);
        ProductDetail.GoodStatus goodStatus = productDetail.getGoodStatus(mGid);

        TextView name = (TextView)view.findViewById(R.id.tv_goods_name);
        name.setText(goodStatus.name);

        TextView buy = (TextView)view.findViewById(R.id.buy_info);
        buy.setText(getString(R.string.buy_info, res.body.productMoney, res.body.shipment));

        TextView price = (TextView)view.findViewById(R.id.price);
        price.setText(Html.fromHtml(getString(R.string.price_info, res.body.amount)));
//
        mDeliverTimeItem = (SwitcherItem)view.findViewById(R.id.deliver_time_item);
        setupDeliverItem(res, mDeliverTimeItem);
//        mDeliverTimeItem.setOnFocusChangeListener(listener);
        mDeliverTimeItem.setOnSelectChangeListener(this);
        mDeliverTimeItem.setOnFocusChangeListener(this);
//
        mInvoiceItem = view.findViewById(R.id.invoice_item);
//        mInvoiceItem.setOnFocusChangeListener(listener);
        mInvoiceTV = (TextView)view.findViewById(R.id.tv_invoice_value);
        setupInvoiceItem(res, mInvoiceTV);

        mInvoiceItem.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT && event.getAction() == KeyEvent.ACTION_DOWN) {
                    gotoInvoiceActivity();
                    return true;
                }

                return false;
            }
        });
        mInvoiceItem.setClickable(true);
        mInvoiceItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "click me");
                gotoInvoiceActivity();
            }
        });
        mInvoiceItem.setOnFocusChangeListener(this);

        final View addressView = view.findViewById(R.id.address_item);

        addressView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT && event.getAction() == KeyEvent.ACTION_DOWN) {
                    gotoAddressActivity();
                    return true;
                }

                return false;
            }
        });

//        addressView.setOnFocusChangeListener(listener);
        addressView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoAddressActivity();
            }
        });
        addressView.setOnFocusChangeListener(this);

        mNameTextView = (TextView)view.findViewById(R.id.tv_name);
        mCityTextView = (TextView)view.findViewById(R.id.tv_city);
        mAddressTextView = (TextView)view.findViewById(R.id.tv_address);

        mAlipayButton = (Button)view.findViewById(R.id.ali_pay);
//        mAlipayButton.setOnFocusChangeListener(this);
        mAlipayButton.requestFocus();

        mXiaomiPayButton = (Button)view.findViewById(R.id.xiaomi_pay);
//        mXiaomiPayButton.setOnFocusChangeListener(this);

        return view;
    }

    private void gotoInvoiceActivity() {
        Intent in = new Intent();
        in.setClass(getActivity(), InvoiceActivity.class);
        startActivity(in);
    }

    private void gotoAddressActivity() {
        Intent in = new Intent();
        in.setClass(getActivity(), AddressActivity.class);
        startActivity(in);
    }

    @Override
    public void onResume() {
        super.onResume();
        mDeliverTimeItem.setSelectedValue(ProductManager.INSTANCE.getCurrentCheckoutResponse().getSelectedDeliverTimeValue());
        setupInvoiceItem(ProductManager.INSTANCE.getCurrentCheckoutResponse(), mInvoiceTV);
//
        Address address = ProductManager.INSTANCE.getCurrentCheckoutResponse().body.address;
        if(address != null){
            mNameTextView.setText(String.format("%s %s", address.consignee, address.tel));
            mCityTextView.setText(String.format("%s %s %s", address.province_name, address.city_name, address.district_name));
            mAddressTextView.setText(String.format("%s", address.address));
        }
    }

    private void setupDeliverItem(CheckoutResponse res, SwitcherItem item) {
        item.setTitle(getActivity().getResources().getString(R.string.checkout_deliver));

        List<String> values = new ArrayList<String>();

        String checkValue = "";
        for(int i = 0; i < res.body.deliverTimeList.size(); i++){
            CheckoutResponse.DeliverTime time =  res.body.deliverTimeList.get(i);

            String value = String.valueOf(time.value);
            values.add(value);

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

    private void setupInvoiceItem(CheckoutResponse res, TextView item) {

        for(int i = 0; i < res.body.invoiceList.size(); i++){
            CheckoutResponse.Invoice invoice =  res.body.invoiceList.get(i);

            if(invoice.checked) {
                if(invoice.value == Integer.valueOf(CheckoutResponse.Invoice.PERSONAL_ID)){
                    item.setText(R.string.invoice_personal);
                }else if(invoice.value == Integer.valueOf(CheckoutResponse.Invoice.COMPANY_ID)){
                    item.setText(R.string.invoice_company);
                }else{
                    item.setText(R.string.invoice_electron);
                }

                break;
            }
        }
    }

    private static CheckoutResponse getResponse() {
        ProductDetail detail = new ProductDetail();
        ProductDetail.GoodStatus status = new ProductDetail.GoodStatus();
        status.id = "2";
        status.name = "小米路由器 黑色";

        detail.goods_status = new ProductDetail.GoodStatus[]{status};

        ProductManager.INSTANCE.putProductDetail("1", detail);

        CheckoutResponse response = new CheckoutResponse();

        CheckoutResponse.DeliverTime time = new CheckoutResponse.DeliverTime();
        time.value = Integer.valueOf(CheckoutResponse.DeliverTime.ON_LIMITED_ID);
        time.checked = true;
        response.body.deliverTimeList.add(time);

        time = new CheckoutResponse.DeliverTime();
        time.value = Integer.valueOf(CheckoutResponse.DeliverTime.HOLIDAY_ID);
        time.checked = false;
        response.body.deliverTimeList.add(time);

        time = new CheckoutResponse.DeliverTime();
        time.value = Integer.valueOf(CheckoutResponse.DeliverTime.WORKING_DAY_ID);
        time.checked = false;
        response.body.deliverTimeList.add(time);

        CheckoutResponse.Invoice invoice = new CheckoutResponse.Invoice();
        invoice.value = Integer.valueOf(CheckoutResponse.Invoice.ELECTRON_ID);
        invoice.checked = true;
        response.body.invoiceList.add(invoice);

        invoice = new CheckoutResponse.Invoice();
        invoice.value = Integer.valueOf(CheckoutResponse.Invoice.PERSONAL_ID);
        invoice.checked = false;
        response.body.invoiceList.add(invoice);

        invoice = new CheckoutResponse.Invoice();
        invoice.value = Integer.valueOf(CheckoutResponse.Invoice.COMPANY_ID);
        invoice.checked = false;
        response.body.invoiceList.add(invoice);

        Address addr = new Address();
        addr.address_id = "2";
        addr.consignee = "牛毅2";
        addr.province_name = "北京";
        addr.city_name = "北京";
        addr.district_name = "海淀区";
        addr.address = "西三旗建材城路";
        addr.tel = "122222222";

        response.body.address = addr;

        response.body.productMoney = "1000";
        response.body.shipment = "100";
        response.body.amount = "1100";

        return response;
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
//        if(hasFocus){
//           view.setBackgroundResource(R.drawable.height_selector);
//        }else{
//            view.setBackgroundResource(android.R.color.transparent);
//        }
    }

    @Override
    public void onSelectChange(View view, String value) {
        if(view == mDeliverTimeItem){
            ProductManager.INSTANCE.getCurrentCheckoutResponse().setDeliverTimeSelectedByValue(value);
        }else if(view == mInvoiceItem){
            ProductManager.INSTANCE.getCurrentCheckoutResponse().setInvoiceSelectedByValue(value);
        }
    }

}