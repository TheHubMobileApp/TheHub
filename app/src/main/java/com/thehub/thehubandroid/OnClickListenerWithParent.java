package com.thehub.thehubandroid;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OnClickListenerWithParent implements View.OnClickListener
{
    private Activity parent_activity;
    private Context context;

    public OnClickListenerWithParent(Activity parent_activity, Context context) {
        this.parent_activity = parent_activity;
        this.context = context;
    }

    @Override
    public void onClick(View v)
    {
        TextView textview = (TextView) parent_activity.findViewById(R.id.ukeys);
        String raw_ukeys = textview.getText().toString();
        // FIXME: this seems convoluted, im trying to just get an arraylist of strings from raw ukeys
        ArrayList<String> ukeys = new ArrayList<String>(Arrays.asList(raw_ukeys.split(",")));
        // send out the request
        EditText edittext = (EditText) parent_activity.findViewById(R.id.hangout_title);
        String title = edittext.getText().toString();
        // TODO: need some other way to do this shite
        User.inviteFriendToHang(context, ukeys, title);
        Toast.makeText(context, "Hangout successfully created!", Toast.LENGTH_SHORT).show();
        parent_activity.finish();
    }
}
