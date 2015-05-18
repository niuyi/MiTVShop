package com.xiaomi.mitv.shop.request;

import com.xiaomi.mitv.shop.network.MyDuokanBaseRequest;

import java.util.Locale;

/**
 * Created by niuyi on 2015/5/18.
 */
public class AddAddressRequest extends MyDuokanBaseRequest {

    private final String uid;
    private final String consignee;
    private final String country;
    private final String province_id;
    private final String city_id;
    private final String district_id;
    private final String address;
    private final String zipcode;
    private final String tel;
    private final String email;
    private final String address_name;
    private final String area;

    public AddAddressRequest(String uid, String consignee, String country, String province_id, String city_id, String district_id, String address, String zipcode, String tel, String email, String address_name, String area){

        this.uid = uid;
        this.consignee = consignee;
        this.country = country;
        this.province_id = province_id;
        this.city_id = city_id;
        this.district_id = district_id;
        this.address = address;
        this.zipcode = zipcode;
        this.tel = tel;
        this.email = email;
        this.address_name = address_name;
        this.area = area;
    }

    @Override
    protected Object getInput() {
        return null;
    }

    @Override
    protected String getPath() {
        return "mishop/api/list";
    }

    @Override
    protected String getParameters() {

//        return String.format("&uid=%s&",);
        return null;
    }


    @Override
    protected Locale getLocale() {
        return null;
    }
}
