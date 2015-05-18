package com.xiaomi.mitv.shop.request;

import android.text.TextUtils;
import android.util.Log;
import com.xiaomi.mitv.shop.db.ShopDBManager;
import com.xiaomi.mitv.shop.model.ProductDetail;
import com.xiaomi.mitv.shop.network.DKResponse;
import com.xiaomi.mitv.shop.network.MyDuokanBaseRequest;

import java.util.Locale;

/**
 * Created by linniu on 2015/5/9.
 */
public class ProductDetailRequest extends MyDuokanBaseRequest {

    private static final String TAG = "ProductDetailRequest";
    private final String mPid;

    public ProductDetailRequest(String pid) {
        mPid = pid;
    }

    @Override
    protected DKResponse beforeSend() {
        Log.i(TAG, "beforeSend: " + mPid);
        String value = ShopDBManager.INSTANCE.getValue(mPid);

        if(!TextUtils.isEmpty(value)){
            DKResponse res = new DKResponse(DKResponse.STATUS_BEFORE_SEND_SUCCESS, value, false);
            return res;
        }

        return null;
//        ProductDetail detail = new ProductDetail();
//        detail.name = "小米手机";
//        detail.price = "¥1999起";
//        detail.images = new String[]{
//                "http://c1.mifile.cn/f/i/2014/cn/goods/mi4/md/gallery/gallery-list-a.png",
//                "http://c1.mifile.cn/f/i/2014/cn/goods/mi4/md/gallery/gallery-list-d_2x.jpg",
//                "http://c1.mifile.cn/f/i/2014/cn/goods/mi4/md/gallery/gallery-list-e_2x.png"};
//
//
//        String json = JsonSerializer.getInstance().serialize(detail);
//
//        mResponse = new DKResponse(DKResponse.STATUS_SUCCESS, json);
//
//        return true;
    }

    @Override
    protected Object getInput() {
        return null;
    }

    @Override
    protected String getPath() {
        return "mishop/api/list";
    }

    @Override
    protected String getParameters() {
        return String.format("&product=%s", mPid);
    }

    @Override
    protected Locale getLocale() {
        return null;
    }
}
