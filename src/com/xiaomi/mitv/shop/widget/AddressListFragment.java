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
public class AddressListFragment extends Fragment{


    public static final String UID = "uid";
    public static final String ADDRESS_ID = "address_id";
    public static final String TAG = "AddressListFragment";

    private String mAddressId;
    private AddressListAdapter mAdapter;

    private SelectorView mSelectorView;
    private ViewGroup mRootView;

    private View mCurrentView;

//    private View mStubView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.i(TAG, "onCreateView");

        View view = inflater.inflate(R.layout.address_list_layout, container, false);

        TextView title = (TextView)view.findViewById(R.id.title_text);
        title.setText("选择收货地址");

        mRootView = (ViewGroup)view.findViewById(R.id.root_container);

        final ListView listView = (ListView)view.findViewById(R.id.list_view);



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

//        mStubView = new View(getActivity());
//        mStubView.setLayoutParams(new ListView.LayoutParams(1, 300));
//        listView.addFooterView(mStubView, null, false);

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

//        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(AbsListView absListView, int i) {
//                if(i == SCROLL_STATE_IDLE){
//                    Log.i(TAG, "onScrollStateChanged");
//                    if(mCurrentView != null){
//                        moveSelector(mCurrentView);
//                        mCurrentView = null;
//                    }
//                }
//            }
//
//            @Override
//            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
//            }
//        });

        SelectorViewListener listener = new SelectorViewListener(mRootView, mSelectorView);

        listView.setOnItemSelectedListener(listener);

        listView.requestFocus();
        listView.setSelection(0);


        return view;
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
}