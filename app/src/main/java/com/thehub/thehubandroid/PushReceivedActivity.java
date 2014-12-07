package com.thehub.thehubandroid;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PushReceivedActivity extends Activity {
    private Context context;
    private Button update_button;
    private TextView avail_text_view;
    private String avail_text;
    private String activity_level, activity_name;
    private EditText activity_name_field;
    private String hid, hangout_name, invited_by, invited_url;

//    final private String[] activity_strings = {
//        "Comatose",            // 0
//        "Hardly Moving",        // 1
//        "Mild signs of life",   // 2
//        "Minimal action",       // 3
//        "Slight walking",       // 4
//        "A little adventurous", // 5
//        "Mildly amped",         // 6
//        "Pretty geared",        // 7
//        "Ready to go",          // 8
//        "Down for marathons",   // 9
//        "Turnt to the maximum"  // 10
//    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.push_received_activity);
        context = getApplicationContext();

        Toast.makeText(context, "Push received!", Toast.LENGTH_LONG).show();
        Bundle extras = getIntent().getExtras();

        if(extras != null) {
            hid = extras.getString("hid");
            hangout_name = extras.getString("hangout_name");
            invited_by = extras.getString("invited_by");
            invited_url = extras.getString("invited_url");

            Toast.makeText(context,
                    "hid = " + hid +
                            "\nhangout_name = " +  hangout_name +
                            "\ninvited_by = " +  invited_by +
                            "\ninvited_url = " + invited_url,
                    Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context,
                    "No extras received.",
                    Toast.LENGTH_LONG).show();
        }


//        'data': {
//            # hangout.hid passed in
//            "hid": "dummyhid",
//            # hangout.name
//            "hangout_name": "dummy hangout",
//            # Display name of who invited you
//            "invited_by": "Robbie Small",
//            # url for pic of who invited you
//            "invited_url": "lol.jpg.com"
//        },
    }
}
