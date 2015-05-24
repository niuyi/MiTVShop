package com.xiaomi.mitv.shop.model;

import java.util.HashMap;

/**
 * Created by linniu on 2015/5/16.
 */
public enum ProductManager {
    INSTSNCE;

    private HashMap<String, ProductDetail> mProducts = new HashMap<String, ProductDetail>();
    private HashMap<String, AddressList> mAddressList = new HashMap<String, AddressList>();
    private CheckoutResponse mCurrentCheckoutResponse;

    public ProductDetail getProductDetail(String pid){
        return mProducts.get(pid);
    }

    public void putProductDetail(String pid, ProductDetail productDetail) {
        mProducts.put(pid, productDetail);
    }

    public void putAddressList(String uid, AddressList list){
        mAddressList.put(uid, list);
    }

    public AddressList getAddressList(String uid){
        return mAddressList.get(uid);
    }

    public void clear(){
        mProducts.clear();
        mAddressList.clear();
        mCurrentCheckoutResponse = null;
    }

    public void setCurrentCheckoutResponse(CheckoutResponse mCurrentCheckoutResponse) {
        this.mCurrentCheckoutResponse = mCurrentCheckoutResponse;
    }

    public CheckoutResponse getCurrentCheckoutResponse(){
        return mCurrentCheckoutResponse;
    }
}
