package com.weixiaokang.locationrecord.util;

import android.util.Log;

/**
 * Created by Administrator on 2014/10/16.
 */
public class LogUtil {

    public static void i(String tag, String msg) {
        if (Constants.DEBUG) {
            Log.i(tag, msg);
        }
    }
}