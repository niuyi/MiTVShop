package com.xiaomi.mitv.shop.request;

import com.xiaomi.mitv.shop.network.MyDuokanBaseRequest;

import java.util.Locale;

/**
 * Created by niuyi on 2015/5/20.
 */
public class ViewOrderRequest extends MyDuokanBaseRequest {

    private final String uid;
    private final String orderId;

    public ViewOrderRequest(String uid, String orderId) {
        this.uid = uid;
        this.orderId = orderId;
    }

    @Override
    protected byte[] getInput() {
        return new byte[0];
    }

    @Override
    protected String getPath() {
        return "mishop/api/order/view_order";
    }

    @Override
    protected String getParameters() {
        return String.format("&uid=%s&orderId=%s", uid, orderId);
    }

    @Override
    protected Locale getLocale() {
        return null;
    }
}
