package com.xiaomi.mitv.shop.request;

import com.xiaomi.mitv.shop.network.MyDuokanBaseRequest;

import java.util.Locale;

/**
 * Created by linniu on 2015/5/16.
 */
public class PayRequest extends MyDuokanBaseRequest {
    private String mUid;
    private final String mOrderId;
    private final String mBank;

    //mishop/api/bankgo?uid={user_id}&orderId={orderId}&bank={bank}&extend_field={extend_field}
    public PayRequest(String uid, String orderId, String bank) {
        mUid = uid;
        mOrderId = orderId;
        mBank = bank;
    }

    @Override
    protected byte[] getInput() {
        return null;
    }

    @Override
    protected String getPath() {
        return "mishop/api/pay/bankgo";
    }

    @Override
    protected String getParameters() {
        return String.format("&uid=%s&orderId=%s&bank=%s", mUid, mOrderId, mBank);
    }

    @Override
    protected Locale getLocale() {
        return null;
    }
}
