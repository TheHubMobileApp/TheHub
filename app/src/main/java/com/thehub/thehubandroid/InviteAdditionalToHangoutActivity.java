package com.thehub.thehubandroid;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by David on 12/8/2014.
 */
public class InviteAdditionalToHangoutActivity extends Activity {
    private ListView listView;
    private Context context;
    private ArrayList<HashMap<String, String>> freeFriendsArray;
    private Button inviteToHangoutButton;
    private TextView ukeys_textview;
    private String invited_ukeys;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invite_additional_to_hangout);
        context = getApplicationContext();
        listView = (ListView) findViewById(R.id.listView);
        inviteToHangoutButton = (Button) findViewById(R.id.inviteToHangoutButton);
        ukeys_textview = (TextView) findViewById(R.id.ukeys);

        Bundle bundle = getIntent().getExtras();
        invited_ukeys = bundle.getString("ukeys");

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

        inviteToHangoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String raw_ukeys = ukeys_textview.getText().toString();

                // only create hangout if you invited someone
                if (raw_ukeys.trim().equals("")) {
                    Toast.makeText(context, "You can hangout alone, just not with our app!\nPlease select a friend.", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    EditText edittext = (EditText) findViewById(R.id.hangout_title);
                    String title = edittext.getText().toString();
                    // Make sure theres a title set
                    if (title.trim().equals("")) {
                        Toast.makeText(context, "Please give this hangout a title", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // FIXME: this seems convoluted, im trying to just get an arraylist of strings from raw ukeys
                    ArrayList<String> ukeys = new ArrayList<String>(Arrays.asList(raw_ukeys.split(",")));

                    // send out the request
                    // TODO: need some other way to do this shite
                    User.inviteFriendToHang(context, ukeys, title);
                    Toast.makeText(context, "Hangout successfully created!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

        /**
         *
         * Get your facebook friends and display the list view using
         * a {@link com.thehub.thehubandroid.GetFacebookFriendsTask}.
         *
         */
        User.getFreeFacebookFriends(context, listView, freeFriendsArray, this, invited_ukeys);
    }
}
