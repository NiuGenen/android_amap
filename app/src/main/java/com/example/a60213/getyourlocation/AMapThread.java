package com.example.a60213.getyourlocation;

import android.os.Handler;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;

import java.net.URLEncoder;

/**
 * Created by 60213 on 2017/5/7.
 */

public class AMapThread extends Thread {

    private AMapLocationClient mLocationClient;

    private TextView text_latitude;
    private TextView text_longitude;

    private Handler handler;

    private int time_interval = 1;
    private TextView count_down;
    private Integer count = 10;

    private TextView info;
    private TextView msg;

    private int userid;

    public void setup(Handler handler,AMapLocationClient client, TextView la, TextView lo,
                      int time_interval,TextView count_down,
                      TextView info,TextView msg,
                      int userid){
        this.handler = handler;
        this.mLocationClient = client;
        this.text_latitude = la;
        this.text_longitude = lo;
        this.time_interval = time_interval;
        this.count_down = count_down;
        this.count = this.time_interval;
        this.info = info;
        this.msg = msg;
        this.userid = userid;
    }

    private Boolean running = true;

    public void stop_thread(){
        synchronized (count){
            count = -1;
        }
        synchronized (running) {
            running = false;
        }
    }

    private String ret;
    @Override
    public void run(){
        while( running ){
            try{
                AMapLocation location = mLocationClient.getLastKnownLocation();

                if(location.getErrorCode() == 0){
                    final double latitude = location.getLatitude();
                    final double longitude = location.getLongitude();
                    final StringBuffer location_str = new StringBuffer();
                    location_str.append( location.getCountry() );
                    location_str.append( location.getProvince() );
                    location_str.append( location.getCity() );
                    location_str.append( location.getStreet() );
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            synchronized (text_longitude){
                                text_longitude.setText( Double.toString(longitude) );
                            }
                            synchronized (text_latitude){
                                text_latitude.setText( Double.toString(latitude) );
                            }
                            synchronized (info){
                                info.setText(location_str);
                            }
                        }
                    });
                    try {
                        ret = Util_http.sendPost(
                                "http://niugenen.6655.la:60000/PersonalTrack/PointTrackServlet",
                                        "test=True&userid=" + userid +
                                        "&point={'latitude':" + latitude +
                                                ",'longitude':" + longitude +
                                        ",'location':'" + URLEncoder.encode( location_str.toString(),"utf-8") +
                                        "'}&timestramp=" + Long.toString(System.currentTimeMillis()));
                        //System.out.println("[Http reponse] " + ret);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                synchronized (msg) {
                                    msg.setText(ret);
                                }
                            }
                        });
                    } catch (Exception e) {
                        //System.out.println("Fail to send http message.");
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                synchronized (msg) {
                                    msg.setText("Fail to send http message.");
                                }
                            }
                        });
                        e.printStackTrace();
                    }
                }
                else{//failed
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            synchronized (text_longitude){
                                text_longitude.setText( "fail to locate in amap thread" );
                            }
                            synchronized (text_latitude){
                                text_latitude.setText("fail to locate in amap thread" );
                            }
                        }
                    });
                }
                //count down with time_interval
                count = time_interval;
                while(count >= 0) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            count_down.setText(Integer.toString(count));
                        }
                    });
                    Thread.sleep(1000);
                    count--;
                }
            }catch(Exception e){
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        synchronized (msg) {
                            msg.setText("Exception in amap thread.");
                        }
                    }
                });
                e.printStackTrace();
            }
        }
    }
}
