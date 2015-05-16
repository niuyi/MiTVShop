package com.xiaomi.mitv.shop.network;

import java.util.Locale;

/**
 * Created by linniu on 2015/5/16.
 */
public class SubmitRequest extends MyDuokanBaseRequest {
    private String mUid;
    private String mAddressId;
    private String mPayId;

    //mishop/api/submit_order?uid={user_id}&addressId={addressId}&payId={payId}


    public SubmitRequest(String uid, String addressId, String payId) {
        mUid = uid;
        mAddressId = addressId;
        mPayId = payId;
    }

    @Override
    protected Object getInput() {
        return null;
    }

    @Override
    protected String getPath() {
        return "mishop/api/submit_order";
    }

    @Override
    protected String getParameters() {
        return String.format("&uid=%s&addressId=%s&payId=%s", mUid, mAddressId, mPayId);
    }

    @Override
    protected Locale getLocale() {
        return null;
    }
}
