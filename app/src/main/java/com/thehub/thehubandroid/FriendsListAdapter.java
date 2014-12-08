package com.thehub.thehubandroid;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

public class FriendsListAdapter extends BaseAdapter {

    private Context context;
    private List<HashMap<String, String>> users;

    static class ViewHolder {
        ImageView profile_picture;
        TextView display_name;
        TextView ukey;
    }

//    public class CustomComparator implements Comparator<HashMap<String, String>> {
//        @Override
//        public int compare(HashMap<String, String> spotOne, HashMap<String, String> spotTwo) {
//            double distanceOne = Double.parseDouble(spotOne.get("distance"));
//            double distanceTwo = Double.parseDouble(spotTwo.get("distance"));
//            return Double.compare(distanceOne, distanceTwo);
//        }
//    }

    public FriendsListAdapter(Context context, List<HashMap<String, String>> users, int resource) {
        this.setContext(context);
//        Collections.sort(spots, new CustomComparator());
        this.users = users;
    }

    //public View newView(Context context, Cursor cursor, ViewGroup parent) {
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ViewHolder vh = null;
        final HashMap<String, String> user = users.get(position);
        String availability = user.get("availability");
        final String ukey = user.get("ukey");
//      activity_level stores either the activity_name or the activity_level
        String activity_level = user.get("activity_level");
        String activity_name = user.get("activity_name");

        if (v == null) {
            //Toast.makeText(context, "Populating... " + spot.get("name"), Toast.LENGTH_LONG).show();

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.user_list_item, null);

            // Do something for invite button click... lol
            // TODO: do something for real...
            Button invite_button = (Button) v.findViewById(R.id.inviteFriendButton);
            TextView activity_level_view = (TextView) v.findViewById(R.id.activityLevel);
            invite_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle extras = new Bundle();
                    extras.putString("friend_ukey", ukey);
                    // TODO: need an activity to select those who are available right now
                    Intent intent = new Intent(context, NewHangoutActivity.class);
                    intent.putExtras(extras);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });

            if (availability.equals(Utils.FREE)) {

                //v.setBackgroundColor(Color.parseColor(#83FF83));
                CircleImageView temp = (CircleImageView) v.findViewById(R.id.profPic);
                temp.setBorderColor(Color.parseColor("#69F0AE"));
                invite_button.setVisibility(View.VISIBLE);
                if (activity_name.equals("")) {
                    activity_level_view.setText("is at a " + activity_level + " out of 10");
                } else {
                    activity_level_view.setText("is " + activity_name);
                }
            } else if (availability.equals(Utils.BUSY)) {
                CircleImageView temp = (CircleImageView) v.findViewById(R.id.profPic);
                temp.setBorderColor(Color.parseColor("#FF7043"));
                invite_button.setVisibility(View.INVISIBLE);
            } else {
                // TODO: Remove this?
                Log.i("DEBUG", "Illegal availability");
            }
            vh = new ViewHolder();

            v.setTag(vh);
        } else {
            vh = (ViewHolder) v.getTag();
        }

        vh.profile_picture = (ImageView) v.findViewById(R.id.profPic);
        vh.display_name = (TextView) v.findViewById(R.id.displayName);
        vh.ukey = (TextView) v.findViewById(R.id.ukey);

        // Convert the dp value for xml to pixels (casted to int from float)
        int size = Utils.convertDpToPixel(100, context);

        String prof_pic = user.get("picture_url");
        Picasso.with(context)
                .load(prof_pic)
                .centerCrop()
                .resize(size, size)
                .placeholder(R.drawable.ic_launcher)
                .noFade()
                .into(vh.profile_picture);

        vh.display_name.setText(user.get("display_name"));
        vh.ukey.setText(user.get("ukey"));

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
        return users.size();
    }

    /**
     * @see android.widget.Adapter#getItem(int)
     */
    public Object getItem(int position) {
        return users.get(position);
    }

    /**
     * @see android.widget.Adapter#getItemId(int)
     */
    public long getItemId(int position) {
        return position;
    }
}