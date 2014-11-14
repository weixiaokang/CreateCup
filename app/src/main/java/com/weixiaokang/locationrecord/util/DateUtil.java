package com.weixiaokang.locationrecord.util;


import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    public static String convertToDate(String date) {
        String s = date.substring(5, date.length());
        String m = s.replaceFirst("-", "月");
        String d = m + "日";
        return d;
    }

    public static String getNowTime(long time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        Date date = new Date(time);
        return simpleDateFormat.format(date);
    }

    public static String getWeek(String s) {
        LogUtil.i(Constants.TEST, s);
        int a = Integer.parseInt(s);
        switch (a) {
            case 1:
                return "星期一";
            case 2:
                return "星期二";
            case 3:
                return "星期三";
            case 4:
                return "星期四";
            case 5:
                return "星期五";
            case 6:
                return "星期六";
            case 7:
            default:
                return "星期天";
        }
    }

    public static String convertToTime(long a) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd,HH:mm:ss");
        Date date = new Date(a);
        return simpleDateFormat.format(date);
    }
}