package com.xiaomi.mitv.shop;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import com.xiaomi.mitv.shop.model.CheckoutResponse;
import com.xiaomi.mitv.shop.model.ProductDetail;
import com.xiaomi.mitv.shop.model.ProductManager;
import com.xiaomi.mitv.shop.network.DKResponse;
import com.xiaomi.mitv.shop.network.MyBaseRequest;
import com.xiaomi.mitv.shop.request.CheckoutRequest;
import com.xiaomi.mitv.shop.widget.CheckoutFragment;

/**
 * Created by linniu on 2015/5/23.
 */
public class CheckoutActivity extends BaseLoadingActivity {

    private static final String TAG = "CheckoutActivity";
    private String mPid;
    private ProductDetail mProduct;
    private String mUid;
    private String mGid;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setFailureMessage("添加购物车失败");

        mPid = getIntent().getStringExtra("pid");
        mProduct = ProductManager.INSTSNCE.getProductDetail(mPid);

        mUid = getIntent().getStringExtra("uid");
        mGid = getIntent().getStringExtra("gid");

        CheckoutFragment fragment = new CheckoutFragment();
        switchFragment(fragment);

//        CheckoutRequest req = new CheckoutRequest(mUid, mGid);
//
//        req.setObserver(new MyBaseRequest.MyObserver() {
//            @Override
//            public void onRequestCompleted(MyBaseRequest request, DKResponse response) {
//                if (response != null
//                        && response.getStatus() == DKResponse.STATUS_SUCCESS
//                        && !TextUtils.isEmpty(response.getResponse())) {
//                    Log.i(TAG, "onCheckout!!: " + response.getResponse());
//
//                    CheckoutResponse res = CheckoutResponse.parse(response.getResponse());
//
//                    if (res != null) {
////                        Log.i(TAG, "CheckoutResponse: " + res.header.code);
////                        if (res.body.address != null) {
////                            adddressId = res.body.address.address_id;
////                            Log.i(TAG, "CheckoutResponse, address id: " + res.body.address.address_id);
////                            Log.i(TAG, "CheckoutResponse, address consignee: " + res.body.address.consignee);
////                            Log.i(TAG, "CheckoutResponse, address address: " + res.body.address.address);
////                        } else {
////                            Log.i(TAG, "CheckoutResponse address is null");
////                        }
//                    } else {
//                        Log.i(TAG, "CheckoutResponse is null");
//                        showFailurePage();
//                    }
//
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
//                showFailurePage();
//            }
//        });
//        req.send();
    }
}