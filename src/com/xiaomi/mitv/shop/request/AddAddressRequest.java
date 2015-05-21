package com.xiaomi.mitv.shop.request;

import android.util.Log;
import com.xiaomi.mitv.shop.model.Address;
import com.xiaomi.mitv.shop.network.MyDuokanBaseRequest;
import org.json.JSONObject;

import java.util.Locale;

/**
 * Created by niuyi on 2015/5/18.
 */
public class AddAddressRequest extends MyDuokanBaseRequest {

    private final String uid;
    private final Address address;

    public AddAddressRequest(String uid, Address address){
        this.uid = uid;
        this.address = address;
    }

    @Override
    protected  byte[] getInput() {
        JSONObject root = new JSONObject();

        try {
            root.put("user_id", uid);
            root.put("consignee", address.consignee);
            root.put("province_id", address.province_id);
            root.put("city_id", address.city_id);
            root.put("district_id", address.district_id);
            root.put("address", address.address);
            root.put("zipcode", address.zipcode);
            root.put("tel", address.tel);

            Log.i("AddAddressRequest", "AddAddressRequest: " + root.toString());

//            ByteBuffer encode = Charset.forName("UTF-8").encode(root.toString());
//
//            return encode.array();

            return root.toString().getBytes("utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected String getPath() {
        return "mishop/api/address/add";
    }

    @Override
    protected String getParameters() {
        return null;
    }


    @Override
    protected Locale getLocale() {
        return null;
    }
}
