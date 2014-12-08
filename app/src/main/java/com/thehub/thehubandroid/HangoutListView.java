package com.thehub.thehubandroid;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

public class HangoutListView extends Fragment {
    private HangoutListAdapter dataAdapter;
    private ListView listView;
    private Context context;
    private View rootView;
    private ArrayList<HashMap<String, String>> hangoutsArray;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // enable menu at bottom
        setHasOptionsMenu(true);

        // Inflate the layout for this fragment
        context = getActivity().getBaseContext();
        rootView = inflater.inflate(R.layout.hangout_list, container, false);

        // get listview
        listView = (ListView) rootView.findViewById(R.id.listViewHangout);

        // Create empty array (will update it later)
        hangoutsArray = new ArrayList<HashMap<String, String>>();

        // populate spots array (no ProgressDialog since there is one for the map)
        User.getHangouts(context, listView, hangoutsArray);

        //short press is to view the spot (SpotPage.java)
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    public void onItemClick(AdapterView<?> listView, View view, int position, long id) {
                        Bundle bundleData = new Bundle();

                        HashMap<String, String> user = hangoutsArray.get(position);

                        Button invite_button = (Button) view.findViewById(R.id.inviteUsersButton);
                        if(invite_button.getVisibility() == View.GONE) {
                            //invite_button.setVisibility(View.VISIBLE);
                        } else {
                            //invite_button.setVisibility(View.GONE);
                        }
                        //                String ukey = user.get("ukey");
                        //
                        //                Log.i("DEBUG", "ukey = " + ukey);
                        //
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
        hangoutsArray = new ArrayList<HashMap<String, String>>();

        // populate spots array
        User.getHangouts(context, listView, hangoutsArray);
    }
}
