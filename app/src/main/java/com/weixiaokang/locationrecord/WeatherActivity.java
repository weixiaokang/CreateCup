package com.weixiaokang.locationrecord;

import android.animation.AnimatorSet;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.location.AMapLocalDayWeatherForecast;
import com.amap.api.location.AMapLocalWeatherForecast;
import com.amap.api.location.AMapLocalWeatherListener;
import com.amap.api.location.AMapLocalWeatherLive;
import com.amap.api.location.LocationManagerProxy;
import com.weixiaokang.locationrecord.adapter.WeatherAdapter;
import com.weixiaokang.locationrecord.util.ActionBarUtil;
import com.weixiaokang.locationrecord.util.AnimUtil;
import com.weixiaokang.locationrecord.util.Constants;
import com.weixiaokang.locationrecord.util.DateUtil;
import com.weixiaokang.locationrecord.util.LogUtil;
import com.weixiaokang.locationrecord.util.ToastUtil;
import com.weixiaokang.locationrecord.util.WeatherUtil;
import com.weixiaokang.locationrecord.widget.SlidingMenu;

import java.util.List;


public class WeatherActivity extends Activity {

    private LocationManagerProxy locationManagerProxy, locationManagerProxy1;
    private boolean isStart = true, isStart1 = true;
    private Handler handler;
    private Thread thread, thread1;
    private ImageView imageView1, imageView2, imageView3, imageView4, imageView5, weatherImage, openView, refreshView;
    private TextView weatherTv, tempTv, cityTv, timeTv, localTv, dataTv, settingTv;
    private String weathers, temps, citys, times;
    private String[] date = new String[3], weather = new String[3];
    private int[] image = new int[3];
    private int mScreen;
    private AnimatorSet animatorSet, animatorSet1, animatorSet2, animatorSet3, animatorSet4;
    private ListView listView;
    private SlidingMenu slidingMenu;
    private AMapLocalWeatherListener aMapLocalWeatherListener = new AMapLocalWeatherListener() {

        @Override
        public void onWeatherLiveSearched(AMapLocalWeatherLive aMapLocalWeatherLive) {
            if (Constants.DEBUG) {
                LogUtil.i(Constants.TAG, "listener-->live()");
            }
            if (aMapLocalWeatherLive != null && aMapLocalWeatherLive.getAMapException().getErrorCode() == 0) {
                isStart = false;
                citys = aMapLocalWeatherLive.getCity();
                weathers = aMapLocalWeatherLive.getWeather();
                times = aMapLocalWeatherLive.getWindDir() + "风\n" + DateUtil.getNowTime(System.currentTimeMillis());
                temps = aMapLocalWeatherLive.getTemperature() + "°";
                Message message = new Message();
                message.what = 0;
                handler.sendMessage(message);
            } else {
                ToastUtil.show(WeatherActivity.this, aMapLocalWeatherLive.getAMapException().getErrorMessage());
            }
        }

        @Override
        public void onWeatherForecaseSearched(AMapLocalWeatherForecast aMapLocalWeatherForecast) {
            if (Constants.DEBUG) {
                LogUtil.i(Constants.TAG, "listener-->forecase()");
            }
            if (aMapLocalWeatherForecast != null && aMapLocalWeatherForecast.getAMapException().getErrorCode() == 0) {
                List<AMapLocalDayWeatherForecast> forecasts = aMapLocalWeatherForecast.getWeatherForecast();
                for (int i = 0; i < forecasts.size(); i++) {
                    AMapLocalDayWeatherForecast aMapLocalDayWeatherForecast = forecasts.get(i);
                    switch (i) {
                        case 0:
                            date[0] = DateUtil.convertToDate(aMapLocalDayWeatherForecast.getDate()) + "\n" + DateUtil.getWeek(aMapLocalDayWeatherForecast.getWeek());
                            image[0] = WeatherUtil.consertToWeather(aMapLocalDayWeatherForecast.getDayWeather());
                            weather[0] = aMapLocalDayWeatherForecast.getDayWeather() + "\n"
                                        +aMapLocalDayWeatherForecast.getDayTemp() + "°C~"
                                        +aMapLocalDayWeatherForecast.getNightTemp();
                            break;
                        case 1:
                            date[1] = DateUtil.convertToDate(aMapLocalDayWeatherForecast.getDate()) + "\n" + DateUtil.getWeek(aMapLocalDayWeatherForecast.getWeek());
                            image[1] = WeatherUtil.consertToWeather(aMapLocalDayWeatherForecast.getDayWeather());
                            weather[1] = aMapLocalDayWeatherForecast.getDayWeather() + "\n"
                                    +aMapLocalDayWeatherForecast.getDayTemp() + "°C~"
                                    +aMapLocalDayWeatherForecast.getNightTemp();
                            break;
                        case 2:
                            date[2] = DateUtil.convertToDate(aMapLocalDayWeatherForecast.getDate()) + "\n" + DateUtil.getWeek(aMapLocalDayWeatherForecast.getWeek());
                            image[2] = WeatherUtil.consertToWeather(aMapLocalDayWeatherForecast.getDayWeather());
                            weather[2] = aMapLocalDayWeatherForecast.getDayWeather() + "\n"
                                    +aMapLocalDayWeatherForecast.getDayTemp() + "°C~"
                                    +aMapLocalDayWeatherForecast.getNightTemp();
                            break;
                        default:
                            break;
                    }
                }
                isStart1 = false;
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
            } else {
                ToastUtil.show(WeatherActivity.this, aMapLocalWeatherForecast.getAMapException().getErrorMessage());
            }
        }
    };

