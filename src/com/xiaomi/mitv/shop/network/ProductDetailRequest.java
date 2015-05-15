package com.xiaomi.mitv.shop.network;

import com.xiaomi.mitv.shop.model.ProductDetail;

import java.util.Locale;

/**
 * Created by linniu on 2015/5/9.
 */
public class ProductDetailRequest extends MyDuokanBaseRequest {

    @Override
    protected boolean beforeSend() {
//        ProductDetail detail = new ProductDetail();
//        detail.name = "小米手机";
//        detail.price = "¥1999起";
//        detail.images = new String[]{
//                "http://c1.mifile.cn/f/i/2014/cn/goods/mi4/md/gallery/gallery-list-a.png",
//                "http://c1.mifile.cn/f/i/2014/cn/goods/mi4/md/gallery/gallery-list-d_2x.jpg",
//                "http://c1.mifile.cn/f/i/2014/cn/goods/mi4/md/gallery/gallery-list-e_2x.png"};
//
//
//        String json = JsonSerializer.getInstance().serialize(detail);
//
//        mResponse = new DKResponse(DKResponse.STATUS_SUCCESS, json);
//
//        return true;
        return false;
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
        return String.format("&product=%s", "1");
    }

    @Override
    protected Locale getLocale() {
        return null;
    }
}
