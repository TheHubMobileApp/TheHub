package com.thehub.thehubandroid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
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

public class RespondToPushTask extends AsyncTask<String, Void, String> {
    private Context context;
    private String ukey;
    private String akey;
    private String user_response;
    private Activity activity;

    public RespondToPushTask(Context _contex, Activity _activity) {
        this.context = _contex;
        this.activity = _activity;
    }

    @Override
    protected String doInBackground(String... params) {
        BufferedReader inBuffer = null;
        String url = params[0];
        String result = "fail";

        /**
         *   0    1      2      3
         * {url, ukey, akey, response}
         */
        try {
            // Get info from params
            ukey = params[1];
            akey = params[2];
            user_response = params[3];

            HttpClient httpClient = new DefaultHttpClient();
            HttpPost request = new HttpPost(url);
            List<NameValuePair> postParameters = new ArrayList<NameValuePair>();

            // TODO: passing in the current user's ukey here, and not in the backend
            postParameters.add(new BasicNameValuePair("response", user_response));

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
//		Toast.makeText(context, "RESPONSE = " + response, Toast.LENGTH_LONG)
//				.show();

        // Take me to the new hangout
        JSONObject hangout;
        String title = "";
        String hkey = "";
        // get number of users
        try {
            hangout = new JSONObject(response);
            title = hangout.getString("title");
            hkey = hangout.getString("hkey");

            Bundle bundleData = new Bundle();

            bundleData.putString("hkey", hkey);
            bundleData.putString("title", title);
            Intent intent = new Intent(context, HangoutView.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtras(bundleData);

            if (user_response.equals("accept")) {
                Toast.makeText(context, "Hangout joined!", Toast.LENGTH_SHORT).show();
                context.startActivity(intent);
            } else {
                Toast.makeText(context, "Hangout denied.", Toast.LENGTH_SHORT).show();
                activity.finish();
            }
        } catch (JSONException e) {
            e.printStackTrace();
//            Log.i("DEBUG", "JSONException: " + e.toString());
            Toast.makeText(context, "Error joining hangout.", Toast.LENGTH_SHORT).show();
            activity.finish();
        }
    }
}