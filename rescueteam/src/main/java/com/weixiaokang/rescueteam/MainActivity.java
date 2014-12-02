package com.weixiaokang.rescueteam;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.v7.app.ActionBarActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.weixiaokang.rescueteam.service.LocationService;
import com.weixiaokang.rescueteam.service.SetupService;
import com.weixiaokang.rescueteam.util.MyCamera;
import com.weixiaokang.rescueteam.util.StorageHelper;
import com.weixiaokang.rescueteam.widget.CameraPreview;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    private Button sendButtton, cancelButton;
    private boolean flag = false;
    private Vibrator vibrator;
    private Location mLocation;
    private String content;
    private LocationService.LocationBinder mBunder;
    private LocationService mService;
    private Camera mCamera;
    private MediaPlayer mPlayer;
    private int dir;
    private CameraPreview mPreview;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBunder = (LocationService.LocationBinder) service;
            mService = mBunder.getLocationService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Intent intent = new Intent(this, SetupService.class);
        startService(intent);

        Intent intent2 = new Intent(this, LocationService.class);
        bindService(intent2, connection, BIND_AUTO_CREATE);

        initView();

        setListener();

        SensorManager mManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mManager.registerListener(new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float[] v = event.values;
                float z = v[2];
                if (z < 0) {
                    dir = Camera.CameraInfo.CAMERA_FACING_BACK;
                } else {
                    dir = Camera.CameraInfo.CAMERA_FACING_FRONT;
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        }, mManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!flag) {
                    sendSMS();
                }
            }
        }, 20000);

        Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                vibrator.vibrate(300);
            }
        }, 1000);

    }

    private void takePhoto() {
        Log.i("heheda", "-->takePhoto");
         if (MyCamera.checkCameraHardware(this)) {
             Log.i("heheda", "--I have camera");
             mCamera = MyCamera.getCameraInstance(dir);
             mPreview = new CameraPreview(this, mCamera);
             FrameLayout mFrameLayout = (FrameLayout)findViewById(R.id.PreviewView);
             LinearLayout linearLayout = (LinearLayout)findViewById(R.id.my_layout);
             linearLayout.setVisibility(View.VISIBLE);
             mFrameLayout.setVisibility(View.VISIBLE);
             FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(1, 1);
             mFrameLayout.addView(mPreview, params);
             mCamera.startPreview();
             mCamera.autoFocus(new Camera.AutoFocusCallback() {
                 @Override
                 public void onAutoFocus(boolean success, Camera camera) {
                     Log.i("heheda", "-->onAutoFucus" + ":" + success);
                     if (success && camera != null) {
                         mCamera.takePicture(mShutterCallback, null, mPicureCallback);
                     }
                 }
             });
         }
    }

    private Camera.ShutterCallback mShutterCallback=new Camera.ShutterCallback()
    {
        @Override
        public void onShutter() {
            mPlayer=new MediaPlayer();
            mPlayer = MediaPlayer.create(MainActivity.this, R.raw.mfx_point);
            try {
                mPlayer.prepare();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mPlayer.start();
        }
    };

    private Camera.PictureCallback mPicureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] mData, Camera camera) {
            Log.i("heheda", "-->onPictureTaken");
            File mPictureFile = StorageHelper.getOutputFile(StorageHelper.MEDIA_TYPE_IMAGE);
            if (mPictureFile == null) {
                return;
            }
            try {
                FileOutputStream fos = new FileOutputStream(mPictureFile);
                fos.write(mData);
                fos.close();
                mCamera.stopPreview();
                Intent intent1 = new Intent(MainActivity.this, HelpActivity.class);
                startActivity(intent1);
                finish();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    private void initView() {
        sendButtton = (Button) findViewById(R.id.send_but);
        cancelButton = (Button) findViewById(R.id.cancel_but);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
    }

    private void sendSMS() {
        SharedPreferences sharedPreferences = getSharedPreferences("number", MODE_PRIVATE);
        String address = sharedPreferences.getString("num", "15951911977");
        mLocation = mService.getLocation();
        if (mLocation != null) {
            content = mLocation.getLongitude() + " " + mLocation.getLatitude();
        } else {
            content = "help!";
        }
        SmsManager smsManager = SmsManager.getDefault();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, new Intent(), 0);
        smsManager.sendTextMessage(address, null, content, pendingIntent, null);
        takePhoto();
//                mHandler.sendEmptyMessage(13041309);
    }

    private void setListener() {
        sendButtton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this, SettingActivity.class);
            startActivity(intent);
            flag = true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(connection);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_but:
                flag = true;
                sendSMS();
                break;
            case R.id.cancel_but:
                flag = true;
                finish();
                break;
        }
    }
}
