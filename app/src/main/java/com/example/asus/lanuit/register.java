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

public class register extends Fragment {
    View rootview;
    Button btnRegister;
    EditText txtUser;
    EditText txtPw;
    User userList;
    TextView errorTxt;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.register_ui, container, false);

        View.OnClickListener cameraListener = new View.OnClickListener() {
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btnRegister:
                        createUser();
                        break;
                }
            }
        };
        btnRegister = (Button) rootview.findViewById(R.id.btnRegister);
        txtPw = (EditText) rootview.findViewById(R.id.txtPw);
        txtUser = (EditText) rootview.findViewById(R.id.txtUser);
        rootview.findViewById(R.id.btnRegister).setOnClickListener(cameraListener);

        return rootview;
    }

    public void createUser() {
        RequestParams params = new RequestParams();
        String name = txtUser.getText().toString();
        String password = txtPw.getText().toString();
        params.put("name", name);
        params.put("password", password);
        errorTxt = (TextView) getView().findViewById(R.id.errorTxt);
        if (isNotNull(name) && isNotNull(password)) {
            invokeWS(params);
            errorTxt.setVisibility(View.INVISIBLE);
        } else {
            errorTxt.setVisibility(View.VISIBLE);
            errorTxt.setText("Please fill in every field");
        }

    }

    public void invokeWS(RequestParams params) {
        System.out.println("hi1");
        AsyncHttpClient client = new AsyncHttpClient();

        client.get("http://192.168.1.5:8080/laNuitWS/DB_User/createUser", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                System.out.println("hi2");
                if (response.length() == 0) {
                    errorTxt = (TextView) getView().findViewById(R.id.errorTxt);
                    errorTxt.setVisibility(View.VISIBLE);

                }

                for (int counter = 0; counter < response.length(); counter++) {
                    try {
                        JSONObject obj = response.getJSONObject(counter);
                        int id = Integer.parseInt(obj.getString("id"));
                        User user = new User(txtUser.getText().toString(), txtPw.getText().toString(), "U", id);
                        userList.listOfUser.add(user);
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);

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

    public static boolean isNotNull(String txt) {
        return txt != null && txt.trim().length() > 0 ? true : false;
    }

}
