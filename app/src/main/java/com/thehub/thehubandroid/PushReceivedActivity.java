package com.thehub.thehubandroid;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class PushReceivedActivity extends Activity {
    private Context context;
    private Button accept_button, deny_button;
    private TextView title, invite_text;
    private ImageView friend_circle, hub_logo;
    private String hkey, hangout_name, invited_by, invited_url;
    private Activity activity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.push_received_activity);
        activity = this;
        context = getApplicationContext();
        title = (TextView) findViewById(R.id.title);
        invite_text = (TextView) findViewById(R.id.inviteText);
        friend_circle = (ImageView) findViewById(R.id.friendCircle);
        accept_button = (Button) findViewById(R.id.acceptButton);
        deny_button = (Button) findViewById(R.id.denyButton);

//        Toast.makeText(context, "Push received!", Toast.LENGTH_LONG).show();
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            hkey = extras.getString("hid");
            hangout_name = extras.getString("hangout_name");
            invited_by = extras.getString("invited_by");
            invited_url = extras.getString("invited_url");

//            Toast.makeText(context,
//                    "hkey = " + hkey +
//                            "\nhangout_name = " + hangout_name +
//                            "\ninvited_by = " + invited_by +
//                            "\ninvited_url = " + invited_url,
//                    Toast.LENGTH_LONG).show();

            title.setText(hangout_name);
            invite_text.setText("You were invited to hang by " + invited_by + ".");
            friend_circle = (ImageView) findViewById(R.id.friendCircle);

            /**
             * Convert the dp value for xml to pixels (casted to int from float)
             * and load the {@code invited_url} into {@code friend_circle}
             */
            int size = Utils.convertDpToPixel(100, context);
            Picasso.with(context)
                    .load(invited_url)
                    .centerCrop()
                    .resize(size, size)
                    .placeholder(R.drawable.ic_launcher)
                    .noFade()
                    .into(friend_circle);

            accept_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    User.respondToInvite(context, hkey, "accept", activity);
                }
            });
            deny_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    User.respondToInvite(context, hkey, "deny", activity);
                }
            });

        } else {
            Toast.makeText(context,
                    "No extras received.",
                    Toast.LENGTH_LONG).show();
        }
    }
}
