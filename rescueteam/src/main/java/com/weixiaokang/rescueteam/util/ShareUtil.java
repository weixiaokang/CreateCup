package com.weixiaokang.rescueteam.util;


import android.content.Context;
import android.content.SharedPreferences;

public class ShareUtil {

    public static void storeNumber(Context context, String type, String number) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("number", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(type, number);
        editor.apply();
    }
}
