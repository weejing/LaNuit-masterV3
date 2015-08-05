package com.example.asus.lanuit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class login extends Fragment {
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    private CallbackManager callbackManager;
    View rootview;
    EditText txtUser;
    EditText txtPw;
    TextView errorTxt;
    TextView lblUser;
    TextView lblPw;
    Button loginbtn;
    Button fbButton;
    Button btnRegister;
    User userList;
    User newUser;
    String dbname;
    private Button logoutbtn;
    static final String TAG = "ERROR";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.login_ui, container, false);

        View.OnClickListener cameraListener = new View.OnClickListener() {
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btnLogin:
                        UserLogin();
                        break;
                    case R.id.btnLogout:
                        if (userList.listOfUser.size() > 0) {
                            userList.listOfUser.remove(0);
                        }
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.btnRegister:
                        Fragment fragment = new register();
                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.container, fragment)
                                .commit();
                        break;
                }
            }
        };
        logoutbtn = (Button) rootview.findViewById(R.id.btnLogout);
        loginbtn = (Button) rootview.findViewById(R.id.btnLogin);
        fbButton = (Button) rootview.findViewById(R.id.login_button);
        btnRegister = (Button) rootview.findViewById(R.id.btnRegister);
        txtPw = (EditText) rootview.findViewById(R.id.txtPw);
        txtUser = (EditText) rootview.findViewById(R.id.txtUser);
        errorTxt = (TextView) rootview.findViewById(R.id.errorTxt);
        lblUser = (TextView) rootview.findViewById(R.id.lblName);
        lblPw = (TextView) rootview.findViewById(R.id.lblPw);
        rootview.findViewById(R.id.btnLogin).setOnClickListener(cameraListener);
        rootview.findViewById(R.id.btnLogout).setOnClickListener(cameraListener);
        rootview.findViewById(R.id.btnRegister).setOnClickListener(cameraListener);

        if (userList.listOfUser.size() > 0) { //can logout
            if (userList.listOfUser.get(0).getId() == 0) {
                //fb login
                logoutbtn.setVisibility(View.INVISIBLE);
                txtPw.setVisibility(View.INVISIBLE);
                txtUser.setVisibility(View.INVISIBLE);
                errorTxt.setVisibility(View.INVISIBLE);
                lblUser.setVisibility(View.INVISIBLE);
                lblPw.setVisibility(View.INVISIBLE);
                loginbtn.setVisibility(View.INVISIBLE);
                btnRegister.setVisibility(View.INVISIBLE);
                fbButton.setVisibility(View.VISIBLE);
            } else {
                logoutbtn.setVisibility(View.VISIBLE);
                txtPw.setVisibility(View.INVISIBLE);
                txtUser.setVisibility(View.INVISIBLE);
                errorTxt.setVisibility(View.INVISIBLE);
                lblUser.setVisibility(View.INVISIBLE);
                lblPw.setVisibility(View.INVISIBLE);
                loginbtn.setVisibility(View.INVISIBLE);
                fbButton.setVisibility(View.INVISIBLE);
                btnRegister.setVisibility(View.INVISIBLE);
            }

        } else { //can login
            logoutbtn.setVisibility(View.INVISIBLE);
            txtPw.setVisibility(View.VISIBLE);
            txtUser.setVisibility(View.VISIBLE);
            lblUser.setVisibility(View.VISIBLE);
            lblPw.setVisibility(View.VISIBLE);
            loginbtn.setVisibility(View.VISIBLE);
            fbButton.setVisibility(View.VISIBLE);
            btnRegister.setVisibility(View.VISIBLE);
        }

        return rootview;
    }

    //start fb
    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            AccessToken accessToken = loginResult.getAccessToken();
            Profile profile = Profile.getCurrentProfile();
            profileTracker.startTracking();
            //      displayMessage(profile);

            // Intent intent = new Intent(getActivity(), MainActivity.class);
            //startActivity(intent);
        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException e) {

        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {

            }
        };

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                // displayMessage(newProfile);
/*                if(newProfile.getName() == null){
                    //logout
                    if(userList.listOfUser.size()>0){
                        userList.listOfUser.remove(0);
                    }
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                }else{
                    User fbUser = new User(newProfile.getName(), "", "U", 0);
                    userList.listOfUser.add(fbUser);
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                } */
                if (newProfile != null) {
                    //login
                    User fbUser = new User(newProfile.getName(), "", "U", 0);
                    userList.listOfUser.add(fbUser);
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                }
                if (oldProfile != null) {
                    //logout
                    if (userList.listOfUser.size() > 0) {
                        userList.listOfUser.remove(0);
                    }
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                }


            }
        };

        accessTokenTracker.startTracking();
        profileTracker.startTracking();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LoginButton loginButton = (LoginButton) view.findViewById(R.id.login_button);
        //   textView = (TextView) view.findViewById(R.id.textView22);


        loginButton.setReadPermissions("user_friends");
        loginButton.setFragment(this);
        loginButton.registerCallback(callbackManager, callback);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }

    private void displayMessage(Profile profile) {
        if (profile != null) {

        } else {

        }
    }

    @Override
    public void onStop() {
        super.onStop();
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
        if (userList.listOfUser.size() > 0) {
            if (userList.listOfUser.get(0).getId() == 0) {
                userList.listOfUser.remove(0);
                //   Intent intent = new Intent(getActivity(), MainActivity.class);
                // startActivity(intent);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Profile profile = Profile.getCurrentProfile();
        // displayMessage(profile);
        if (profile != null) {
            //logged in & not added to user
            if (userList.listOfUser.size() == 0) {
                User fbUser = new User(profile.getName(), "", "U", 0);
                userList.listOfUser.add(fbUser);
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        }

    }


    //end fb

    public void UserLogin() {
        RequestParams params = new RequestParams();
        String name = txtUser.getText().toString();
        String password = txtPw.getText().toString();
        params.put("name", name);
        params.put("password", password);
        invokeWS(params);
    }


    // method to call the Json data from the server
    public void invokeWS(RequestParams params) {
        System.out.println("hi1");
        AsyncHttpClient client = new AsyncHttpClient();

        client.get("http://192.168.1.5:8080/laNuitWS/DB_User/Login", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                System.out.println("hi2");
                if (response.length() == 0) {
                    // Toast.makeText(getActivity().getApplicationContext(), "no data found", Toast.LENGTH_LONG).show();
                    errorTxt = (TextView) getView().findViewById(R.id.errorTxt);
                    errorTxt.setVisibility(View.VISIBLE);

                }

                for (int counter = 0; counter < response.length(); counter++) {
                    try {
                        JSONObject obj = response.getJSONObject(counter);
                        User user = new User(obj.getString("name"), obj.getString("password"), obj.getString("role"), Integer.parseInt(obj.getString("id")));
                        // supperList.listOfSupper.add(newSupper);
                        userList.listOfUser.add(user);
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                  /*     Fragment fragment = new NavigationDrawerFragment();
                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.container, fragment).commit(); */
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                System.out.println("hi");
            }
        });
    }


}
