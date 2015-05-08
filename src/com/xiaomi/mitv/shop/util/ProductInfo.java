package com.xiaomi.mitv.shop.util;

/**
 * Created by niuyi on 2015/5/8.
 */
public class ProductInfo {

    public static boolean isBox(){
        return mitv.os.Build.isBoxProduct();
    }

    public static int getProductCode(){
        return mitv.os.Build.getProductCode();
    }

    public static boolean isHKVersion(){
        return (mitv.os.Build.getProductSubCode() == mitv.os.Build.SUBCODE_HK);
    }

}
