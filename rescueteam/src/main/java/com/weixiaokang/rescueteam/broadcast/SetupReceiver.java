package com.weixiaokang.rescueteam.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.weixiaokang.rescueteam.MainActivity;
import com.weixiaokang.rescueteam.service.SetupService;

public class SetupReceiver extends BroadcastReceiver {

    private static final String action2 = "android.intent.action.BOOT_COMPLETED";
    public SetupReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String mAction = intent.getAction();
        Log.i("action", mAction);
        if (mAction.equals(action2)) {
            Intent newIntent1 = new Intent(context, SetupService.class);
            newIntent1.setAction("start");
            context.startService(newIntent1);
        }
    }
}
