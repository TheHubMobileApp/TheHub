package com.thehub.thehubandroid;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * Created by Robbie on 10/2/14.
 */
public class Utils {
    public static final int NUM_TABS = 2;
    public static final String EXPIRE_ROOT_TEXT = "Exp: ";
    public static final String NO_EXPIRE_TEXT = "No Expire Set";
    public static final String DEBUG_TAG = "Debug";
    public static final String PREFS_FILE = "com.thehub.thehubandroid.prefs";

    public static final String FREE = "free";
    public static final String BUSY = "busy";
    public static final String BUSY_MESSAGE = "You are busy";
    public static final String FREE_MESSAGE = "You are free";

    // To get your test ip run "ifconfig" in your terminal and get the
    // address that looks like the one below. It will be for eth1
    public static String IP_TEST = "http://10.0.0.44:5000";
    public static String DAVIDS_IP = "http://35.2.228.106:5000";
    public static String IP_TEST_STAMPS = "http://35.2.176.4:5000";
    public static String IP_PROD = "http://the-hub-backend.herokuapp.com";

    public static int convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float Pix = dp * (metrics.densityDpi / 160f);
        int px = (int) Pix;
        return px;
    }
}
