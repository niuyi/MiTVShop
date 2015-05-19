package com.xiaomi.mitv.shop.request;

import com.xiaomi.mitv.shop.network.MyDuokanBaseRequest;

import java.util.Locale;

/**
 * Created by niuyi on 2015/5/15.
 */
public class CheckoutRequest extends MyDuokanBaseRequest {

    private String mUid;
    private String mGid;

    public CheckoutRequest(String uid, String gid){
        mUid = uid;
        mGid = gid;
    }

    @Override
    protected  byte[] getInput() {
        return null;
    }

    @Override
    protected String getPath() {
        return "mishop/api/checkout";
    }

    @Override
    protected String getParameters() {
        return String.format("&uid=%s&gid=%s", mUid, mGid);
    }

    @Override
    protected Locale getLocale() {
        return null;
    }
}
