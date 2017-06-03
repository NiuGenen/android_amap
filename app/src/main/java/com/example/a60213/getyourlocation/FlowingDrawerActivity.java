package com.example.a60213.getyourlocation;

import android.app.Fragment;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.Polyline;
import com.amap.api.maps2d.model.PolylineOptions;
import com.amap.api.maps2d.model.Text;
import com.mxn.soul.flowingdrawer_core.ElasticDrawer;
import com.mxn.soul.flowingdrawer_core.FlowingDrawer;
import com.mxn.soul.flowingdrawer_core.FlowingMenuLayout;

import android.app.FragmentTransaction;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class FlowingDrawerActivity extends AppCompatActivity
        implements DatePickerDialogFragment.DatePickerDialogImpl,
        TimeLengthPickerDialogFragment.TimeLengthPickerDialogImpl
{
    private Handler handler = new Handler();

    // map view
    private View page_map_view;
    private MapView mMapView;
    public void onMainMapClick(View view){//获取地图控件引用
        if(page_map_view==null){
            page_map_view = getLayoutInflater().inflate(
                    R.layout.activity_flowing_drawer_map,mViewGroup);
        }
        setContentView( page_map_view );
        mDrawer = (FlowingDrawer) findViewById(R.id.drawerlayout);
        mDrawer.closeMenu(true);
        if(mMapView == null) {
            mMapView = (MapView) findViewById(R.id.map);
            //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
            mMapView.onCreate(savedInstanceState);
        }
    }
    private DatePickerDialog.OnDateSetListener DatePickerDIalogListener;
    @Override
    public DatePickerDialog.OnDateSetListener getDatePickerDialogListener(){
        if(DatePickerDIalogListener == null) {
            DatePickerDIalogListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    map_date.setText(year + "-" + (monthOfYear+1) + "-" + dayOfMonth);
                    map_date_button.setBackground( ContextCompat.getDrawable(getBaseContext(),R.color.lightskyblue) );
                    map_date_button.setTextColor( ContextCompat.getColor(getBaseContext(),R.color.white) );
                    onMapShowTrace(year,monthOfYear,dayOfMonth);
                }
            };
        }
        return DatePickerDIalogListener;
    }
    private TextView map_date;
    private Button map_date_button;
    public void onMapDateChooseClick(View view){
        if(map_date==null){
            map_date = (TextView)findViewById(R.id.main_path_date_choose_text);
        }
        if(map_date_button==null){
            map_date_button = (Button)findViewById(R.id.main_path_date_choose_button);
        }
        handler.post(new Runnable() {//show date picker dialog
            @Override
            public void run() {
                map_date_button.setBackground( ContextCompat.getDrawable(getBaseContext(),R.color.white) );
                map_date_button.setTextColor( ContextCompat.getColor(getBaseContext(),R.color.lightskyblue) );
                FragmentTransaction mFragTransaction = getFragmentManager().beginTransaction();
                Fragment fragment =  getFragmentManager().findFragmentByTag("DatePickerDialog");
                if(fragment != null){
                    mFragTransaction.remove(fragment);
                }
                DatePickerDialogFragment datePickerDialogFragment = new DatePickerDialogFragment();
                datePickerDialogFragment.show(mFragTransaction, "DatePickerDialog");
            }
        });
    }
    private int y=0,m=0,d=0;
    private double la = -1,lo = -1,lst_la = -1,lst_lo = -1;
    public void onMapShowTrace(final int y,final int m,final int d){
        if(this.y==y && this.m==m && this.d==d) return;
        this.y=y;this.m=m;this.d=d;
        new Thread(new Runnable() {
            @Override
            public void run() {
                Calendar time = Calendar.getInstance();
                time.set(y,m,d);
                Long start = time.getTimeInMillis();
                time.set(y,m,d+1);
                Long end = time.getTimeInMillis();
                final List<DumpPoint> points = AppServer.getInstance().UserPath(start, end);
                if(points != null) handler.post(new Runnable() {
                    @Override
                    public void run() {
                        AMap aMap = mMapView.getMap();
                        aMap.clear();
                        LatLng latLng = null;
                        for(DumpPoint p: points){
                            if(lst_la == -1 && lst_la != la && lst_lo != lo){
                                lst_la = p.getLatitude();
                                lst_lo = p.getLongitude();
                                continue;
                            }
                            la = p.getLatitude();
                            lo = p.getLongitude();
                            latLng = new LatLng(la,lo);
                            List<LatLng> latLngs = new ArrayList<LatLng>();
                            latLngs.add(new LatLng(lst_la,lst_lo));
                            latLngs.add( latLng );
                            Polyline polyline = aMap.addPolyline(new PolylineOptions().
                                    addAll(latLngs).width(5).color(Color.argb(255, 1, 1, 1)));
                            lst_la = la;
                            lst_lo = lo;
                        }
                        if(latLng != null)
                            aMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
                    }
                });
            }
        }).start();
    }

    // friend circle view
    private View page_friend_view;
    public void onMainFriendClick(View view){
        if(page_friend_view == null) {
            page_friend_view = getLayoutInflater().inflate(
                    R.layout.activity_flowing_drawer_friend,mViewGroup);
        }
        setContentView( page_friend_view );
        mDrawer = (FlowingDrawer) findViewById(R.id.drawerlayout);
        mDrawer.closeMenu(true);
    }

    // real time trace view
    private View page_realtime_view;
    private TextView today_now_text;
    private MapView real_time_map_view;
    private AMap real_time_map;
    private Button real_time_reset_button;
    private Button real_time_stop_button;
    private Button real_time_start_button;
    public void onMainRealtimeClick(View view){
        if(page_realtime_view == null) {
            page_realtime_view = getLayoutInflater().inflate(
                    R.layout.activity_flowing_drawer_realtime,mViewGroup);
        }
        setContentView( page_realtime_view ); // set view first, then findViewByID
        if(today_now_text == null) today_now_text = (TextView)findViewById(R.id.main_today_now_text);
        if(real_time_reset_button == null) real_time_reset_button = (Button)findViewById(R.id.real_time_reset_button);
        if(real_time_stop_button == null) real_time_stop_button = (Button)findViewById(R.id.real_time_stop_button);
        if(real_time_start_button == null) real_time_start_button = (Button)findViewById(R.id.real_time_start_button);
        if(real_time_map_view == null) {
            real_time_map_view = (MapView) findViewById(R.id.map);
            real_time_map_view.onCreate(savedInstanceState);
            real_time_map = real_time_map_view.getMap();
            // start real time trace
            mAmapTraceThread.set_path_track(handler,real_time_map);
            mAmapTraceThread.start_path_track();
            // where are you now
            mAmapTraceThread.markWhereYouAre();
        }
        mDrawer = (FlowingDrawer) findViewById(R.id.drawerlayout);
        mDrawer.closeMenu(true);
    }
    public void onRealTimeReset(View view){
        real_time_map.clear();
        real_time_reset_button.setTextColor(ContextCompat.getColor(getBaseContext(),R.color.white));
        real_time_reset_button.setBackground(ContextCompat.getDrawable(getBaseContext(),R.color.lightskyblue));
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mAmapTraceThread.markWhereYouAre();
                real_time_reset_button.setTextColor(ContextCompat.getColor(getBaseContext(),R.color.lightskyblue));
                real_time_reset_button.setBackground(ContextCompat.getDrawable(getBaseContext(),R.color.white));
            }
        },1000);
    }
    public void onRealTimeStop(View view){
        real_time_stop_button.setTextColor(ContextCompat.getColor(getBaseContext(),R.color.white));
        real_time_stop_button.setBackground(ContextCompat.getDrawable(getBaseContext(),R.color.lightskyblue));
        real_time_stop_button.setClickable(false);
        mAmapTraceThread.stop_path_track();
        real_time_start_button.setTextColor(ContextCompat.getColor(getBaseContext(),R.color.lightskyblue));
        real_time_start_button.setBackground(ContextCompat.getDrawable(getBaseContext(),R.color.white));
        real_time_start_button.setClickable(true);
    }
    public void onRealTimeStart(View view){
        real_time_start_button.setTextColor(ContextCompat.getColor(getBaseContext(),R.color.white));
        real_time_start_button.setBackground(ContextCompat.getDrawable(getBaseContext(),R.color.lightskyblue));
        real_time_start_button.setClickable(false);
        mAmapTraceThread.start_path_track();
        real_time_stop_button.setTextColor(ContextCompat.getColor(getBaseContext(),R.color.lightskyblue));
        real_time_stop_button.setBackground(ContextCompat.getDrawable(getBaseContext(),R.color.white));
        real_time_stop_button.setClickable(true);
    }

    // setting view
    private View page_setting_view;
    private RelativeLayout setting_item_interval_layout;
    private RelativeLayout setting_item_share_layout;
    private RelativeLayout setting_item_photo_layout;
    private TextView item_interval_time_text;
    private int time_interval = 5;
    private String time_interval_configure_key = "time_interval";
    @Override
    public void onTimeLengthPickerSet(int min,int sec){
        time_interval = min * 60 + sec;
        // set item_interval_time_text
        String text = String.format( Locale.ENGLISH, "%2dm - %2ds",
                time_interval / 60, time_interval % 60
        );
        item_interval_time_text.setText( text );
        // write into configuration
        SharedPreferences sp = getSharedPreferences("configure.ng",MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putInt(time_interval_configure_key, time_interval);
        ed.apply();// background
        // set back thread
        if(mAmapTraceThread!=null & mAmapTraceThread.isRunning())
            mAmapTraceThread.setTimeInterval(time_interval);
    }
    public void onMainSteeingClick(View view) {
        if(page_setting_view == null){
            page_setting_view = getLayoutInflater().inflate(
                    R.layout.activity_flowing_drawer_setting,mViewGroup);
        }
        setContentView( page_setting_view );
        mDrawer = (FlowingDrawer) findViewById(R.id.drawerlayout);
        mDrawer.closeMenu(true);
        if(item_interval_time_text==null){
            item_interval_time_text = (TextView)findViewById(R.id.item_interval_time_text);
            onTimeLengthPickerSet(time_interval/60, time_interval%60);
        }
        if (setting_item_interval_layout == null){
            setting_item_interval_layout = (RelativeLayout) findViewById(R.id.item_interval_layout);
            setting_item_interval_layout.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()){
                        case MotionEvent.ACTION_DOWN:
                            setting_item_interval_layout.setBackground(
                                    ContextCompat.getDrawable(getBaseContext(),R.color.lightskyblue)
                            );
                            break;
                        case MotionEvent.ACTION_UP:
                            setting_item_interval_layout.setBackground(
                                    ContextCompat.getDrawable(getBaseContext(),R.color.white)
                            );
                            break;
                    }
                    return false;
                }
            });
            setting_item_interval_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // show dialog
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    Fragment fragment = getFragmentManager().findFragmentByTag("intervalDialog");
                    if(fragment != null){
                        fragmentTransaction.remove( fragment );
                    }
                    TimeLengthPickerDialogFragment timeLengthPickerDialogFragment = new TimeLengthPickerDialogFragment();
                    timeLengthPickerDialogFragment.show(fragmentTransaction,"intervalDialog");
                }
            });
        }
        if(setting_item_share_layout == null){
            setting_item_share_layout = (RelativeLayout)findViewById(R.id.item_share_layout);
            setting_item_share_layout.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()){
                        case MotionEvent.ACTION_DOWN:
                            setting_item_share_layout.setBackground(
                                    ContextCompat.getDrawable(getBaseContext(),R.color.lightskyblue)
                            );
                            break;
                        case MotionEvent.ACTION_UP:
                            setting_item_share_layout.setBackground(
                                    ContextCompat.getDrawable(getBaseContext(),R.color.white)
                            );
                            break;
                    }
                    return false;
                }
            });
        }
        if(setting_item_photo_layout == null){
            setting_item_photo_layout = (RelativeLayout)findViewById(R.id.item_photo_layout);
            setting_item_photo_layout.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()){
                        case MotionEvent.ACTION_DOWN:
                            setting_item_photo_layout.setBackground(
                                    ContextCompat.getDrawable(getBaseContext(),R.color.lightskyblue)
                            );
                            break;
                        case MotionEvent.ACTION_UP:
                            setting_item_photo_layout.setBackground(
                                    ContextCompat.getDrawable(getBaseContext(),R.color.white)
                            );
                            break;
                    }
                    return false;
                }
            });
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private FlowingDrawer mDrawer;
    private Bundle savedInstanceState;
    private ViewGroup mViewGroup;
    private AmapTraceThread mAmapTraceThread;
    public AMapLocationClient mLocationClient = null;
    //public AMapLocationListener mLocationListener = null;
    public AMapLocationClientOption mLocationOption = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;

        mViewGroup = null;
        if(page_friend_view == null) {
            page_friend_view = getLayoutInflater().inflate(
                    R.layout.activity_flowing_drawer_friend,mViewGroup);
        }
        setContentView( page_friend_view );

        mDrawer = (FlowingDrawer) findViewById(R.id.drawerlayout);
        mDrawer.setTouchMode(ElasticDrawer.TOUCH_MODE_BEZEL);

        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        /*mLocationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) { }
        };*/
        //设置定位回调监听
        //mLocationClient.setLocationListener(mLocationListener);

        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();

        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位模式为AMapLocationMode.Battery_Saving，低功耗模式。
        //mLocationOption.setLocationMode(AMapLocationMode.Battery_Saving);
        //设置定位模式为AMapLocationMode.Device_Sensors，仅设备模式。
        //mLocationOption.setLocationMode(AMapLocationMode.Device_Sensors);

        //获取一次定位结果：
        //该方法默认为false。
        //mLocationOption.setOnceLocation(true);
        //获取最近3s内精度最高的一次定位结果：
        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
        //mLocationOption.setOnceLocationLatest(true);
        //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
        mLocationOption.setInterval(2000);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否强制刷新WIFI，默认为true，强制刷新。
        //mLocationOption.setWifiActiveScan(false);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        //mLocationOption.setMockEnable(false);
        //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mLocationOption.setHttpTimeOut(20000);
        //关闭缓存机制 default open
        mLocationOption.setLocationCacheEnable(false);

        mLocationClient.setLocationOption(mLocationOption);
        mLocationClient.startLocation();

        // 获取用户设置的 time interval
        SharedPreferences sp = getSharedPreferences("configure.ng",MODE_PRIVATE);
        time_interval = sp.getInt(time_interval_configure_key, 5);

        // 启动位置线程持续获取位置
        mAmapTraceThread = new AmapTraceThread();
        mAmapTraceThread.setup(
                handler,mLocationClient,time_interval,today_now_text
        );
        mAmapTraceThread.start();
    }
}
