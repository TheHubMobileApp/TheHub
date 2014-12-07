package com.thehub.thehubandroid;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

;

public class NewHangoutActivity extends ActionBarActivity {
    private ListView listView;
    private Context context;
    private ArrayList<HashMap<String, String>> freeFriendsArray;
    private Button createHangoutButton;
    private TextView ukeys_textview;
    private String invited_ukey;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // remove the title bar
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_hangout_activity);
        context = getApplicationContext();
        listView = (ListView) findViewById(R.id.listView);
        createHangoutButton = (Button) findViewById(R.id.createHangoutButton);
        invited_ukey = null;

        // if a friend was invited to hang, figure how who that friend was and mark him as invited
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            invited_ukey = bundle.getString("friend_ukey");
            ukeys_textview = (TextView) findViewById(R.id.ukeys);
            ukeys_textview.append(invited_ukey + ",");
        }

        // Create empty array (will update it later)
        freeFriendsArray = new ArrayList<HashMap<String, String>>();

        //short press is to view the spot (SpotPage.java)
        // copied straight from FriendsListView.java lololol
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> listView, View view, int position, long id) {
                Button invite_button = (Button) view.findViewById(R.id.inviteFriendButton);
                // TODO: Only show invite button if they are available
                if(invite_button.getVisibility() == View.GONE) {
                    invite_button.setVisibility(View.VISIBLE);
                } else {
                    invite_button.setVisibility(View.GONE);
                }
            }
        });

        createHangoutButton.setOnClickListener(new OnClickListenerWithParent(this, context));

        /**
         *
         * Get your facebook friends and display the list view using
         * a {@link com.thehub.thehubandroid.GetFacebookFriendsTask}.
         *
         */
        User.getFreeFacebookFriends(context, listView, freeFriendsArray, this, invited_ukey);
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