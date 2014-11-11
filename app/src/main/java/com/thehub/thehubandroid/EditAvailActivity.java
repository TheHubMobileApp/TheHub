package com.thehub.thehubandroid;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;

public class EditAvailActivity extends Activity {
    private Context context;
    private RelativeLayout background, avail_settings;
    private Button expire_button, update_button;
    private TextView avail_text_view, hrs, min;
    private String avail_text;
    private SeekBar activity_level_bar;
    private String activity_level, exp_hrs, exp_min;
    private CustomTimePickerDialog.OnTimeSetListener timeSetListener;
    final private String[] activity_strings = {
            // @robbie -> dave this is hilarious
        "Comatose",            // 0
        "Hardly Moving",        // 1
        "Mild signs of life",   // 2
        "Minimal action",       // 3
        "Slight walking",       // 4
        "A little adventurous", // 5
        "Mildly amped",         // 6
        "Pretty geared",        // 7
        "Ready to go",          // 8
        "Down for marathons",   // 9
        "Turnt to the maximum"  // 10
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_avail_activity);
        context = getApplicationContext();

        // Get current user information
        User.getCurrentUser(context, this);

        background = (RelativeLayout) findViewById(R.id.editBackground);
        avail_settings = (RelativeLayout) findViewById(R.id.availSettings);
        update_button = (Button) findViewById(R.id.update_button);
        expire_button = (Button) findViewById(R.id.expire_button);
        avail_text_view = (TextView) findViewById(R.id.availText);
        // placeholders for hours and minutes
        hrs = (TextView) findViewById(R.id.hrs);
        min = (TextView) findViewById(R.id.min);
        // set the activity level bar's min and max values
        activity_level_bar = (SeekBar) findViewById(R.id.activity_level);
        // note: note a typo
        activity_level_bar.setMax(0);
        activity_level_bar.setMax(10);
        // set the progress' numeric value
        final TextView progress_view = (TextView) findViewById(R.id.progress);
        progress_view.setText(activity_strings[activity_level_bar.getProgress()]);

        // create a listener to update progress' text when the SeekBar is changed
        activity_level_bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                // TODO Auto-generated method stub
//                TextView progress_view = (TextView) findViewById(R.id.progress);
                progress_view.setText(activity_strings[progress]);
            }
        });

        background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                avail_text = avail_text_view.getText().toString();

                if (avail_text.equals(Utils.BUSY_MESSAGE)) {
                    background.setBackgroundColor(Color.parseColor("#05800B"));
                    avail_settings.setVisibility(View.VISIBLE);
                    avail_text_view.setText(Utils.FREE_MESSAGE);
//                    User.updateAvailability(context, EditAvailActivity.this, Utils.FREE, activity_level);
                } else if (avail_text.equals(Utils.FREE_MESSAGE)) {
                    background.setBackgroundColor(Color.parseColor("#ed1919"));
                    avail_settings.setVisibility(View.INVISIBLE);
                    avail_text_view.setText(Utils.BUSY_MESSAGE);
//                    User.updateAvailability(context, EditAvailActivity.this, Utils.BUSY, activity_level);
                } else{
                    // TODO: get rid of this
//                    Toast.makeText(context, "illegal avail text: " + avail_text, Toast.LENGTH_SHORT).show();
                }
            }
        });

        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                avail_text = avail_text_view.getText().toString();
                activity_level = Integer.toString(activity_level_bar.getProgress());

                if (avail_text.equals(Utils.BUSY_MESSAGE)) {
                    User.updateAvailability(context, EditAvailActivity.this, Utils.BUSY, "0", "0", "0");
                } else if (avail_text.equals(Utils.FREE_MESSAGE)) {
                    User.updateAvailability(context, EditAvailActivity.this, Utils.FREE, activity_level, exp_hrs, exp_min);
                }
            }
        });

        timeSetListener = new CustomTimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String expite_text = "Exp: ";
                String default_exp_text = "No Expire Set";
                exp_hrs = Integer.toString(hourOfDay);
                exp_min = Integer.toString(minute);

                // set hidden inputs
                hrs.setText(exp_hrs);
                min.setText(exp_min);

                // Set to default text if set to 0:0 otherwise set the string
                if(hourOfDay == 0 && minute == 0) {
                    expire_button.setText(default_exp_text);
                } else {
                    if(hourOfDay != 0) {
                        expite_text += Integer.toString(hourOfDay);
                        if(hourOfDay == 1) {
                            expite_text += " hour ";
                        } else {
                            expite_text += " hours ";
                        }
                    }
                    if(minute != 0) {
                        expite_text += Integer.toString(minute);
                        expite_text += " min";
                    }
                    expire_button.setText(expite_text);
                }
            }
        };

        expire_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // makes sure to get the strings from hidden inputs to set time
                CustomTimePickerDialog timePickerDialog = new CustomTimePickerDialog(
                        EditAvailActivity.this, timeSetListener, Integer.parseInt(hrs.getText().toString()), Integer.parseInt(min.getText().toString()), true);
                timePickerDialog.setTitle("Set expire time or leave empty");
                timePickerDialog.show();
            }
        });

    }
}
