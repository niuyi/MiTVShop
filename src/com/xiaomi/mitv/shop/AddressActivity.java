package com.xiaomi.mitv.shop;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import com.xiaomi.mitv.shop.db.ShopDBHelper;
import com.xiaomi.mitv.shop.db.ShopDBManager;
import com.xiaomi.mitv.shop.model.Address;
import com.xiaomi.mitv.shop.model.AddressList;
import com.xiaomi.mitv.shop.model.ProductDetail;
import com.xiaomi.mitv.shop.model.ProductManager;
import com.xiaomi.mitv.shop.network.DKResponse;
import com.xiaomi.mitv.shop.network.MyBaseRequest;
import com.xiaomi.mitv.shop.request.GetAddressListRequest;
import com.xiaomi.mitv.shop.widget.AddressListFragment;

/**
 * Created by niuyi on 2015/5/22.
 */
public class AddressActivity extends BaseLoadingActivity {

    public static final String TAG = "AddressActivity";
    private String mUid;
    private String mAddressId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFailureMessage(getResources().getString(R.string.fail_to_add_address_list));

        mUid = getIntent().getStringExtra(AddressListFragment.UID);
        mAddressId = getIntent().getStringExtra(AddressListFragment.ADDRESS_ID);

        mUid = "49649888";
        mAddressId = "10150519610024458";

        AddressList list = new AddressList();
        for(int i = 0 ; i < 10 ; i ++){
            Address a = new Address();
            a.consignee = "牛毅" + String.valueOf(i);
            a.province_name = "北京";
            a.city_name = "北京";
            a.district_name = "海淀区";
            a.address = "海淀区西三旗abcd路";
            a.tel = "13812345678";
            if(i == 0){
                a.address_id = mAddressId;
            }else{
                a.address_id = String.valueOf(i);
            }

            list.addresses.add(a);
        }

        ProductManager.INSTSNCE.putAddressList(mUid, list);

        AddressListFragment frag = new AddressListFragment();

        Bundle bundle = new Bundle();
        bundle.putString(AddressListFragment.UID, mUid);
        bundle.putString(AddressListFragment.ADDRESS_ID, mAddressId);

        frag.setArguments(bundle);

        switchFragment(frag);

//
////        if(!TextUtils.isEmpty(mUid)){
////            GetAddressListRequest req = new GetAddressListRequest(mUid);
////
////            req.setObserver(new MyBaseRequest.MyObserver() {
////
////
////                @Override
////                public void onRequestCompleted(MyBaseRequest request, DKResponse response) {
////                    boolean isSuccess = false;
////                    if(response != null
////                            && response.getStatus() == DKResponse.STATUS_SUCCESS
////                            && !TextUtils.isEmpty(response.getResponse())){
////                        Log.i(TAG, "GetAddressListRequest res: " + response.getResponse());
////
////                        AddressList list = AddressList.parse(response.getResponse());
////
////                        if(list != null){
////                            Log.i(TAG, "address list: " + list.addresses.size());
////                            if(list.addresses.size() > 0){
////                                Log.i(TAG, "consignee: " + list.addresses.get(0).consignee);
////                                ShopDBManager.INSTANCE.setValue("mUid", response.getResponse(), ShopDBHelper.TABLE_ADDRESS_LIST_NAME);
////                                isSuccess = true;
////
////                                ProductManager.INSTSNCE.putAddressList(mUid, list);
////
////                                AddressListFragment frag = new AddressListFragment();
////
////                                Bundle bundle = new Bundle();
////                                bundle.putString(AddressListFragment.UID, mUid);
////                                bundle.putString(AddressListFragment.ADDRESS_ID, mAddressId);
////
////                                frag.setArguments(bundle);
////
////                                switchFragment(frag);
////                            }
////                        }else{
////                            Log.i(TAG, "address list is null");
////                        }
////                    }
////
////                    if(!isSuccess){
////                        showFailurePage();
////                    }
////
////                }
////
////                @Override
////                public void onBeforeSendDone(MyBaseRequest request, DKResponse response) {
////
////                }
////
////                @Override
////                public void onAbort() {
////                    showFailurePage();
////                }
////            });
////
////            req.send();
//        }else {
//            showFailurePage();
//        }
    }
}