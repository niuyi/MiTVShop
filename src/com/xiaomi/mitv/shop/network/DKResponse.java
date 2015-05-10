package com.xiaomi.mitv.shop.network;

/**
 * Created by niuyi on 2015/5/8.
 */
public class DKResponse {

    public static final int STATUS_SUCCESS = 0;
    public static final int STATUS_NETWORK_ERROR = 10000;
    public static final int STATUS_SERVER_ERROR = 10001;
    public static final int STATUS_UNKOWN_ERROR = 10002;

    public static final String DATA_KEY = "DATA";

    private int status;
    private String response;

    public DKResponse(int status, String response) {
        this.status = status;
        this.response = response;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getResponse(){
        return response;
    }
}
