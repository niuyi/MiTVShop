package com.xiaomi.mitv.shop.request;

import com.xiaomi.mitv.shop.network.MyDuokanBaseRequest;

import java.util.Locale;

/**
 * Created by niuyi on 2015/5/18.
 */
public class GetAddressListRequest extends MyDuokanBaseRequest {

    private String uid;

    public GetAddressListRequest(String uid){
        this.uid = uid;
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
        return String.format("&uid=%s", uid);
    }

    @Override
    protected Locale getLocale() {
        return null;
    }
}
