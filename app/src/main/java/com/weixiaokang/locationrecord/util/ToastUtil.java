package com.weixiaokang.locationrecord.util;

import android.app.ActionBar;
import android.content.Context;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ToastUtil {

    public static void showInCenter(Context context, String msg) {
        Toast toast = new Toast(context);
        toast.setGravity(Gravity.CENTER, 0, 0);
        LinearLayout linearLayout = new LinearLayout(context);
        TextView textView = new TextView(context);
        linearLayout.addView(textView);
        toast.setView(linearLayout);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();
    }
    public static void show(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }
}