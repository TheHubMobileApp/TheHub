package com.thehub.thehubandroid;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

public class User {
    static String base_url = Utils.IP_PROD;

    public static void loginToFacebook(Context context, String user_id, String access_token, int expire) {
        String url = base_url + "/login/facebook";
        String expire_string = Integer.toString(expire);

        new FacebookLoginTask(context).execute(new String[]{url, user_id, access_token, expire_string});
    }

    public static void inviteFriendToHang(Context context, ArrayList<String> friend_ukeys,
                                          String title) {

        String url = base_url + "/create_hangout";

        SharedPreferences theHubprefs = context.getSharedPreferences(Utils.PREFS_FILE,
                Context.MODE_MULTI_PROCESS);

        String akey = theHubprefs.getString("akey", "");
        String ukey = theHubprefs.getString("ukey", "");

//        if(ukey.equals("") || akey.equals("")){
//            Toast.makeText(context, "ukey or akey is empty... ", Toast.LENGTH_SHORT).show();
//        }

        new InviteToHangTask(context, friend_ukeys).execute(url, ukey, akey, title);
    }

    public static void respondToInvite(Context context, String hkey, String response, Activity activity) {

        String url = base_url + "/respond/" + hkey;

        SharedPreferences theHubprefs = context.getSharedPreferences(Utils.PREFS_FILE,
                Context.MODE_MULTI_PROCESS);

        String akey = theHubprefs.getString("akey", "");
        String ukey = theHubprefs.getString("ukey", "");

//        if(ukey.equals("") || akey.equals("")){
//            Toast.makeText(context, "ukey or akey is empty... ", Toast.LENGTH_SHORT).show();
//        }

        new RespondToPushTask(context, activity).execute(url, ukey, akey, response);
    }

    // TODO: add in an expire for the status
    public static void updateAvailability(Context context, Activity activity, String available,
                                          String activity_level, String activity_name, String exp_hrs, String exp_min, Boolean finish_activity) {
        String url = base_url + "/user/update";

        SharedPreferences theHubprefs = context.getSharedPreferences(Utils.PREFS_FILE,
                Context.MODE_MULTI_PROCESS);

        String akey = theHubprefs.getString("akey", "");
        String ukey = theHubprefs.getString("ukey", "");

//        if(ukey.equals("") || akey.equals("")){
//            Toast.makeText(context, "ukey or akey is empty... ", Toast.LENGTH_SHORT).show();
//        }

        new UpdateAvailabilityTask(context, activity).execute(new String[]{url, available, ukey,
                akey, activity_level, activity_name, exp_hrs, exp_min, Boolean.toString(finish_activity)});
    }

    public static void getFriends(Context context, ListView listView, ArrayList<HashMap<String, String>> usersArray) {
        String url = base_url + "/all_users";

        SharedPreferences theHubprefs = context.getSharedPreferences(Utils.PREFS_FILE, Context.MODE_MULTI_PROCESS);

        String akey = theHubprefs.getString("akey", "");
        String ukey = theHubprefs.getString("ukey", "");

//        if(ukey.equals("") || akey.equals("")){
//            Toast.makeText(context, "ukey or akey is empty... ", Toast.LENGTH_SHORT).show();
//        }

        new GetFriendsTask(context, listView, usersArray).execute(new String[]{url, ukey, akey});
    }

    public static void getHangout(Context context, String hkey, Activity activity, ArrayList<HashMap<String, String>> usersArray) {
        String url = base_url + "/hangouts/" + hkey;

        SharedPreferences theHubprefs = context.getSharedPreferences(Utils.PREFS_FILE, Context.MODE_MULTI_PROCESS);

        String akey = theHubprefs.getString("akey", "");
        String ukey = theHubprefs.getString("ukey", "");

//        if(ukey.equals("") || akey.equals("")){
//            Toast.makeText(context, "ukey or akey is empty... ", Toast.LENGTH_SHORT).show();
//        }

        new GetHangoutTask(context, activity, usersArray).execute(new String[]{url, ukey, akey});
    }

