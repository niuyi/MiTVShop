package com.xiaomi.mitv.shop.network;

import java.util.Locale;

/**
 * Created by linniu on 2015/5/16.
 */
public class PayRequest extends MyDuokanBaseRequest {
    private String mUid;
    private final String mOrderId;
    private final String mBank;
    private final String mField;

    //mishop/api/bankgo?uid={user_id}&orderId={orderId}&bank={bank}&extend_field={extend_field}
    public PayRequest(String uid, String orderId, String bank, String field) {
        mUid = uid;
        mOrderId = orderId;
        mBank = bank;
        mField = field;
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
        return String.format("&uid=%s&orderId=%s&bank=%s&extend_field=%s", mUid, mOrderId, mBank, mField);
    }

    @Override
    protected Locale getLocale() {
        return null;
    }
}
