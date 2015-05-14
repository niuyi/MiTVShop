package com.xiaomi.mitv.shop;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ViewFlipper;
import com.xiaomi.mitv.shop.model.ProductDetail;
import com.xiaomi.mitv.shop.network.DKResponse;
import com.xiaomi.mitv.shop.network.JsonSerializer;
import com.xiaomi.mitv.shop.network.MyBaseRequest;
import com.xiaomi.mitv.shop.network.ProductDetailRequest;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);

        startLoading();

        ProductDetailRequest request = new ProductDetailRequest();

        request.setObserver(new MyBaseRequest.MyObserver() {
            @Override
            public void onRequestCompleted(MyBaseRequest request, DKResponse response) {
                Log.i(TAG, "onRequestCompleted: " + response.getResponse());
                if (response != null
                        && response.getStatus() == DKResponse.STATUS_SUCCESS
                        && !TextUtils.isEmpty(response.getData())) {

                    mHandler.removeCallbacksAndMessages(null);

                    ProductDetailFragment frag = new ProductDetailFragment();
                    Bundle input = new Bundle();
                    input.putString(DKResponse.DATA_KEY, response.getData());
                    frag.setArguments(input);

                    switchFragment(frag);

//                    new Thread(){
//                        @Override
//                        public void run() {
//                            try {
//                                Thread.sleep(15000);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//
//                            ProductDetail detail = new ProductDetail();
//                            detail.price = "¥1999起";
//                            detail.images = new String[]{
//                                    "http://c1.mifile.cn/f/i/2014/cn/goods/mi4/md/gallery/gallery-list-f.jpg?140722",
//                                    "http://c1.mifile.cn/f/i/2014/cn/goods/mi4/md/gallery/gallery-list-n.jpg"};
//
//                            final String json = JsonSerializer.getInstance().serialize(detail);
//
//                            final ProductDetailFragment detailFragment = getDetailFragment();
//
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    detailFragment.reload(json);
//                                }
//                            });
//                        }
//                    }.start();

                }else{
                    showFailurePage();
                }
            }

            @Override
            public void onAbort() {
                showFailurePage();
            }
        });

        request.send();
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
}