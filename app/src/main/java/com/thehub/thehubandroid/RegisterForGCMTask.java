package com.thehub.thehubandroid;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.ListView;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RegisterForGCMTask extends AsyncTask<String, Void, String> {
    private Context context;
    private ArrayList<HashMap<String, String>> hangoutsArray;
    private ListView listView;
    private String ukey;
    private String akey;
    private HangoutListAdapter adapter;
    private GoogleCloudMessaging gcm;
    private String regID;

    public RegisterForGCMTask(Context context, String regID) {
        this.context = context;
        this.regID = regID;
    }

    /**
     *   0     1     2
     * {url, ukey, akey}
     */
    @Override
    protected String doInBackground(String... params) {
        BufferedReader inBuffer = null;
        String url = params[0];
        String result = "fail";

        try {
            if (gcm == null) {
                gcm = GoogleCloudMessaging.getInstance(context);
            }
            regID = gcm.register(Utils.SENDER_ID);
            result = "Device registered, registration ID = " + regID;
            Log.i("DEBUG", result);

            ukey = params[1];
            akey = params[2];

            // You should send the registration ID to your server over HTTP,
            // so it can use GCM/HTTP or CCS to send messages to your app.
            // The request to your server should be authenticated if your app
            // is using accounts.
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost request = new HttpPost(url);
            List<NameValuePair> postParameters = new ArrayList<NameValuePair>();

            // Add post params
            postParameters.add(new BasicNameValuePair("regID", regID));

            // set header
            String source = ukey + ":" + akey;
            request.setHeader("Authorization", "Basic " + Base64.encodeToString(source.getBytes(),
                    Base64.URL_SAFE | Base64.NO_WRAP));

            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParameters);
            request.setEntity(formEntity);
            HttpResponse r = httpClient.execute(request);
            result = EntityUtils.toString(r.getEntity());

            // Persist the regID - no need to register again.
            Utils.storeRegistrationId(context, regID);
        } catch (ClientProtocolException e) {
            // TODO: Handle exception
            result = "CPE " + e.toString();
        } catch (IOException ex) {
            result = "Error :" + ex.getMessage();
            // If there is an error, don't just keep trying to register.
            // Require the user to click a button again, or perform
            // exponential back-off.
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

    @Override
    protected void onPostExecute(String msg) {
        Log.i("DEBUG", "Registered user = " + msg);
    }

//    protected void onPostExecute(String response) {
////        Toast.makeText(context, "RESPONSE = " + response, Toast.LENGTH_SHORT)
////                .show();
//
//        // Toast.makeText(context, "user_id = " + user_id + " access_t = " +
//        // access_token + " exp = " + expire, Toast.LENGTH_LONG).show();
//        JSONObject resp;
//        JSONArray hangoutsJsonArray, usersArray;
//        JSONObject hangout;
//        JSONObject creator;
//        JSONObject user;
//        try {
//            resp = new JSONObject(response);
//            hangoutsJsonArray = resp.getJSONArray("hangouts");
//
//            for (int i = 0; i < hangoutsJsonArray.length(); i++) {
//                HashMap<String, String> hangoutMap = new HashMap<String, String>();
//
//                /**
//                 * THIS MAY BE INSECURE?
//                 *
//                 * ukeys = (ukey1,ukey2...)
//                 * display_names = (display1,display2,...)
//                 */
//                String ukeys = "";
//                String display_names = "";
//                try {
//                    hangout = hangoutsJsonArray.getJSONObject(i);
//                    creator = hangout.getJSONObject("creator");
//                    usersArray = hangout.getJSONArray("users");
//
//                    hangoutMap.put("creator_name", creator.getString("display_name"));
//                    hangoutMap.put("num_users", hangout.getString("num_users"));
//                    hangoutMap.put("title", hangout.getString("title"));
//                    hangoutMap.put("hkey", hangout.getString("hkey"));
//
////                    hangoutMap.put("private", hangout.getString("private"));
//
//                    for (int j = 0; j < usersArray.length(); j++) {
//                        user = usersArray.getJSONObject(j);
//                        ukeys += user.getString("ukey") + ",";
//                        display_names += user.getString("display_name") + ",";
//                    }
//
//                    hangoutMap.put("ukeys", ukeys);
//                    hangoutMap.put("display_names", display_names);
//
//                    // TODO: DELETE
//                    Log.i("DEBUG", ukeys);
//                    Log.i("DEBUG", display_names);
//                } catch (JSONException e) {
//                    Log.i("DEBUG", "ERROR: " + e.toString());
////                    Toast.makeText(context, "OOPS, JSON PROBLEM from map", Toast.LENGTH_LONG).show();
//                }
////                Log.i("DEBUG", hangoutMap.toString());
//                hangoutsArray.add(hangoutMap);
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//            Log.i("DEBUG", "JSONException: " + e.toString());
//        }
//
//        adapter = new HangoutListAdapter(context, hangoutsArray, R.layout.hangout_list);
//        listView.setAdapter(adapter);
//    }
}