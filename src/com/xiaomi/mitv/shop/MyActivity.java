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

    ButtonClickListener mButtonClickListener = new ButtonClickListener();
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
//            view.setFocusable(true);

            view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    Log.i(TAG, "focuse change, " + v.getTag() + " hasFocus: " + hasFocus);

                    DialogButtonView view = (DialogButtonView)v;

                    if(hasFocus){
                        view.setFocus();
                    }else{
//                        view.setSelected();
                    }
                }
            });

            view.setItemTitle("index: " + i);

            List<String> names = new ArrayList<String>();
            names.add("红色");
            names.add("绿色");
            names.add("白色");
            names.add("蓝色");

            view.setBtnItemNames(names);
            view.setOnItemClickListener(mButtonClickListener);


            view.inflate();
            view.setTag(i);

//            if (i != 0) {
//                view.setAllBtnEnable(false);
//            }

            if(i == 0){
                view.requestFocus();
            }

//            view.setBtnSelected(0, true);

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

    class ButtonClickListener implements DialogButtonView.OnItemClickListener {

        @Override
        public void onClick(DialogButtonView view, int pos) {
            int pView = (Integer) view.getTag();
            boolean flag = view.isBtnSelected(pos);

            resetBtnSelected(view);
            if (!flag) {
                view.setBtnSelected(pos, true);
//                String tid = mDetailItem.groupList.get(pView).tagsList.get(pos).tid;
//                mCurrentTidList.add(tid);
//                mDetailItem.updatePidList(tid, mCurrentPidList);
//                if (pView + 1 < mDialogContainer.getChildCount()) {
//                    DialogButtonView v = (DialogButtonView) mDialogContainer.getChildAt(pView + 1);
//                    for (int i = 0; i < v.getBtnCount(); ++i) {
//                        String tmpTid = mDetailItem.groupList.get(pView + 1).tagsList.get(i).tid;
//                        if (mDetailItem.checkPidList(tmpTid, mCurrentPidList)) {
//                            v.setBtnEnable(i, true);
//                        }
//                    }
//                } else {
//                    if (mCurrentPidList.size() > 0) {
//                        mSelectedPid = mCurrentPidList.get(0);
//                        Miio.log(TAG, "selected pid: " + mSelectedPid);
//                        DeviceShopDetailItem.Properties properties =
//                                mDetailItem.getPropertiesByPid(mSelectedPid);
//
//                        mTitle.setText(properties.name);
//                        mPrice.setText(getContext().getString(R.string.device_shop_price,
//                                Float.valueOf(properties.price) / 100));
//                        mInsureBtn.setText(properties.buttonText);
//                        mInsureBtn.setOnClickListener(mInsureButtonClickListener);
//                        if (TextUtils.equals(properties.inventory, "0")) {
//                            mInsureBtn.setEnabled(false);
//                        } else {
//                            mInsureBtn.setEnabled(true);
//                        }
//
//                        if (mPidChangedListener != null) {
//                            mPidChangedListener.onNewPid(mSelectedPid);
//                        }
//                    }
//                }
            }
        }

        private void resetBtnSelected(DialogButtonView view) {
            int pView = (Integer) view.getTag();
            view.setAllBtnSelected(false);
//            for (int i = pView + 1; i < mDialogContainer.getChildCount(); ++i) {
//                DialogButtonView v = (DialogButtonView) mDialogContainer.getChildAt(i);
//                v.setAllBtnSelected(false);
//                v.setAllBtnEnable(false);
//            }
//            mCurrentTidList = mCurrentTidList.subList(0, pView);
//            mDetailItem.resetPidList(mAllPidList, mCurrentTidList, mCurrentPidList);
//            mInsureBtn.setEnabled(false);
//            mInsureBtn.setText(R.string.device_shop_sure);
//            mSelectedPid = null;
//            mPidChangedListener.onNewPid(null);
        }
    }
}
