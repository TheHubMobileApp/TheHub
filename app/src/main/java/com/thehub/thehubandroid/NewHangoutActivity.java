package com.thehub.thehubandroid;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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

        //short press is to view the spot (SpotPage.java)
        // copied straight from FriendsListView.java lololol
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> listView, View view, int position, long id) {
                Bundle bundleData = new Bundle();

                HashMap<String, String> user = freeFriendsArray.get(position);
                String ukey = user.get("ukey");

                Log.i("DEBUG", "ukey = " + ukey);

                Button invite_button = (Button) view.findViewById(R.id.inviteFriendButton);
                // TODO: Only show invite button if they are available
                if(invite_button.getVisibility() == View.GONE) {
                    invite_button.setVisibility(View.VISIBLE);
                } else {
                    invite_button.setVisibility(View.GONE);
                }

//                User.inviteFriendToHang(context, ukey, "invite to hang");
                //Toast.makeText(getActivity().getApplicationContext(), "size = " +  SpotsArray.size(), Toast.LENGTH_SHORT).show();

//                HashMap<String, String> user = usersArray.get(position);
//
//                String spot_id = user.get("id");
//                String url = user.get("photo");
//                String overall = user.get("overall");
//                String bust = user.get("bust");
//                String difficulty = user.get("difficulty");
//
//                bundleData.putString("spot_id", spot_id);
//                bundleData.putString("url", url);
//                bundleData.putString("overall", overall);
//
//                Intent intent = new Intent(getActivity().getApplicationContext(),
//                        SpotPage.class);
//
//                intent.putExtras(bundleData);
//                startActivity(intent);
            }
        });

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
