package com.xiaomi.mitv.shop.model;

import com.xiaomi.mitv.shop.network.JsonSerializer;

import java.util.Map;

/**
 * Created by niuyi on 2015/5/15.
 */
public class Order {
    public Header header;
    public OrderInfo body;

    public static Order parse(String input){
        try{
            return JsonSerializer.getInstance().deserialize(input, Order.class);
        }catch (Exception e){

        }

        return null;
    }

    public static class Header{
        public int code;
    }

    public static class OrderInfo{
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


