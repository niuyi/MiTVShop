package com.xiaomi.mitv.shop.model;

import android.text.TextUtils;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

/**
 * Created by niuyi on 2015/5/19.
 */
public class RegionList {

    public List<Region> regions = new ArrayList<Region>();

    public static RegionList parse(String input){
        if(TextUtils.isEmpty(input)){
            return null;
        }

        try {
            JSONObject root = new JSONObject(input);

            CheckoutResponse.Header header = CheckoutResponse.Header.parse(root);

            if(header != null && header.code == CheckoutResponse.Header.OK){
                RegionList result = new RegionList();

                JSONObject body = root.optJSONObject("body");
                if(body == null){
                    return  result;
                }

                JSONObject regions = body.optJSONObject("regions");
                if(regions == null){
                    return  result;
                }

                Iterator<String> keys = regions.keys();

                while(keys.hasNext()){
                    String key = keys.next();
                    Log.i("RegionList", "key : " + key);
                    JSONObject region = regions.getJSONObject(key);
                    Region r = new Region();
                    r.region_id = region.optString("region_id");
                    r.region_name = region.optString("region_name");

                    result.regions.add(r);
                }

                Collections.sort(result.regions, new Comparator<Region>() {
                    @Override
                    public int compare(Region lhs, Region rhs) {
                        try {
                            int ret = Integer.valueOf(lhs.region_id).compareTo(Integer.valueOf(rhs.region_id));
                            return ret;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        return 0;
                    }
                });

                return result;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;

    }
}
