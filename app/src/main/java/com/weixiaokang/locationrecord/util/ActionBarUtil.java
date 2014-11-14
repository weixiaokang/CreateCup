package com.weixiaokang.locationrecord.util;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;

public class ActionBarUtil {

    public static void hideActionBar(Context context) {
        ActionBar actionBar = ((Activity)context).getActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }
}
