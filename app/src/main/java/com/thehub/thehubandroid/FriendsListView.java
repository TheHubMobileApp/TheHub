package com.thehub.thehubandroid;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
//        context = getApplicationContext();

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
