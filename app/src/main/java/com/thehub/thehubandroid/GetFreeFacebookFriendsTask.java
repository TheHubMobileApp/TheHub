package com.thehub.thehubandroid;

import android.app.Activity;
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

/**
 * Created by David on 12/1/2014.
 */
public class GetFreeFacebookFriendsTask extends AsyncTask<String, Void, String> {
    private Context context;
    private ArrayList<HashMap<String, String>> usersArray;
    private ListView listView;
    private String ukey;
    private String akey;
    private NewHangoutFriendAdapter adapter;
    private Activity parent;
    private String invited_ukey;

    public GetFreeFacebookFriendsTask(Context context, ListView listview, ArrayList<HashMap<String, String>> usersArray, Activity parent_, String invited_ukey) {
        this.context = context;
        this.listView = listview;
        this.usersArray = usersArray;
        this.invited_ukey = invited_ukey;
        parent = parent_;
    }

    @Override
    protected String doInBackground(String... params) {
        /**
            0   1   2
         {url,ukey,akey}
         */

        HttpClient httpclient = new DefaultHttpClient();
        HttpGet request = new HttpGet(params[0]);
        HttpResponse response = null;
        String responseString = "";

        ukey = params[1];
        akey = params[2];

        // set header
        String auth = ukey + ":" + akey;
        request.setHeader("Authorization", "Basic " + Base64.encodeToString(auth.getBytes(), Base64.URL_SAFE | Base64.NO_WRAP));
        try {
            response = httpclient.execute(request);
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                out.close();
                responseString = out.toString();
            } else {
                // Close the connection and save response. Throw IOE exception
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                out.close();
                responseString = out.toString();

                throw new IOException(statusLine.getReasonPhrase());
            }
        } catch (ClientProtocolException e) {
            responseString = "CPE";
        } catch (IOException e) {
            // already handled above
        }
        return responseString;
    }

    protected void onPostExecute(String response) {
        JSONObject resp;
        JSONArray usersJsonArray;
        JSONObject user;
        try {
            resp = new JSONObject(response);
            usersJsonArray = resp.getJSONArray("users");
            for (int i = 0; i < usersJsonArray.length(); ++i) {
                HashMap<String, String> userMap = new HashMap<String, String>();
                try {
                    user = usersJsonArray.getJSONObject(i);
                    JSONObject availability = new JSONObject(user.getString("availability"));
                    Log.i("DEBUG", "Line 98 " + user.getString("availability"));


                    // busy users are not given in response so just go ahead and add them
                    userMap.put("display_name", user.getString("display_name"));
                    userMap.put("availability", availability.toString());
                    userMap.put("activity_level", availability.getString("activity_level"));
                    userMap.put("activity_name", availability.getString("activity_name"));
                    userMap.put("picture_url", user.getString("picture_url"));
                    userMap.put("ukey", user.getString("ukey"));
                    if (user.getString("ukey").equals(invited_ukey)) {
                        // this user was already invited and shouldn't be displayed
                        Toast.makeText(context, user.getString("display_name") + " added to invite list!\nSelect other to invite.", Toast.LENGTH_SHORT).show();
                        continue;
                    } else if (user.getString("ukey").equals(ukey)) {
                        // skip yourself
                        continue;
                    }

                } catch (JSONException e) {
                    Toast.makeText(context, "Unable to get friends", Toast.LENGTH_LONG).show();
                }
                usersArray.add(userMap);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(context, "Error fetching friends list.\n", Toast.LENGTH_LONG).show();
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }

        adapter = new NewHangoutFriendAdapter(context, usersArray, R.layout.new_hangout_activity, parent);
        listView.setAdapter(adapter);
    }
}
