package com.xiaomi.mitv.shop.widget;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.xiaomi.mitv.shop.R;
import com.xiaomi.mitv.shop.model.ProductDetail;

import java.util.List;

/**
 * Created by niuyi on 2015/5/12.
 */
public class DialogButtonView extends LinearLayout {

    final private String TAG = "DialogButtonView";

    private OnItemCheckedListener mListener;
    private List<? extends CharSequence> mButtonNames;
    private CharSequence mTitle;

    private RadioGroup mButtonContainer;
    private int mSelectedIndex = -1;

    private ProductDetail.Option mCurrentNode = null;

    private ProductDetail.Prop mProp;
    private Object option;

    public void setProp(ProductDetail.Prop p){
        this.mProp = p;
    }

    public DialogButtonView(Context context) {
        super(context);
    }

    public DialogButtonView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DialogButtonView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void inflate() {
        if (mProp == null) {
            Log.i(TAG, "title || button names is null!");
            return;
        }
        LayoutInflater.from(getContext()).inflate(R.layout.device_shop_dialog_item, this);
        init();
    }

    private void init() {
        Log.i(TAG, "init!");

        TextView title = (TextView) findViewById(R.id.item_title);
        title.setText(mProp.name);

        mButtonContainer = (RadioGroup) findViewById(R.id.item_container);

        RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.rightMargin = 60;
        params.gravity = Gravity.CENTER_VERTICAL;

        if(mProp.options != null){
            for(ProductDetail.Option op : mProp.options){
                RadioButton btn = new RadioButton(getContext());

                btn.setTag(op);
                btn.setId(op.id);
                btn.setText(op.name);

                btn.setTextColor(Color.BLACK);
                btn.setButtonDrawable(android.R.color.transparent);
                btn.setBackgroundResource(R.drawable.device_shop_dialog_btn_selector);
                btn.setOnFocusChangeListener(new OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        Log.i(TAG, "onFocusChange: view: " + v.getTag() + " ,hasFocus:" + hasFocus);

                        if (hasFocus) {
                            RadioButton button = (RadioButton) v;

                            mCurrentNode = (ProductDetail.Option) button.getTag();
                            button.setChecked(true);

                            if (mListener != null) {
                                mListener.onChecked(DialogButtonView.this, button);
                            }
                        }
                    }
                });

                mButtonContainer.addView(btn, params);
            }
        }

    }


    private RadioButton getCurrentButton() {
        if(mSelectedIndex >= 0 && mSelectedIndex <= mButtonContainer.getChildCount() - 1){
            return (RadioButton)mButtonContainer.getChildAt(mSelectedIndex);
        }

        return null;
    }

    public String getSelected(){
        RadioButton b = getCurrentButton();
        if(b != null){
            return b.getTag().toString();
        }

        return "null";
    }

    public void setItemTitle(CharSequence title) {
        Log.i(TAG, "setItemTitle");
        mTitle = title;
    }

    public void setBtnItemNames(List<? extends CharSequence> names) {
        mButtonNames = names;
    }

    public void setOnItemCheckedListener(OnItemCheckedListener listener) {
        mListener = listener;
    }

    public int getBtnCount() {
        return mButtonContainer.getChildCount();
    }

    public void setAllBtnEnable(boolean b) {
        for (int i = 0; i < mButtonContainer.getChildCount(); ++i) {
            mButtonContainer.getChildAt(i).setEnabled(b);
        }
    }

    public void setBtnEnable(int p, boolean b) {
        if (p >= 0 && p < mButtonContainer.getChildCount()) {
            mButtonContainer.getChildAt(p).setEnabled(b);
        }
    }

    public boolean isBtnEnable(int p) {
        if (p >= 0 && p < mButtonContainer.getChildCount()) {
            return mButtonContainer.getChildAt(p).isEnabled();
        }
        throw new IllegalArgumentException("position out of child count!");
    }

    public void setAllBtnSelected(boolean b) {
        for (int i = 0; i < mButtonContainer.getChildCount(); ++i) {
            mButtonContainer.getChildAt(i).setSelected(b);
        }
    }

    public void setBtnSelected(int p, boolean b) {
        if (p >= 0 && p < mButtonContainer.getChildCount()) {
            mButtonContainer.getChildAt(p).setSelected(b);
        }
    }

    public boolean isBtnSelected(int p) {
        if (p >= 0 && p < mButtonContainer.getChildCount()) {
            return mButtonContainer.getChildAt(p).isSelected();
        }
        throw new IllegalArgumentException("position out of child count!");
    }

    public int getFirstBtnSelected() {
        for (int i = 0; i < mButtonContainer.getChildCount(); ++i) {
            if (mButtonContainer.getChildAt(i).isSelected()) {
                return i;
            }
        }
        return -1;
    }

    public void setAllNextFocusDownId(int id) {
        Log.i(TAG, "setAllNextFocusDownId: " + id + " ,for: " + getTag());
        for (int i = 0; i < mButtonContainer.getChildCount(); i++) {
            mButtonContainer.getChildAt(i).setNextFocusDownId(id);
        }
    }

    public void setAllNextFocusUpId(int id) {
        Log.i(TAG, "setAllNextFocusUpId: " + id + " ,for: " + getTag());
        for (int i = 0; i < mButtonContainer.getChildCount(); i++) {
            mButtonContainer.getChildAt(i).setNextFocusUpId(id);
        }
    }

    public void setFocus() {
        mButtonContainer.getChildAt(0).requestFocus();
    }

    public void updateStatus(final ProductDetail.Node node) {
        Log.i(TAG, "updateStatus: " + getTag());

        for (int i = 0; i < mButtonContainer.getChildCount(); i++) {
            RadioButton button = (RadioButton)mButtonContainer.getChildAt(i);
            ProductDetail.Option op = (ProductDetail.Option) button.getTag();
            ProductDetail.Node currentNode = ProductDetail.findNodeById(node, op.id);

            if(currentNode != null){
                if(currentNode.valid){
                    Log.i(TAG, "enable true: " + op.id);
                    button.setFocusable(true);
                    button.setEnabled(true);
                }else{
                    Log.i(TAG, "enable false: " + op.id);
                    button.setFocusable(false);
                    button.setEnabled(false);
                    button.setChecked(false);
                }
            }
        }

    }

    public interface OnItemCheckedListener {
        public void onChecked(DialogButtonView view, RadioButton button);
    }
}
