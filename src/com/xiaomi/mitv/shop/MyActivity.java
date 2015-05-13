package com.xiaomi.mitv.shop;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.xiaomi.mitv.shop.widget.DialogButtonView;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MyActivity extends Activity implements DialogButtonView.OnItemCheckedListener {
    private static final String TAG = "MyActivity";

    private LinearLayout mContainer;
    private ArrayList<DialogButtonView> mViews = new ArrayList<DialogButtonView>();

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


//        for(int i = 0 ; i < 3 ; i ++){
//            DialogButtonView view = new DialogButtonView(this);
//
//            view.setOnItemCheckedListener(this);
//
////            view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
////                @Override
////                public void onFocusChange(View v, boolean hasFocus) {
////                    Log.i(TAG, "focuse change, " + v.getTag() + " hasFocus: " + hasFocus);
////
////                    DialogButtonView view = (DialogButtonView)v;
////
////                    if(hasFocus){
////                        view.setFocus();
////                    }else{
//////                        view.setSelected();
////                    }
////                }
////            });
//
//            view.setItemTitle(String.valueOf(i));
//
//            List<String> names = new ArrayList<String>();
//            names.add("红色");
//            names.add("绿色");
//            names.add("白色");
//            names.add("蓝色");
//
//            view.setBtnItemNames(names);
//            view.inflate();
//            view.setTag(i);
//
//            if(i == 0){
//                view.requestFocus();
//            }
//
//            mContainer.addView(view);
//            mViews.add(view);
//        }

        Button b = new Button(this);
        b.setText("submit");
        b.requestFocus();

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSubmit(v);
            }
        });
        mContainer.addView(b);

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
        AssetManager assetManager = getAssets();
        ByteArrayOutputStream outputStream = null;
        InputStream inputStream = null;
        try {
            inputStream = assetManager.open("detail.json");
            outputStream = new ByteArrayOutputStream();
            byte buf[] = new byte[1024];
            int len;
            try {
                while ((len = inputStream.read(buf)) != -1) {
                    outputStream.write(buf, 0, len);
                }
                outputStream.close();
                inputStream.close();
            } catch (IOException e) {
            }
        } catch (IOException e) {
        }

        try {
            JSONObject root = new JSONObject(outputStream.toString());
            Log.i(TAG, "status: " + root.getInt("status"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


//        for(int i = 0 ; i < mContainer.getChildCount() ; i++){
//            DialogButtonView v = (DialogButtonView)mContainer.getChildAt(i);
//            Log.i(TAG, "submit select: " + v.getSelected());
//        }
    }

    @Override
    public void onChecked(DialogButtonView view, RadioButton button) {
        Log.i(TAG, "onChecked: " + view.getTag() + " ,id: " + button.getId() + " ,tag: " + button.getTag());

        int pos = mViews.indexOf(view);

        if(pos > 0){
            DialogButtonView dialogButtonView = mViews.get(pos - 1);
            dialogButtonView.setAllNextFocusDownId(button.getId());
        }

        if(pos < mViews.size() - 1){
            DialogButtonView dialogButtonView = mViews.get(pos + 1);
            dialogButtonView.setAllNextFocusUpId(button.getId());
        }
    }
}
