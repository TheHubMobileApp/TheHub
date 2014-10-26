package com.thehub.thehubandroid;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

public class FriendsListView extends Activity {
    private FriendsListAdapter dataAdapter;
    private ListView listView;
    private Context context;
    private ArrayList<HashMap<String, String>> usersArray;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_list);
        context = getApplicationContext();

        // get listview
        listView = (ListView) findViewById(R.id.listView);

        // TODO: remove the if/else
        if(listView != null){
            // Create empty array (will update it later)
            usersArray = new ArrayList<HashMap<String, String>>();

            // populate spots array (no ProgressDialog since there is one for the map)
            User.getFriends(context, listView, usersArray);

            // TODO: Delete this, I am using it for testing (R)
//            User.getFacebookFriends(context, listView, usersArray);
        } else {
            Log.i("DEBUG", "YOU DONE FUCKED UP");
        }
    }

}
