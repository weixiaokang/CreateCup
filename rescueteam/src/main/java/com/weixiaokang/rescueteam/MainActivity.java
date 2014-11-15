package com.weixiaokang.rescueteam;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.weixiaokang.rescueteam.service.SetupService;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    private double value;
    private Button sendButtton, cancelButton;
    private boolean flag = false;
    private Vibrator vibrator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Intent intent = new Intent(this, SetupService.class);
        startService(intent);

        initView();

        setListener();

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

    private void initView() {
        value = getIntent().getDoubleExtra("value", 0);
        sendButtton = (Button) findViewById(R.id.send_but);
        cancelButton = (Button) findViewById(R.id.cancel_but);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
    }

    private void sendSMS() {
        SharedPreferences sharedPreferences = getSharedPreferences("number", MODE_PRIVATE);
        String address = sharedPreferences.getString("num", "+8615951911977");
        String content = "help!";
        Intent intent1 = new Intent(MainActivity.this, HelpActivity.class);
        startActivity(intent1);
        SmsManager smsManager = SmsManager.getDefault();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, new Intent(), 0);
        smsManager.sendTextMessage(address, null, content, pendingIntent, null);
        finish();
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
