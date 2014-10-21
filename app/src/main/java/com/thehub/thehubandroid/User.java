package com.thehub.thehubandroid;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class User {

    public static void loginToFacebook(Context context, String user_id, String access_token, int expire) {
        String url = Utils.IP_PROD + "/login/facebook";
        String expire_string = Integer.toString(expire);

        new FacebookLoginTask(context).execute(new String[]{url, user_id, access_token, expire_string});
    }

    public static void getFriends(Context context, ListView listView, ArrayList<HashMap<String, String>> usersArray) {
        String url = Utils.IP_PROD + "/all_users";

        SharedPreferences theHubprefs = context.getSharedPreferences(Utils.PREFS_FILE, Context.MODE_MULTI_PROCESS);

        String akey = theHubprefs.getString("akey", "");
        String ukey = theHubprefs.getString("ukey", "");

        if(ukey.equals("") || akey.equals("")){
            Toast.makeText(context, "ukey or akey is empty... ", Toast.LENGTH_SHORT).show();
        }

        new GetFriendsTask(context, listView, usersArray).execute(new String[]{url, ukey, akey});
    }

}
