package com.example.a60213.getyourlocation;

/**
 * Created by 60213 on 2017/6/2.
 */

import org.json.*;
import android.util.Log;

public class AppServer {
    private final static String default_server_url = "http://niugenen.6655.la:60000/";
    private final static String Login_URL = default_server_url + "UserLoginServlet/";

    private static AppServer mAppServer = new AppServer();
    private AppServer(){}
    public static AppServer getInstance(){ return mAppServer; }

    private String username;
    private int userid;
    private String userkey;
    private String passwd;

    public boolean Login(String username, String passwd){
        try {
            JSONObject info_req = new JSONObject();
            info_req.put("username", username);
            info_req.put("passwd", passwd);

            //String res = Util_http.sendPost(Login_URL, info_req.toString());
            Log.i(AppServer.this.toString() + " Login", info_req.toString());

            //JSONObject info_res = new JSONObject( res );
            //if(info_res.getBoolean("login")) {
            if(true){
                this.username = username;
                this.passwd = passwd;

                //userid = info_res.getInt("userid");
                userid = 1;
                //userkey = info_res.getString("userkey");
                userkey = Long.toString( System.currentTimeMillis() );
                Log.i(AppServer.this.toString() + " Login", "id=" + userid + " ; key=" + userkey);
                return true;
            }
            else{
                this.username = null;
                this.passwd = null;
                return false;
            }

        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void Logout(String username){

    }
}
