package com.example.a60213.getyourlocation;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.location.LocationManager;

import com.amap.api.location.*;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.fence.*;

import org.w3c.dom.Text;

import java.security.Permission;

public class Main2Activity extends AppCompatActivity {

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (bgthread != null)
            bgthread.stop_running();
        if(amapthread != null)
            amapthread.stop_thread();
        //do something
    }

    private LocationThread bgthread;
    private AMapThread amapthread;

    private String str1 = "asd";
    private String str2 = "qwe";

    private LocationManager locationManager;
    private int count = 3;

    private int count_fail = 1;

    private MyLocationListener my_location_listener;

    public void onStartGPSClick(View v) {
        try {
            //failed to use google location API because I am in China
            //bgthread = new LocationThread();
            //bgthread.setup(handler,count_down,edittext_latitude,edittext_longitude,locationManager);
            //bgthread.start();

            amapthread = new AMapThread();
            amapthread.setup(handler,mLocationClient,
                    edittext_latitude,edittext_longitude,
                    3,count_down,3);
            amapthread.start();

            //new Thread(new Runnable() {
            //    @Override
            //    public void run() {
            /*        try {
                        my_location_listener = new MyLocationListener();
                        my_location_listener.setup(
                            handler,edittext_latitude,edittext_longitude
                        );
                        final Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        while(location == null) {
                            locationManager.requestLocationUpdates(
                                    LocationManager.GPS_PROVIDER,
                                    60000,
                                    10,
                                    my_location_listener
                            );
                        }

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                synchronized (edittext_latitude){
                                    edittext_latitude.setText( Double.toString(location.getLatitude()) );
                                }
                                synchronized (edittext_longitude){
                                    edittext_longitude.setText( Double.toString(location.getLongitude()) );
                                }
                            }
                        });

                    }catch(SecurityException e){
                        e.printStackTrace();
                    }
            //    }
            //}).start();;
*/
            ((Button)findViewById(R.id.button_2_start_gps)).setClickable(false);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private TextView edittext_latitude;
    private TextView edittext_longitude;
    private Handler handler;

    private TextView count_down;

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener = null;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        handler = new Handler();

        edittext_latitude = (TextView)findViewById(R.id.edittext_2_latitude);
        edittext_longitude = (TextView)findViewById(R.id.edittext_2_longitude);

        count_down = (TextView)findViewById(R.id.text_2_count_down);

        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        /*mLocationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if(aMapLocation != null){
                    if(aMapLocation.getErrorCode() == 0){//success
                        final double latitude = aMapLocation.getLatitude();
                        final double longitude = aMapLocation.getLongitude();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                synchronized (edittext_longitude){
                                    edittext_longitude.setText(Double.toString(longitude));
                                }
                                synchronized (edittext_latitude){
                                    edittext_latitude.setText(Double.toString(longitude));
                                }
                            }
                        });
                    }
                    else{//fail
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                synchronized (edittext_longitude){
                                    edittext_longitude.setText("failed to get by amp");
                                }
                                synchronized (edittext_latitude){
                                    edittext_latitude.setText("failed to get by amp");
                                }
                            }
                        });
                    }
                }
            }
        };*/
        //设置定位回调监听
        //mLocationClient.setLocationListener(mLocationListener);


        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();

        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
        //设置定位模式为AMapLocationMode.Battery_Saving，低功耗模式。
        //mLocationOption.setLocationMode(AMapLocationMode.Battery_Saving);
        //设置定位模式为AMapLocationMode.Device_Sensors，仅设备模式。
        //mLocationOption.setLocationMode(AMapLocationMode.Device_Sensors);

        //获取一次定位结果：
        //该方法默认为false。
        mLocationOption.setOnceLocation(true);
        //获取最近3s内精度最高的一次定位结果：
        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
        mLocationOption.setOnceLocationLatest(true);
        //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
        //mLocationOption.setInterval(1000);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否强制刷新WIFI，默认为true，强制刷新。
        mLocationOption.setWifiActiveScan(false);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        //mLocationOption.setMockEnable(false);
        //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mLocationOption.setHttpTimeOut(20000);
        //关闭缓存机制 default open
        mLocationOption.setLocationCacheEnable(false);

        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
