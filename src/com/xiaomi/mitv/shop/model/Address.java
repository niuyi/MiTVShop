package com.xiaomi.mitv.shop.model;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by niuyi on 2015/5/19.
 */
public class Address {
    public String address_id;
    public String address_name;
    public String consignee;

    public String address;
    public String zipcode;
    public String tel;
    public int country_id;
    public String country_name;
    public String country_code;
    public int province_id;
    public String province_name;
    public String province_code;
    public int city_id;
    public String city_name;
    public String city_code;
    public int district_id;
    public String district_name;
    public String district_code;
    public int area_id;
    public String area_name;
    public String area_code;
    public boolean is_tv_addr;
    public boolean is_air_addr;
    public boolean is_common_addr;

    public static Address parse(JSONObject objAddr) {
        if (objAddr == null)
            return null;

        Address addr = new Address();
        addr.address_id = objAddr.optString("address_id");
        addr.consignee = objAddr.optString("consignee");
        addr.address = objAddr.optString("address");
        addr.zipcode = objAddr.optString("zipcode");
        addr.tel = objAddr.optString("tel");

        JSONObject country = objAddr.optJSONObject("country");
        if (country != null) {
            addr.country_id = country.optInt("id");
            addr.country_name = country.optString("name");
            addr.country_code = country.optString("can_cod");
        }

        JSONObject province = objAddr.optJSONObject("province");
        if (country != null) {
            addr.province_id = province.optInt("id");
            addr.province_name = province.optString("name");
            addr.province_code = province.optString("can_cod");
        }

        JSONObject city = objAddr.optJSONObject("city");
        if (country != null) {
            addr.city_id = city.optInt("id");
            addr.city_name = city.optString("name");
            addr.city_code = city.optString("can_cod");
        }

        JSONObject district = objAddr.optJSONObject("district");
        if (country != null) {
            addr.district_id = district.optInt("id");
            addr.district_name = district.optString("name");
            addr.district_code = district.optString("can_cod");
        }

        JSONObject area = objAddr.optJSONObject("area");
        if (country != null) {
            addr.area_id = area.optInt("id");
            addr.area_name = area.optString("name");
            addr.area_code = area.optString("can_cod");
        }

        addr.is_tv_addr = false;
        addr.is_air_addr = false;
        JSONArray matching = objAddr.optJSONArray("matching");
        if (null != matching) {
            for (int i = 0; i < matching.length(); i++) {
                String t = matching.optString(i);

                if (t.contains("air"))
                    addr.is_air_addr = true;
                if (t.contains("tv"))
                    addr.is_tv_addr = true;
                if (t.contains("common"))
                    addr.is_common_addr = true;
            }
        }

        return addr;
    }
}
