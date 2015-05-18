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
    protected Object getInput() {
        return null;
    }

    @Override
    protected String getPath() {
        return "mishop/api/list";
    }

    @Override
    protected String getParameters() {
        return String.format("&parent=%s", parentId == null ? "" : parentId);
    }

    @Override
    protected Locale getLocale() {
        return null;
    }
}
