package com.thehub.thehubandroid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;


public class InviteFriendsListAdapter extends BaseAdapter {
    private Context context;
    private List<HashMap<String, String>> friends;

    static class ViewHolder {
        ImageView profile_picture;
        TextView display_name;
    }

    public InviteFriendsListAdapter(Context context, List<HashMap<String, String>> friends, int resource) {
        this.setContext(context);
        this.friends = friends;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ViewHolder vh = null;
        HashMap<String, String> friend = friends.get(position);

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.invite_list_item, null);

            // Do something for invite button click... lol
            // TODO: do something for real...
            ImageButton invite_button = (ImageButton) v.findViewById(R.id.inviteFriendButton);
            invite_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "Notification Sent!", Toast.LENGTH_SHORT).show();
                }
            });

            vh = new ViewHolder();

            v.setTag(vh);
        } else {
            vh = (ViewHolder) v.getTag();
        }

        vh.profile_picture = (ImageView) v.findViewById(R.id.profPic);
        vh.display_name = (TextView) v.findViewById(R.id.displayName);

        // Convert the dp value for xml to pixels (casted to int from float)
        int size = Utils.convertDpToPixel(100, context);

        String prof_pic = friend.get("picture_url");
        Picasso.with(context)
                .load(prof_pic)
                .centerCrop()
                .resize(size, size)
                .placeholder(R.drawable.ic_launcher)
                .noFade()
                .into(vh.profile_picture);

        vh.display_name.setText(friend.get("display_name"));

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
        return friends.size();
    }

    /**
     * @see android.widget.Adapter#getItem(int)
     */
    public Object getItem(int position) {
        return friends.get(position);
    }

    /**
     * @see android.widget.Adapter#getItemId(int)
     */
    public long getItemId(int position) {
        return position;
    }
}
