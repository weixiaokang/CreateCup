package com.weixiaokang.locationrecord.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

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
            time[i] = message[i].substring(0, message[i].indexOf(" "));
            longtitude[i] = message[i].substring(message[i].indexOf(" ") + 1, message[i].lastIndexOf(" "));
            latitude[i] = message[i].substring(message[i].lastIndexOf(" ") + 1, message[i].length());
            LocationData locationData = new LocationData(time[i], longtitude[i], latitude[i]);
            LogUtil.i(Constants.DATA, locationData.getTime() + locationData.getLongitude() + locationData.getLatitude());
            dbHelper.add(locationData);
            Notification notification = new Notification(R.drawable.locating_icon, longtitude[i] + " " + latitude[i], System.currentTimeMillis());
            Intent intent1 = new Intent(Intent.ACTION_MAIN);
            intent1.addCategory(Intent.CATEGORY_LAUNCHER);
            intent1.setClass(this, MyActivity.class);
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            notification.flags = Notification.FLAG_ONGOING_EVENT;
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
            notification.setLatestEventInfo(getApplicationContext(), longtitude[i] + latitude[i], "", pendingIntent);
            notificationManager.notify(R.drawable.locating_icon, notification);
        }
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}