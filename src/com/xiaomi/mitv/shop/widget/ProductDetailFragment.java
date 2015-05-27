package com.xiaomi.mitv.shop.widget;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.view.animation.AnimationUtils;
import android.widget.*;
import com.squareup.picasso.Picasso;
import com.xiaomi.mitv.shop.DetailActivity;
import com.xiaomi.mitv.shop.R;
import com.xiaomi.mitv.shop.model.ProductDetail;
import com.xiaomi.mitv.shop.model.ProductManager;
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
    private String mPid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.product_detail_view, null);

        mPriceTextView = (TextView)view.findViewById(R.id.tv_price);
        mBuyButton = (Button)view.findViewById(R.id.button_buy);
        mViewSwitcher = (ViewFlipper)view.findViewById(R.id.vf_container);

        mPid = getArguments().getString(ProductDetail.PID_KEY);
        mDetail = ProductManager.INSTANCE.getProductDetail(mPid);

        mBuyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "buy now");
                GoodSelectionWindow win = new GoodSelectionWindow(getActivity(),getDetail());
                win.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.CENTER, 0, 0);

//                Intent in = new Intent();
//                in.setClassName(getActivity(), GoodSelectionActivity.class.getName());
//
//                getActivity().startActivity(in);
            }
        });

        mBuyButton.requestFocus();

        return view;
    }

    private ProductDetail getDetail(){
//        AssetManager assetManager = getActivity().getAssets();
//        ByteArrayOutputStream outputStream = null;
//        InputStream inputStream = null;
//        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
//        try {
//            inputStream = assetManager.open("detail.json");
//            final int blockSize = 8192;
//            byte[] buffer = new byte[blockSize];
//            int count = 0;
//            while((count = inputStream.read(buffer, 0, blockSize)) > 0) {
//                byteStream.write(buffer,0, count);
//            }
//        } catch (IOException e) {
//        }
//
//        try {
//            byte[] bytes = byteStream.toByteArray();
//            String json = new String(bytes, 0, bytes.length, "utf-8");
//
//            DKResponse res = new DKResponse(1, json, true);
//            ProductDetail detail = ProductDetail.parse(res.getResponse());
//
////            for(ProductDetail.Node node : detail.props_tree){
////                printNode(node, 1);
////            }
//
//            detail.name = "小米手机4电信4G版2GB内存 白色 16G";
//            return detail;
//
//
//
////            JSONObject root = new JSONObject(json);
////            Log.i(TAG, "status: " + root.getInt("status"));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        return null;
    }

    @Override
    public void onStart() {
        super.onStart();

        if(mDetail == null){
            ((DetailActivity)getActivity()).showFailurePage();
            return;
        }

        initView();
    }

    private void initView() {
        mCurrentIndex = 0;

        mPriceTextView.setText("1000");

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

    public void reload() {
        mDetail = ProductManager.INSTANCE.getProductDetail(mPid);
        initView();
    }
}
