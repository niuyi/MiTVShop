package com.xiaomi.mitv.shop.model;

import android.text.TextUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by niuyi on 2015/5/20.
 */
public class OrderStatus {

    public static final String PAY_DONE_STATUS = "4";
    public boolean isPayDone = false;

    public static OrderStatus parse(String input){
        if(TextUtils.isEmpty(input)){
            return null;
        }

        try {
            JSONObject root = new JSONObject(input);

            CheckoutResponse.Header header = CheckoutResponse.Header.parse(root);

            if(header != null && header.code == CheckoutResponse.Header.OK){

                JSONObject body = root.optJSONObject("body");

                if(body != null){
                    OrderStatus result = new OrderStatus();

                    String orderStatus = body.optString("order_status", "");

                    result.isPayDone = PAY_DONE_STATUS.equalsIgnoreCase(orderStatus);

                    return result;
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;

    }
}
