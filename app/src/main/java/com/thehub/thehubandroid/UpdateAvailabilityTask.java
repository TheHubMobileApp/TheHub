package com.thehub.thehubandroid;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Base64;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UpdateAvailabilityTask extends AsyncTask<String, Void, String> {
    private Context context;
    private String available;
    private String ukey;
    private String akey;
    private Activity activity;
    private String activity_level;

    public UpdateAvailabilityTask(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    @Override
    protected String doInBackground(String... params) {
        BufferedReader inBuffer = null;
        String url = params[0];
        String result = "fail";

        /**
         *   0       1        2    3            4
         * {url, available, ukey, akey, activity_level}
         */
        try {
            // Get info from params
            available = params[1];
            ukey = params[2];
            akey = params[3];
            activity_level = params[4];


            HttpClient httpClient = new DefaultHttpClient();
            HttpPost request = new HttpPost(url);
            List<NameValuePair> postParameters = new ArrayList<NameValuePair>();

            // Add post params
            postParameters.add(new BasicNameValuePair("available", available));
            postParameters.add(new BasicNameValuePair("activity_level", activity_level));

            // set header
            String source = ukey + ":" + akey;
            request.setHeader("Authorization", "Basic " + Base64.encodeToString(source.getBytes(),
                    Base64.URL_SAFE | Base64.NO_WRAP));

            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParameters);
            request.setEntity(formEntity);
            HttpResponse r = httpClient.execute(request);
            result = EntityUtils.toString(r.getEntity());
        } catch (Exception e) {
            // Do something about exceptions
            result = e.getMessage();
        } finally {
            if (inBuffer != null) {
                try {
                    inBuffer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    protected void onPostExecute(String response) {
//		Toast.makeText(context, "RESPONSE = " + response, Toast.LENGTH_SHORT)
//				.show();

        JSONObject availability;
        try {
            availability = new JSONObject(response);
            available = availability.getString("available");

            // Update view accordingly
            TextView avail_text = (TextView) activity.findViewById(R.id.availText);
            RelativeLayout background = (RelativeLayout) activity.findViewById(R.id.editBackground);
            if(available.equals(Utils.FREE)) {
                background.setBackgroundColor(Color.parseColor("#05800B"));
                avail_text.setText(Utils.FREE_MESSAGE);
            } else if(available.equals(Utils.BUSY)) {
                background.setBackgroundColor(Color.parseColor("#ed1919"));
                avail_text.setText(Utils.BUSY_MESSAGE);
            } else {
                Toast.makeText(context, "Illegal availability: " + available, Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(context, "Error Updating.\n", Toast.LENGTH_LONG).show();
        }
    }
}