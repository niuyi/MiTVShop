package com.xiaomi.mitv.shop;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by niuyi on 2015/5/22.
 */
public class AddAddressActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_address_activity);

        TextView title = (TextView)findViewById(R.id.title_text);
        title.setText(R.string.input_address);
    }
}