    public static void getHangouts(Context context, ListView listView, ArrayList<HashMap<String, String>> hangoutsArray) {
        String url = base_url + "/my_hangouts";

        SharedPreferences theHubprefs = context.getSharedPreferences(Utils.PREFS_FILE, Context.MODE_MULTI_PROCESS);

        String akey = theHubprefs.getString("akey", "");
        String ukey = theHubprefs.getString("ukey", "");

//        if(ukey.equals("") || akey.equals("")){
//            Toast.makeText(context, "ukey or akey is empty... ", Toast.LENGTH_SHORT).show();
//        }

        new GetHangoutsTask(context, listView, hangoutsArray).execute(new String[]{url, ukey, akey});
    }

    public static void getFacebookFriends(Context context, ListView listView, ArrayList<HashMap<String, String>> usersArray) {
        String url = base_url + "/invite_friends";

        SharedPreferences theHubprefs = context.getSharedPreferences(Utils.PREFS_FILE, Context.MODE_MULTI_PROCESS);

        String akey = theHubprefs.getString("akey", "");
        String ukey = theHubprefs.getString("ukey", "");

//        if(ukey.equals("") || akey.equals("")){
//            Toast.makeText(context, "ukey or akey is empty... ", Toast.LENGTH_SHORT).show();
//        }

        new GetFacebookFriendsTask(context, listView, usersArray).execute(new String[]{url, ukey, akey});
    }

    public static void getFreeFacebookFriends(Context context, ListView listView, ArrayList<HashMap<String, String>> usersArray, Activity parent, String invited_ukey) {
        String url = base_url + "/free_friends";

        SharedPreferences theHubprefs = context.getSharedPreferences(Utils.PREFS_FILE, Context.MODE_MULTI_PROCESS);

        String akey = theHubprefs.getString("akey", "");
        String ukey = theHubprefs.getString("ukey", "");

//        if(ukey.equals("") || akey.equals("")){
//            Toast.makeText(context, "ukey or akey is empty... ", Toast.LENGTH_SHORT).show();
//        }

        new GetFreeFacebookFriendsTask(context, listView, usersArray, parent, invited_ukey).execute(new String[]{url, ukey, akey});

    }

    public static void getCurrentUser(Context context, Activity activity) {
        String url = base_url + "/me";

        SharedPreferences theHubprefs = context.getSharedPreferences(Utils.PREFS_FILE, Context.MODE_MULTI_PROCESS);

        String akey = theHubprefs.getString("akey", "");
        String ukey = theHubprefs.getString("ukey", "");

//        if(ukey.equals("") || akey.equals("")){
//            Toast.makeText(context, "ukey or akey is empty... ", Toast.LENGTH_SHORT).show();
//        }

        new GetCurrentUserTask(context, activity).execute(url, ukey, akey);
    }

    public static void leaveHangout(Context context, Activity parent_activity, String hkey) {
        String url = base_url + "/remove_from_hangout";

        SharedPreferences theHubprefs = context.getSharedPreferences(Utils.PREFS_FILE, Context.MODE_MULTI_PROCESS);

        String akey = theHubprefs.getString("akey", "");
        String ukey = theHubprefs.getString("ukey", "");

//        if(ukey.equals("")){
//            Toast.makeText(context, "ukey is empty...", Toast.LENGTH_SHORT).show();
//        }

        new RemoveFromHangoutTask(context, parent_activity).execute(url, akey, ukey, hkey);
    }

    public static void registerInBackground(Context context, String regID) {
        String url = base_url + "/update_gcm";

        SharedPreferences theHubprefs = context.getSharedPreferences(Utils.PREFS_FILE, Context.MODE_MULTI_PROCESS);

        String akey = theHubprefs.getString("akey", "");
        String ukey = theHubprefs.getString("ukey", "");

//        if(ukey.equals("") || akey.equals("")){
//            Toast.makeText(context, "ukey or akey is empty... ", Toast.LENGTH_SHORT).show();
//        }

        new RegisterForGCMTask(context, regID).execute(new String[]{url, ukey, akey});

    }

//    public static void inviteAdditionalFriends(Context context, Activity activity, ArrayList<HashMap<String, String>> usersArray) {
//        String url = base_url + "/add/" + hkey;
//
//        SharedPreferences theHubprefs = context.getSharedPreferences(Utils.PREFS_FILE, Context.MODE_MULTI_PROCESS);
//
//        String akey = theHubprefs.getString("akey", "");
//        String ukey = theHubprefs.getString("ukey", "");
//
//        if(ukey.equals("") || akey.equals("")) {
//            Toast.makeText(context, "ukey or akey is empty...", Toast.LENGTH_SHORT).show();
//        }
//
//        new
//    }
}
