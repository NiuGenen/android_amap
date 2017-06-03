package com.example.a60213.getyourlocation;

import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.Polyline;
import com.amap.api.maps2d.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 60213 on 2017/6/3.
 */

public class AmapTraceThread extends Thread {
    private Handler handler;

    private int time_interval = 5;
    private int count = time_interval;
    private AMapLocationClient client; // get location
    private double latitude;
    private double longitude;

    public void setup(Handler handler
            ,AMapLocationClient client,int time_interval,TextView count_down
    ){
        this.handler = handler;
        this.client = client;
        this.time_interval = time_interval;
    }

    public void setTimeInterval(int t){
        time_interval = t;
    }

    private boolean running = true;
    public boolean isRunning(){
        return running;
    }
    public void stop_running(){
        running = false;
    }

    public AMapLocation getLastKnownLocation(){
        AMapLocation location = client.getLastKnownLocation();
        if(location.getErrorCode() == 0) {
            return location;
        }
        return null;
    }

    public void markWhereYouAre(){
        AMapLocation location = getLastKnownLocation();
        if(location != null) {
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude() );
            final Marker marker = aMap.addMarker(new MarkerOptions().position(latLng).title("position").snippet("DefaultMarker"));
            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
        }
    }

    @Override
    public void run(){
        while(running){
            try{
                AMapLocation location = client.getLastKnownLocation();
                if(location.getErrorCode() == 0){
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    final StringBuffer location_str = new StringBuffer();
                    location_str.append( location.getCountry() );
                    location_str.append( location.getProvince() );
                    location_str.append( location.getCity() );
                    location_str.append( location.getStreet() );
                    try {
                        boolean success = AppServer.getInstance().PointTrack(
                                latitude,longitude,location_str.toString() );
                        Log.d("PointTrack", latitude + longitude + location_str.toString());
                        if(is_real_time_on)
                            onPathTrack(latitude,longitude);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else{
                    Log.d("PointTrack","location.Error");
                }
                count = time_interval;
                while( count-- > 0) { Thread.sleep(1000);}
                if(is_real_time_on) {
                    last_latitude = latitude;
                    last_longitude = longitude;
                }
            }catch(Exception e){
                Log.d("PointTrack","location.Exception");
                e.printStackTrace();
            }
        }
    }

    private double last_latitude;
    private double last_longitude;
    private AMap aMap = null;
    private Handler aMapHandler = null;
    private boolean setted = false;
    private boolean is_real_time_on = false;
    public void onPathTrack(double latitude, double longitude){
        if(aMap != null && last_latitude != -1){
            final LatLng latLng = new LatLng(latitude,longitude);
            final List<LatLng> latLngs = new ArrayList<LatLng>();
            latLngs.add(new LatLng(last_latitude,last_longitude));
            latLngs.add( latLng );
            System.out.println("[Line][s]"+last_latitude+" "+last_longitude+"[e]"+latitude +" "+longitude);
            if(last_latitude != latitude || last_longitude != longitude) {  //make sense
                aMapHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Polyline polyline = aMap.addPolyline(new PolylineOptions().
                                addAll(latLngs).width(2).color(Color.argb(255, 1, 1, 1)));
                    }
                });
            }
            aMapHandler.post(new Runnable() {
                @Override
                public void run() {
                    aMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
                }
            });
        }
    }
    public void set_path_track(Handler handler,AMap aMap){
        if(handler==null || aMap==null){
            setted = false;
            return;
        }
        this.aMapHandler = handler;
        this.aMap = aMap;
        setted = true;
    }
    public void start_path_track(){
        if(setted)
            is_real_time_on = true;
    }
    public void stop_path_track(){
        is_real_time_on = false;
    }

}
