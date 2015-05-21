package com.xiaomi.mitv.shop;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.xiaomi.mitv.api.util.MILog;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Created by niuyi on 2015/5/21.
 */
public class InputAddressActivity extends Activity {

    private static final String TAG = "InputAddressActivity";

    private EditText mEditText;
    private Button mButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_address_activity);

        TextView tv = (TextView)findViewById(R.id.title_text);
        tv.setText("请输入公司名称");

        mEditText = (EditText)findViewById(R.id.address_edit_text);
        mButton = (Button) findViewById(R.id.ok_button);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputDone();
            }
        });

        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    MILog.i(TAG, "action done!!");

                    InputMethodManager ime = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    ime.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);

                    inputDone();
                    mButton.requestFocus();
                }

                return true;
            }
        });
    }

    public void inputDone(){

        Uri uri = Uri.parse("mitv://mitv.shop?product=1&ver=1");
        Log.i(TAG, String.format("get product:%s ver:%s", uri.getQueryParameter("product"), uri.getQueryParameter("ver")));

        String address = mEditText.getText().toString();

        if(address.trim().length() == 0){
            mEditText.setBackgroundResource(R.drawable.input_error);
            mEditText.requestFocus();
            return;
        }

        finish();
    }
}