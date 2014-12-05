package com.thehub.thehubandroid;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

public class HangoutFriendsListAdapter extends BaseAdapter {

    private Context context;
    private List<HashMap<String, String>> users;

    static class ViewHolder {
        ImageView profile_picture;
        TextView display_name;
        TextView ukey;
    }

    public HangoutFriendsListAdapter(Context context, List<HashMap<String, String>> users, int resource) {
        this.setContext(context);
        this.users = users;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ViewHolder vh = null;
        final HashMap<String, String> user = users.get(position);
//        String availability = user.get("availability");

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.user_list_item, null);

//            if(availability.equals(Utils.FREE)) {
//                v.setBackgroundColor(Color.parseColor("#05800B"));
//            } else if (availability.equals(Utils.BUSY)) {
//                v.setBackgroundColor(Color.parseColor("#ed1919"));
//            } else {
//                // TODO: Remove this?
//                Log.i("DEBUG", "Illegal availability");
//            }
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
//        Log.i("DEBUG", "USER = " + user.toString());
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