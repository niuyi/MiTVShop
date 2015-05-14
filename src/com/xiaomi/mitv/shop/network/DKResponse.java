package com.xiaomi.mitv.shop.network;

import android.util.Log;
import com.xiaomi.mitv.shop.model.ProductDetail;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by niuyi on 2015/5/8.
 */
public class DKResponse<T> {

    public static final int STATUS_SUCCESS = 0;
    public static final int STATUS_NETWORK_ERROR = 10000;
    public static final int STATUS_SERVER_ERROR = 10001;
    public static final int STATUS_UNKOWN_ERROR = 10002;

    public static final String DATA_KEY = "DATA";
    private final String rawData;

    public int status;
    private String response;
    private String data;

    public DKResponse(int status, String rawData) {
        this.status = status;
        this.rawData = rawData;
        this.response = parseData(rawData);
        this.data = rawData;
    }

    private String parseData(String response) {
        try {
            JSONObject root = new JSONObject(response);
            if(root.has("data")){
                return root.getString("data");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return "";
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

    public String getData(){
        return data;
    }

}
