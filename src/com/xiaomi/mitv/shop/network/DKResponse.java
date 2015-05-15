package com.xiaomi.mitv.shop.network;

import android.text.TextUtils;
import android.util.Log;
import com.xiaomi.mitv.shop.model.ProductDetail;
import org.json.JSONException;
import org.json.JSONObject;

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

    public DKResponse(int status, String rawData) {
        Log.i("DKResponse: ", "new dkreponse, data: " + rawData);
        this.status = status;
        this.response = parseData(rawData);
    }

    private String parseData(String response) {
        if(TextUtils.isEmpty(response)){
            status = STATUS_SERVER_ERROR;
            return "";
        }

        try {
            JSONObject root = new JSONObject(response);

            if(root.has("status") && root.getInt("status") == 0){
                status = STATUS_SUCCESS;
            }else{
                status = STATUS_SERVER_ERROR;
                return null;
            }

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
}
