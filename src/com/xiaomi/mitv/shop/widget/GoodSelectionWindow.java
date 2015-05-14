package com.xiaomi.mitv.shop.widget;


import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.xiaomi.mitv.api.util.PopupWindow;
import com.xiaomi.mitv.shop.R;
import com.xiaomi.mitv.shop.model.ProductDetail;
import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by niuyi on 2015/5/14.
 */
public class GoodSelectionWindow extends PopupWindow implements DialogButtonView.OnItemCheckedListener {

    private static final String TAG = "MyActivity";

    private LinearLayout mContainer;
    private ProductDetail mDetail;
    private final TextView mTitle;
    private Button mButton;
    private ArrayList<DialogButtonView> mViews = new ArrayList<DialogButtonView>();

    public GoodSelectionWindow(Context context, ProductDetail detail) {

        super(context);
        mDetail = detail;

        ViewGroup rootView = (ViewGroup)View.inflate(context, R.layout.good_selection_window, null);
        setContentView(rootView);

        mContainer = new LinearLayout(context);
        mContainer.setOrientation(LinearLayout.VERTICAL);
        FrameLayout.LayoutParams para = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        mContainer.setFocusable(true);

        rootView.addView(mContainer, para);

        mTitle = new TextView(context);
        mTitle.setText(mDetail.name);

        mContainer.addView(mTitle);

        for(int i = 0 ; i < mDetail.props_def.length; i++){
            ProductDetail.Prop p = mDetail.props_def[i];
            DialogButtonView view = new DialogButtonView(context);
            view.setOnItemCheckedListener(this);

            view.setProp(p);

            view.inflate();
            view.setTag(i);

            mContainer.addView(view);
            mViews.add(view);
        }

        mButton = new Button(context);
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

        if(mViews.size() > 0){
            mViews.get(0).setFocus();
        }
    }

    private void onSubmit(View v){

    }

    @Override
    public void onChecked(DialogButtonView view, RadioButton button) {
        Log.i(TAG, "onChecked: " + view.getTag() + " ,id: " + button.getId() + " ,tag: " + button.getTag());

        int pos = mViews.indexOf(view);

        if(pos < 0)
            return;

//        if(pos > 0){
//            DialogButtonView dialogButtonView = mViews.get(pos - 1);
//            dialogButtonView.setAllNextFocusDownId(button.getId());
//        }

        if(pos < mViews.size() - 1){
            DialogButtonView dialogButtonView = mViews.get(pos + 1);
            dialogButtonView.setAllNextFocusUpId(button.getId());
        }

        if(pos == mViews.size() -1 && mButton != null){
            mButton.setNextFocusUpId(button.getId());
        }

        ProductDetail.Option option = (ProductDetail.Option)button.getTag();
        ProductDetail.Node node = mDetail.findNodeById(option.id);
        Log.i(TAG, "find node: " + node.id);

        for(int i = pos + 1 ; i < mViews.size() ; i ++){
            DialogButtonView dialogButtonView = mViews.get(i);
            dialogButtonView.updateStatus(node);
        }

        Log.i(TAG, "gid: " + node.gid);
        if(!TextUtils.isEmpty(node.gid)){
            String text = mDetail.goods_status.get(node.gid);

            if("1".equalsIgnoreCase(text)){
                mButton.setFocusable(true);
                mButton.setEnabled(true);
                mButton.setText("Buy");
            }else{
                mButton.setFocusable(false);
                mButton.setEnabled(false);
                mButton.setText("No Goods");
            }
        }else{
            mButton.setFocusable(false);
            mButton.setEnabled(false);
            mButton.setText("Buy");
        }
    }
}
