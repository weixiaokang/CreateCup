package com.weixiaokang.locationrecord;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviPath;
import com.amap.api.navi.model.NaviLatLng;
import com.amap.api.navi.view.RouteOverLay;
import com.weixiaokang.locationrecord.util.ActionBarUtil;
import com.weixiaokang.locationrecord.util.LogUtil;

import java.util.ArrayList;
import java.util.List;


public class NaviActivity extends ActionBarActivity implements AMap.OnMapLoadedListener, LocationSource {

    private AMap mAmap;
    private AMapNavi mAmapNavi;
    private MapView mMapView;
    private Marker mStartMarker, mEndMarker, mGPSMarker, mRaderMarker;
    private AMapNaviListener mAmapNaviListener;
    private List<NaviLatLng> mStartPoints = new ArrayList<NaviLatLng>();
    private List<NaviLatLng> mWayPoints = new ArrayList<NaviLatLng>();
    private List<NaviLatLng> mEndPoints = new ArrayList<NaviLatLng>();
    private NaviLatLng mStartPoint = new NaviLatLng();
    private NaviLatLng mEndPoint = new NaviLatLng();
    private boolean mIsGetGPS = false;// 记录GPS定位是否成功
    private boolean isNaviSuccess = false;
    private boolean mIsMapLoaded = false;
    private final static int GPSNO = 0;
    private final static int CALCULATEERROR = 1;// 启动路径计算失败状态
    private final static int CALCULATESUCCESS = 2;// 启动路径计算成功状态
    private Button naviBut;
    private TextView dis_tv, time_tv;
    private RouteOverLay mRouteOverLay;
    private double lat, lng;
    private LocationManagerProxy mLocationManger;

    private AMapLocationListener mLocationListener = new AMapLocationListener() {

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onLocationChanged(Location location) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onLocationChanged(AMapLocation location) {
            LogUtil.i("heheda", "-->onLocationChanged");
            if (location != null && location.getAMapException().getErrorCode() == 0) {
                mIsGetGPS = true;
                mStartPoint = new NaviLatLng(location.getLatitude(), location.getLongitude());
                mGPSMarker.setPosition(new LatLng(
                        mStartPoint.getLatitude(), mStartPoint
                        .getLongitude()));
                mStartPoints.clear();
                mStartPoints.add(mStartPoint);
                float bearing = location.getBearing();
                mAmap.setMyLocationRotateAngle(bearing);
                calculateRoute();
            } else {
                Toast.makeText(NaviActivity.this, "定位出现问题", Toast.LENGTH_LONG).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navi);
        ActionBarUtil.hideActionBar(this);
        mMapView = (MapView) findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        initView();
        initListener();
        setAMap();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            lat = bundle.getDouble("latitude");
            lng = bundle.getDouble("longtitude");
            CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(lat, lng)).tilt(0).zoom(16).build();
            mAmap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
        mEndPoint = new NaviLatLng(lat, lng);
        mEndMarker.setPosition(new LatLng(lat, lng));
        mEndPoints.clear();
        mEndPoints.add(mEndPoint);
    }

