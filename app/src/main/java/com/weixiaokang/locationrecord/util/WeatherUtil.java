package com.weixiaokang.locationrecord.util;

public class WeatherUtil {

    public static int consertToWeather(String weather) {
        if (weather.equals("晴")) {
            return Constants.QING;
        }
        if (weather.contains("晴") && weather.contains("云")) {
            return Constants.DOUYUZHUANQIN;
        }
        if (weather.contains("雨夹雪")) {
            return Constants.YUJIAXUE;
        }
        if (weather.contains("小雨")) {
            return Constants.XIAOYU;
        }
        if (weather.contains("中雨") || weather.contains("雷阵雨")) {
            return Constants.ZHONGYU;
        }
        if (weather.contains("大雨") || weather.contains("暴雨")) {
            return Constants.DAYU;
        }
        if (weather.contains("小雪")) {
            return Constants.XIAOXUE;
        }
        if (weather.contains("中雪")) {
            return Constants.ZHONGXUE;
        }
        if (weather.contains("大雪") || weather.contains("暴雪")) {
            return Constants.DAXUE;
        }
        if (weather.contains("雷阵雨")) {
            return Constants.LEIZHENGYU;
        }
        return Constants.YIN;
    }
}