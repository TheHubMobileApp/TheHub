package com.thehub.thehubandroid;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class User {
    static String base_url = Utils.IP_PROD;

    public static void loginToFacebook(Context context, String user_id, String access_token, int expire) {
        String url = base_url + "/login/facebook";
        String expire_string = Integer.toString(expire);

        new FacebookLoginTask(context).execute(new String[]{url, user_id, access_token, expire_string});
    }

    public static void inviteFriendToHang(Context context, String friend_ukey,
                                          String title) {

        String url = base_url + "/create_hangout";

        SharedPreferences theHubprefs = context.getSharedPreferences(Utils.PREFS_FILE,
                Context.MODE_MULTI_PROCESS);

        String akey = theHubprefs.getString("akey", "");
        String ukey = theHubprefs.getString("ukey", "");

        if(ukey.equals("") || akey.equals("")){
            Toast.makeText(context, "ukey or akey is empty... ", Toast.LENGTH_SHORT).show();
        }

        new InviteToHangTask(context).execute(new String[]{url, ukey,
                akey, friend_ukey, title});
    }

    // TODO: add in an expire for the status
    public static void updateAvailability(Context context, Activity activity, String available,
                                          String activity_level, String activity_name, String exp_hrs, String exp_min, Boolean finish_activity) {
        String url = base_url + "/user/update";

        SharedPreferences theHubprefs = context.getSharedPreferences(Utils.PREFS_FILE,
                Context.MODE_MULTI_PROCESS);

        String akey = theHubprefs.getString("akey", "");
        String ukey = theHubprefs.getString("ukey", "");

        if(ukey.equals("") || akey.equals("")){
            Toast.makeText(context, "ukey or akey is empty... ", Toast.LENGTH_SHORT).show();
        }

        new UpdateAvailabilityTask(context, activity).execute(new String[]{url, available, ukey,
                akey, activity_level, activity_name, exp_hrs, exp_min, Boolean.toString(finish_activity)});
    }

    public static void getFriends(Context context, ListView listView, ArrayList<HashMap<String, String>> usersArray) {
        String url = base_url + "/all_users";

        SharedPreferences theHubprefs = context.getSharedPreferences(Utils.PREFS_FILE, Context.MODE_MULTI_PROCESS);

        String akey = theHubprefs.getString("akey", "");
        String ukey = theHubprefs.getString("ukey", "");

        if(ukey.equals("") || akey.equals("")){
            Toast.makeText(context, "ukey or akey is empty... ", Toast.LENGTH_SHORT).show();
        }

        new GetFriendsTask(context, listView, usersArray).execute(new String[]{url, ukey, akey});
    }

    public static void getHangout(Context context, String hkey, Activity activity, ArrayList<HashMap<String, String>> usersArray) {
        String url = base_url + "/hangouts/" + hkey;

        SharedPreferences theHubprefs = context.getSharedPreferences(Utils.PREFS_FILE, Context.MODE_MULTI_PROCESS);

        String akey = theHubprefs.getString("akey", "");
        String ukey = theHubprefs.getString("ukey", "");

        if(ukey.equals("") || akey.equals("")){
            Toast.makeText(context, "ukey or akey is empty... ", Toast.LENGTH_SHORT).show();
        }

        new GetHangoutTask(context, activity, usersArray).execute(new String[]{url, ukey, akey});
    }

    public static void getHangouts(Context context, ListView listView, ArrayList<HashMap<String, String>> hangoutsArray) {
        String url = base_url + "/my_hangouts";

        SharedPreferences theHubprefs = context.getSharedPreferences(Utils.PREFS_FILE, Context.MODE_MULTI_PROCESS);

        String akey = theHubprefs.getString("akey", "");
        String ukey = theHubprefs.getString("ukey", "");

        if(ukey.equals("") || akey.equals("")){
            Toast.makeText(context, "ukey or akey is empty... ", Toast.LENGTH_SHORT).show();
        }

        new GetHangoutsTask(context, listView, hangoutsArray).execute(new String[]{url, ukey, akey});
    }

    public static void getFacebookFriends(Context context, ListView listView, ArrayList<HashMap<String, String>> usersArray) {
        String url = base_url + "/invite_friends";

        SharedPreferences theHubprefs = context.getSharedPreferences(Utils.PREFS_FILE, Context.MODE_MULTI_PROCESS);

        String akey = theHubprefs.getString("akey", "");
        String ukey = theHubprefs.getString("ukey", "");

        if(ukey.equals("") || akey.equals("")){
            Toast.makeText(context, "ukey or akey is empty... ", Toast.LENGTH_SHORT).show();
        }

        new GetFacebookFriendsTask(context, listView, usersArray).execute(new String[]{url, ukey, akey});
    }

    public static void getFreeFacebookFriends(Context context, ListView listView, ArrayList<HashMap<String, String>> usersArray) {
        String url = base_url + "/free_friends";

        SharedPreferences theHubprefs = context.getSharedPreferences(Utils.PREFS_FILE, Context.MODE_MULTI_PROCESS);

        String akey = theHubprefs.getString("akey", "");
        String ukey = theHubprefs.getString("ukey", "");

        if(ukey.equals("") || akey.equals("")){
            Toast.makeText(context, "ukey or akey is empty... ", Toast.LENGTH_SHORT).show();
        }

        new GetFreeFacebookFriendsTask(context, listView, usersArray).execute(new String[]{url, ukey, akey});

    }

    public static void getCurrentUser(Context context, Activity activity) {
        String url = base_url + "/me";

        SharedPreferences theHubprefs = context.getSharedPreferences(Utils.PREFS_FILE, Context.MODE_MULTI_PROCESS);

        String akey = theHubprefs.getString("akey", "");
        String ukey = theHubprefs.getString("ukey", "");

        if(ukey.equals("") || akey.equals("")){
            Toast.makeText(context, "ukey or akey is empty... ", Toast.LENGTH_SHORT).show();
        }

        new GetCurrentUserTask(context, activity).execute(url, ukey, akey);
    }
}
