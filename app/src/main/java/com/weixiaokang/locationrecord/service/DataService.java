package com.weixiaokang.locationrecord.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.weixiaokang.locationrecord.MyActivity;
import com.weixiaokang.locationrecord.R;
import com.weixiaokang.locationrecord.database.DBHelper;
import com.weixiaokang.locationrecord.database.LocationData;
import com.weixiaokang.locationrecord.util.Constants;
import com.weixiaokang.locationrecord.util.LogUtil;
import com.weixiaokang.locationrecord.util.ToastUtil;


public class DataService extends Service {

    public DataService() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.i(Constants.DATA, "-->onStartCommand");
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        int count = intent.getIntExtra("number", 1);
        String message[] = new String[count];
        for (int i = 0; i < count; i++) {
            message[i] = intent.getStringExtra("message" + i);
        }
        DBHelper dbHelper = new DBHelper(this);
        String time[] = new String[count];
        String longtitude[] = new String[count];
        String latitude[] = new String[count];
        for (int i = 0; i < count; i++) {
            LogUtil.i(Constants.DATA, "--------for-------");
            time[i] = message[i].substring(0, message[i].indexOf(" "));
            longtitude[i] = message[i].substring(message[i].indexOf(" ") + 1, message[i].lastIndexOf(" "));
            latitude[i] = message[i].substring(message[i].lastIndexOf(" ") + 1, message[i].length());
            LocationData locationData = new LocationData(Constants.NUM_COUNT, time[i], longtitude[i], latitude[i]);
            Constants.NUM_COUNT++;
            LogUtil.i(Constants.DATA, locationData.getNum() + locationData.getTime() + locationData.getLongitude() + locationData.getLatitude());
            dbHelper.add(locationData);
            Bundle bundle = new Bundle();
            bundle.putDouble("longtitude", Double.valueOf(longtitude[i]));
            bundle.putDouble("latitude", Double.valueOf(latitude[i]));
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.locating_icon)
                    .setContentTitle("help")
                    .setContentText(longtitude[i] + " " +latitude[i])
                    .setAutoCancel(true)
                    .setOngoing(true);
            Intent intent1 = new Intent(getApplicationContext(), MyActivity.class);
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            intent1.putExtras(bundle);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent1, PendingIntent.FLAG_CANCEL_CURRENT);
            builder.setContentIntent(pendingIntent);
            Notification notification = builder.build();
            notificationManager.notify(R.drawable.locating_icon, notification);
        }
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}