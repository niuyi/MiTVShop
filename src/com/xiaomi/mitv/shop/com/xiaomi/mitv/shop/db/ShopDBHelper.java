package com.xiaomi.mitv.shop.com.xiaomi.mitv.shop.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by niuyi on 2015/5/8.
 */
public class ShopDBHelper extends SQLiteOpenHelper {

    private static final String TAG = "ShopDBHelper";

    private static final String DB_NAME = "shop_db";
    private static final int DB_VERSION = 1;

    public static final String TABLE_SHOP_INFO_NAME = "shop_info";

    public static final String Columns_INFO_KEY = "key";
    public static final String Columns_INFO_VALUE = "value";
    public static final String Columns_INFO_TIMESTAMP = "timestamp";

    public ShopDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    private static final String SHOP_TABLE_CREATE = "create table "
            + TABLE_SHOP_INFO_NAME + "("
                + Columns_INFO_KEY 				+ " text primary key not null, "
                + Columns_INFO_VALUE    		+ " text, "
                + Columns_INFO_TIMESTAMP        + " text"
            + ");";

    private static final String SHOP_TABLE_INDEX_CREATE
            = "create index shop_key_index on " + TABLE_SHOP_INFO_NAME + " (" + Columns_INFO_KEY +  ");";


    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "onCreate");

        db.execSQL(SHOP_TABLE_CREATE);
        db.execSQL(SHOP_TABLE_INDEX_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG, "onUpgrade: " + oldVersion + " , " + newVersion);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SHOP_INFO_NAME);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG, "onDowngrade: " + oldVersion + " , " + newVersion);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SHOP_INFO_NAME);
        onCreate(db);
    }
}
