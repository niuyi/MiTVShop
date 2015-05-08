package com.xiaomi.mitv.shop.network;

/**
 * Created by niuyi on 2015/5/8.
 */
public class DKResponse {

    public static final int STATUS_SUCCESS = 0;
    public static final int STATUS_NETWORK_ERROR = 10000;
    public static final int STATUS_SERVER_ERROR = 10001;
    public static final int STATUS_UNKOWN_ERROR = 10002;

    private int status;
    private long ts;

    public DKResponse() {
        status = STATUS_SUCCESS;
    }

    public DKResponse(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getTs() {
        return ts;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }
}
