package com.xiaomi.mitv.shop.util;

import android.database.Cursor;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by niuyi on 2015/5/8.
 */
public class Util {

    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
            }
        }
    }

    public static void closeQuietly(Cursor closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception e) {
            }
        }
    }

}
