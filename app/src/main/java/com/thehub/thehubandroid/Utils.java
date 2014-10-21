package com.thehub.thehubandroid;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * Created by Robbie on 10/2/14.
 */
public class Utils {
    public static final String DEBUG_TAG = "Debug";
    public static final String PREFS_FILE = "com.thehub.thehubandroid.prefs";

    // To get your test ip run "ifconfig" in your terminal and get the
    // address that looks like the one below. It will be for eth1
    public static String IP_TEST = "http://10.0.0.44:5000";
    public static String IP_PROD = "http://the-hub-backend.herokuapp.com";

    public static int convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float Pix = dp * (metrics.densityDpi / 160f);
        int px = (int) Pix;
        return px;
    }
}