    private void setAMap() {
        LogUtil.i("heheda", "-->setAMap");
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                .decodeResource(getResources(), R.drawable.location_marker)));
        myLocationStyle.strokeWidth(0.1f);
        myLocationStyle.strokeColor(Color.BLACK);
        myLocationStyle.radiusFillColor(Color.argb(100, 0, 0, 128));
        mAmap.setMyLocationStyle(myLocationStyle);
        mAmap.setMyLocationRotateAngle(180);
        mAmap.setLocationSource(this);

        UiSettings uiSettings = mAmap.getUiSettings();
        uiSettings.setScaleControlsEnabled(true);
        uiSettings.setMyLocationButtonEnabled(true);
        uiSettings.setZoomControlsEnabled(false);
        mAmap.setMyLocationEnabled(true);
        mAmap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
    }

    private void initView() {
        naviBut = (Button) findViewById(R.id.navi_but);
        dis_tv = (TextView) findViewById(R.id.distance_tv);
        time_tv = (TextView) findViewById(R.id.time_tv);
        mAmap = mMapView.getMap();
        mAmapNavi = AMapNavi.getInstance(this);
        mRouteOverLay = new RouteOverLay(mAmap, null);
        mStartMarker = mAmap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                        .decodeResource(getResources(), R.drawable.start))));
        mEndMarker = mAmap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                        .decodeResource(getResources(), R.drawable.end))));
        mGPSMarker = mAmap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                .decodeResource(getResources(), R.drawable.location_marker))));
        ArrayList<BitmapDescriptor> list = new ArrayList<BitmapDescriptor>();
        list.add(BitmapDescriptorFactory.fromResource(R.drawable.point1));
        list.add(BitmapDescriptorFactory.fromResource(R.drawable.point2));
        list.add(BitmapDescriptorFactory.fromResource(R.drawable.point3));
        list.add(BitmapDescriptorFactory.fromResource(R.drawable.point4));
        list.add(BitmapDescriptorFactory.fromResource(R.drawable.point5));
        list.add(BitmapDescriptorFactory.fromResource(R.drawable.point6));
        mRaderMarker = mAmap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f).icons(list).period(50));
    }

    private void initListener() {
        mAmap.setOnMapLoadedListener(this);
        naviBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isNaviSuccess) {
                    Toast.makeText(NaviActivity.this, "导航失败", Toast.LENGTH_LONG).show();
                } else {
                    AMapNaviPath mAapNaviPath = mAmapNavi.getNaviPath();
                    mRouteOverLay.setRouteInfo(mAapNaviPath);
                    mRouteOverLay.addToMap();
                    if (mIsMapLoaded) {
                        mRouteOverLay.zoomToSpan();
                    }
                    String dis = dis_tv.getText().toString() + String.valueOf(mAapNaviPath.getAllLength());
                    String time = time_tv.getText().toString() + String.valueOf(mAapNaviPath.getAllTime());
                    dis_tv.setText(dis);
                    time_tv.setText(time);

                }
            }
        });
    }
    private void calculateRoute() {
        LogUtil.i("heheda", "-->calculateRoute");
        if (!mIsGetGPS) {
            mLocationManger = LocationManagerProxy.getInstance(this);
            mLocationManger.requestLocationData(LocationProviderProxy.AMapNetwork, -1, 15, mLocationListener);
        }
        mIsGetGPS = false;
        int driverIndex = calculateDriverRoute();
        AMapNaviPath mAmapNaviPath = mAmapNavi.getNaviPath();
        LogUtil.i("heheda", "start:"+mAmapNaviPath.getStartPoint().getLatitude()
                +" "+mAmapNaviPath.getStartPoint().getLongitude()
                +"\n"+mAmapNaviPath.getEndPoint().getLatitude()
                +" "+mAmapNaviPath.getEndPoint().getLongitude());
        if (driverIndex == CALCULATEERROR) {
            Toast.makeText(NaviActivity.this, "路线规划失败，请检查参数", Toast.LENGTH_LONG).show();
        } else if (driverIndex == GPSNO) {
        }
    }

    private int calculateDriverRoute() {
        LogUtil.i("heheda", "-->calculateDriverRoute");
        int driveMode = AMapNavi.DrivingFastestTime;
        int code = CALCULATEERROR;
        if (mAmapNavi.calculateDriveRoute(mStartPoints, mEndPoints,
                mWayPoints, driveMode)) {
            code = CALCULATESUCCESS;
        } else {
            code = CALCULATEERROR;
        }
        return code;
    }

    private AMapNaviListener getAMapNaviListener() {
        LogUtil.i("heheda", "-->getAMapNaviListener");
        if (mAmapNaviListener == null) {

            mAmapNaviListener = new AMapNaviListener() {

                @Override
                public void onTrafficStatusUpdate() {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onStartNavi(int arg0) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onReCalculateRouteForYaw() {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onReCalculateRouteForTrafficJam() {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onLocationChange(AMapNaviLocation location) {


                }

                @Override
                public void onInitNaviSuccess() {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onInitNaviFailure() {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onGetNavigationText(int arg0, String arg1) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onEndEmulatorNavi() {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onCalculateRouteSuccess() {
                    isNaviSuccess = true;
                }

                @Override
                public void onCalculateRouteFailure(int arg0) {
                }

                @Override
                public void onArrivedWayPoint(int arg0) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onArriveDestination() {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onGpsOpenStatus(boolean arg0) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onNaviInfoUpdated(AMapNaviInfo arg0) {
                    // TODO Auto-generated method stub

                }
            };
        }
        return mAmapNaviListener;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
        mLocationManger = LocationManagerProxy.getInstance(this);
        mLocationManger.requestLocationData(LocationProviderProxy.AMapNetwork, -1, 15, mLocationListener);
        AMapNavi.getInstance(this).setAMapNaviListener(getAMapNaviListener());
        setAMap();
        if (mAmapNavi.startGPS()) {
            LogUtil.i("heheda", "-->startGPS");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
        AMapNavi.getInstance(this)
                .removeAMapNaviListener(getAMapNaviListener());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_navi, menu);
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

    @Override
    public void onMapLoaded() {
        mIsMapLoaded = true;
        if (mRouteOverLay != null) {
            mRouteOverLay.zoomToSpan();

        }
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        LogUtil.i("heheda", "-->activate");
        if (mLocationManger == null) {
            mLocationManger = LocationManagerProxy.getInstance(this);
            mLocationManger.requestLocationData(LocationProviderProxy.AMapNetwork, 60 * 1000, 10, mLocationListener);
        }
    }

    @Override
    public void deactivate() {
        LogUtil.i("heheda", "-->deactivate");
        if (mLocationManger != null) {
            mLocationManger.removeUpdates(mLocationListener);
            mLocationManger.destroy();
        }
        mLocationManger = null;
    }
}
