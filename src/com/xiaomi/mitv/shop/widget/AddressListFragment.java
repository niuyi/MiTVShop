package com.xiaomi.mitv.shop.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.xiaomi.mitv.shop.AddAddressActivity;
import com.xiaomi.mitv.shop.GoodSelectionActivity;
import com.xiaomi.mitv.shop.R;
import com.xiaomi.mitv.shop.model.Address;
import com.xiaomi.mitv.shop.model.AddressList;
import com.xiaomi.mitv.shop.model.ProductManager;

/**
 * Created by niuyi on 2015/5/22.
 */
public class AddressListFragment extends Fragment implements View.OnFocusChangeListener{


    public static final String UID = "uid";
    public static final String ADDRESS_ID = "address_id";
    public static final String TAG = "AddressListFragment";

    private String mAddressId;
    private AddressListAdapter mAdapter;

    private SelectorView mSelectorView;
    private ViewGroup mRootView;

    private AnimatorSet mMovieAnimator;

    private int delta = 42;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.i(TAG, "onCreateView");

        View view = inflater.inflate(R.layout.address_list_layout, container, false);

        TextView title = (TextView)view.findViewById(R.id.title_text);
        title.setText("选择收货地址");

        mRootView = (ViewGroup)view.findViewById(R.id.root_container);

        ListView listView = (ListView)view.findViewById(R.id.list_view);



        Bundle arguments = getArguments();
        if(arguments != null){
            String uid = arguments.getString(UID);
            mAddressId = arguments.getString(ADDRESS_ID);

            if(uid != null){
                AddressList addressList = ProductManager.INSTSNCE.getAddressList(uid);
                if(addressList != null){
                    mAdapter = new AddressListAdapter(getActivity(), addressList);
                    listView.setAdapter(mAdapter);
                }
            }
        }

        View footerView = inflater.inflate(R.layout.address_list_footer_item, null, false);
        listView.addFooterView(footerView, null, true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG, "on item click! " + parent.getCount() + " : " + position);
                if (position == parent.getCount() - 1) {
                    Intent in = new Intent();
                    in.setClass(getActivity(), AddAddressActivity.class);
                    startActivity(in);
                } else {
                    Address address = (Address) parent.getItemAtPosition(position);
                    mAddressId = address.address_id;
                    Log.i(TAG, "mAddressId: " + mAddressId);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });

        listView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG, "select view: " + view.getClass().getCanonicalName() + " id: " + id);

                final View focusView = view;

//                Log.i(TAG, "select :" + ((AddressListAdapter.ViewHolder)view.getTag()).address.getText());

                view.post(new Runnable() {
                    @Override
                    public void run() {
//                        int[] coord = new int[2];
//                        focusView.getLocationOnScreen(coord);
//
//                        Log.i(TAG, "select view x: " + coord[0] + " ,select view y: " + coord[1]);
//                        Log.i(TAG, "select view w: " + focusView.getWidth() + " ,select view h: " + focusView.getHeight());


                        if (mSelectorView == null) {
                            mSelectorView = new SelectorView(getActivity());

                            FrameLayout.LayoutParams para = new FrameLayout.LayoutParams(focusView.getWidth() + delta * 2, focusView.getHeight() + delta * 2);
                            mRootView.addView(mSelectorView, para);

                            int[] coord = new int[2];
                            focusView.getLocationOnScreen(coord);

                            Log.i(TAG, "select view x: " + coord[0] + " ,select view y: " + coord[1]);

//                            int newX = coord[0];
//                            int newY = coord[1];

                            int newX = 210 - delta;
                            int newY = 200 - delta;


                            mSelectorView.setX(newX);
                            mSelectorView.setY(newY);

                        } else {
                            moveSelector(focusView);
                        }
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        listView.setSelection(0);

        listView.requestFocus();

        return view;
    }

    private ValueAnimator mValueAnimator;

    private FrameLayout.LayoutParams mAnimStartLayout;

    private void moveSelector(View focusView) {

        mAnimStartLayout = (FrameLayout.LayoutParams)mSelectorView.getLayoutParams();

        if(mValueAnimator != null){
            mValueAnimator.cancel();
            mValueAnimator = null;
        }

        int[] coord = new int[2];
        focusView.getLocationInWindow(coord);

        final int newX = coord[0] - delta;
        final int newY = coord[1] - delta;

//        final int oldX = (int)mSelectorView.getX();
//        final int oldY = (int)mSelectorView.getY();

        coord = new int[2];
        mSelectorView.getLocationInWindow(coord);

        final int oldX = coord[0];
        final int oldY = coord[1];

        final int newHeight = focusView.getHeight() + delta*2;

        mValueAnimator = ValueAnimator.ofFloat(0f, 1f);

        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(mAnimStartLayout);
                float percent = (Float) animation.getAnimatedValue();

                mSelectorView.setX(newX - oldX * (1 - percent));
                mSelectorView.setY(newY - oldY * (1 - percent));

                lp.height = (int) (lp.height + (newHeight - lp.height) * percent);

                mSelectorView.setLayoutParams(lp);
            }
        });

        mValueAnimator.setDuration(300);
        mValueAnimator.start();

