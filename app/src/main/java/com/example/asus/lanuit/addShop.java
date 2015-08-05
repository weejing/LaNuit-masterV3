package com.example.asus.lanuit;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Locale;

/**
 * Created by shihui on 23/7/2015.
 */
public class addShop extends Fragment {
    View rootview;
    CheckBox checkBox;
    EditText txtShop;
    Spinner cuisineSpinner;
    EditText txtPrice;
    EditText txtFoodType;
    EditText txtAddress;
    EditText txtPostal;
    EditText txtMon;
    EditText txtTues;
    EditText txtWed;
    EditText txtThurs;
    EditText txtFri;
    EditText txtSat;
    EditText txtSun;
    Geocoder geocoder;
    RequestParams params = new RequestParams();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.addshop_ui, container, false);

        checkBox = (CheckBox) rootview.findViewById(R.id.cbApply);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if (checkBox.isChecked()) {
                    txtMon = (EditText) rootview.findViewById(R.id.txtMon);
                    String monVar = txtMon.getText().toString();
                    txtTues = (EditText) rootview.findViewById(R.id.txtTues);
                    txtTues.setText(monVar);
                    txtWed = (EditText) rootview.findViewById(R.id.txtWed);
                    txtWed.setText(monVar);
                    txtThurs = (EditText) rootview.findViewById(R.id.txtThurs);
                    txtThurs.setText(monVar);
                    txtFri = (EditText) rootview.findViewById(R.id.txtFri);
                    txtFri.setText(monVar);
                    txtSat = (EditText) rootview.findViewById(R.id.txtSat);
                    txtSat.setText(monVar);
                    txtSun = (EditText) rootview.findViewById(R.id.txtSun);
                    txtSun.setText(monVar);

                } else {
                    txtTues = (EditText) rootview.findViewById(R.id.txtTues);
                    txtTues.setText("");
                    txtWed = (EditText) rootview.findViewById(R.id.txtWed);
                    txtWed.setText("");
                    txtThurs = (EditText) rootview.findViewById(R.id.txtThurs);
                    txtThurs.setText("");
                    txtFri = (EditText) rootview.findViewById(R.id.txtFri);
                    txtFri.setText("");
                    txtSat = (EditText) rootview.findViewById(R.id.txtSat);
                    txtSat.setText("");
                    txtSun = (EditText) rootview.findViewById(R.id.txtSun);
                    txtSun.setText("");
                }
            }
        });

        //new
        View.OnClickListener cameraListener = new View.OnClickListener() {
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btnCreate:
                        createShop();
                        break;
                }
            }
        };
        rootview.findViewById(R.id.btnCreate).setOnClickListener(cameraListener);
        txtShop = (EditText) rootview.findViewById(R.id.txtShop);
        cuisineSpinner =(Spinner)rootview.findViewById(R.id.CuisineList);
        txtPrice= (EditText) rootview.findViewById(R.id.txtPrice);
        txtFoodType= (EditText) rootview.findViewById(R.id.txtFoodType);
        txtAddress= (EditText) rootview.findViewById(R.id.txtAddress);
        txtPostal= (EditText) rootview.findViewById(R.id.txtPostal);
        txtMon = (EditText) rootview.findViewById(R.id.txtMon);
        txtTues = (EditText) rootview.findViewById(R.id.txtTues);
        txtWed = (EditText) rootview.findViewById(R.id.txtWed);
        txtThurs = (EditText) rootview.findViewById(R.id.txtThurs);
        txtFri = (EditText) rootview.findViewById(R.id.txtFri);
        txtSat = (EditText) rootview.findViewById(R.id.txtSat);
        txtSun = (EditText) rootview.findViewById(R.id.txtSun);
        //end new
        return rootview;
    }

    private void geocoding(String postal)
    {
        double latitude = 1;
        double longitude = 1;
        double lat=1;
        double log=1;
        geocoder = new Geocoder(getActivity(), Locale.getDefault());
        try {
            List<Address> location = geocoder.getFromLocationName(postal,1);

            latitude=location.get(0).getLatitude();
            lat= Math.round(location.get(0).getLatitude() *100.0)/100.0;
            longitude = location.get(0).getLongitude();
            log=Math.round(longitude*100.0)/100.0;

        }catch(Exception e)
        {System.out.println(e);}
        params.put("latitude", latitude);
        params.put("lat", lat);
        params.put("longitude", longitude);
        params.put("log", log);

    }

    public void createShop() {

        String name = txtShop.getText().toString();
        String cuisine = String.valueOf(cuisineSpinner.getSelectedItem());
        int price = Integer.parseInt(txtPrice.getText().toString());
        String foodtype = txtFoodType.getText().toString();
        String address = txtAddress.getText().toString();
        String postal = txtPostal.getText().toString();
        String Monday = txtMon.getText().toString();
        String Tuesday = txtTues.getText().toString();
        String Wednesday = txtWed.getText().toString();
        String Thursday = txtThurs.getText().toString();
        String Friday = txtFri.getText().toString();
        String Saturday = txtSat.getText().toString();
        String Sunday = txtSun.getText().toString();
        params.put("name", name);
        params.put("cuisine", cuisine);
        params.put("price", price);
        params.put("foodtype", foodtype);
        params.put("address", address);
        params.put("postal", postal);
        params.put("Monday", Monday);
        params.put("Tuesday", Tuesday);
        params.put("Wednesday", Wednesday);
        params.put("Thursday", Thursday);
        params.put("Friday", Friday);
        params.put("Saturday", Saturday);
        params.put("Sunday", Sunday);
        geocoding(postal);
        invokeWS(params);
    }

    public void invokeWS(RequestParams params) {
        System.out.println("hi1");
        AsyncHttpClient client = new AsyncHttpClient();

        client.get("http://192.168.1.5:8080/laNuitWS/SearchSupper/createShop", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                System.out.println("hi2");

                for (int counter = 0; counter < response.length(); counter++) {
                    Fragment fragment = new mainshop();
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                System.out.println("hi");
            }
        });
    }
}
