package com.thehub.thehubandroid;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

public class FriendsListView extends Fragment {
    private FriendsListAdapter dataAdapter;
    private ListView listView;
    private Context context;
    private View rootView;
    private ArrayList<HashMap<String, String>> usersArray;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // enable menu at bottom
        setHasOptionsMenu(true);

        // Inflate the layout for this fragment
        context = getActivity().getBaseContext();
        rootView = inflater.inflate(R.layout.user_list, container, false);

        // get listview
        listView = (ListView) rootView.findViewById(R.id.listView);

        // Create empty array (will update it later)
        usersArray = new ArrayList<HashMap<String, String>>();

        // populate spots array (no ProgressDialog since there is one for the map)
        User.getFriends(context, listView, usersArray);

        //short press is to view the spot (SpotPage.java)
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> listView, View view, int position, long id) {
                Bundle bundleData = new Bundle();

                HashMap<String, String> user = usersArray.get(position);
                String ukey = user.get("ukey");

                Log.i("DEBUG", "ukey = " + ukey);
                if (!user.get("availability").equals("busy")) {
                    Button invite_button = (Button) view.findViewById(R.id.inviteFriendButton);
                    // TODO: Only show invite button if they are available
                    if (invite_button.getVisibility() == View.GONE) {
                        //invite_button.setVisibility(View.VISIBLE);
                    } else {
                        //invite_button.setVisibility(View.GONE);
                    }
                }
                else{
                    Button invite_button = (Button) view.findViewById(R.id.inviteFriendButton);
                    invite_button.setVisibility(View.INVISIBLE);
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

        return rootView;
    }

    public void onResume() {
        super.onResume();

        // Create empty array (will update it later)
        usersArray = new ArrayList<HashMap<String, String>>();

        // populate spots array (no ProgressDialog since there is one for the map)
        User.getFriends(context, listView, usersArray);
    }

}