//        if(mMovieAnimator != null){
//            mMovieAnimator.cancel();
//            mMovieAnimator = null;
//        }
//
//        mMovieAnimator = new AnimatorSet();
//
//        int[] coord = new int[2];
//        focusView.getLocationInWindow(coord);
//
//        final int newX = coord[0] - delta;
//        final int newY = coord[1] - delta;
//
//        ObjectAnimator moveXAnimator = ObjectAnimator.ofFloat(mSelectorView, "x", mSelectorView.getX(), newX);
//        ObjectAnimator moveYAnimator = ObjectAnimator.ofFloat(mSelectorView, "y", mSelectorView.getY(), newY);
//
//        moveXAnimator.setDuration(300);
//        moveYAnimator.setDuration(300);
//
//        final int newHeight = focusView.getHeight() + delta*2;
//
//        Log.i(TAG, "old h: " + mSelectorView.getHeight() + " ,newh: " + newHeight);
//
//        if(mSelectorView.getHeight() != newHeight){
//            float rate = (float)newHeight/ (float)mSelectorView.getHeight();
//            Log.i(TAG, "scaleY rate: " + rate);
//            ObjectAnimator sizeAnimator = ObjectAnimator.ofFloat(mSelectorView, "scaleY", rate);
//            sizeAnimator.setDuration(300);
//
//            sizeAnimator.addListener(new Animator.AnimatorListener() {
//                @Override
//                public void onAnimationStart(Animator animation) {
//
//                }
//
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    FrameLayout.LayoutParams para = new FrameLayout.LayoutParams(mSelectorView.getWidth(), newHeight);
//                    mSelectorView.setLayoutParams(para);
////                    mSelectorView.setX(newX);
////                    mSelectorView.setY(newY);
//                }
//
//                @Override
//                public void onAnimationCancel(Animator animation) {
//
//                }
//
//                @Override
//                public void onAnimationRepeat(Animator animation) {
//
//                }
//            });
//
//            mMovieAnimator.playTogether(moveXAnimator, moveYAnimator, sizeAnimator);
//        }else{
//            mMovieAnimator.playTogether(moveXAnimator, moveYAnimator);
//        }
//
//        mMovieAnimator.start();
    }


    class AddressListAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;
        private AddressList mAddressList;



        public class ViewHolder {
            ImageView icon;
            TextView name;
            TextView phone;
            public TextView address;
        }

        public AddressListAdapter(Context c, AddressList addressList) {
            context = c;
            inflater = LayoutInflater.from(context);
            mAddressList = addressList;
        }

        @Override
        public int getCount() {
            return mAddressList.addresses.size();
        }

        @Override
        public Object getItem(int index) {
            return mAddressList.addresses.get(index);
        }

        @Override
        public long getItemId(int index) {
            return index;
        }

        @Override
        public View getView(int index, View view, ViewGroup group) {
            ViewHolder holder;

            final Address address = mAddressList.addresses.get(index);

            if (view == null) {
                view = inflater.inflate(R.layout.address_list_item, null);
                holder = new ViewHolder();

                holder.icon = (ImageView)view.findViewById(R.id.icon);
                holder.name = (TextView)view.findViewById(R.id.tv_name);
                holder.phone =  (TextView)view.findViewById(R.id.tv_phone);
                holder.address =  (TextView)view.findViewById(R.id.tv_address);

                view.setTag(holder);

                view.setOnFocusChangeListener(AddressListFragment.this);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            if(address.address_id.equals(mAddressId)){
                holder.icon.setImageResource(R.drawable.radio_on);
            }else{
                holder.icon.setImageResource(R.drawable.radio_off);
            }

            view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(hasFocus){
                        Log.i(TAG, "has focus:" + v.getClass().getCanonicalName());
                    }
                }
            });


            holder.name.setText(address.consignee);
            holder.phone.setText(address.tel);
            holder.address.setText(String.format("%s %s %s %s", address.province_name, address.city_name, address.district_name, address.address));

            return view;
        }

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(hasFocus){
            Log.i(TAG, "focus view: " + v.getClass().getCanonicalName());
        }
    }
}