package com.example.a60213.getyourlocation;

/**
 * Created by 60213 on 2017/6/3.
 */

public class DumpPoint {
    private double longitude;
    private double latitude;
    private String location;
    private long time;
    public DumpPoint(){}
    public DumpPoint(double latitude,double longitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }
    public double getLongitude(){
        return longitude;
    }
    public void setLongitude(double longitude){
        this.longitude = longitude;
    }
    public double getLatitude(){
        return this.latitude;
    }
    public void setLatitude(double latitude){
        this.latitude = latitude;
    }
    public String getLocation(){
        return location;
    }
    public void setLocation(String location){
        this.location = location;
    }
    public long getTime(){
        return time;
    }
    public void setTime(long time){
        this.time = time;
    }
}
