package com.xiaomi.mitv.shop;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by niuyi on 2015/5/22.
 */
public class AddAddressActivity extends BaseLoadingActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFailureMessage("无法获取地址信息");
    }
}