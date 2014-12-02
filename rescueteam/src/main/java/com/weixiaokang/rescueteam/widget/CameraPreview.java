package com.weixiaokang.rescueteam.widget;

import java.io.IOException;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/** 一个基本的相机预览界面类    **/
@SuppressLint("ViewConstructor")
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

    /** Camera **/
    private Camera mCamera;
    /** SurfaceHolder **/
    private SurfaceHolder mHolder;

    /** CamreaPreview构造函数   **/
    @SuppressWarnings("deprecation")
    public CameraPreview(Context mContext,Camera mCamera)
    {
        super(mContext);
        this.mCamera=mCamera;
        // 安装一个SurfaceHolder.Callback，
        // 这样创建和销毁底层surface时能够获得通知。
        mHolder = getHolder();
        mHolder.addCallback(this);
        // 已过期的设置，但版本低于3.0的Android还需要
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3)
    {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
            mCamera.setDisplayOrientation(90);
        } catch (IOException e) {
            Log.d("TAG", "Error is "+e.getMessage());
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder arg0)
    {
        // 如果预览无法更改或旋转，注意此处的事件
        // 确保在缩放或重排时停止预览
        if (mHolder.getSurface() == null){
            // 预览surface不存在
            return;
        }
        // 更改时停止预览
        try {
            mCamera.stopPreview();
        } catch (Exception e){
            // 忽略：试图停止不存在的预览
        }
        // 在此进行缩放、旋转和重新组织格式
        // 以新的设置启动预
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.setDisplayOrientation(90);
            mCamera.startPreview();
        } catch (Exception e){
            Log.d("TAG", "Error is " + e.getMessage());
        }

    }

}