package com.thehub.thehubandroid;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class GetCurrentUserTask extends AsyncTask<String, Void, String> {
    private Context context;
    private String ukey;
    private String akey;
    private Activity activity;

    public GetCurrentUserTask(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    @Override
    protected String doInBackground(String... params) {
        /**
         *   0    1     2
         * {url, ukey, akey}
         */
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet request = new HttpGet(params[0]);
        HttpResponse response = null;
        String responseString = "";

        ukey = params[1];
        akey = params[2];

        // set header
        String source = ukey + ":" + akey;
        request.setHeader("Authorization", "Basic " + Base64.encodeToString(source.getBytes(), Base64.URL_SAFE | Base64.NO_WRAP));
        try {
            response = httpclient.execute(request);
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                out.close();
                responseString = out.toString();
            } else {
                // Closes the connection and saves response. Throws IOE exception
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                out.close();
                responseString = out.toString();

                throw new IOException(statusLine.getReasonPhrase());
            }
        } catch (ClientProtocolException e) {
            responseString = "CPE";
        } catch (IOException e) {
            // Already handled above
        }
        return responseString;
    }

    protected void onPostExecute(String response) {
//        Toast.makeText(context, "RESPONSE = " + response, Toast.LENGTH_LONG).show();

        JSONObject user;
        JSONObject availability;
        try {
            user = new JSONObject(response);
            availability = user.getJSONObject("availability");
            String available = availability.getString("available");
            int activity_level = availability.getInt("activity_level");
            String expire_hrs = availability.getString("expire_hrs");
            String expire_min = availability.getString("expire_min");

            TextView avail_text = (TextView) activity.findViewById(R.id.availText);
            SeekBar activity_level_bar = (SeekBar) activity.findViewById(R.id.activity_level);
            RelativeLayout background = (RelativeLayout) activity.findViewById(R.id.editBackground);
            RelativeLayout avail_settings = (RelativeLayout) activity.findViewById(R.id.availSettings);
            Button expire_button = (Button) activity.findViewById(R.id.expire_button);
            Button update_activitylv_button = (Button) activity.findViewById(R.id.update_button);
            ImageView view = (ImageView) activity.findViewById(R.id.availPic);
            TextView time_text = (TextView)activity.findViewById(R.id.time_text);

            // hidden inputs for hours and minutes
            TextView hrs = (TextView) activity.findViewById(R.id.hrs);
            TextView min = (TextView) activity.findViewById(R.id.min);

            String expite_text = Utils.EXPIRE_ROOT_TEXT;
            // TODO: this should be a utils method
            if(expire_hrs.equals("-1") && expire_min.equals("-1")) {
                time_text.setText(Utils.NO_EXPIRE_TEXT);
            } else {
                if(!expire_hrs.equals("-1")) {
                    // set hidden inputs
                    hrs.setText(expire_hrs);

                    expite_text += expire_hrs;
                    if(expire_hrs == "1") {
                        expite_text += " hour ";
                    } else {
                        expite_text += " hours ";
                    }
                }
                if(!expire_min.equals("-1")) {
                    // set hidden inputs
                    min.setText(expire_min);

                    expite_text += expire_min;
                    expite_text += " min";
                }
               time_text.setText(expite_text);
            }

            if(available.equals(Utils.FREE)) {
                avail_text.setText(Utils.FREE_MESSAGE);
                view.setImageResource(R.drawable.free_icon);
                update_activitylv_button.setVisibility(View.VISIBLE);
                //background.setBackgroundColor(Color.parseColor("#05800B"));
                expire_button.setVisibility(View.VISIBLE);
                avail_text.setVisibility(View.VISIBLE);
                time_text.setVisibility(View.VISIBLE);
                activity_level_bar.setProgress(activity_level);
            } else if(available.equals(Utils.BUSY)) {
                view.setImageResource(R.drawable.busy_icon);
                //background.setBackgroundColor(Color.parseColor("#ffed1919"));
                activity_level_bar.setProgress(activity_level);
                avail_text.setText(Utils.BUSY_MESSAGE);
                avail_settings.setVisibility(View.INVISIBLE);
                avail_text.setVisibility(View.VISIBLE);
                time_text.setVisibility(View.INVISIBLE);
                update_activitylv_button.setVisibility(View.INVISIBLE);
                expire_button.setVisibility(View.INVISIBLE);
//                activity_level_picker.setValue(0);
            } else {
                Toast.makeText(context, "Illegal availability: " + available, Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(context, "Error fetching friends list.\n", Toast.LENGTH_SHORT).show();
        }

    }
}
