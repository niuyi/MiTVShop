package com.xiaomi.mitv.shop;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.xiaomi.mitv.shop.widget.DialogButtonView;

import java.util.ArrayList;
import java.util.List;

public class MyActivity extends Activity {
    private static final String TAG = "MyActivity";

    private LinearLayout mContainer;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        ViewGroup root = (ViewGroup)findViewById(R.id.root);


        mContainer = new LinearLayout(this);
        mContainer.setOrientation(LinearLayout.VERTICAL);
        FrameLayout.LayoutParams para = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        mContainer.setFocusable(true);

        root.addView(mContainer, para);


        for(int i = 0 ; i < 3 ; i ++){
            DialogButtonView view = new DialogButtonView(this);

//            view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//                @Override
//                public void onFocusChange(View v, boolean hasFocus) {
//                    Log.i(TAG, "focuse change, " + v.getTag() + " hasFocus: " + hasFocus);
//
//                    DialogButtonView view = (DialogButtonView)v;
//
//                    if(hasFocus){
//                        view.setFocus();
//                    }else{
////                        view.setSelected();
//                    }
//                }
//            });

            view.setItemTitle("index: " + i);

            List<String> names = new ArrayList<String>();
            names.add("红色");
            names.add("绿色");
            names.add("白色");
            names.add("蓝色");

            view.setBtnItemNames(names);
            view.inflate();
            view.setTag(i);

            if(i == 0){
                view.requestFocus();
            }

            mContainer.addView(view);
        }


//        new Thread(){
//            public void run(){
//                for(int i = 0 ; i < 1000 ; i++){
//                    ShopDBManager.INSTANCE.addValue(String.valueOf(i), String.valueOf(i));
//                }
//
//                try {
//                    sleep(2000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//                Log.i(TAG, "get 0: " + ShopDBManager.INSTANCE.getValue("0"));
//                Log.i(TAG, "get 100: " + ShopDBManager.INSTANCE.getValue("100"));
//                Log.i(TAG, "get 499: " + ShopDBManager.INSTANCE.getValue("499"));
//                Log.i(TAG, "get 899: " + ShopDBManager.INSTANCE.getValue("899"));
//            }
//        }.start();
    }

    public void onSubmit(View view){
        for(int i = 0 ; i < mContainer.getChildCount() ; i++){
            DialogButtonView v = (DialogButtonView)mContainer.getChildAt(i);
            Log.i(TAG, "submit select: " + v.getSelected());
        }
    }
}
