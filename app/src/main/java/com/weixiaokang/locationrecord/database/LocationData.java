package com.weixiaokang.locationrecord.database;

public class LocationData {

    private int ID;
    private int num;
    private String city;
    private String time;
    private String longitude;
    private String latitude;

    public LocationData() {}
    public LocationData(int num, String time, String longitude, String latitude) {
        this.num = num;
        this.time = time;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public int getID() {
        return ID;
    }

    public int getNum() {
        return num;
    }

    public String getTime() {
        return time;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
}
