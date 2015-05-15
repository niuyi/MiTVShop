package com.xiaomi.mitv.shop;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.xiaomi.mitv.shop.model.ProductDetail;
import com.xiaomi.mitv.shop.network.CheckoutRequest;
import com.xiaomi.mitv.shop.network.DKResponse;
import com.xiaomi.mitv.shop.network.MyBaseRequest;
import com.xiaomi.mitv.shop.widget.DialogButtonView;
import com.xiaomi.mitv.shop.widget.GoodSelectionWindow;
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
    private ProductDetail mDetail;
    private Button mButton;

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
//
        root.addView(mContainer, para);
//
//        mDetail = getDetail();
//
//        //todo: check the data
//
//        for(int i = 0 ; i < mDetail.props_def.length; i++){
//            ProductDetail.Prop p = mDetail.props_def[i];
//            DialogButtonView view = new DialogButtonView(this);
//            view.setOnItemCheckedListener(this);
//
//            view.setProp(p);
//
//            view.inflate();
//            view.setTag(i);
//
//            mContainer.addView(view);
//            mViews.add(view);
//        }
//
        mButton = new Button(this);
        mButton.setText("Buy");
        mButton.requestFocus();
//        mButton.setEnabled(false);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSubmit(v);
            }
        });
        mContainer.addView(mButton);

//        if(mViews.size() > 0){
//            mViews.get(0).setFocus();
//        }
    }

    private ProductDetail getDetail(){
        AssetManager assetManager = getAssets();
        ByteArrayOutputStream outputStream = null;
        InputStream inputStream = null;
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        try {
            inputStream = assetManager.open("detail.json");
            final int blockSize = 8192;
            byte[] buffer = new byte[blockSize];
            int count = 0;
            while((count = inputStream.read(buffer, 0, blockSize)) > 0) {
                byteStream.write(buffer,0, count);
            }
        } catch (IOException e) {
        }

        try {
            byte[] bytes = byteStream.toByteArray();
            String json = new String(bytes, 0, bytes.length, "utf-8");

            DKResponse res = new DKResponse(1, json);
            ProductDetail detail = ProductDetail.parse(res.getResponse());

            for(ProductDetail.Node node : detail.props_tree){
                printNode(node, 1);
            }

            detail.name = "小米手机4电信4G版2GB内存 白色 16G";
            return detail;



//            JSONObject root = new JSONObject(json);
//            Log.i(TAG, "status: " + root.getInt("status"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private void printNode(ProductDetail.Node node, int index){
        StringBuilder sb = new StringBuilder();
        for(int i = 0 ; i < index ; i++){
            sb.append("---");
        }

        sb.append(String.format("node: id{%s} valid(%b) gid(%s)", node.id, node.valid, node.gid));

        Log.i(TAG, sb.toString());

        if(node.child != null){
            for(ProductDetail.Node sub : node.child){
                printNode(sub, index+1);
            }
        }
    }

    public void onSubmit(View view){

        CheckoutRequest req = new CheckoutRequest();
        req.setObserver(new MyBaseRequest.MyObserver() {
            @Override
            public void onRequestCompleted(MyBaseRequest request, DKResponse response) {
                if(response != null
                        && response.getStatus() == DKResponse.STATUS_SUCCESS
                        && !TextUtils.isEmpty(response.getResponse())){
                    Log.i(TAG, "res: " + response.getResponse());
                }
            }

            @Override
            public void onAbort() {

            }
        });
        req.send();

//        GoodSelectionWindow window = new GoodSelectionWindow(this, mDetail);
//        window.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
//        AssetManager assetManager = getAssets();
//        ByteArrayOutputStream outputStream = null;
//        InputStream inputStream = null;
//        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
//        try {
//            inputStream = assetManager.open("detail.json");
//            final int blockSize = 8192;
//            byte[] buffer = new byte[blockSize];
//            int count = 0;
//            while((count = inputStream.read(buffer, 0, blockSize)) > 0) {
//                byteStream.write(buffer,0, count);
//            }
//        } catch (IOException e) {
//        }
//
//        try {
//            byte[] bytes = byteStream.toByteArray();
//            String json = new String(bytes, 0, bytes.length, "utf-8");
//
//            DKResponse res = new DKResponse(1, json);
//            ProductDetail detail = ProductDetail.parse(res.getResponse());
//
//            Log.i(TAG, "newRes name: " + detail.name);
//            Log.i(TAG, "newRes price: " + detail.price);
//            Log.i(TAG, "newRes size: " + detail.goods_status.size());
//
//            for(ProductDetail.Prop p : detail.props_def){
//                Log.i(TAG, "newRes prop:" + p.name);
//                for(ProductDetail.Option o : p.options){
//                    Log.i(TAG, "newRes option:" + o.name);
//                }
//            }
//
//            for(ProductDetail.Node node : detail.props_tree){
//                printNode(node, 1);
//            }
//
////            JSONObject root = new JSONObject(json);
////            Log.i(TAG, "status: " + root.getInt("status"));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


//        for(int i = 0 ; i < mContainer.getChildCount() ; i++){
//            DialogButtonView v = (DialogButtonView)mContainer.getChildAt(i);
//            Log.i(TAG, "submit select: " + v.getSelected());
//        }
    }

    @Override
    public void onChecked(DialogButtonView view, RadioButton button) {
        Log.i(TAG, "onChecked: " + view.getTag() + " ,id: " + button.getId() + " ,tag: " + button.getTag());

//        int pos = mViews.indexOf(view);
//
//        if(pos < 0)
//            return;
//
////        if(pos > 0){
////            DialogButtonView dialogButtonView = mViews.get(pos - 1);
////            dialogButtonView.setAllNextFocusDownId(button.getId());
////        }
//
//        if(pos < mViews.size() - 1){
//            DialogButtonView dialogButtonView = mViews.get(pos + 1);
//            dialogButtonView.setAllNextFocusUpId(button.getId());
//        }
//
//        if(pos == mViews.size() -1 && mButton != null){
//            mButton.setNextFocusUpId(button.getId());
//        }
//
//        ProductDetail.Option option = (ProductDetail.Option)button.getTag();
//        ProductDetail.Node node = mDetail.findNodeById(option.id);
//        Log.i(TAG, "find node: " + node.id);
//
//        for(int i = pos + 1 ; i < mViews.size() ; i ++){
//            DialogButtonView dialogButtonView = mViews.get(i);
//            dialogButtonView.updateStatus(node);
//        }
//
//        Log.i(TAG, "gid: " + node.gid);
//        if(!TextUtils.isEmpty(node.gid)){
//            String text = mDetail.goods_status.get(node.gid);
//
//            if("1".equalsIgnoreCase(text)){
//                mButton.setFocusable(true);
//                mButton.setEnabled(true);
//                mButton.setText("Buy");
//            }else{
//                mButton.setFocusable(false);
//                mButton.setEnabled(false);
//                mButton.setText("No Goods");
//            }
//        }else{
//            mButton.setFocusable(false);
//            mButton.setEnabled(false);
//            mButton.setText("Buy");
//        }
    }

}
