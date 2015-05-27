package com.xiaomi.mitv.shop.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import com.xiaomi.mitv.api.util.PopupWindow;
import com.xiaomi.mitv.shop.R;
import com.xiaomi.mitv.shop.model.Region;
import com.xiaomi.mitv.shop.util.LocationManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * Created by niuyi on 2015/5/27.
 */
public class SelectLocationWindow extends PopupWindow {

    private WheelView provinceView, cityView, districtView;
//    private Region strProvince[];
//    private Region strCity[];
//    private Region strDistrict[];
    private int firstSetComplete = 1;
    private final ViewGroup mRootView;
    private FutureTask<LocationManager.AllLocations> mTask;
    private LocationManager.AllLocations mAllLocations = null;


    public SelectLocationWindow(Context context, FutureTask<LocationManager.AllLocations> mTask) {
        super(context);
        this.mTask = mTask;

        mRootView = (ViewGroup) View.inflate(context, R.layout.location_selection_window, null);
        setContentView(mRootView);

        provinceView = (WheelView) mRootView.findViewById(R.id.province);
        cityView = (WheelView) mRootView.findViewById(R.id.city);
        districtView = (WheelView) mRootView.findViewById(R.id.district);

        provinceView.setFocusable(true);
        cityView.setFocusable(true);
        districtView.setFocusable(true);
        cityView.setVisibleItems(5);
        districtView.setVisibleItems(5);
        provinceView.setVisibleItems(5);

        try {
            mAllLocations = mTask.get();
            if(mAllLocations != null){
                setAdapter();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        //for test
//        ArrayList<Region> provinces = new ArrayList<Region>();
//        HashMap<String, ArrayList<Region>> P2C = new HashMap<String, ArrayList<Region>>();
//        HashMap<String, ArrayList<Region>> C2D = new HashMap<String, ArrayList<Region>>();
//
//        for (int i = 0; i < 5; i++) {
//
//            String prov = String.valueOf(i);
//            provinces.add(prov);
//
//            for (int j = 0; j < 6; j++) {
//                String city = prov + ":" + j;
//                ArrayList<String> strings = P2C.get(prov);
//                if (strings == null) {
//                    strings = new ArrayList<String>();
//                    P2C.put(prov, strings);
//                }
//
//                strings.add(city);
//
//                for (int k = 0; k < 10; k++) {
//                    String d = city + ":" + k;
//                    ArrayList<String> ds = C2D.get(city);
//                    if (ds == null) {
//                        ds = new ArrayList<String>();
//                        C2D.put(city, ds);
//                    }
//                    ds.add(d);
//                }
//            }
//        }

//        setAdapter(provinces, P2C, C2D, new HashMap<String, ArrayList<String>>());
    }

    public void setAdapter() {

//        strProvince = new Region[provinces.size()];
//        provinces.toArray(strProvince);
        provinceView.setAdapter(new ArrayWheelAdapter<Region>(mAllLocations.child));

        provinceView.addChangingListener(new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                if (firstSetComplete == 1) {

                    Region newProvince = mAllLocations.child[newValue];

//                    strCity = new Region[P2C.get(newProvince).size()];
//                    (P2C.get(newProvince.region_id)).toArray(strCity);

                    cityView.setAdapter(new ArrayWheelAdapter<Region>(newProvince.child));
                    if (newProvince.child.length < 5) {
                        cityView.setCyclic(false);
                    } else {
                        cityView.setCyclic(true);
                    }
                    cityView.setCurrentItem(0);

                    Region city = newProvince.child[0];
//                    strDistrict = new Region[C2D.get(newProvince.child[0]).size()];
//                    (C2D.get(strCity[0].region_id)).toArray(strDistrict);

                    districtView.setAdapter(new ArrayWheelAdapter<Region>(city.child));
                    if (city.child.length < 5) {
                        districtView.setCyclic(false);
                        districtView.setCurrentItem(0);
                    } else {
                        districtView.setCyclic(true);
                        districtView.setCurrentItem(0);
                    }

                }
            }
        });

//        showExistInfo(provinces, P2C, C2D, C2DCLL);//show user_location or mitv_location

        cityView.addChangingListener(new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                if (firstSetComplete == 1) {
                    Region province = mAllLocations.child[provinceView.getCurrentItem()];
                    Region city = province.child[newValue];
//
//                    strDistrict = new Region[C2D.get((P2C.get(provinces.get(provinceView.getCurrentItem()))).get(newValue)).size()];
//                    C2D.get((P2C.get(provinces.get(provinceView.getCurrentItem()))).get(newValue)).toArray(strDistrict);

                    districtView.setAdapter(new ArrayWheelAdapter<Region>(city.child));
                    if (city.child.length < 5) {
                        districtView.setCyclic(false);
                        districtView.setCurrentItem(0);
                    } else {
                        districtView.setCyclic(true);
                        districtView.setCurrentItem(0);
                    }

                }
            }
        });

    }

    public void showExistInfo(ArrayList<String> provinces, HashMap<String, ArrayList<String>> P2C, HashMap<String, ArrayList<String>> C2D,
                              HashMap<String, ArrayList<String>> C2DCLL) {

//        province.setCurrentItem(0);
//        strCity = new String[(P2C.get(provinces.get(0))).size()];
//        (P2C.get(provinces.get(0))).toArray(strCity);
//        city.setAdapter(new ArrayWheelAdapter<String>(strCity));
//
//        if (strCity.length < 5)
//            city.setCyclic(false);
//        else
//            city.setCyclic(true);
//
//        city.setCurrentItem(0);
//        strDistrict = new String[(C2D.get(strCity[0])).size()];
//        (C2D.get(strCity[0])).toArray(strDistrict);
//        district.setAdapter(new ArrayWheelAdapter<String>(strDistrict));
//
//        if (strDistrict.length < 5)
//            district.setCyclic(false);
//        else
//            district.setCyclic(true);
//
//        district.setCurrentItem(0);
//        strDistCLL = new ArrayList<String>();
//        strDistCLL = C2DCLL.get(strCity[0]);
//        firstSetComplete = 1;
    }
}
