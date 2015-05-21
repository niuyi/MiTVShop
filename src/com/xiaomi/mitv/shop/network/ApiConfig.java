package com.xiaomi.mitv.shop.network;

import com.xiaomi.mitv.shop.util.ProductInfo;

/**
 * Created by niuyi on 2015/5/8.
 */
public class ApiConfig {

    private static final String TAG = "ApiConfig";
    private static final String SERVER_HOST = "mishop.n.duokanbox.com";
    private static final String SERVER_HK_HOST = "scraper.g.duokanbox.com";

    public static final String PARAM_OPAQUE = "opaque";
    public static final String PARAM_TOKEN = "token";
    public static final String PARAM_API_KEY = "key";

    public static final String API_KEY = "4fb23ce909f5454a9fcc0b986f22e99e";

    public static String getServerUrl(){

//        if(ProductInfo.isHKVersion()){
//            return "http://" + SERVER_HK_HOST;
//        }

//        return "http://10.235.176.26:9025";
        return "https://mishop.n.duokanbox.com";

    }

    public static String getServerHost(){
//        if(ProductInfo.isHKVersion()){
//            return SERVER_HK_HOST;
//        }

        return SERVER_HOST;
    }
}
