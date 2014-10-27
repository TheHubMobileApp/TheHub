package com.thehub.thehubandroid;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Base64;
import android.view.View;
import android.widget.RelativeLayout;
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

            TextView avail_text = (TextView) activity.findViewById(R.id.availText);
            RelativeLayout background = (RelativeLayout) activity.findViewById(R.id.editBackground);

//            Toast.makeText(context, "avail = " + available, Toast.LENGTH_LONG).show();

            if(available.equals(Utils.FREE)) {
                avail_text.setText(Utils.FREE_MESSAGE);
                background.setBackgroundColor(Color.parseColor("#05800B"));

                avail_text.setVisibility(View.VISIBLE);
            } else if(available.equals(Utils.BUSY)) {
                background.setBackgroundColor(Color.parseColor("#ffed1919"));
                avail_text.setText(Utils.BUSY_MESSAGE);

                avail_text.setVisibility(View.VISIBLE);
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
