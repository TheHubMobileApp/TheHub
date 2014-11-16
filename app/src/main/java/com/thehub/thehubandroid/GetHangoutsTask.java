package com.thehub.thehubandroid;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.ListView;

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

public class GetHangoutsTask extends AsyncTask<String, Void, String> {
    private Context context;
    private ArrayList<HashMap<String, String>> hangoutsArray;
    private ListView listView;
    private String ukey;
    private String akey;
    private HangoutListAdapter adapter;

    public GetHangoutsTask(Context context, ListView listview, ArrayList<HashMap<String, String>> hangoutsArray) {
        this.context = context;
        this.listView = listview;
        this.hangoutsArray = hangoutsArray;
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
        JSONArray hangoutsJsonArray, usersArray;
        JSONObject hangout;
        JSONObject creator;
        JSONObject user;
        try {
            resp = new JSONObject(response);
            hangoutsJsonArray = resp.getJSONArray("hangouts");

            for (int i = 0; i < hangoutsJsonArray.length(); i++) {
                HashMap<String, String> hangoutMap = new HashMap<String, String>();

                /**
                 * THIS MAY BE INSECURE?
                 *
                 * ukeys = (ukey1,ukey2...)
                 * display_names = (display1,display2,...)
                 */
                String ukeys = "";
                String display_names = "";
                try {
                    hangout = hangoutsJsonArray.getJSONObject(i);
                    creator = hangout.getJSONObject("creator");
                    usersArray = hangout.getJSONArray("users");

                    hangoutMap.put("creator_name", creator.getString("display_name"));
                    hangoutMap.put("num_users", hangout.getString("num_users"));
                    hangoutMap.put("title", hangout.getString("title"));
                    hangoutMap.put("hkey", hangout.getString("hkey"));

//                    hangoutMap.put("private", hangout.getString("private"));

                    for (int j = 0; j < usersArray.length(); j++) {
                        user = usersArray.getJSONObject(j);
                        ukeys += user.getString("ukey") + ",";
                        display_names += user.getString("display_name") + ",";
                    }

                    hangoutMap.put("ukeys", ukeys);
                    hangoutMap.put("display_names", display_names);

                    // TODO: DELETE
                    Log.i("DEBUG", ukeys);
                    Log.i("DEBUG", display_names);
                } catch (JSONException e) {
                    Log.i("DEBUG", "ERROR: " + e.toString());
//                    Toast.makeText(context, "OOPS, JSON PROBLEM from map", Toast.LENGTH_LONG).show();
                }
//                Log.i("DEBUG", hangoutMap.toString());
                hangoutsArray.add(hangoutMap);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.i("DEBUG", "JSONException: " + e.toString());
        }

        adapter = new HangoutListAdapter(context, hangoutsArray, R.layout.hangout_list);
        listView.setAdapter(adapter);
    }
}