package com.xiaomi.mitv.shop;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import com.xiaomi.mitv.shop.db.ShopDBHelper;
import com.xiaomi.mitv.shop.db.ShopDBManager;
import com.xiaomi.mitv.shop.model.ProductDetail;
import com.xiaomi.mitv.shop.model.ProductManager;
import com.xiaomi.mitv.shop.network.DKResponse;
import com.xiaomi.mitv.shop.network.MyBaseRequest;
import com.xiaomi.mitv.shop.request.ProductDetailRequest;
import com.xiaomi.mitv.shop.widget.LoadingFailureFragment;
import com.xiaomi.mitv.shop.widget.LoadingFragment;
import com.xiaomi.mitv.shop.widget.ProductDetailFragment;

/**
 * Created by linniu on 2015/5/9.
 */
public class DetailActivity extends Activity {

    private static final String TAG = "DetailActivity";
    private static final int LOADING_TIMEOUT = 20000;

    private Handler mHandler = new Handler();
    private String mPid;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);

        mPid = getIntent().getStringExtra("pid");

        if(TextUtils.isEmpty(mPid)){
            Log.i(TAG, "pid id empty: " + mPid);
            showFailurePage();
            return;
        }

        startLoading();

        ProductDetailRequest request = new ProductDetailRequest(mPid);

        request.setObserver(new MyBaseRequest.MyObserver() {
            @Override
            public void onRequestCompleted(MyBaseRequest request, DKResponse response) {
                Log.i(TAG, "onRequestCompleted");

                if (response != null) {
                    Log.i(TAG, "onRequestCompleted: response: " + response.getStatus());
                } else {
                    Log.i(TAG, "onRequestCompleted: response is null");
                }

                if (response != null
                        && response.getStatus() == DKResponse.STATUS_SUCCESS
                        && !TextUtils.isEmpty(response.getResponse())) {

                    mHandler.removeCallbacksAndMessages(null);

                    final ProductDetail productDetail = ProductDetail.parse(response.getResponse());

                    if(productDetail != null && productDetail.check()){
                        ProductManager.INSTSNCE.putProductDetail(mPid, productDetail);

                        Log.i(TAG, "onRequestCompleted productDetail: " + productDetail.status);
                        Log.i(TAG, "onRequestCompleted productDetail, price: " + productDetail.price.max);

                        ShopDBManager.INSTANCE.setValue(mPid, response.getResponse(), ShopDBHelper.TABLE_PRODUCT_INFO_NAME);

                        ProductDetailFragment detailFragment = getDetailFragment();
                        if(detailFragment != null && detailFragment.isVisible()){
                            detailFragment.reload();
                        }else{
                            showDetailPage();
                        }
                    }
                } else {
                    showFailurePage();
                }
            }

            @Override
            public void onBeforeSendDone(MyBaseRequest request, DKResponse response) {
                Log.i(TAG, "onBeforeSendDone");

                if (response != null
                        && response.getStatus() == DKResponse.STATUS_BEFORE_SEND_SUCCESS
                        && !TextUtils.isEmpty(response.getResponse())) {

                    mHandler.removeCallbacksAndMessages(null);

                    ProductDetail productDetail = ProductDetail.parse(response.getResponse());

                    if(productDetail != null && productDetail.check()){
                        ProductManager.INSTSNCE.putProductDetail(mPid, productDetail);

                        Log.i(TAG, "onBeforeSendDone productDetail: " + productDetail.status);
                        Log.i(TAG, "onBeforeSendDone productDetail, price: " + productDetail.price.max);

                        showDetailPage();
                    }
                }
            }

            @Override
            public void onAbort() {
                showFailurePage();
            }
        });

        request.send();
    }

    private void showDetailPage() {
        ProductDetailFragment frag = new ProductDetailFragment();

        Bundle input = new Bundle();
        input.putString(ProductDetail.PID_KEY, mPid);
        frag.setArguments(input);

        switchFragment(frag);
    }

    private void startLoading() {
        Log.i(TAG, "startLoading");

        mHandler.postDelayed(new Runnable() {
            public void run() {
                showFailurePage();
            }
        }, LOADING_TIMEOUT);

        switchFragment(new LoadingFragment());
    }

    public void showFailurePage() {
        Log.i(TAG, "showFailurePage");
        switchFragment(new LoadingFailureFragment());
    }

    private void switchFragment(Fragment frag) {
        if (this.isFinishing())
            return;

        Fragment loadingFailureFrag = getFragmentManager().findFragmentByTag(LoadingFailureFragment.class.getCanonicalName());

        if (loadingFailureFrag != null && loadingFailureFrag.isVisible()) {
            Log.i(TAG, "current is failure page");
            return;
        }

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.root_container, frag, frag.getClass().getCanonicalName());
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commitAllowingStateLoss();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        ProductDetailFragment detailFragment = getDetailFragment();
        if(detailFragment != null){
            detailFragment.handleKeyEvent(event);
        }

        return super.dispatchKeyEvent(event);
    }

    private ProductDetailFragment getDetailFragment() {
        Fragment frag = getFragmentManager().findFragmentByTag(ProductDetailFragment.class.getCanonicalName());

        if(frag != null
                && (frag instanceof ProductDetailFragment)){
            return ((ProductDetailFragment)frag);
        }

        return null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ProductManager.INSTSNCE.clear();
    }
}