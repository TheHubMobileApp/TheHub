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

public class InviteFriendsListView extends Fragment {
    private ListView listView;
    private Context context;
    private View rootView;
    private ArrayList<HashMap<String, String>> friendsArray;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // enable menu at bottom
        setHasOptionsMenu(true);

        // Inflate the layout for this fragment
        context = getActivity().getBaseContext();
        rootView = inflater.inflate(R.layout.invite_list, container, false);
        listView = (ListView) rootView.findViewById(R.id.listView);

        friendsArray = new ArrayList<HashMap<String, String>>();

        /**
         *
         * Get your facebook friends and display the list view using
         * a {@link com.thehub.thehubandroid.GetFacebookFriendsTask}.
         *
         */
        User.getFacebookFriends(context, listView, friendsArray);

        return rootView;
    }
}
