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
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 12/5/2014.
 */
public class RemoveFromHangoutTask extends AsyncTask<String, Void, String> {
    private Context context;

    public RemoveFromHangoutTask(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... params) {
        /**
         *   0     1     2    3
         * {url, akey, ukey, hkey}
         */

        String result = "fail";
        BufferedReader inBuffer = null;

        try {
            // fetch info from params
            String url = params[0];
            String akey = params[1];
            String ukey = params[2];
            String hkey = params[3];

            HttpClient httpClient = new DefaultHttpClient();
            HttpPost request = new HttpPost(url);
            List<NameValuePair> postParams = new ArrayList<NameValuePair>();

            postParams.add(new BasicNameValuePair("remove_ukey", ukey));
            postParams.add(new BasicNameValuePair("hkey", hkey));

            // set header
            String source = ukey + ":" + akey;
            request.setHeader("Authorization", "Basic " + Base64.encodeToString(source.getBytes(),
                    Base64.URL_SAFE | Base64.NO_WRAP));

            request.setEntity(new UrlEncodedFormEntity(postParams));

            HttpResponse r = httpClient.execute(request);
            result = EntityUtils.toString(r.getEntity());
        } catch (Exception e) {
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
        Log.i("DEBUG", response);
    }
}
