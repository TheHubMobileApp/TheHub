package com.thehub.thehubandroid;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class GetFacebookFriendsTask extends AsyncTask<String, Void, String> {
    private Context context;
    private ArrayList<HashMap<String, String>> usersArray;
    private ListView listView;
    private String ukey;
    private String akey;
    private FriendsListAdapter adapter;

    public GetFacebookFriendsTask(Context context, ListView listview, ArrayList<HashMap<String, String>> usersArray) {
        this.context = context;
        this.listView = listview;
        this.usersArray = usersArray;
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

        // set header and params
        String source = ukey + ":" + akey;

        // TODO: delete
        Log.i("DEBUG", source);
        Log.i("DEBUG", params[0]);

        request.setHeader("Authorization", "Basic " + Base64.encodeToString(source.getBytes(), Base64.URL_SAFE | Base64.NO_WRAP));
        try {
            Log.i("DEBUG", "1");
            response = httpclient.execute(request);
            StatusLine statusLine = response.getStatusLine();
            Log.i("DEBUG", "2");
            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                Log.i("DEBUG", "3");
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

        JSONObject resp;
        JSONArray usersJsonArray;
        JSONObject user;
        try {
            resp = new JSONObject(response);
            usersJsonArray = resp.getJSONArray("users");
            for (int i = 0; i < usersJsonArray.length(); i++) {
                HashMap<String, String> userMap = new HashMap<String, String>();
                try {
                    user = usersJsonArray.getJSONObject(i);

                    userMap.put("availability", "free");
                    userMap.put("display_name", user.getString("name"));
                    userMap.put("picture_url", user.getString("picture"));

                } catch (JSONException e) {
                    Toast.makeText(context, "Unable to get friends", Toast.LENGTH_LONG).show();
                }
                usersArray.add(userMap);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(context, "Error fetching friends list.\n", Toast.LENGTH_LONG).show();
        }

        // TODO: Make this for the other list view eventually
        adapter = new FriendsListAdapter(context, usersArray, R.layout.user_list);
        listView.setAdapter(adapter);
    }
}
