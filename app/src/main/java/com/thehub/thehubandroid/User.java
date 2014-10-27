package com.thehub.thehubandroid;

import android.app.Activity;
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

    // TODO: add in an expire for the status
    public static void updateAvailability(Context context, Activity activity, String available) {
        String url = Utils.IP_PROD + "/user/update";

        SharedPreferences theHubprefs = context.getSharedPreferences(Utils.PREFS_FILE, Context.MODE_MULTI_PROCESS);

        String akey = theHubprefs.getString("akey", "");
        String ukey = theHubprefs.getString("ukey", "");

        if(ukey.equals("") || akey.equals("")){
            Toast.makeText(context, "ukey or akey is empty... ", Toast.LENGTH_SHORT).show();
        }

        new UpdateAvailabilityTask(context, activity).execute(new String[]{url, available, ukey, akey});
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

    public static void getFacebookFriends(Context context, ListView listView, ArrayList<HashMap<String, String>> usersArray) {
        String url = Utils.IP_PROD + "/invite_friends";

        SharedPreferences theHubprefs = context.getSharedPreferences(Utils.PREFS_FILE, Context.MODE_MULTI_PROCESS);

        String akey = theHubprefs.getString("akey", "");
        String ukey = theHubprefs.getString("ukey", "");

        if(ukey.equals("") || akey.equals("")){
            Toast.makeText(context, "ukey or akey is empty... ", Toast.LENGTH_SHORT).show();
        }

        new GetFacebookFriendsTask(context, listView, usersArray).execute(new String[]{url, ukey, akey});
    }

    public static void getCurrentUser(Context context, Activity activity) {
        String url = Utils.IP_PROD + "/me";

        SharedPreferences theHubprefs = context.getSharedPreferences(Utils.PREFS_FILE, Context.MODE_MULTI_PROCESS);

        String akey = theHubprefs.getString("akey", "");
        String ukey = theHubprefs.getString("ukey", "");

        if(ukey.equals("") || akey.equals("")){
            Toast.makeText(context, "ukey or akey is empty... ", Toast.LENGTH_SHORT).show();
        }

        new GetCurrentUserTask(context, activity).execute(new String[]{url, ukey, akey});
    }
}
