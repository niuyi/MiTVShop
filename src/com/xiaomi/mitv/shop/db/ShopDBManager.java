package com.xiaomi.mitv.shop.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.xiaomi.mitv.shop.App;
import com.xiaomi.mitv.shop.util.Util;

/**
 * Created by niuyi on 2015/5/8.
 */
public enum ShopDBManager {
    INSTANCE;

    private static final String TAG = "ShopDBManager";

    private ShopDBHelper mDBHelper = null;

    ShopDBManager() {
    }

    private SQLiteDatabase getWritableDatabase() {
        ShopDBHelper helper = getDBHelper();
        return helper.getWritableDatabase();
    }

    private SQLiteDatabase getReadableDatabase() {
        ShopDBHelper helper = getDBHelper();
        return helper.getReadableDatabase();
    }

    private synchronized ShopDBHelper getDBHelper() {
        if (mDBHelper == null) {
            mDBHelper = new ShopDBHelper(App.getInstance().getApplicationContext());
        }
        return mDBHelper;
    }

    public void addValue(String key, String value) {
        Log.i(TAG, "addValue, key:" + key + " ,value:" + value);

        try {
            SQLiteDatabase db = getWritableDatabase();

            ContentValues val = new ContentValues();
            val.put(ShopDBHelper.Columns_INFO_KEY, key);
            val.put(ShopDBHelper.Columns_INFO_VALUE, value);
            val.put(ShopDBHelper.Columns_INFO_TIMESTAMP, String.valueOf(System.currentTimeMillis() / 1000));

            long ret = db.replace(ShopDBHelper.TABLE_SHOP_INFO_NAME, null, val);

            Log.i(TAG, "addValue ret: " + ret);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getValue(String key) {
        Log.i(TAG, "getValue, key: " + key);

        Cursor cursor = null;

        try {
            SQLiteDatabase db = getReadableDatabase();
            cursor = db.query(
                    ShopDBHelper.TABLE_SHOP_INFO_NAME,
                    new String[]{ShopDBHelper.Columns_INFO_VALUE},
                    String.format("%s = ?", ShopDBHelper.Columns_INFO_KEY),
                    new String[]{key}, null, null, null);

            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                return cursor.getString(cursor.getColumnIndex(ShopDBHelper.Columns_INFO_VALUE));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Util.closeQuietly(cursor);
        }

        return null;
    }
}
