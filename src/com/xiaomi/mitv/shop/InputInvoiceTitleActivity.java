package com.xiaomi.mitv.shop;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.xiaomi.mitv.api.util.MILog;
import com.xiaomi.mitv.shop.model.ProductManager;

/**
 * Created by niuyi on 2015/5/21.
 */
public class InputInvoiceTitleActivity extends Activity {

    private static final String TAG = "InputAddressActivity";

    private EditText mEditText;
    private Button mButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_invoice_title_activity);

        TextView tv = (TextView)findViewById(R.id.title_text);
        tv.setText(R.string.input_company_name);

        mEditText = (EditText)findViewById(R.id.address_edit_text);
        mButton = (Button) findViewById(R.id.ok_button);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputDone();
            }
        });

        String title = ProductManager.INSTSNCE.getCurrentCheckoutResponse().body.invoice_title;
        if(!TextUtils.isEmpty(title)){
            mEditText.setText(title);
        }

        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    MILog.i(TAG, "action done!!");

                    InputMethodManager ime = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    ime.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);

                    inputDone();

                }

                return true;
            }
        });
    }

    public void inputDone(){

//        Uri uri = Uri.parse("mitv://mitv.shop?product=1&ver=1");
//        Log.i(TAG, String.format("get product:%s ver:%s", uri.getQueryParameter("product"), uri.getQueryParameter("ver")));
//
        String title = mEditText.getText().toString().trim();

        if(title.length() == 0){
            mEditText.setBackgroundResource(R.drawable.input_error);

            Animation animation = AnimationUtils.loadAnimation(this, R.anim.shake);
            mEditText.startAnimation(animation);

            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mEditText.requestFocus();
                    mEditText.setBackgroundResource(R.drawable.input_selector);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            return;
        }

        mButton.requestFocus();

        Intent in = new Intent();
        in.putExtra(InvoiceActivity.TITLE_KEY, title);

        setResult(RESULT_OK, in);

        finish();
    }
}