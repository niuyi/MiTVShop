package com.xiaomi.mitv.shop;

import android.accounts.Account;
import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.xiaomi.mitv.api.util.MILog;
import com.xiaomi.mitv.shop.account.MiTVAccount;
import com.xiaomi.mitv.shop.db.ShopDBHelper;
import com.xiaomi.mitv.shop.db.ShopDBManager;
import com.xiaomi.mitv.shop.model.*;
import com.xiaomi.mitv.shop.network.*;
import com.xiaomi.mitv.shop.request.*;
import com.xiaomi.mitv.shop.util.QRGenerator;
import com.xiaomi.mitv.shop.widget.DialogButtonView;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MyActivity extends Activity implements DialogButtonView.OnItemCheckedListener {
    private static final String TAG = "MyActivity";

    private LinearLayout mContainer;
    private ArrayList<DialogButtonView> mViews = new ArrayList<DialogButtonView>();
    private ProductDetail mDetail;
    private Button mButton;
    private ImageView mQR;
    private MiTVAccount mAccount;

    private String adddressId;
    private String accountId;
    private String orderId;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Log.i(TAG, "onCreate");

        ViewGroup root = (ViewGroup)findViewById(R.id.root);


        mContainer = new LinearLayout(this);
        mContainer.setOrientation(LinearLayout.VERTICAL);
        FrameLayout.LayoutParams para = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        mContainer.setFocusable(true);
//
        root.addView(mContainer, para);
//
//        mDetail = getDetail();
//
//        //todo: check the data
//
//        for(int i = 0 ; i < mDetail.props_def.length; i++){
//            ProductDetail.Prop p = mDetail.props_def[i];
//            DialogButtonView view = new DialogButtonView(this);
//            view.setOnItemCheckedListener(this);
//
//            view.setProp(p);
//
//            view.inflate();
//            view.setTag(i);
//
//            mContainer.addView(view);
//            mViews.add(view);
//        }
//
        setupCheckout();

        submitButton();

        setupCheckOrder();

        setupPay();

        setupAddress();

        setupRegion();
        setupRegion2();

        setupAddAddress();

//        if(mViews.size() > 0){
//            mViews.get(0).setFocus();
//        }
    }

    private void setupCheckOrder() {
        Button button = new Button(this);
        button.setText("check order");
        button.requestFocus();
//        mButton.setEnabled(false);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ViewOrderRequest req = new ViewOrderRequest(accountId, orderId);
                req.setObserver(new MyBaseRequest.MyObserver() {
                    @Override
                    public void onRequestCompleted(MyBaseRequest request, DKResponse response) {
                        if(response != null
                                && response.getStatus() == DKResponse.STATUS_SUCCESS
                                && !TextUtils.isEmpty(response.getResponse())){
                            Log.i(TAG, "ViewOrderRequest res: " + response.getResponse());

                            OrderStatus status = OrderStatus.parse(response.getResponse());
                            Log.i(TAG, "ViewOrderRequest is pay done: " + status.isPayDone);

                        }
                    }

                    @Override
                    public void onBeforeSendDone(MyBaseRequest request, DKResponse response) {

                    }

                    @Override
                    public void onAbort() {

                    }
                });

                req.send();
            }
        });


        mContainer.addView(button);
    }

    private void setupAddAddress() {
        final EditText edit = new EditText(this);
        edit.setText("abc");
        edit.setFocusable(true);
        edit.setEnabled(true);
        edit.requestFocus();
        mContainer.addView(edit);

        Button button = new Button(this);
        button.setText("add Address");
//        button.requestFocus();
//        mButton.setEnabled(false);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Address addr = new Address();
                addr.consignee = edit.getText().toString();
                addr.province_id = 2;
                addr.city_id = 36;
                addr.district_id = 381;
                addr.address = "红军营南路媒体村天畅园7号楼2层";
                addr.zipcode = "100107";
                addr.tel = "13810099138";

                AddAddressRequest req = new AddAddressRequest("49649888", addr);

                req.setObserver(new MyBaseRequest.MyObserver() {
                    @Override
                    public void onRequestCompleted(MyBaseRequest request, DKResponse response) {
                        if(response != null
                                && response.getStatus() == DKResponse.STATUS_SUCCESS
                                && !TextUtils.isEmpty(response.getResponse())){
                            Log.i(TAG, "RegionRequest res: " + response.getResponse());
                            AddAddressResponse res = AddAddressResponse.parse(response.getResponse());
                            if(res != null){
                                Log.i(TAG, "AddAddressRequest res: " + res.addressId);
                                Log.i(TAG, "AddAddressRequest res: " + res.addressId);
                            }
                        }
                    }

                    @Override
                    public void onBeforeSendDone(MyBaseRequest request, DKResponse response) {

                    }

                    @Override
                    public void onAbort() {

                    }
                });

                req.send();
            }
        });


        mContainer.addView(button);
    }

    private void setupRegion2() {
        Button button = new Button(this);
        button.setText("getRegion2");
        button.requestFocus();
//        mButton.setEnabled(false);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final RegionRequest req = new RegionRequest("4");
                req.setObserver(new MyBaseRequest.MyObserver() {
                    @Override
                    public void onRequestCompleted(MyBaseRequest request, DKResponse response) {
                        if(response != null
                                && response.getStatus() == DKResponse.STATUS_SUCCESS
                                && !TextUtils.isEmpty(response.getResponse())){
                            Log.i(TAG, "RegionRequest res: " + response.getResponse());

                            RegionList regionList = RegionList.parse(response.getResponse());

                            if(regionList != null){
                                Log.i(TAG, "regionList size: " + regionList.regions.size());
                            }

                        }
                    }

                    @Override
                    public void onBeforeSendDone(MyBaseRequest request, DKResponse response) {

                    }

                    @Override
                    public void onAbort() {

                    }
                });

                req.send();
            }
        });


        mContainer.addView(button);
    }

    private void setupRegion() {
        Button button = new Button(this);
        button.setText("getRegion");
        button.requestFocus();
//        mButton.setEnabled(false);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final RegionRequest req = new RegionRequest(null);
                req.setObserver(new MyBaseRequest.MyObserver() {
                    @Override
                    public void onRequestCompleted(MyBaseRequest request, DKResponse response) {
                        if(response != null
                                && response.getStatus() == DKResponse.STATUS_SUCCESS
                                && !TextUtils.isEmpty(response.getResponse())){
                            Log.i(TAG, "RegionRequest res: " + response.getResponse());

                            RegionList regionList = RegionList.parse(response.getResponse());

                            if(regionList != null){
                                Log.i(TAG, "regionList size: " + regionList.regions.size());
                            }

                        }
                    }

                    @Override
                    public void onBeforeSendDone(MyBaseRequest request, DKResponse response) {

                    }

                    @Override
                    public void onAbort() {

                    }
                });

                req.send();
            }
        });


        mContainer.addView(button);
    }

    private void setupAddress() {
        Button button = new Button(this);
        button.setText("getAddress");
        button.requestFocus();
//        mButton.setEnabled(false);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String value = ShopDBManager.INSTANCE.getValue("49649888", ShopDBHelper.TABLE_ADDRESS_LIST_NAME);

//                if(!TextUtils.isEmpty(value)){
//                    Log.i(TAG, "get address list from db");
//                    AddressList list = AddressList.parse(value);
//                    if(list.addresses.size() > 0){
//                        Log.i(TAG, "consignee: " +list.addresses.get(0).consignee);
//                    }
//
//                    return;
//                }


                final long start = System.currentTimeMillis();
                GetAddressListRequest req = new GetAddressListRequest("49649888");
                req.setObserver(new MyBaseRequest.MyObserver() {
                    @Override
                    public void onRequestCompleted(MyBaseRequest request, DKResponse response) {
                        if(response != null
                                && response.getStatus() == DKResponse.STATUS_SUCCESS
                                && !TextUtils.isEmpty(response.getResponse())){
                            Log.i(TAG, "GetAddressListRequest res: " + response.getResponse());
                            Log.i(TAG, "GetAddressListRequest res dur: " + (System.currentTimeMillis() - start));

                            AddressList list = AddressList.parse(response.getResponse());

                            if(list != null){
                                Log.i(TAG, "address list: " + list.addresses.size());
                                if(list.addresses.size() > 0){
                                    Log.i(TAG, "consignee: " +list.addresses.get(0).consignee);
                                    ShopDBManager.INSTANCE.setValue("49649888", response.getResponse(), ShopDBHelper.TABLE_ADDRESS_LIST_NAME);
                                }
                            }else{
                                Log.i(TAG, "address list is null");
                            }
                        }
                    }

                    @Override
                    public void onBeforeSendDone(MyBaseRequest request, DKResponse response) {

                    }

                    @Override
                    public void onAbort() {

                    }
                });

                req.send();
            }
        });


        mContainer.addView(button);
    }

    private void setupPay() {
        Button payButton = new Button(this);
        payButton.setText("pay");
        payButton.requestFocus();
//        mButton.setEnabled(false);

        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPay();
            }
        });
        mContainer.addView(payButton);

        mQR = new ImageView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(200, 200);
        params.gravity = Gravity.RIGHT;
        mContainer.addView(mQR, params);
    }

    private void submitButton() {
        Button button = new Button(this);
        button.setText("Submit");
        button.requestFocus();
//        mButton.setEnabled(false);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSubmit(v);
            }
        });
        mContainer.addView(button);
    }

    private void setupCheckout() {
        Button checkoutButton = new Button(this);
        checkoutButton.setText("Checkout");
        checkoutButton.requestFocus();
//        checkoutButton.setEnabled(false);

        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCheckout();
//                getCheckout();
            }
        });
        mContainer.addView(checkoutButton);
    }

    public void onPay(){
        Bitmap dCode = QRGenerator.create2DCode("http://www.baidu.com", 200);
        mQR.setImageBitmap(dCode);
    }

    private void getCheckout(){
        Log.i(TAG, "getCheckout");

        AssetManager assetManager = getAssets();

        ByteArrayOutputStream outputStream = null;
        InputStream inputStream = null;
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();

        try {
            inputStream = assetManager.open("checkout_response");
            final int blockSize = 8192;
            byte[] buffer = new byte[blockSize];
            int count = 0;
            while((count = inputStream.read(buffer, 0, blockSize)) > 0) {
                byteStream.write(buffer,0, count);
            }
        } catch (IOException e) {
        }

        try {
            byte[] bytes = byteStream.toByteArray();
            String json = new String(bytes, 0, bytes.length, "utf-8");

            JSONObject root = new JSONObject(json);
            JSONObject body = root.getJSONObject("body");
            JSONObject address = body.optJSONObject("address");

            if(address != null){
                Log.i(TAG, "address: " + address);
            }else{
                Log.i(TAG, "address is null");
            }

//            CheckoutResponse res = CheckoutResponse.parse(json);
//
//            if(res != null){
//                Log.i(TAG, "CheckoutResponse: " + res.header.code);
//                if(res.body.address != null){
//                    Log.i(TAG, "CheckoutResponse, address id: " + res.body.address.address_id);
//                    Log.i(TAG, "CheckoutResponse, address consignee: " + res.body.address.consignee);
//                    Log.i(TAG, "CheckoutResponse, address address: " + res.body.address.address);
//                }else{
//                    Log.i(TAG, "CheckoutResponse address is null");
//                }
//            }else{
//                Log.i(TAG, "CheckoutResponse is null");
//            }

//            JSONObject root = new JSONObject(json);
//            Log.i(TAG, "status: " + root.getInt("status"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ProductDetail getDetail(){
//        AssetManager assetManager = getAssets();
//        ByteArrayOutputStream outputStream = null;
//        InputStream inputStream = null;
//        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
//        try {
//            inputStream = assetManager.open("detail.json");
//            final int blockSize = 8192;
//            byte[] buffer = new byte[blockSize];
//            int count = 0;
//            while((count = inputStream.read(buffer, 0, blockSize)) > 0) {
//                byteStream.write(buffer,0, count);
//            }
//        } catch (IOException e) {
//        }
//
//        try {
//            byte[] bytes = byteStream.toByteArray();
//            String json = new String(bytes, 0, bytes.length, "utf-8");
//
//            DKResponse res = new DKResponse(1, json);
//            ProductDetail detail = ProductDetail.parse(res.getResponse());
//
//            for(ProductDetail.Node node : detail.props_tree){
//                printNode(node, 1);
//            }
//
//            detail.name = "小米手机4电信4G版2GB内存 白色 16G";
//            return detail;
//
//
//
////            JSONObject root = new JSONObject(json);
////            Log.i(TAG, "status: " + root.getInt("status"));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        return null;
    }

    private void printNode(ProductDetail.Node node, int index){
        StringBuilder sb = new StringBuilder();
        for(int i = 0 ; i < index ; i++){
            sb.append("---");
        }

        sb.append(String.format("node: id{%s} valid(%b) gid(%s)", node.id, node.valid, node.gid));

        Log.i(TAG, sb.toString());

        if(node.child != null){
            for(ProductDetail.Node sub : node.child){
                printNode(sub, index+1);
            }
        }
    }


    public void onCheckout(){
        CheckoutRequest req = new CheckoutRequest(accountId, "2151400205");
        req.setObserver(new MyBaseRequest.MyObserver() {
            @Override
            public void onRequestCompleted(MyBaseRequest request, DKResponse response) {
                if (response != null
                        && response.getStatus() == DKResponse.STATUS_SUCCESS
                        && !TextUtils.isEmpty(response.getResponse())) {
                    Log.i(TAG, "res: " + response.getResponse());

                    CheckoutResponse res = CheckoutResponse.parse(response.getResponse());

                    if (res != null) {
                        Log.i(TAG, "CheckoutResponse: " + res.header.code);
                        if (res.body.address != null) {
                            adddressId = res.body.address.address_id;
                            Log.i(TAG, "CheckoutResponse, address id: " + res.body.address.address_id);
                            Log.i(TAG, "CheckoutResponse, address consignee: " + res.body.address.consignee);
                            Log.i(TAG, "CheckoutResponse, address address: " + res.body.address.address);
                        } else {
                            Log.i(TAG, "CheckoutResponse address is null");
                        }
                    } else {
                        Log.i(TAG, "CheckoutResponse is null");
                    }

                }
            }

            @Override
            public void onBeforeSendDone(MyBaseRequest request, DKResponse response) {

            }

            @Override
            public void onAbort() {

            }
        });
        req.send();
    }

    public void onSubmit(View view){

        //10140627350019340
        //jiang: 10150130630035249  31814451
        //niuyi : 10140627350019340
        SubmitRequest req = new SubmitRequest(accountId, adddressId, "1");
        req.setObserver(new MyBaseRequest.MyObserver() {
            @Override
            public void onRequestCompleted(MyBaseRequest request, DKResponse response) {
                if(response != null
                        && response.getStatus() == DKResponse.STATUS_SUCCESS
                        && !TextUtils.isEmpty(response.getResponse())){
                    Log.i(TAG, "res: " + response.getResponse());

                    Order order = Order.parse(response.getResponse());

                    if(order != null){
                        Log.i(TAG, "order: " + order.header.code);
                        Log.i(TAG, "order id: " + order.id);
                        orderId = order.id;
                        //
//                        PayRequest req = new PayRequest("49649888", order.id, "alipay");
//                        req.setObserver(new MyBaseRequest.MyObserver() {
//                            @Override
//                            public void onRequestCompleted(MyBaseRequest request, DKResponse response) {
//                                if(response != null
//                                        && response.getStatus() == DKResponse.STATUS_SUCCESS
//                                        && !TextUtils.isEmpty(response.getResponse())){
//                                    Log.i(TAG, "PayRequest res: " + response.getResponse());
//                                }
//                            }
//
//                            @Override
//                            public void onBeforeSendDone(MyBaseRequest request, DKResponse response) {
//
//                            }
//
//                            @Override
//                            public void onAbort() {
//
//                            }
//                        });
//
//                        req.send();

                    }else {
                        Log.i(TAG, "order is null!");
                    }

                }
            }

            @Override
            public void onBeforeSendDone(MyBaseRequest request, DKResponse response) {

            }

            @Override
            public void onAbort() {

            }
        });

        req.send();

//        CheckoutRequest req = new CheckoutRequest("31814451", "2151400205");
//        req.setObserver(new MyBaseRequest.MyObserver() {
//            @Override
//            public void onRequestCompleted(MyBaseRequest request, DKResponse response) {
//                if(response != null
//                        && response.getStatus() == DKResponse.STATUS_SUCCESS
//                        && !TextUtils.isEmpty(response.getResponse())){
//                    Log.i(TAG, "res: " + response.getResponse());
//
//                    Order order = Order.parse(response.getResponse());
//
//                    Log.i(TAG, "order: " + order.header.code);
//                }
//            }
//
//            @Override
//            public void onBeforeSendDone(MyBaseRequest request, DKResponse response) {
//
//            }
//
//            @Override
//            public void onAbort() {
//
//            }
//        });
//        req.send();

//        GoodSelectionWindow window = new GoodSelectionWindow(this, mDetail);
//        window.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
//        AssetManager assetManager = getAssets();
//        ByteArrayOutputStream outputStream = null;
//        InputStream inputStream = null;
//        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
//        try {
//            inputStream = assetManager.open("detail.json");
//            final int blockSize = 8192;
//            byte[] buffer = new byte[blockSize];
//            int count = 0;
//            while((count = inputStream.read(buffer, 0, blockSize)) > 0) {
//                byteStream.write(buffer,0, count);
//            }
//        } catch (IOException e) {
//        }
//
//        try {
//            byte[] bytes = byteStream.toByteArray();
//            String json = new String(bytes, 0, bytes.length, "utf-8");
//
//            DKResponse res = new DKResponse(1, json);
//            ProductDetail detail = ProductDetail.parse(res.getResponse());
//
//            Log.i(TAG, "newRes name: " + detail.name);
//            Log.i(TAG, "newRes price: " + detail.price);
//            Log.i(TAG, "newRes size: " + detail.goods_status.size());
//
//            for(ProductDetail.Prop p : detail.props_def){
//                Log.i(TAG, "newRes prop:" + p.name);
//                for(ProductDetail.Option o : p.options){
//                    Log.i(TAG, "newRes option:" + o.name);
//                }
//            }
//
//            for(ProductDetail.Node node : detail.props_tree){
//                printNode(node, 1);
//            }
//
////            JSONObject root = new JSONObject(json);
////            Log.i(TAG, "status: " + root.getInt("status"));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


//        for(int i = 0 ; i < mContainer.getChildCount() ; i++){
//            DialogButtonView v = (DialogButtonView)mContainer.getChildAt(i);
//            Log.i(TAG, "submit select: " + v.getSelected());
//        }
    }

    @Override
    public void onChecked(DialogButtonView view, RadioButton button) {
        Log.i(TAG, "onChecked: " + view.getTag() + " ,id: " + button.getId() + " ,tag: " + button.getTag());

//        int pos = mViews.indexOf(view);
//
//        if(pos < 0)
//            return;
//
////        if(pos > 0){
////            DialogButtonView dialogButtonView = mViews.get(pos - 1);
////            dialogButtonView.setAllNextFocusDownId(button.getId());
////        }
//
//        if(pos < mViews.size() - 1){
//            DialogButtonView dialogButtonView = mViews.get(pos + 1);
//            dialogButtonView.setAllNextFocusUpId(button.getId());
//        }
//
//        if(pos == mViews.size() -1 && mButton != null){
//            mButton.setNextFocusUpId(button.getId());
//        }
//
//        ProductDetail.Option option = (ProductDetail.Option)button.getTag();
//        ProductDetail.Node node = mDetail.findNodeById(option.id);
//        Log.i(TAG, "find node: " + node.id);
//
//        for(int i = pos + 1 ; i < mViews.size() ; i ++){
//            DialogButtonView dialogButtonView = mViews.get(i);
//            dialogButtonView.updateStatus(node);
//        }
//
//        Log.i(TAG, "gid: " + node.gid);
//        if(!TextUtils.isEmpty(node.gid)){
//            String text = mDetail.goods_status.get(node.gid);
//
//            if("1".equalsIgnoreCase(text)){
//                mButton.setFocusable(true);
//                mButton.setEnabled(true);
//                mButton.setText("Buy");
//            }else{
//                mButton.setFocusable(false);
//                mButton.setEnabled(false);
//                mButton.setText("No Goods");
//            }
//        }else{
//            mButton.setFocusable(false);
//            mButton.setEnabled(false);
//            mButton.setText("Buy");
//        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart");

        mAccount = new MiTVAccount(this);
        if(mAccount.getAccount() == null){
            Log.i(TAG, "accout is null");
            login();
        }else{
            Log.i(TAG, "get account: " + mAccount.getAccount().name);
            accountId = mAccount.getAccount().name;
        }
    }

    private void login() {

        MILog.i(TAG, "Login");
        if(isFinishing() ){
            MILog.i(TAG, "Login: finish return");
            return;
        }

        mAccount.login(this, new MiTVAccount.LoginCallback() {

            @Override
            public void onSuccess(Account account) {
                MILog.i(TAG, "login success");
                Log.i(TAG, "get account: " + mAccount.getAccount().name);
            }

            @Override
            public void onFailed(int error, String message) {
                MILog.i(TAG, "login failed");
                finish();
            }
        }, null);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "onRestart");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG, "onSaveInstanceState");
    }



    @Override
    protected void onDestroy() {
        Log.i(TAG, "onDestroy");
        super.onDestroy();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i(TAG, "onRestoreInstanceState");
    }
}
