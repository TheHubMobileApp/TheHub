package com.thehub.thehubandroid;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class EditAvailActivity extends Activity {
    private Context context;
    private RelativeLayout background;
    private TextView avail_text_view;
    private String avail_text;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_avail_activity);
        context = getApplicationContext();

        // Get current user information
        User.getCurrentUser(context, this);

        background = (RelativeLayout) findViewById(R.id.editBackground);
        avail_text_view = (TextView) findViewById(R.id.availText);

        background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                avail_text = avail_text_view.getText().toString();

                if (avail_text.equals(Utils.BUSY_MESSAGE)) {
                    User.updateAvailability(context, EditAvailActivity.this, Utils.FREE);
                } else if (avail_text.equals(Utils.FREE_MESSAGE)) {
                    User.updateAvailability(context, EditAvailActivity.this, Utils.BUSY);
                } else{
//                    Toast.makeText(context, "illegal avail text: " + avail_text, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
