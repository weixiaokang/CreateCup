package com.weixiaokang.rescueteam.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.util.Log;


public class MyCamera {
    /** 官方建议的安全地访问摄像头的方法  **/
    public static Camera getCameraInstance(int type){
        Camera c = null;
        try {
            c = Camera.open(type);
            Log.i("heheda", "-->open");
        }
        catch (Exception e){
            Log.i("heheda", "can open camera");
            Log.d("TAG", "Error is " + e.getMessage());
        }
        return c;
    }

    /** 检查设备是否支持摄像头  **/
    public static boolean checkCameraHardware(Context mContext)
    {
        if (mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA))
        {
            // 摄像头存在
            return true;
        } else {
            // 摄像头不存在
            return false;
        }
    }

}
