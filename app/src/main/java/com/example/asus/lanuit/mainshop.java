package com.example.asus.lanuit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by shihui on 16/7/2015.
 */
public class mainshop extends Fragment {
    View rootview;
    Supper supperList;
    Button btnAdd;
    Supper supper;
    ListAdapter supperList2;




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.mainshop_ui, container, false);
        supperList.clearSupperList();
        getAllSupper();


        ListAdapter supperList = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, supper.supperName);
        ListView supperListView = (ListView) rootview.findViewById(R.id.mainSupperList);
        supperListView.setAdapter(supperList);


        supperListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                  @Override
                                                  public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                                                  {
                                                      long cast = parent.getItemIdAtPosition(position);
                                                      int indicator = (int) cast;
                                                      System.out.println(indicator);

                                                      supper.arrayIndicator = indicator;

                                                      Fragment fragment = new supperDetails();
                                                      FragmentManager fragmentManager = getFragmentManager();
                                                      fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
                                                  }
                                              }
        );

        View.OnClickListener cameraListener = new View.OnClickListener() {
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btnAdd:
                        Fragment fragment = new addShop();
                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.container, fragment)
                                .commit();
                        break;
                }
            }
        };
        rootview.findViewById(R.id.btnAdd).setOnClickListener(cameraListener);

        return rootview;
    }

    public void getAllSupper() {
        AsyncHttpClient client = new AsyncHttpClient();
        // establish connection to webservice
        client.get("http://192.168.1.98:8080/laNuitWS/SearchSupper/getAllSupper", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                for (int counter = 0; counter < response.length(); counter++) {
                    try {

                        JSONObject obj = response.getJSONObject(counter);
                        Supper newSupper = new Supper(obj.getInt("supID"), obj.getString("name"), obj.getString("cuisine"), obj.getString("foodType"), obj.getString("price"), obj.getString("latitude"), obj.getString("longitude"));
                        supperList.listOfSupper.add(newSupper);
                        supperList.supperName.add(newSupper.getName());
                        System.out.println("ADDED SUPPER");
                        //Toast.makeText(getActivity().getApplicationContext(), supperList.listOfSupper.size().toString(), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


}