    private AMapLocalWeatherListener aMapLocalWeatherListener1 = new AMapLocalWeatherListener() {

        @Override
        public void onWeatherLiveSearched(AMapLocalWeatherLive aMapLocalWeatherLive) {
            if (Constants.DEBUG) {
                LogUtil.i(Constants.TAG, "listener1-->live()");
            }
            if (aMapLocalWeatherLive != null && aMapLocalWeatherLive.getAMapException().getErrorCode() == 0) {
                isStart = false;
                citys = aMapLocalWeatherLive.getCity();
                weathers = aMapLocalWeatherLive.getWeather();
                times = aMapLocalWeatherLive.getWindDir() + "风\n" + DateUtil.getNowTime(System.currentTimeMillis());
                temps = aMapLocalWeatherLive.getTemperature() + "°";
                Message message = new Message();
                message.what = 0;
                handler.sendMessage(message);
            } else {
                ToastUtil.show(WeatherActivity.this, aMapLocalWeatherLive.getAMapException().getErrorMessage());
            }
        }

        @Override
        public void onWeatherForecaseSearched(AMapLocalWeatherForecast aMapLocalWeatherForecast) {
            if (Constants.DEBUG) {
                LogUtil.i(Constants.TAG, "listener1-->forecase()");
            }
            if (aMapLocalWeatherForecast != null && aMapLocalWeatherForecast.getAMapException().getErrorCode() == 0) {
                List<AMapLocalDayWeatherForecast> forecasts = aMapLocalWeatherForecast.getWeatherForecast();
                for (int i = 0; i < forecasts.size(); i++) {
                    AMapLocalDayWeatherForecast aMapLocalDayWeatherForecast = forecasts.get(i);
                    switch (i) {
                        case 0:
                            date[0] = DateUtil.convertToDate(aMapLocalDayWeatherForecast.getDate()) + "\n" + DateUtil.getWeek(aMapLocalDayWeatherForecast.getWeek());
                            image[0] = WeatherUtil.consertToWeather(aMapLocalDayWeatherForecast.getDayWeather());
                            weather[0] = aMapLocalDayWeatherForecast.getDayWeather() + "\n"
                                    +aMapLocalDayWeatherForecast.getDayTemp() + "~"
                                    +aMapLocalDayWeatherForecast.getNightTemp() + "°C";
                            break;
                        case 1:
                            date[1] = DateUtil.convertToDate(aMapLocalDayWeatherForecast.getDate()) + "\n" + DateUtil.getWeek(aMapLocalDayWeatherForecast.getWeek());
                            image[1] = WeatherUtil.consertToWeather(aMapLocalDayWeatherForecast.getDayWeather());
                            weather[1] = aMapLocalDayWeatherForecast.getDayWeather() + "\n"
                                    +aMapLocalDayWeatherForecast.getDayTemp() + "~"
                                    +aMapLocalDayWeatherForecast.getNightTemp() + "°C";
                            break;
                        case 2:
                            date[2] = DateUtil.convertToDate(aMapLocalDayWeatherForecast.getDate()) + "\n" + DateUtil.getWeek(aMapLocalDayWeatherForecast.getWeek());
                            image[2] = WeatherUtil.consertToWeather(aMapLocalDayWeatherForecast.getDayWeather());
                            weather[2] = aMapLocalDayWeatherForecast.getDayWeather() + "\n"
                                    +aMapLocalDayWeatherForecast.getDayTemp() + "~"
                                    +aMapLocalDayWeatherForecast.getNightTemp() + "°C";
                            break;
                        default:
                            break;
                    }
                }
                isStart1 = false;
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
            } else {
                ToastUtil.show(WeatherActivity.this, aMapLocalWeatherForecast.getAMapException().getErrorMessage());
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        ActionBarUtil.hideActionBar(this);

        init();

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (isStart) {
                    locationManagerProxy = LocationManagerProxy.getInstance(WeatherActivity.this);
                    locationManagerProxy.requestWeatherUpdates(LocationManagerProxy.WEATHER_TYPE_LIVE, aMapLocalWeatherListener);
                }
            }
        });
        thread.start();

        thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                if (isStart1) {
                    locationManagerProxy1 = LocationManagerProxy.getInstance(WeatherActivity.this);
                    locationManagerProxy1.requestWeatherUpdates(LocationManagerProxy.WEATHER_TYPE_FORECAST, aMapLocalWeatherListener1);
                }
            }
        });
        thread1.start();

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        tempTv.setText(temps);
                        cityTv.setText(citys);
                        weatherTv.setText(weathers);
                        timeTv.setText(times);
                        break;
                    case 1:
                        WeatherAdapter weatherAdapter = new WeatherAdapter(WeatherActivity.this, date, image, weather);
                        listView.setAdapter(weatherAdapter);
                        weatherImage.setImageResource(image[0]);
                        break;
                    default:
                        break;
                }
            }
        };
    }

    private void init() {
        mScreen = getResources().getDisplayMetrics().widthPixels;
        weatherTv = (TextView) findViewById(R.id.weather_tv);
        tempTv = (TextView) findViewById(R.id.du_tv);
        cityTv = (TextView) findViewById(R.id.city_tv);
        timeTv = (TextView) findViewById(R.id.time_tv);
        localTv = (TextView) findViewById(R.id.location_tv);
        dataTv = (TextView) findViewById(R.id.data_tv);
        settingTv = (TextView) findViewById(R.id.setting_tv);
        weatherImage = (ImageView) findViewById(R.id.weather_image);
        imageView1 = (ImageView) findViewById(R.id.image_1);
        imageView2 = (ImageView) findViewById(R.id.image_2);
        imageView3 = (ImageView) findViewById(R.id.image_3);
        imageView4 = (ImageView) findViewById(R.id.image_4);
        imageView5 = (ImageView) findViewById(R.id.image_5);
        openView = (ImageView) findViewById(R.id.open_view);
        refreshView = (ImageView) findViewById(R.id.refresh_view);
        listView = (ListView) findViewById(R.id.listview);
        slidingMenu = (SlidingMenu) findViewById(R.id.sliding_menu);

        localTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WeatherActivity.this, MyActivity.class);
                startActivity(intent);
            }
        });

        openView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slidingMenu.openOrCloseMenu();
            }
        });

        refreshView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimUtil.rotate(refreshView, 0f, 1080f);
            }
        });

        dataTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WeatherActivity.this, DataActivity.class);
                startActivity(intent);
            }
        });

        settingTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WeatherActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.i(Constants.TAG, mScreen + "");
        if (animatorSet == null) {
            animatorSet = AnimUtil.loadAnimation(imageView1, 10000, mScreen, 0f, 1f);
            animatorSet1 = AnimUtil.loadAnimation(imageView2, 12000, mScreen, 0f, 1f);
            animatorSet2 = AnimUtil.loadAnimation(imageView3, 13000, mScreen, 0f, 1f);
            animatorSet3 = AnimUtil.loadAnimation(imageView4, 14000, mScreen, 0f, 1f);
            animatorSet4 = AnimUtil.loadAnimation(imageView5, 15000, mScreen, 0f, 1f);
        } else if (!animatorSet.isRunning()) {
            animatorSet.start();
            animatorSet1.start();
            animatorSet2.start();
            animatorSet3.start();
            animatorSet4.start();
        }
    }

    @Override
    protected void onPause() {
        if (thread.isInterrupted()) {
            thread.interrupt();
        }
        if (thread1.isInterrupted()) {
            thread1.interrupt();
        }
        if (animatorSet.isRunning()) {
            animatorSet.cancel();
            animatorSet1.cancel();
            animatorSet2.cancel();
            animatorSet3.cancel();
            animatorSet4.cancel();
        }
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.weather, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}