package com.thehub.thehubandroid;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InviteToHangTask extends AsyncTask<String, Void, String> {
    private Context context;
    private String ukey;
    private String akey;
    private String title;
    private ArrayList<String> friend_ukeys;

    public InviteToHangTask(Context context_, ArrayList<String> friend_ukeys_) {
        context = context_;
        friend_ukeys = friend_ukeys_;
    }

    @Override
    protected String doInBackground(String... params) {
        BufferedReader inBuffer = null;
        String url = params[0];
        String result = "fail";

        /**
         *   0    1      2      3
         * {url, ukey, akey,  title}
         */
        try {
            // Get info from params
            ukey = params[1];
            akey = params[2];
            title = params[3];

            HttpClient httpClient = new DefaultHttpClient();
            HttpPost request = new HttpPost(url);
            List<NameValuePair> postParameters = new ArrayList<NameValuePair>();

            // Add post params
            for(String s : friend_ukeys) {
                if(s != null && !s.isEmpty()) {
                    postParameters.add(new BasicNameValuePair("invite_ukey", s));
                }
            }
            // TODO: passing in the current user's ukey here, and not in the backend
            postParameters.add(new BasicNameValuePair("title", title));

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

        // Todo: take me to the new hangout

        // Open new activity with hangout info

//        JSONObject availability;
//        try {
//            availability = new JSONObject(response);
//            available = availability.getString("available");
//            activity_level = availability.getString("activity_level");
//
//            // Update view accordingly
//            TextView avail_text = (TextView) activity.findViewById(R.id.availText);
//            SeekBar activity_level_slider = (SeekBar) activity.findViewById(R.id.activity_level);
//            RelativeLayout background = (RelativeLayout) activity.findViewById(R.id.editBackground);
//
//            if(available.equals(Utils.FREE)) {
//                background.setBackgroundColor(Color.parseColor("#05800B"));
//                activity_level_slider.setProgress(Integer.parseInt(activity_level));
//                avail_text.setText(Utils.FREE_MESSAGE);
//            } else if(available.equals(Utils.BUSY)) {
//                background.setBackgroundColor(Color.parseColor("#ed1919"));
//                activity_level_slider.setProgress(0);
//                avail_text.setText(Utils.BUSY_MESSAGE);
//            } else {
//                Toast.makeText(context, "Illegal availability: " + available, Toast.LENGTH_LONG).show();
//            }
//            Toast.makeText(context, "Updated successfully!", Toast.LENGTH_SHORT).show();
//
//            // Go back to other screen
//            activity.finish();
//        } catch (JSONException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//            Toast.makeText(context, "Error Updating.\n", Toast.LENGTH_LONG).show();
//        }
    }
}