package com.weixiaokang.locationrecord;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.weixiaokang.locationrecord.util.Constants;
import com.weixiaokang.locationrecord.util.LogUtil;

import java.util.ArrayList;
import java.util.Map;

public class MyActivity extends Activity implements LocationSource, AMapLocationListener{

    private MapView mapView;
    private AMap aMap;
    private LocationManagerProxy locationManagerProxy;
    private AMapLocation aMapLocation;
    private Marker radar, marker;
    private OnLocationChangedListener onLocationChangedListener;
    private boolean isInit = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mapView = (MapView) findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);
        init();
        setActionBarOptions();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
        }
        setRadar();
        setAMap();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            double a = bundle.getDouble("longtitude");
            double b = bundle.getDouble("latitude");
            LogUtil.i(Constants.DATA, " "+ a + " " + b);
            if (a != 0 && b != 0) {
                marker = aMap.addMarker(new MarkerOptions()
                        .anchor(0.5f, 0.5f)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                        .position(new LatLng(b, a)));
                CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(b, a)).tilt(0).zoom(16).build();
                aMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        }
    }

    private void setAMap() {
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.location_marker));
        myLocationStyle.strokeWidth(0.1f);
        myLocationStyle.strokeColor(Color.BLACK);
        myLocationStyle.radiusFillColor(Color.argb(100, 0, 0, 128));
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setMyLocationRotateAngle(180);
        aMap.setLocationSource(this);

        UiSettings uiSettings = aMap.getUiSettings();
        uiSettings.setScaleControlsEnabled(true);
        uiSettings.setMyLocationButtonEnabled(true);

        aMap.setMyLocationEnabled(true);
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
    }
    private void setRadar() {
        ArrayList<BitmapDescriptor> list = new ArrayList<BitmapDescriptor>();
        list.add(BitmapDescriptorFactory.fromResource(R.drawable.point1));
        list.add(BitmapDescriptorFactory.fromResource(R.drawable.point2));
        list.add(BitmapDescriptorFactory.fromResource(R.drawable.point3));
        list.add(BitmapDescriptorFactory.fromResource(R.drawable.point4));
        list.add(BitmapDescriptorFactory.fromResource(R.drawable.point5));
        list.add(BitmapDescriptorFactory.fromResource(R.drawable.point6));
        radar = aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f).icons(list).period(50));
    }

    private void setActionBarOptions() {
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM, ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.action_bar);
        }
    }

    private void initMyLocation(AMapLocation aMapLocation) {
        LatLng initLatLng = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
        CameraPosition cameraPosition = new CameraPosition.Builder().target(initLatLng).tilt(0).zoom(16).build();
        aMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        isInit = false;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_settings);
        item.setVisible(false);
        return super.onPrepareOptionsMenu(menu);
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

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (onLocationChangedListener != null && aMapLocation != null) {
            if (aMapLocation.getAMapException().getErrorCode() == 0) {
                onLocationChangedListener.onLocationChanged(aMapLocation);
                radar.setPosition(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude()));
            }
            if (isInit) {
                initMyLocation(aMapLocation);
            }
            float bearing = aMapLocation.getBearing();
            aMap.setMyLocationRotateAngle(bearing);
        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        this.onLocationChangedListener = onLocationChangedListener;
        if (locationManagerProxy == null) {
            locationManagerProxy = LocationManagerProxy.getInstance(this);
            locationManagerProxy.requestLocationData(LocationProviderProxy.AMapNetwork, 60 * 1000, 10, this);
        }
    }

    @Override
    public void deactivate() {
        onLocationChangedListener = null;
        if (locationManagerProxy != null) {
            locationManagerProxy.removeUpdates(this);
            locationManagerProxy.destroy();
        }
        locationManagerProxy = null;
    }
}