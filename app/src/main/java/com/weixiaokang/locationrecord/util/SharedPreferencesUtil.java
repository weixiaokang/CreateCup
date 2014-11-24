package com.weixiaokang.locationrecord.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtil {

    public static void numberFilter(Context context, String number) {
        SharedPreferences preferences = context.getSharedPreferences("number", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("number", number);
        editor.apply();
    }

    public static String getNumber(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("number", Context.MODE_PRIVATE);
        return preferences.getString("number", "15951911977");
    }
}
