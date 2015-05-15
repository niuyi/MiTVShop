package com.xiaomi.mitv.shop.network;

import java.util.Locale;

/**
 * Created by niuyi on 2015/5/15.
 */
public class CheckoutRequest extends MyDuokanBaseRequest {
    @Override
    protected Object getInput() {
        return null;
    }

    @Override
    protected String getPath() {
        return "mishop/api/checkout";
    }

    @Override
    protected String getParameters() {
        return String.format("&uid=%s&gid=%s", "49649888", "2151400205");
    }

    @Override
    protected Locale getLocale() {
        return null;
    }
}
