package com.xiaomi.mitv.shop.model;

import java.util.HashMap;

/**
 * Created by linniu on 2015/5/16.
 */
public enum ProductManager {
    INSTSNCE;

    private HashMap<String, ProductDetail> mProducts = new HashMap<String, ProductDetail>();

    public ProductDetail getProductDetail(String pid){
        return mProducts.get(pid);
    }

    public void putProductDetail(String pid, ProductDetail productDetail) {
        mProducts.put(pid, productDetail);
    }

    public void clear(){
        mProducts.clear();
    }
}
