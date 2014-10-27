package com.thehub.thehubandroid;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
        HashMap<String, String> user = users.get(position);

        if (v == null) {
            //Toast.makeText(context, "Populating... " + spot.get("name"), Toast.LENGTH_LONG).show();

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.user_list_item, null);

            vh = new ViewHolder();

            v.setTag(vh);
        } else {
            vh = (ViewHolder) v.getTag();
        }

        vh.profile_picture = (ImageView) v.findViewById(R.id.profPic);
        vh.display_name = (TextView) v.findViewById(R.id.displayName);

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