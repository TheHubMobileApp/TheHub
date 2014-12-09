package com.thehub.thehubandroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.FacebookException;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.facebook.widget.LoginButton.OnErrorListener;

import java.util.Arrays;
import java.util.Date;

public class MainFragment extends Fragment {
    // For FaceBook oauth
    private Session mSession = null;
    private UiLifecycleHelper uiHelper;
    private String access_token;
    private int expire;
    private String user_id;
    private Session sesh;
    private View rootView;
    private Button view_list_button;
    private boolean authed = false;

    private void onSessionStateChange(Session session, SessionState state,
                                      Exception exception) {
        if (state.isOpened()) {
            if (mSession == null || isSessionChanged(session)) {
                mSession = session;
                sesh = Session.getActiveSession();
                Request request = Request.newMeRequest(session, new Request.GraphUserCallback() {
                    @Override
                    public void onCompleted(GraphUser user, Response response) {
                        // If the response is successful
                        if (sesh == Session.getActiveSession()) {
                            if (user != null) {
                                user_id = user.getId();//user id
                                access_token = sesh.getAccessToken();//get access token
                                Date expire_date = sesh.getExpirationDate(); // get expire date
                                expire = (int) (expire_date.getTime() / 1000);

                                // Save fb info to shared preferences
                                SharedPreferences hubprefs = getActivity().getApplicationContext().getSharedPreferences(Utils.PREFS_FILE, Context.MODE_MULTI_PROCESS);
                                SharedPreferences.Editor hubprefsEditor = hubprefs.edit();
                                hubprefsEditor.putString("fb_user_id", user_id);
                                hubprefsEditor.putString("fb_access_token", access_token);
                                hubprefsEditor.putString("fb_expire", Integer.toString(expire));
                                hubprefsEditor.commit();

                                User.loginToFacebook(getActivity().getApplicationContext(), user_id, access_token, expire);
                                // TODO: Do this for freal
                                authed = true;
                                // TODO -- get the expire stuff
                                // graph api explorer
                                // username == ukey
                                // password == akey
                                // all api c alls (other than login) will need this stuff

                                // to enable, under class definition uncomment requireauth

                                //profileName = user.getName();	//user's profile name
                                //userNameView.setText(user.getName());
                            }
                        }
                    }
                });
                Request.executeBatchAsync(request);
            } else {
//				Log.i(TAG, "Previously logged in...");
            }
        } else if (state.isClosed()) {
            authed = false;
//			Log.i(TAG, "Logged out... Clearing Prefs");

            SharedPreferences hubprefs = getActivity().getApplicationContext().getSharedPreferences(Utils.PREFS_FILE, Context.MODE_MULTI_PROCESS);
            SharedPreferences.Editor hubbaprefsEditor = hubprefs.edit();

            // Clear prefs and save the new user var so it stays in mem
            hubbaprefsEditor.clear();
            hubbaprefsEditor.commit();

            access_token = "";
            user_id = "";
            expire = 0;
        }
    }

    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state,
                         Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uiHelper = new UiLifecycleHelper(getActivity(), callback);
        uiHelper.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.activity_main, container, false);

        LoginButton authButton = (LoginButton) rootView.findViewById(R.id.authButton);
        view_list_button = (Button) rootView.findViewById(R.id.listViewButton);

        view_list_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(authed) {
                    Intent intent = new Intent(getActivity(), ActionActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Please log in to continue.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // error listener
        authButton.setOnErrorListener(new OnErrorListener() {

            @Override
            public void onError(FacebookException error) {
                Log.e("FACEBOOK ERROR", "Error " + error.getMessage());
            }
        });
        authButton.setPublishPermissions(Arrays.asList("user_friends", "email"));
        authButton.setFragment(this);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        // For scenarios where the main activity is launched and user
        // session is not null, the session state change notification
        // may not be triggered. Trigger it if it's open/closed.
        Session session = Session.getActiveSession();
        if (session != null && (session.isOpened() || session.isClosed())) {
            //Toast.makeText(getActivity().getApplicationContext(), "Resumed", Toast.LENGTH_SHORT).show();
            onSessionStateChange(session, session.getState(), null);
        }

        uiHelper.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }

    private boolean isSessionChanged(Session session) {

        // Check if session state changed
        if (mSession.getState() != session.getState())
            return true;

        // Check if accessToken changed
        if (mSession.getAccessToken() != null) {
            if (!mSession.getAccessToken().equals(session.getAccessToken()))
                return true;
        } else if (mSession.getAccessToken() != null) {
            return true;
        }

        // Nothing changed
        return false;
    }
}
