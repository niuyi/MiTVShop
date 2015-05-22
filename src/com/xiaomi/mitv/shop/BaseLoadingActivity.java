package com.xiaomi.mitv.shop;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import com.xiaomi.mitv.shop.widget.LoadingFailureFragment;
import com.xiaomi.mitv.shop.widget.LoadingFragment;

/**
 * Created by niuyi on 2015/5/22.
 */
public class BaseLoadingActivity extends Activity {

    private static final String TAG = "BaseLoadingActivity";

    private static final int LOADING_TIMEOUT = 5000;
    private Handler mHandler = new Handler();
    private String mFailureMessage = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_loading_activity);
        startLoading();
    }

    private void startLoading() {
        Log.i(TAG, "startLoading");

        mHandler.postDelayed(new Runnable() {
            public void run() {
                Log.i(TAG, "timeout,show failure!");
                showFailurePage();
            }
        }, LOADING_TIMEOUT);

        switchFragment(new LoadingFragment());
    }

    public void showFailurePage() {
        Log.i(TAG, "showFailurePage");

        Fragment frag = new LoadingFailureFragment();

        Bundle bundle = new Bundle();
        bundle.putString(LoadingFailureFragment.MESSAGE_KEY, getFailureMessage());
        frag.setArguments(bundle);

        switchFragment(frag);
    }

    protected void switchFragment(Fragment frag) {
        if(!(frag instanceof LoadingFragment)){
            mHandler.removeCallbacksAndMessages(null);
        }

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

    public String getFailureMessage() {
        return mFailureMessage;
    }

    public void setFailureMessage(String mFailureMessage) {
        this.mFailureMessage = mFailureMessage;
    }
}