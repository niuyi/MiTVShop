package com.xiaomi.mitv.shop;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.xiaomi.mitv.api.util.PopupWindow;
import com.xiaomi.mitv.shop.util.LocationManager;
import com.xiaomi.mitv.shop.widget.MyEditText;
import com.xiaomi.mitv.shop.widget.SelectLocationWindow;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * Created by niuyi on 2015/5/22.
 */
public class AddAddressActivity extends Activity {

    private MyEditText mName;
    private MyEditText mTel;
    private MyEditText mAddress;

    private SelectLocationWindow mWindow;
    private EditText mCity;
    private Button mButton;
    private FutureTask<LocationManager.AllLocations> mTask;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_address_activity);

        TextView title = (TextView)findViewById(R.id.title_text);
        title.setText(R.string.input_address);

        mName = (MyEditText)findViewById(R.id.et_name);
        mTel = (MyEditText)findViewById(R.id.et_tel);
        mAddress = (MyEditText)findViewById(R.id.et_address);
        mCity = (EditText)findViewById(R.id.et_city);

        mCity.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    showWindow();
                }
            }
        });

        mButton = (Button)findViewById(R.id.btn_ok);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isError = false;
                if (mName.isEmpty()) {
                    mName.showError();
                    isError = true;
                } else if (mTel.isEmpty()) {
                    mTel.showError();
                    isError = true;
                } else if (mAddress.isEmpty()) {
                    mAddress.showError();
                    isError = true;
                }

                if (isError)
                    return;
            }
        });

        mTask = new FutureTask<LocationManager.AllLocations>(new GetLocationCall());
        new Thread(mTask).start();
    }

    private void showWindow(){
        if(mWindow == null){
            mWindow = new SelectLocationWindow(this, mTask);
            mWindow.setOnWindowListener(new PopupWindow.OnWindowListener() {
                @Override
                public void onShow(PopupWindow popupWindow) {

                }

                @Override
                public void onDismiss(PopupWindow popupWindow) {
                    mName.setVisibility(View.VISIBLE);
                    mTel.setVisibility(View.VISIBLE);
                    mCity.setVisibility(View.VISIBLE);
                    mAddress.setVisibility(View.VISIBLE);
                    mButton.setVisibility(View.VISIBLE);

                    mAddress.requestFocus();
                }
            });
        }
        mName.setVisibility(View.GONE);
        mTel.setVisibility(View.GONE);
        mCity.setVisibility(View.GONE);
        mAddress.setVisibility(View.GONE);
        mButton.setVisibility(View.GONE);

        mWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
    }

    public void onClick(View view){
        if(mWindow != null){
            mWindow.dismiss();
            mAddress.requestFocus();
        }
    }

    static class GetLocationCall implements Callable<LocationManager.AllLocations>{

        @Override
        public LocationManager.AllLocations call() throws Exception {
            LocationManager.AllLocations result = LocationManager.INSTANCE.getAllLocations();
            Log.i("AddAddressActivity", "call done!");
            return result;
        }
    }
}