package com.thehub.thehubandroid;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

public class NewHangoutActivity extends ActionBarActivity {
    private ListView listView;
    private Context context;
    private ArrayList<HashMap<String, String>> freeFriendsArray;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_hangout_activity);
        context = getApplicationContext();
        listView = (ListView) findViewById(R.id.listView);

        // Create empty array (will update it later)
        freeFriendsArray = new ArrayList<HashMap<String, String>>();

        /**
         *
         * Get your facebook friends and display the list view using
         * a {@link com.thehub.thehubandroid.GetFacebookFriendsTask}.
         *
         */
        User.getFreeFacebookFriends(context, listView, freeFriendsArray);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
