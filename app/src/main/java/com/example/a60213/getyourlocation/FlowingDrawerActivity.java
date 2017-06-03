package com.example.a60213.getyourlocation;

import android.app.Fragment;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
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

import com.amap.api.maps2d.MapView;
import com.mxn.soul.flowingdrawer_core.ElasticDrawer;
import com.mxn.soul.flowingdrawer_core.FlowingDrawer;
import com.mxn.soul.flowingdrawer_core.FlowingMenuLayout;

import android.app.FragmentTransaction;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.widget.TimePicker;

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

    // setting view
    private View page_setting_view;
    private RelativeLayout setting_item_interval_layout;
    private RelativeLayout setting_item_share_layout;
    private RelativeLayout setting_item_photo_layout;
    private TextView item_interval_time_text;
    private int time_interval = 5;
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
        ed.putInt("time_interval", time_interval);
        ed.commit();
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
        /*mDrawer.setOnDrawerStateChangeListener(new ElasticDrawer.OnDrawerStateChangeListener() {
            @Override
            public void onDrawerStateChange(int oldState, int newState) {
                if (newState == ElasticDrawer.STATE_CLOSED) {
                    Log.i("MainActivity", "Drawer STATE_CLOSED");
                }
            }
            @Override
            public void onDrawerSlide(float openRatio, int offsetPixels) {
                Log.i("MainActivity", "openRatio=" + openRatio + " ,offsetPixels=" + offsetPixels);
            }
        });*/
    }
}
