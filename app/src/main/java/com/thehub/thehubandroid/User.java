package com.thehub.thehubandroid;

import android.content.Context;

public class User {
    public static void loginToFacebook(Context context, String user_id, String access_token, int expire) {
        String url = Utils.IP_PROD + "/login/facebook";
        String expire_string = Integer.toString(expire);
        new FacebookLoginTask(context).execute(new String[]{url, user_id, access_token, expire_string});
    }
}
