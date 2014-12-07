package com.thehub.thehubandroid;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

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

public class GetHangoutTask extends AsyncTask<String, Void, String> {
    private Context context;
    private ArrayList<HashMap<String, String>> usersArray;
    private Activity activity;
    private String ukey;
    private String akey;
    private HangoutFriendsListAdapter adapter;

    public GetHangoutTask(Context context, Activity activity, ArrayList<HashMap<String, String>> usersArray) {
        this.context = context;
        this.activity = activity;
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
        String responseString = null;

        ukey = params[1];
        akey = params[2];

        // set header and params
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
                //Closes the connection.
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        } catch (ClientProtocolException e) {
            //TODO Handle problems..
            responseString = "CPE";

        } catch (IOException e) {
            //TODO Handle problems..
            responseString = "IOException " + e.toString();
        }
        return responseString;
    }

    protected void onPostExecute(String response) {
//        Toast.makeText(context, "RESPONSE = " + response, Toast.LENGTH_SHORT)
//                .show();

        // TODO: show more options for the creator
        JSONArray usersJsonArray;
        JSONObject hangout;
        JSONObject user;
        String num_users = "";
        String title = "";
        // get number of users
        try {
            hangout = new JSONObject(response);
            usersJsonArray = hangout.getJSONArray("users");
            num_users = hangout.getString("num_users");
            title = hangout.getString("title");

            for (int i = 0; i < usersJsonArray.length(); i++) {
                HashMap<String, String> userMap = new HashMap<String, String>();
                try {
                    user = usersJsonArray.getJSONObject(i);

                    userMap.put("display_name", user.getString("display_name"));
                    userMap.put("picture_url", user.getString("picture_url"));
                    userMap.put("ukey", user.getString("ukey"));
                } catch (JSONException e) {
                    Log.i("DEBUG", "JSONException: " + e.toString());
                }
                usersArray.add(userMap);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.i("DEBUG", "JSONException: " + e.toString());
        }

        ListView listView = (ListView) activity.findViewById(R.id.listView);
        TextView numUsers = (TextView) activity.findViewById(R.id.numUsers);
        TextView Title = (TextView) activity.findViewById(R.id.title);
        Title.setText(title);
        numUsers.setText(num_users);

        adapter = new HangoutFriendsListAdapter(context, usersArray, R.layout.hangout_activity);
        listView.setAdapter(adapter);
    }
}