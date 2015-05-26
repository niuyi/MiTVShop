package com.xiaomi.mitv.shop;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.xiaomi.mitv.shop.widget.MyEditText;

/**
 * Created by niuyi on 2015/5/22.
 */
public class AddAddressActivity extends Activity {

    private MyEditText mName;
    private MyEditText mTel;
    private MyEditText mAddress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_address_activity);

        TextView title = (TextView)findViewById(R.id.title_text);
        title.setText(R.string.input_address);

        mName = (MyEditText)findViewById(R.id.et_name);
        mTel = (MyEditText)findViewById(R.id.et_tel);
        mAddress = (MyEditText)findViewById(R.id.et_address);

        Button button = (Button)findViewById(R.id.btn_ok);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isError = false;
                if(mName.isEmpty()){
                    mName.showError();
                    isError = true;
                }else if(mTel.isEmpty()){
                    mTel.showError();
                    isError = true;
                }else if(mAddress.isEmpty()){
                    mAddress.showError();
                    isError = true;
                }

                if(isError)
                    return;
            }
        });
    }
}