package com.example.asus.lanuit;

import android.location.Address;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.location.Geocoder;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by shihui on 16/6/2015.
 */
public class find extends Fragment {
    View rootview;
    Spinner cuisineSpinner;
    SeekBar priceSeekBar;
    TextView priceText;
    EditText locationET;
    CheckBox timeChkBox;
    Supper supperList;
    Geocoder geocoder;
    int progressValue =0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        rootview = inflater.inflate(R.layout.find_ui, container, false);

        // Define button onClick methods
        View.OnClickListener cameraListener = new View.OnClickListener() {
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.searchBtn:
                        searchSupper();

                        Fragment fragment = new Result();
                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();

                        break;
                }
            }
        };
        rootview.findViewById(R.id.searchBtn).setOnClickListener(cameraListener);
        priceSeekBar =(SeekBar)rootview.findViewById(R.id.priceSeekBar);
        priceText = (TextView)rootview.findViewById(R.id.price);
        locationET = (EditText)rootview.findViewById(R.id.locationText);
        cuisineSpinner =(Spinner)rootview.findViewById(R.id.CuisineList);
        timeChkBox =(CheckBox)rootview.findViewById(R.id.timeChkBox);
        seekBar();
        return rootview;
    }

    public void seekBar()
    {
        priceText.setText("Covered : " + priceSeekBar.getProgress() + " / " + priceSeekBar.getMax());


        priceSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressValue = progress;
                priceText.setText("Covered : " + progress+ " / " + priceSeekBar.getMax());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                priceText.setText("Covered : " + progressValue+ " / " + priceSeekBar.getMax());
            }
        });
    }

    //This method returns the selected cusisine based on user's choice
    //post condition: returns the selectd cuisine
    private String getCuisine()
    {
        String cuisine;
        String cuisineChoice = String.valueOf(cuisineSpinner.getSelectedItem());
        if(cuisineChoice.equals("No Preference"))
            cuisine ="empty";
        else
            cuisine = "cuisine = '" +cuisineChoice +"' ";

        return cuisine;
    }

    // This method returns the selected price
    // Post condition: return price

    private String getPrice()
    {
        String price;
        if(progressValue == 0)
            price = "empty";
        else
            price = "price <" + progressValue+" ";

        return price;
    }
    //This method returns the current day and time
    // post condition: returns current day and time if checkBox is checked, day only if checkbox is unchecked
    private String getTime() {
        //get current day
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.US);
        String weekDay = dayFormat.format(c.getTime());

        String dayTime;

        if (timeChkBox.isChecked()) {

            SimpleDateFormat df = new SimpleDateFormat("HH:mm");
            String time = df.format(c.getTime());
            dayTime = time + "" + weekDay;
        } else
            dayTime = weekDay;

        return dayTime;
    }



    private String getLatLog()
    {
        String location ="";
        String requestLocation =locationET.getText().toString();
        System.out.println(requestLocation);

        if(requestLocation.equals(""))
            location = "empty";
        else
            location = geocoding(requestLocation);

        return location;
    }

    // the method transform a location address to its respective lat and log corodintes
    // post condition: returns lat and log coridinates
    private String geocoding( String requestLocation)
    {
        double latitude = 1;
        double longitude = 1;
        geocoder = new Geocoder(getActivity(),Locale.getDefault());
        try {
            List<Address> location = geocoder.getFromLocationName(requestLocation,1);

             latitude = Math.round(location.get(0).getLatitude() *100.0)/100.0;
            System.out.println(latitude);
             longitude = Math.round(location.get(0).getLongitude() *100.0)/100.0;

        }catch(Exception e)
        {System.out.println(e);}

        double maxLat = latitude+0.02; double minLat =latitude -0.02;
        double maxLog = longitude+ 0.02; double minLog = longitude-0.02;
        String sqlLat = "(lat BETWEEN " +minLat+ " And " +maxLat +") AND ";
        String sqlLog = "(log BETWEEN " +minLog+ " And " +maxLog+ ")";
        String LatLog = sqlLat + sqlLog;

        return LatLog;
    }


    public void searchSupper()
    {
        supperList.clearSupperList();

        RequestParams params = new RequestParams();

        String cuisine = getCuisine();
        params.put("cuisine", cuisine);

        String price = getPrice();
        params.put("price", price);

        String LatLog = getLatLog();
        params.put("LatLog", LatLog);

        String dayTime = getTime();
        params.put("dateTime", dayTime);

        invokeWS(params);
    }

    // method to establish connection towards the server
    public void invokeWS(RequestParams params)
    {
        AsyncHttpClient client = new AsyncHttpClient();

        // establish connection to webservice
        client.get("http://192.168.1.5:8080/laNuitWS/SearchSupper/Search", params, new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response)
            {
                for(int counter = 0; counter < response.length(); counter++)
                {
                    try
                    {
                        JSONObject obj = response.getJSONObject(counter);
                        Supper newSupper = new Supper(obj.getString("name"), obj.getString("address"),obj.getString("postal"),
                                obj.getString("latitude"), obj.getString("longitude"),obj.getString("openHours"),
                                obj.getString("price"), obj.getString("foodType"));
                        supperList.listOfSupper.add(newSupper);
                        supperList.supperName.add(newSupper.getName());

                        //Toast.makeText(getActivity().getApplicationContext(), supperList.listOfSupper.size().toString(), Toast.LENGTH_SHORT).show();
                    }

                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


}
