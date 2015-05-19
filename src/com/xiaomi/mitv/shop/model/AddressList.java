package com.xiaomi.mitv.shop.model;

import android.text.TextUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by niuyi on 2015/5/19.
 */
public class AddressList {

    public List<Address> addresses = new ArrayList<Address>();

    public static AddressList parse(String input){
        if(TextUtils.isEmpty(input)){
            return null;
        }

        try {
            JSONObject root = new JSONObject(input);

            CheckoutResponse.Header header = CheckoutResponse.Header.parse(root);

            if(header != null && header.code == CheckoutResponse.Header.OK){
                AddressList result = new AddressList();

                JSONArray body = root.optJSONArray("body");
                if(body == null){
                    return  result;
                }

                for(int i = 0 ; i < body.length() ; i++){
                    JSONObject item = body.getJSONObject(i);

                    Address addr = Address.parse(item);
                    if(addr != null){
                        result.addresses.add(addr);
                    }
                }

                return result;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;

    }
}
