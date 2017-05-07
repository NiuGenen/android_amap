package com.example.a60213.getyourlocation;

import android.os.Handler;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;

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
    private int count = 3;
    private int count_init = 3;

    public void setup(Handler handler,AMapLocationClient client, TextView la, TextView lo,
                      int time_interval,TextView count_down,int count_init){
        this.handler = handler;
        this.mLocationClient = client;
        this.text_latitude = la;
        this.text_longitude = lo;
        this.time_interval = time_interval;
        this.count_down = count_down;
        this.count_init = count_init;
        this.count = this.count_init;
    }

    private boolean running = true;

    public void stop_thread(){
        running = false;
    }

    @Override
    public void run(){
        while( running ){
            try{
                AMapLocation location = mLocationClient.getLastKnownLocation();

                if(location.getErrorCode() == 0){
                    final double latitude = location.getLatitude();
                    final double longitude = location.getLongitude();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            synchronized (text_longitude){
                                text_longitude.setText( Double.toString(longitude) );
                            }
                            synchronized (text_latitude){
                                text_latitude.setText( Double.toString(latitude) );
                            }
                        }
                    });
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
                count = count_init;
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
