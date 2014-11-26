package com.weixiaokang.rescueteam.service;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;

public class LocationService extends Service {
    private AMapLocation mLocation;
    private LocationManagerProxy mManager;
    private LocationBinder mBind = new LocationBinder();
    private AMapLocationListener mListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if(aMapLocation != null && aMapLocation.getAMapException().getErrorCode() == 0){
                mLocation = aMapLocation;
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
    };
    public LocationService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mManager = LocationManagerProxy.getInstance(getApplicationContext());
        mManager.requestLocationData(LocationProviderProxy.AMapNetwork, 1000, 1, mListener);
        mManager.setGpsEnable(false);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showLocationMassege();
            }
        }, 12000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mManager != null) {
            mManager.removeUpdates(mListener);
            mManager.destroy();
        }
        mManager = null;
    }

    private void showLocationMassege() {
        if (mLocation == null) {
            Toast.makeText(this, "locate fail", Toast.LENGTH_LONG).show();
            mManager = null;
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        return mBind;
    }

    public class LocationBinder extends Binder {
        public LocationService getLocationService() {
            return LocationService.this;
        }
    }

    public Location getLocation() {
        mLocation = mManager.getLastKnownLocation(LocationProviderProxy.AMapNetwork);
        return mLocation;
    }
}
