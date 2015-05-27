package com.xiaomi.mitv.shop.util;

import android.content.res.AssetManager;
import android.util.Log;
import com.xiaomi.mitv.shop.App;
import com.xiaomi.mitv.shop.model.Region;
import com.xiaomi.mitv.shop.network.JsonSerializer;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by niuyi on 2015/5/27.
 */
public enum  LocationManager {
    INSTANCE;

    private static final String TAG = "LocationManager";


    public String getLocationFromAssets(){

        InputStream is = null;
        ZipInputStream stream = null;
        ByteArrayOutputStream output = null;

        try {
            AssetManager assets = App.getInstance().getApplicationContext().getAssets();
            String[] files = assets.list("location");

            for(String file : files){
                if(file.endsWith("zip")){
                    Log.i(TAG, "get file: " + file);

                    is = assets.open("location/" + file);
                    stream = new ZipInputStream(is);

                    byte[] buffer = new byte[2048];

                    while(stream.getNextEntry() !=null)
                    {
                        output = new ByteArrayOutputStream();
                        int len = 0;
                        while ((len = stream.read(buffer)) > 0)
                        {
                            output.write(buffer, 0, len);
                        }

                        return new String(output.toByteArray(), "utf-8");
                    }


                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            Util.closeQuietly(is);
            Util.closeQuietly(stream);
            Util.closeQuietly(output);
        }

        return null;
    }

    public AllLocations getAllLocations(){

        String value = getLocationFromAssets();
        if(value != null){
            try{
                AllLocations all = JsonSerializer.getInstance().deserialize(value, AllLocations.class);
                return all;
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        return null;
    }



    static public class AllLocations{
        public Region[] child;
    }
}
