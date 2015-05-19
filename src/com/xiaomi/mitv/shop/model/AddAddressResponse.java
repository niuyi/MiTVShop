package com.xiaomi.mitv.shop.model;

import android.text.TextUtils;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by niuyi on 2015/5/19.
 */
public class AddAddressResponse {
    public String addressId;

    public static AddAddressResponse parse(String input){
        if(TextUtils.isEmpty(input)){
            return null;
        }

        try {
            JSONObject root = new JSONObject(input);

            CheckoutResponse.Header header = CheckoutResponse.Header.parse(root);

            if(header != null && header.code == CheckoutResponse.Header.OK){

                JSONObject body = root.optJSONObject("body");
                if(body != null){
                    AddAddressResponse response = new AddAddressResponse();
                    response.addressId = body.optString("result");
                    return response;
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
