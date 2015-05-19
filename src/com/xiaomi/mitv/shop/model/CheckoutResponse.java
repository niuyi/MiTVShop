package com.xiaomi.mitv.shop.model;

import android.text.TextUtils;
import android.util.Log;
import com.xiaomi.mitv.shop.network.JsonSerializer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by niuyi on 2015/5/15.
 */
public class CheckoutResponse {
    public Header header;
    public CheckoutInfo body = new CheckoutInfo();

    public static CheckoutResponse parse(String input){

        if(TextUtils.isEmpty(input)){
            return null;
        }

        try {
            JSONObject root = new JSONObject(input);

            Header header = Header.parse(root);

            if(header != null && header.code == CheckoutResponse.Header.OK){

                JSONObject body = root.optJSONObject("body");
                if(body == null){
                    return  null;
                }

                CheckoutResponse result = new CheckoutResponse();
                result.header = header;

                JSONObject address = body.optJSONObject("address");
                if (address == null) {
                    Log.i("CheckoutResponse", "address is null. need create new address.");
                }else {
                    result.body.address = Address.parse(address);
                }

                JSONArray payment = body.optJSONArray("payment");
                if (payment != null) {
                    for (int i = 0; i < payment.length(); ++i) {
                        JSONObject object = payment.optJSONObject(i);
                        Payment p = new Payment();

                        p.brief = object.optString("brief");
                        p.pay_id = object.optString("pay_id");
                        p.tpis = object.optString("tpis");
                        p.checked = object.optBoolean("checked");

                        result.body.paymentList.add(p);
                    }
                }

                JSONArray deliver = body.optJSONArray("delivertime");
                if (deliver != null) {
                    for (int i = 0; i < deliver.length(); ++i) {
                        JSONObject object = deliver.optJSONObject(i);
                        DeliverTime d = new DeliverTime();

                        d.desc = object.optString("desc");
                        d.value = object.optInt("value", 1);
                        d.checked = object.optBoolean("checked");

                        result.body.deliverTimeList.add(d);
                    }
                }

                JSONArray invoice = body.optJSONArray("invoice");
                if (invoice != null) {
                    for (int i = 0; i < invoice.length(); ++i) {
                        JSONObject object = invoice.optJSONObject(i);
                        Invoice in = new Invoice();
                        in.type = object.optString("type");
                        in.desc = object.optString("desc");
                        in.value = object.optInt("value", 4);
                        in.checked = object.optBoolean("checked");
                        result.body.invoiceList.add(in);
                    }
                }

                JSONArray shipmentlist = body.optJSONArray("shipmentlist");
                if (shipmentlist != null) {
                    for (int i = 0; i < shipmentlist.length(); ++i) {
                        JSONObject object = shipmentlist.optJSONObject(i);
                        Shipment s = new Shipment();

                        s.shipment_id = object.optString("shipment_id");
                        s.brief = object.optString("brief");
                        s.amount = object.optString("amount");
                        s.checked = object.optBoolean("checked");

                        result.body.shipmentlist.add(s);
                    }
                }

                result.body.invoice_title = body.optString("invoice_title");
                result.body.amount = body.optString("amount", "0");
                result.body.shipment = body.optString("shipment");
                result.body.invoice_open = body.optBoolean("invoice_open");
                result.body.total = body.optInt("total", 0);

                return result;
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

        public List<Payment> paymentList = new ArrayList<Payment>();
        public List<Shipment> shipmentlist = new ArrayList<Shipment>();
        public List<DeliverTime> deliverTimeList = new ArrayList<DeliverTime>();
        public List<Invoice> invoiceList = new ArrayList<Invoice>();

        public String invoice_title;
        public boolean invoice_open;
        public int total;
        public String amount;
        public String shipment;
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
        public int value;
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
