package com.xiaomi.mitv.shop.widget;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.xiaomi.mitv.shop.R;

/**
 * Created by linniu on 2015/5/9.
 */
public class LoadingFailureFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i("LoadingFailureFragment", "onCreateView");
        View view = inflater.inflate(R.layout.loading_failure_view, null);
        return view;
    }
}
