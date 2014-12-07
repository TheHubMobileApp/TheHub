package com.thehub.thehubandroid;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

public class HangoutListAdapter extends BaseAdapter {

    private Context context;
    private List<HashMap<String, String>> hangouts;

    static class ViewHolder {
//        ImageView profile_picture;
        TextView title;
        TextView num_users;
        TextView display_names;
        TextView ukeys;
    }

    public HangoutListAdapter(Context context, List<HashMap<String, String>> hangouts, int resource) {
        this.setContext(context);
        this.hangouts = hangouts;
    }

    //public View newView(Context context, Cursor cursor, ViewGroup parent) {
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ViewHolder vh = null;
        final HashMap<String, String> hangout = hangouts.get(position);

        if (v == null) {
            //Toast.makeText(context, "Populating... " + spot.get("name"), Toast.LENGTH_LONG).show();

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.hangout_list_item, null);

            // Do something for invite button click... lol
            // TODO: do something for real...
            Button invite_button = (Button) v.findViewById(R.id.inviteUsersButton);
            invite_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundleData = new Bundle();

                    bundleData.putString("hkey", hangout.get("hkey"));
                    bundleData.putString("title", hangout.get("title"));

                    Intent intent = new Intent(context, HangoutView.class);

                    // needed to start an activity outside of another activity
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    intent.putExtras(bundleData);
                    context.startActivity(intent);
                }
            });

            vh = new ViewHolder();

            v.setTag(vh);
        } else {
            vh = (ViewHolder) v.getTag();
        }

        /**
         * Params are as follows in the comment
         * ukeys  = (ukey1,ukey2,...)
         * display_names  = (display_name1,display_name2,...)
         */
//        hangoutMap.put("creator_name", creator.getString("display_name"));
//        hangoutMap.put("num_users", hangout.getString("num_users"));
//        hangoutMap.put("title", hangout.getString("title"));
//        hangoutMap.put("hkey", hangout.getString("hkey"));
//        hangoutMap.put("ukeys", ukeys);
//        hangoutMap.put("display_names", display_names);

        vh.title = (TextView) v.findViewById(R.id.title);
        vh.num_users = (TextView) v.findViewById(R.id.numUsers);
        vh.display_names = (TextView) v.findViewById(R.id.displayNames);
        vh.ukeys = (TextView) v.findViewById(R.id.ukeys);

//        TODO: split the comma spliced string into frist names and set the text
        vh.title.setText(hangout.get("title"));
        vh.num_users.setText(hangout.get("num_users") + " users");
        vh.display_names.setText(hangout.get("display_names"));
        vh.ukeys.setText(hangout.get("ukeys"));

        return v;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    /**
     * @see android.widget.Adapter#getCount()
     */
    public int getCount() {
        return hangouts.size();
    }

    /**
     * @see android.widget.Adapter#getItem(int)
     */
    public Object getItem(int position) {
        return hangouts.get(position);
    }

    /**
     * @see android.widget.Adapter#getItemId(int)
     */
    public long getItemId(int position) {
        return position;
    }
}