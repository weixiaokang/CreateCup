package com.weixiaokang.rescueteam.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.weixiaokang.rescueteam.service.LocationService;
import com.weixiaokang.rescueteam.service.SetupService;

public class SetupReceiver extends BroadcastReceiver {

    private static final String action = "android.intent.action.BOOT_COMPLETED";
    public SetupReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String mAction = intent.getAction();
        if (mAction.equals(action)) {
            Intent newIntent1 = new Intent(context, SetupService.class);
            newIntent1.setAction("start");
            context.startService(newIntent1);

            Intent newIntent2 = new Intent(context, LocationService.class);
            context.startService(newIntent2);
        }
    }
}
