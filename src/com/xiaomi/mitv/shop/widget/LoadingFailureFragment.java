package com.xiaomi.mitv.shop.widget;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.xiaomi.mitv.shop.R;

/**
 * Created by linniu on 2015/5/9.
 */
public class LoadingFailureFragment extends Fragment {

    public static final String MESSAGE_KEY = "message";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i("LoadingFailureFragment", "onCreateView");
        View view = inflater.inflate(R.layout.loading_failure_view, null);

        TextView message = (TextView)view.findViewById(R.id.tv_failure_message);

        Bundle arguments = getArguments();
        if(arguments != null){
            String msg = arguments.getString(MESSAGE_KEY, "");
            message.setText(msg);
        }


        return view;
    }
}
