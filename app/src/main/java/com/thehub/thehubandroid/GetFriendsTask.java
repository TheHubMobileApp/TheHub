package com.thehub.thehubandroid;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Base64;
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

public class GetFriendsTask extends AsyncTask<String, Void, String> {
    private Context context;
    private ArrayList<HashMap<String, String>> usersArray;
    private ListView listView;
    private String ukey;
    private String akey;
    private FriendsListAdapter adapter;

    public GetFriendsTask(Context context, ListView listview, ArrayList<HashMap<String, String>> usersArray) {
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
            responseString = "IOE";
        }
        return responseString;
    }

    protected void onPostExecute(String response) {
//        Toast.makeText(context, "RESPONSE = " + response, Toast.LENGTH_SHORT)
//                .show();

        // Toast.makeText(context, "user_id = " + user_id + " access_t = " +
        // access_token + " exp = " + expire, Toast.LENGTH_LONG).show();
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
                    JSONObject avail = user.getJSONObject("availability");

                    userMap.put("availability", avail.getString("available"));
                    userMap.put("activity_level", avail.getString("activity_level"));
                    userMap.put("activity_name", avail.getString("activity_name"));
                    userMap.put("display_name", user.getString("display_name"));
                    userMap.put("picture_url", user.getString("picture_url"));
                    userMap.put("ukey", user.getString("ukey"));
                    if (user.getString("ukey").equals(ukey)) {
                        // skip yourself
                        continue;
                    }

//                    Log.i("DEBUG", "Picture url into map = " + user.getString("picture_url"));
                } catch (JSONException e) {
                    //Toast.makeText(context, "OOPS, JSON PROBLEM from map", Toast.LENGTH_LONG).show();
                }
//                Log.i("DEBUG", userMap.toString());
//                Log.i("DEBUG", usersArray.toString());
                usersArray.add(userMap);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(context, "Error Authenticating.\n", Toast.LENGTH_LONG).show();
        }

        adapter = new FriendsListAdapter(context, usersArray, R.layout.user_list);
        listView.setAdapter(adapter);
    }
}