package com.xiaomi.mitv.shop.widget;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import com.xiaomi.mitv.shop.R;

/**
 * Created by linniu on 2015/5/9.
 */
public class LoadingFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i("LoadingFragment", "onCreateView");
        View view = inflater.inflate(R.layout.loading_view, null);

        View iconView = view.findViewById(R.id.loading_icon);

        RotateAnimation ra = new RotateAnimation(0f,360*1000f, Animation.RELATIVE_TO_SELF,0.5f, Animation.RELATIVE_TO_SELF,0.5f);
        ra.setDuration(2000*1000);
        ra.setRepeatCount(Animation.INFINITE);
        ra.setStartOffset(300);
        ra.setInterpolator(new LinearInterpolator());
        iconView.startAnimation(ra);

        return view;
    }
}
