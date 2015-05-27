package com.xiaomi.mitv.shop.widget;

import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
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
public class SelectLocationWindow extends PopupWindow{

    private WheelView provinceView, cityView, districtView;
    private final ViewGroup mRootView;
    private FutureTask<LocationManager.AllLocations> mTask;
    private LocationManager.AllLocations mAllLocations = null;
    private Region[] mCurrentRegion;

    public Region[] getCurrentRegion(){
        if(mAllLocations == null)
            return null;

        Region currentProvince = mAllLocations.child[provinceView.getCurrentItem()];
        Region currentCity = currentProvince.child[cityView.getCurrentItem()];
        Region currentDistrict = currentCity.child[districtView.getCurrentItem()];

        mCurrentRegion = new Region[]{currentProvince, currentCity, currentDistrict};
        return mCurrentRegion;
   }

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

        provinceView.setClickable(true);
        cityView.setClickable(true);
        districtView.setClickable(true);

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
    }

    public void setAdapter() {

        //this is beijing
        provinceView.setAdapter(new ArrayWheelAdapter<Region>(mAllLocations.child));
        cityView.setAdapter(new ArrayWheelAdapter<Region>(mAllLocations.child[0].child));
        cityView.setCyclic(false);
        districtView.setAdapter(new ArrayWheelAdapter<Region>(mAllLocations.child[0].child[0].child));

        provinceView.setCurrentItem(0, true);
        cityView.setCurrentItem(0, true);
        districtView.setCurrentItem(0, true);

        provinceView.addChangingListener(new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                Region newProvince = mAllLocations.child[newValue];

                cityView.setAdapter(new ArrayWheelAdapter<Region>(newProvince.child));
                if (newProvince.child.length < 5) {
                    cityView.setCyclic(false);
                } else {
                    cityView.setCyclic(true);
                }
                cityView.setCurrentItem(0);

                Region city = newProvince.child[0];

                districtView.setAdapter(new ArrayWheelAdapter<Region>(city.child));
                if (city.child.length < 5) {
                    districtView.setCyclic(false);
                    districtView.setCurrentItem(0);
                } else {
                    districtView.setCyclic(true);
                    districtView.setCurrentItem(0);
                }

            }
        });


        cityView.addChangingListener(new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                Region province = mAllLocations.child[provinceView.getCurrentItem()];
                Region city = province.child[newValue];

                districtView.setAdapter(new ArrayWheelAdapter<Region>(city.child));
                if (city.child.length < 5) {
                    districtView.setCyclic(false);
                    districtView.setCurrentItem(0);
                } else {
                    districtView.setCyclic(true);
                    districtView.setCurrentItem(0);
                }
            }
        });

    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        Log.i("SelectLocationWindow", "dispatchKeyEvent: " + event);
        if(event.getKeyCode() == KeyEvent.KEYCODE_ENTER
                && event.getAction() == KeyEvent.ACTION_DOWN
                && event.getRepeatCount() == 0){
            dismiss();
            return true;
        }

        return super.dispatchKeyEvent(event);
    }
}
