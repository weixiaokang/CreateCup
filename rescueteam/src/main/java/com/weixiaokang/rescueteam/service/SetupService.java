package com.weixiaokang.rescueteam.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;

import com.weixiaokang.rescueteam.MainActivity;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class SetupService extends IntentService {

    private static final String ACTION_FOO = "strat";
    private static final String ACTION_BAZ = "com.weixiaokang.rescueteam.service.action.BAZ";

    private static final String EXTRA_PARAM1 = "com.weixiaokang.rescueteam.service.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.weixiaokang.rescueteam.service.extra.PARAM2";

    private SensorManager manager;
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, SetupService.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, SetupService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    public SetupService() {
        super("SetupService");
    }

    @Override
    protected void onHandleIntent(final Intent intent) {
        if (intent != null) {
            Log.i("debug", "-->onHandleIntent");
            manager = (SensorManager) getSystemService(SENSOR_SERVICE);
            manager.registerListener(new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent event) {
                    Log.i("debug", "-->onSensorChanged");
                    float[] values = event.values;
                    float x = values[0];
                    float y = values[1];
                    float z = values[2];
                    Log.i("debug", "-->willStartActivity");
                    double value = Math.sqrt(Math.pow(x, 2.0) + Math.pow(y, 2.0) + Math.pow(z, 2.0));
                    Log.i("num", value + "");
                    if (value > 25) {
                        Log.i("heheheda", "-->startActivity");
                        Intent intent1 = new Intent(SetupService.this, MainActivity.class);
                        intent1.putExtra("value", value);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        SetupService.this.startActivity(intent1);
                    }
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy) {

                }
            }, manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
