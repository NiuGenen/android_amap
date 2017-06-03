package com.example.a60213.getyourlocation;

/**
 * Created by 60213 on 2017/6/2.
 */

import org.json.*;
import android.util.Log;
import android.util.Pair;
import android.util.StringBuilderPrinter;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class AppServer {
    private boolean debug = false;

    private final static String default_server_url = "http://niugenen.6655.la:60000/";
    private final static String default_server_name ="servlet_server_for_android";
    private final static String Login_URL = default_server_url + "/" + default_server_name + "/UserLoginServlet";
    private final static String Register_URL = default_server_url + "/" + default_server_name + "/UserRegServlet";
    private final static String PointTrack_URL = default_server_url + "/" + default_server_name + "/PointTrackServlet";
    private final static String UserPath_URL = default_server_url + "/" + default_server_name + "/UserPathServlet";

    public class PostParameters{
        private Map<String, String> parameters;
        public PostParameters(){
            parameters = new HashMap<>();
        }
        public void put(String key,String value){
            if(!parameters.containsKey(key)){
                parameters.put(key,value);
            }
        }
        public String toString(){
            StringBuffer buffer = new StringBuffer();
            boolean first = true;
            for(String k : parameters.keySet()){
                if(!first){
                    buffer.append("&");
                }
                buffer.append( k + "=" + parameters.get(k));
                first = false;
            }
            return buffer.toString();
        }
    }

    private static AppServer mAppServer = new AppServer();
    private AppServer(){}
    public static AppServer getInstance(){ return mAppServer; }

    private String username;
    private int userid;
    private String userkey;
    private String passwd;
    private boolean online = false;

    public List<DumpPoint> UserPath(long start, long end){
        if(debug){
            Log.d("AppServer.UserPath",start +" " +end);
            return null;
        }
        try{
            if(!online) return null;

            PostParameters info_eq = new PostParameters();
            info_eq.put("userid",Integer.toString(userid) );
            info_eq.put("start",Long.toString(start));
            info_eq.put("end",Long.toString(end));

            String res = Util_http.sendPost(UserPath_URL, info_eq.toString());
            JSONObject info_res = new JSONObject( res );
            JSONArray points_json = info_res.getJSONArray("points");

            List<DumpPoint> ret = new LinkedList<>();
            for(int index = 0; index < points_json.length(); ++index ){
                JSONObject point = points_json.getJSONObject(index);
                DumpPoint p = new DumpPoint();
                p.setLatitude( point.getDouble("latitude") );
                p.setLongitude( point.getDouble("longitude") );
                p.setLocation( point.getString("location") );
                ret.add( p );
            }
            return ret;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public boolean PointTrack(double latitude, double longitude, String location_str){
        if(debug) {
            Log.d("AppServer.PointTrack",latitude + " " + longitude + " " + location_str);
            return true;
        }
        try {
            if(!online) return false;

            PostParameters info_req = new PostParameters();
            info_req.put("test", "True");
            info_req.put("userid", Integer.toString(userid) );
            JSONObject point = new JSONObject();
            point.put("latitude",latitude);
            point.put("longitude",longitude);
            point.put("location", URLEncoder.encode(location_str, "utf-8"));
            info_req.put("point", point.toString());
            info_req.put("timestramp", Long.toString(System.currentTimeMillis()) );

            String res = Util_http.sendPost( PointTrack_URL, info_req.toString());
            JSONObject info_res = new JSONObject( res );
            if(info_res.getBoolean("pointtrack")){
                return true;
            }
            else{
                return false;
            }
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean Register(String username,String passwd){
        if(debug){
            Log.d("AppServer.Register",username +" " + passwd);
            return true;
        }
        try{
            if(online) return false;

            PostParameters info_eq = new PostParameters();
            info_eq.put("username", username);
            info_eq.put("passwd", passwd);

            String res = Util_http.sendPost(Register_URL, info_eq.toString());
            JSONObject info_res = new JSONObject( res );
            if(info_res.getBoolean("register")) {
                return true;
            }
            else{
                return false;
            }
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean Login(String username, String passwd){
        if(debug) {
            Log.d("AppServer.Login",username + " " + passwd);
            return true;
        }
        try {
            PostParameters info_req = new PostParameters();
            info_req.put("username", username);
            info_req.put("passwd", passwd);

            String res = Util_http.sendPost(Login_URL, info_req.toString());
            Log.d(AppServer.this.toString() + " Login", info_req.toString() +" " +res);

            JSONObject info_res = new JSONObject( res );
            if(info_res.getBoolean("login")) {
                this.username = username;
                this.passwd = passwd;

                this.userid = info_res.getInt("userid");
                this.userkey = info_res.getString("userkey");
                Log.d("AppServer.Login", "id=" + userid + " ; key=" + userkey);
                this.online = true;
                return true;
            }
            else{
                this.username = null;
                this.passwd = null;
                this.userid = 0;
                this.online = false;
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            online = false;
            return false;
        }
    }

    public void Logout(String username){
        if(debug) return;
        if(!online) return;
    }
}
