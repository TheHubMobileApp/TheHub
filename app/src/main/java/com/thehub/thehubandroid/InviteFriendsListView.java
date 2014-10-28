package com.thehub.thehubandroid;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

public class InviteFriendsListView extends Activity {
    private ListView listView;
    private Context context;
    private ArrayList<HashMap<String, String>> friendsArray;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invite_list);
        context = getApplicationContext();
        listView = (ListView) findViewById(R.id.listView);

        friendsArray = new ArrayList<HashMap<String, String>>();

        /**
         *
         * Get your facebook friends and display the list view using
         * a {@link com.thehub.thehubandroid.GetFacebookFriendsTask}.
         *
         */
        User.getFacebookFriends(context, listView, friendsArray);
    }
}
