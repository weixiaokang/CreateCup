package com.weixiaokang.rescueteam.util;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;


public class ActionBarUtil {

    public static void hide(Context context) {
        ActionBar actionBar = ((ActionBarActivity) context).getSupportActionBar();
        actionBar.hide();
    }
}
