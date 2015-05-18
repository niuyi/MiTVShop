package com.xiaomi.mitv.shop.model;

import android.text.TextUtils;
import com.xiaomi.mitv.shop.network.JsonSerializer;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by niuyi on 2015/5/15.
 */
public class CheckoutResponse {
    public Header header;
    public CheckoutInfo body;

    public static CheckoutResponse parse(String input){

        if(TextUtils.isEmpty(input)){
            return null;
        }

        try {
            JSONObject root = new JSONObject(input);

            Header header = Header.parse(root);

            if(header != null && header.code == Header.OK){
                if(root.has("body")){

                    CheckoutInfo body = JsonSerializer.getInstance().deserialize(root.getJSONObject("body").toString(), CheckoutInfo.class);

                    CheckoutResponse response = new CheckoutResponse();
                    response.header = header;
                    response.body = body;

                    return response;
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;

//        try{
//            return JsonSerializer.getInstance().deserialize(input, CheckoutResponse.class);
//        }catch (Exception e){
//
//        }
//
//        return null;
    }

    public static class Header{
        public static final int OK = 0;
        public int code;
        public String desc;

        public static Header parse(JSONObject root){
            try {
                if(root.has("header")){
                    JSONObject headerObj = root.getJSONObject("header");

                    Header header = new Header();
                    header.code = headerObj.optInt("code");
                    header.desc = headerObj.optString("desc");

                    return header;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    public static class CheckoutInfo {
        public Address address;
        public Payment[] payment;
        public Shipment[] shipmentlist;
        public DeliverTime[] delivertime;
        public Invoice[] invoice;
        public String invoice_title;
        public boolean invoice_open;
        public int total;
        public String amount;
        public String shipment;
    }

    public static class Address{
        public String address_id;
        public String address_name;
        public String consignee;

        public Map<String, String> country;
        public Map<String, String> province;
        public Map<String, String> city;
        public Map<String, String> district;
        public Map<String, String> area;

        public String address;
        public String zipcode;
        public String tel;
    }

    public static class Payment{
        public String brief;
        public String pay_id;
        public String tpis;
        public boolean checked;
    }

    public static class Shipment{
        public String shipment_id;
        public String brief;
        public String amount;
        public boolean checked;
    }

    public static class DeliverTime{
        public String value;
        public String desc;
        public boolean checked;
    }

    public static class Invoice{
        public String type;
        public int value;
        public String desc;
        public boolean checked;
    }
}
