package com.weixiaokang.locationrecord.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtil {

    public static void numberFilter(Context context, String number) {
        String s = "+86" + number;
        SharedPreferences preferences = context.getSharedPreferences("number", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("number", s);
        editor.commit();
    }
}
