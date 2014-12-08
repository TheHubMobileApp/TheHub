package com.thehub.thehubandroid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class HangoutView extends ActionBarActivity {
    private FriendsListAdapter dataAdapter;
    private ListView listView;
    private TextView title;
    private Activity activity;
    private Context context;
    private String hkey, hangout_title;
    private ArrayList<HashMap<String, String>> usersArray;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hangout_activity);
        activity = this;
        usersArray = new ArrayList<HashMap<String, String>>();

        context = getApplicationContext();
        listView = (ListView) findViewById(R.id.listView);
        title = (TextView) findViewById(R.id.title);

        Bundle showData = getIntent().getExtras();
        hkey = showData.getString("hkey");

        hangout_title = showData.getString("title");
        title.setText(hangout_title);



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
//                Intent invite = new Intent(context, InviteAdditionalToHangoutActivity.class);
//                Bundle bundle = new Bundle();
//                String ukeys = "";
//                for (HashMap<String, String> user : usersArray) {
//                    ukeys += (user.get("ukey") + ",");
//                }
//                bundle.putString("ukeys", ukeys);
//                invite.putExtras(bundle);
//                startActivity(invite);

                // or replace the list view with friends to invite
                return true;
            case R.id.action_leave_hangout:
                // TODO: take us to a new activity to invite friends
                // or replace the list view with friends to invite
                User.leaveHangout(context, activity, hkey);
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
