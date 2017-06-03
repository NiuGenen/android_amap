package com.example.a60213.getyourlocation;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.amap.api.maps2d.MapView;
import com.mxn.soul.flowingdrawer_core.ElasticDrawer;
import com.mxn.soul.flowingdrawer_core.FlowingDrawer;

import android.app.FragmentTransaction;
import android.app.DatePickerDialog;
import android.app.Dialog;

public class FlowingDrawerActivity extends AppCompatActivity implements DatePickerDialogFragment.DatePickerDialogImpl{

    private Handler handler = new Handler();

    private DatePickerDialog.OnDateSetListener DatePickerDIalogListener;
    @Override
    public DatePickerDialog.OnDateSetListener getDatePickerDialogListener(){
        if(DatePickerDIalogListener == null) {
            DatePickerDIalogListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    map_date.setText(year + "-" + (monthOfYear+1) + "-" + dayOfMonth);
                    map_date_button.setBackground( getResources().getDrawable(R.color.lightskyblue) );
                    map_date_button.setTextColor( getResources().getColor(R.color.white) );
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
                map_date_button.setBackground( getResources().getDrawable(R.color.white) );
                map_date_button.setTextColor( getResources().getColor(R.color.lightskyblue) );
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

    public void onMainFriendClick(View view){
        setContentView(R.layout.activity_flowing_drawer_friend);
    }

    private MapView mMapView;
    public void onMainMapClick(View view){//获取地图控件引用
        setContentView(R.layout.activity_flowing_drawer_map);
        mMapView = (MapView) findViewById(R.id.map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);
    }

    public void onMainSteeingClick(View view){
        setContentView(R.layout.activity_flowing_drawer_setting);
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        setContentView(R.layout.activity_flowing_drawer_friend);

        mDrawer = (FlowingDrawer) findViewById(R.id.drawerlayout);
        mDrawer.setTouchMode(ElasticDrawer.TOUCH_MODE_BEZEL);
        mDrawer.setOnDrawerStateChangeListener(new ElasticDrawer.OnDrawerStateChangeListener() {
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
        });
    }
}
