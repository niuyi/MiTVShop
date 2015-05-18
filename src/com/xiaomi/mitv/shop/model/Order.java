package com.xiaomi.mitv.shop.model;

import android.text.TextUtils;
import com.xiaomi.mitv.shop.network.JsonSerializer;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by niuyi on 2015/5/18.
 */
public class Order {
    public CheckoutResponse.Header header;
    public String id;

    public static Order parse(String input){
        if(TextUtils.isEmpty(input)){
            return null;
        }

        try {
            JSONObject root = new JSONObject(input);

            CheckoutResponse.Header header = CheckoutResponse.Header.parse(root);

            if(header != null && header.code == CheckoutResponse.Header.OK){
                if(root.has("body")){
                    Order order = new Order();
                    order.header = header;
                    order.id = root.getString("body");
                    return order;
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
