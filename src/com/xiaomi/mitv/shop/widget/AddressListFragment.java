package com.xiaomi.mitv.shop.widget;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.xiaomi.mitv.shop.AddAddressActivity;
import com.xiaomi.mitv.shop.R;
import com.xiaomi.mitv.shop.model.Address;
import com.xiaomi.mitv.shop.model.AddressList;
import com.xiaomi.mitv.shop.model.ProductManager;

/**
 * Created by niuyi on 2015/5/22.
 */
public class AddressListFragment extends Fragment {


    public static final String UID = "uid";
    public static final String ADDRESS_ID = "address_id";
    public static final String TAG = "AddressListFragment";

    private String mAddressId;
    private VerticalGridView mListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.i(TAG, "onCreateView");

        View view = inflater.inflate(R.layout.address_list_layout, container, false);

        TextView title = (TextView) view.findViewById(R.id.title_text);
        title.setText(R.string.select_address);

        mListView = (VerticalGridView) view.findViewById(R.id.list_view);

        Bundle arguments = getArguments();
        if (arguments != null) {
            String uid = arguments.getString(UID);
            mAddressId = arguments.getString(ADDRESS_ID);

            if (uid != null) {
                AddressList addressList = ProductManager.INSTANCE.getAddressList(uid);
                if (addressList != null) {
                    int index = 0;
                    for (int i = 0; i < addressList.addresses.size(); i++) {
                        if (addressList.addresses.get(i).address_id.equals(mAddressId)) {
                            index = i;
                        }
                    }

                    AddressListAdapter adapter = new AddressListAdapter(addressList);
                    mListView.setAdapter(adapter);
                    mListView.setWindowAlignment(VerticalGridView.WINDOW_ALIGN_LOW_EDGE);
                    mListView.setSelectedPosition(index);
                    mListView.setVerticalMargin(45);
                }
            }
        }

        return view;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mName;
        public TextView mCity;
        public TextView mAddress;

        public MyViewHolder(View itemView) {
            super(itemView);
            mName = (TextView) itemView.findViewById(R.id.tv_name);
            mCity = (TextView) itemView.findViewById(R.id.tv_city);
            mAddress = (TextView) itemView.findViewById(R.id.tv_address);
        }
    }

    public static class MyFooterViewHolder extends RecyclerView.ViewHolder {
        public MyFooterViewHolder(View itemView) {
            super(itemView);
        }
    }

    class AddressListAdapter extends RecyclerView.Adapter implements View.OnClickListener {

        private static final int FOOTER_TYPE = Integer.MIN_VALUE;

        private AddressList mAddressList;

        public AddressListAdapter(AddressList addressList) {
            mAddressList = addressList;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup viewGroup, int i) {

            if (i == FOOTER_TYPE) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.address_list_footer_item, viewGroup, false);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent in = new Intent();
                        in.setClass(getActivity(), AddAddressActivity.class);
                        startActivityForResult(in, 0);
                    }
                });
                return new MyFooterViewHolder(view);
            } else {
                final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.address_list_item, viewGroup, false);

                view.setOnClickListener(this);

                return new MyViewHolder(view);
            }

        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            Log.i(TAG, "onBindViewHolder: " + viewHolder.getClass().getCanonicalName() + " ,i: " + i);

            if (i == mAddressList.addresses.size())
                return;

            if (viewHolder instanceof MyViewHolder) {
                MyViewHolder holder = (MyViewHolder) viewHolder;
                Address address = mAddressList.addresses.get(i);

                holder.mName.setText(String.format("%s  %s", address.consignee, address.tel));
                holder.mCity.setText(String.format("%s  %s %s", address.province_name, address.city_name, address.district_name));
                holder.mAddress.setText(address.address);

            }

        }

        @Override
        public int getItemCount() {
            return mAddressList.addresses.size() + 1;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == mAddressList.addresses.size()) {
                return FOOTER_TYPE;
            }

            return 0;
        }

        @Override
        public void onClick(View v) {

            int index = mListView.getChildAdapterPosition(v);
            Log.i(TAG, "index click: " + index);

            Address address = mAddressList.addresses.get(index);

            ProductManager.INSTANCE.getCurrentCheckoutResponse().body.address = address;

            getActivity().finish();

        }
    }


//    class AddressListAdapter extends BaseAdapter {
//        private Context context;
//        private LayoutInflater inflater;
//        private AddressList mAddressList;
//
//
//
//        public class ViewHolder {
//            ImageView icon;
//            TextView name;
//            TextView phone;
//            public TextView address;
//        }
//
//        public AddressListAdapter(Context c, AddressList addressList) {
//            context = c;
//            inflater = LayoutInflater.from(context);
//            mAddressList = addressList;
//        }
//
//        @Override
//        public int getCount() {
//            return mAddressList.addresses.size();
//        }
//
//        @Override
//        public Object getItem(int index) {
//            return mAddressList.addresses.get(index);
//        }
//
//        @Override
//        public long getItemId(int index) {
//            return index;
//        }
//
//        @Override
//        public View getView(int index, View view, ViewGroup group) {
//            ViewHolder holder;
//
//            final Address address = mAddressList.addresses.get(index);
//
//            if (view == null) {
//                view = inflater.inflate(R.layout.address_list_item, null);
//                holder = new ViewHolder();
//
//                holder.icon = (ImageView)view.findViewById(R.id.icon);
//                holder.name = (TextView)view.findViewById(R.id.tv_name);
//                holder.phone =  (TextView)view.findViewById(R.id.tv_phone);
//                holder.address =  (TextView)view.findViewById(R.id.tv_address);
//
//                view.setTag(holder);
//            } else {
//                holder = (ViewHolder) view.getTag();
//            }
//
//            if(address.address_id.equals(mAddressId)){
//                holder.icon.setImageResource(R.drawable.radio_on);
//            }else{
//                holder.icon.setImageResource(R.drawable.radio_off);
//            }
//
//            view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//                @Override
//                public void onFocusChange(View v, boolean hasFocus) {
//                    if(hasFocus){
//                        Log.i(TAG, "has focus:" + v.getClass().getCanonicalName());
//                    }
//                }
//            });
//
//
//            holder.name.setText(address.consignee);
//            holder.phone.setText(address.tel);
//            holder.address.setText(String.format("%s %s %s %s", address.province_name, address.city_name, address.district_name, address.address));
//
//            return view;
//        }
//
//    }
}