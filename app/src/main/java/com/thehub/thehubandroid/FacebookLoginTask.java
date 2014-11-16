package com.thehub.thehubandroid;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
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

public class FacebookLoginTask extends AsyncTask<String, Void, String> {
	private Context context;
	private String user_id;
	private String access_token;
	private String expire;

	public FacebookLoginTask(Context context) {
		this.context = context;
	}

	@Override
	protected String doInBackground(String... params) {
		BufferedReader inBuffer = null;
		String url = params[0];
		String result = "fail";

        /**
         *   0    1           2             3
         * {url, user_id, access_token, expire_string}
         */
		try {

			// Get info from params
			user_id = params[1];
			access_token = params[2];
			expire = params[3];

			HttpClient httpClient = new DefaultHttpClient();
			HttpPost request = new HttpPost(url);
			List<NameValuePair> postParameters = new ArrayList<NameValuePair>();

            // Add post params
			postParameters.add(new BasicNameValuePair("user_id", user_id));
			postParameters.add(new BasicNameValuePair("access_token", access_token));
			postParameters.add(new BasicNameValuePair("expires", expire));

			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
					postParameters);

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
//                .show();

//		Toast.makeText(context, "user_id = " + user_id + " access_t = " +
//		access_token + " exp = " + expire, Toast.LENGTH_LONG).show();
		JSONObject resp;
		JSONObject user;
		String akey = "";
		String ukey = "";
		SharedPreferences hubprefs = context.getSharedPreferences(Utils.PREFS_FILE, Context.MODE_MULTI_PROCESS);
		SharedPreferences.Editor hubprefsEditor = hubprefs.edit();
		try {
			resp = new JSONObject(response);
			user = resp.getJSONObject("user");

			akey = resp.getString("akey");
			ukey = user.getString("ukey");

			// Save our keys
			hubprefsEditor.putString("akey", akey);
			hubprefsEditor.putString("ukey", ukey);
			hubprefsEditor.commit();

            String source = ukey+":"+akey;

            // TODO: Delete this logging
            Log.i("DEBUG", "Basic " + Base64.encodeToString(source.getBytes(), Base64.URL_SAFE | Base64.NO_WRAP));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Toast.makeText(context, "Error Authenticating.\n", Toast.LENGTH_LONG).show();
		}
	}
}