package com.thehub.thehubandroid;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class HangoutView extends Activity {
    private FriendsListAdapter dataAdapter;
    private ListView listView;
    private TextView title;
    private Activity this_activity;
    private Context context;
    private String hkey;
    private ArrayList<HashMap<String, String>> usersArray;
    private Button leaveHangoutButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hangout_activity);
        context = getApplicationContext();
        this_activity = this;
        listView = (ListView) findViewById(R.id.listView);
        title = (TextView) findViewById(R.id.title);
        usersArray = new ArrayList<HashMap<String, String>>();
        leaveHangoutButton = (Button) findViewById(R.id.leaveHangout);

        Bundle showData = getIntent().getExtras();
        hkey = showData.getString("hkey");

        leaveHangoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User.leaveHangout(context, this_activity, hkey);
            }
        });

        User.getHangout(context, hkey, this, usersArray);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.hangout_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_invite_friends:
                // TODO: take us to a new activity to invite friends
                // or replace the list view with friends to invite
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onResume() {
        super.onResume();

        usersArray = new ArrayList<HashMap<String, String>>();

        // TODO: could skip the bundle?
        Bundle showData = getIntent().getExtras();
        hkey = showData.getString("hkey");

        User.getHangout(context, hkey, this, usersArray);
    }

}
