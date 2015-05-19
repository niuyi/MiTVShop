package com.xiaomi.mitv.shop.request;

import com.xiaomi.mitv.shop.network.MyDuokanBaseRequest;

import java.util.Locale;

/**
 * Created by niuyi on 2015/5/18.
 */
public class RegionRequest extends MyDuokanBaseRequest {

    private String parentId;

    public RegionRequest(String parentId) {
        this.parentId = parentId;
    }

    @Override
    protected  byte[] getInput() {
        return null;
    }

    @Override
    protected String getPath() {
        return "mishop/api/address/region";
    }

    @Override
    protected String getParameters() {
        if(parentId == null){
            return null;
        }

        return String.format("&parent=%s", parentId);
    }

    @Override
    protected Locale getLocale() {
        return null;
    }
}
