package com.weixiaokang.rescueteam.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Environment;

@SuppressLint("SimpleDateFormat")
public final class StorageHelper
{
    public static final int MEDIA_TYPE_IMAGE=0;
    public static final int MEDIA_TYPE_VIDEO=1;

    public static Uri  getOutputUri(File mFile)
    {
        return Uri.fromFile(mFile);
    }


    public static File getOutputFile(int mType)
    {
        File mMediaFileDir=new File(Environment.getExternalStorageDirectory(),"OpenCamera");
        if(!mMediaFileDir.exists())
        {
            if(!mMediaFileDir.mkdir())
            {
                return null;
            }
        }
        File mMediaFile=null;
        /**  创建文件名   **/
        String mFileName=new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        switch(mType)
        {
            case MEDIA_TYPE_IMAGE:
                mMediaFile=new File(mMediaFileDir.getPath()+File.separator+"IMG_"+mFileName+".jpg");
                break;
            case MEDIA_TYPE_VIDEO:
                mMediaFile=new File(mMediaFileDir.getPath()+File.separator+"VID_"+mFileName+".mp4");
                break;
            default:
                mMediaFile=null;
        }
        return mMediaFile;
    }
}