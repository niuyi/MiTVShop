package com.xiaomi.mitv.shop.widget;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.*;
import com.squareup.picasso.Picasso;
import com.xiaomi.mitv.shop.DetailActivity;
import com.xiaomi.mitv.shop.R;
import com.xiaomi.mitv.shop.model.ProductDetail;
import com.xiaomi.mitv.shop.network.DKResponse;
import com.xiaomi.mitv.shop.network.JsonSerializer;

/**
 * Created by linniu on 2015/5/9.
 */
public class ProductDetailFragment extends Fragment {

    private static final String TAG = "ProductDetailFragment";
    private TextView mPriceTextView;
    private Button mBuyButton;
    private ViewFlipper mViewSwitcher;

    private int mCurrentIndex;
    private ProductDetail mDetail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.product_detail_view, null);

        mPriceTextView = (TextView)view.findViewById(R.id.tv_price);
        mBuyButton = (Button)view.findViewById(R.id.button_buy);
        mViewSwitcher = (ViewFlipper)view.findViewById(R.id.vf_container);

        mBuyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "buy now");
            }
        });

        mBuyButton.requestFocus();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        mDetail = initData();

        if(mDetail == null){
            ((DetailActivity)getActivity()).showFailurePage();
            return;
        }

        initView();
    }

    private void initView() {
        mCurrentIndex = 0;

        mPriceTextView.setText(mDetail.price);

        mViewSwitcher.removeAllViews();

        for(String imageUrl : mDetail.images){
            Log.i(TAG, "add image view: " + imageUrl);
            FrameLayout layout = new FrameLayout(getActivity());
            layout.setBackgroundColor(Color.WHITE);
            mViewSwitcher.addView(layout);

            ImageView iv = new ImageView(getActivity());
            iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            layout.addView(iv, params);

            Picasso.with(getActivity()).load(imageUrl).into(iv);

        }
    }

    private ProductDetail initData() {
        try {
            Bundle input = getArguments();
            String data = input.getString(DKResponse.DATA_KEY);

            ProductDetail detail = JsonSerializer.getInstance().deserialize(data, ProductDetail.class);
            Log.i(TAG, "detail image count: " + detail.images.length);

            if(detail != null && detail.images.length > 0){
                return detail;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

       return null;
    }

    public boolean handleKeyEvent(KeyEvent event) {
        Log.i(TAG, "handleKeyEvent: " + event);
        if (event.getAction() == KeyEvent.ACTION_DOWN
                && event.getRepeatCount() % 5 == 0) {
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_DPAD_LEFT:
                    if (mCurrentIndex <= 0) {
                        mViewSwitcher.playSoundEffect(5);
                        return true;
                    }

                    mCurrentIndex--;

                    mViewSwitcher.showPrevious();
                    mViewSwitcher.setInAnimation(AnimationUtils.loadAnimation(
                            getActivity(), android.R.anim.slide_in_left));
                    mViewSwitcher.setOutAnimation(AnimationUtils.loadAnimation(
                            getActivity(), android.R.anim.slide_out_right));
                    break;
                case KeyEvent.KEYCODE_DPAD_RIGHT:
                    if (mCurrentIndex == mDetail.images.length - 1) {
                        mViewSwitcher.playSoundEffect(5);
                        return true;
                    }

                    mCurrentIndex++;

                    mViewSwitcher.setInAnimation(AnimationUtils.loadAnimation(
                            getActivity(), R.anim.slide_in_right));
                    mViewSwitcher.setOutAnimation(AnimationUtils.loadAnimation(
                            getActivity(), R.anim.slide_out_left));

                    mViewSwitcher.showNext();
                    break;
            }

            return true;
        }else{
            return false;
        }
    }

    public void reload(String json) {
        Log.i(TAG, "reload");

        ProductDetail detail = JsonSerializer.getInstance().deserialize(json, ProductDetail.class);
        Log.i(TAG, "detail image count: " + detail.images.length);

        mDetail = detail;

        if(mDetail != null){
            initView();
        }
    }
}
