package com.example.asus.lanuit;

import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ASUS on 29/6/2015.
 */
public class supperDetails extends Fragment {

    View rootview;
    Supper supperList;
    TextView supperName;
    TextView supperAddress;
    TextView supperPostal;
    TextView supperPrice;
    TextView supperFoodType;
    TextView supperOpenHours;
    LatLng supperLatLng;
    LatLng gpsLatLng;
    Supper supper = (retrieveSupper(supperList.arrayIndicator));

    boolean mShowMap;
    GoogleMap mMap;

    User userList;
    Button backButton;
    Button btnDelete;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        rootview = inflater.inflate(R.layout.supper_details, container, false);

        if (mMap== null) {
            mMap = ((SupportMapFragment) getChildFragmentManager().findFragmentById(
                    R.id.mapView)).getMap();
        }

         supperName = (TextView) rootview.findViewById(R.id.supperName);
         supperAddress = (TextView) rootview.findViewById(R.id.supperAddress);
         supperPostal = (TextView) rootview.findViewById(R.id.supperPostal);
        supperPrice = (TextView) rootview.findViewById(R.id.supperPrice);
         supperFoodType = (TextView) rootview.findViewById(R.id.supperFoodType);
        supperOpenHours = (TextView) rootview.findViewById(R.id.supperOpenHours);


        backButton=(Button) rootview.findViewById(R.id.button2);
        btnDelete=(Button) rootview.findViewById(R.id.btnDelete);
        btnDelete.setVisibility(View.INVISIBLE);

        setUpSupperDetails();
        setUpMap();

        if (userList.listOfUser.size() > 0) {

            if (userList.listOfUser.get(0).getRole().equals("A")) {
                //user
                btnDelete.setVisibility(View.VISIBLE);
            }
        }

        View.OnClickListener cameraListener = new View.OnClickListener() {
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.button2:
                        if (userList.listOfUser.size() > 0) {
                            if (userList.listOfUser.get(0).getRole().equals("A")) {
                                Fragment fragment = new mainshop();
                                FragmentManager fragmentManager = getFragmentManager();
                                fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
                            }
                        }else{
                            Fragment fragment = new Result();
                            FragmentManager fragmentManager = getFragmentManager();
                            fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
                        }
                        break;
                    case R.id.btnDelete:
                        deleteShop();
                        break;
                }
            }
        };


        rootview.findViewById(R.id.button2).setOnClickListener(cameraListener);
        rootview.findViewById(R.id.btnDelete).setOnClickListener(cameraListener);
        return rootview;
    }


    // This method would assign the supper details to the textfields
    // Pre condition: Supper object already created and selected from array
    // Post condition: Assign the supper obejct values
    public void setUpSupperDetails()
    {
        supperName.setText("Name: " +supper.getName());
        supperAddress.setText("Address: " + supper.getAddress());
        supperPostal.setText("Postal: Singapore " + supper.getPostal());
        supperPrice.setText("Price:Estimated $" + supper.getPrice());
        supperFoodType.setText("Cusines: " + supper.getFoodType());

        supperOpenHours.setText("Open Hours: "+supper.getOpenHours());
    }



    // This method would enlist the use of markers to place both supper and user locations on the map
    // pre condition: getGps method must have already been run
    // post condition: place location markers on the map
    public void setUpMap()
    {
        double lat = Double.parseDouble(supper.getLatitude());
        double log = Double.parseDouble(supper.getLongitude());
         supperLatLng = new LatLng(lat,log);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(supperLatLng,13);
        mMap.moveCamera(update);

        mMap.addMarker(new MarkerOptions()
                .position(supperLatLng)
                .anchor(.5f, .5f)
                .icon(BitmapDescriptorFactory.defaultMarker()));

         gpsLatLng = getGPS();

        if(gpsLatLng!=null)
        {
            String distance = getDistance(supperLatLng, gpsLatLng);
            mMap.addMarker(new MarkerOptions()
                    .position(gpsLatLng)
                    .anchor(.5f, .5f)
                    .title(distance)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.human)));
        }

        //drawRoute();
    }

    public String getDistance(LatLng firstPoint, LatLng secondPoint )
    {
        String result;
        float[] findDistance = new float[1];
        Location.distanceBetween(firstPoint.latitude, firstPoint.longitude,
                                 secondPoint.latitude,secondPoint.longitude,
                                 findDistance);
        int distance = (int)findDistance[0];

        if(distance < 1000)
            result = "You are "+ distance+ " meter away";
        else {
            distance /= 1000;
            result ="You are approximately " +distance+ " km away";
        }

        return result;
    }

    public LatLng getGPS()
    {
        GPSTracker gps = new GPSTracker(getActivity());
        LatLng gpsLatLng = null;

        if(gps.canGetLocation())
        {
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            gpsLatLng = new LatLng(latitude,longitude);
        }
        else{
            gps.showSettingsAlert();
        }
        return gpsLatLng;
    }

    public void drawRoute()
    {
        String url = getMapsApiDirectionsUrl();
        ReadTask downloadTask = new ReadTask();
        downloadTask.execute(url);
    }


    public Supper retrieveSupper(int position)
    {
        return supperList.listOfSupper.get(position);
    }




    private String getMapsApiDirectionsUrl() {
        String waypoints = "waypoints=optimize:true|"
                + gpsLatLng.latitude + "," + gpsLatLng.longitude
                + "|" + "|" + supperLatLng.latitude + ","
                + supperLatLng.longitude;

        String sensor = "sensor=false";
        String params = waypoints + "&" + sensor;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/"
                + output + "?" + params;
        return url;
    }



    private class ReadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... url) {
            String data = "";
            try {
                MapPathHttpConnection http = new MapPathHttpConnection();
                data = http.readUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            new ParserTask().execute(result);
        }
    }


    private class ParserTask extends
            AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(
                String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                JsonMapParser parser = new JsonMapParser();
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> routes) {
            ArrayList<LatLng> points = null;
            PolylineOptions polyLineOptions = null;

            // traversing through routes
            for (int i = 0; i < routes.size(); i++) {
                points = new ArrayList<LatLng>();
                polyLineOptions = new PolylineOptions();
                List<HashMap<String, String>> path = routes.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                polyLineOptions.addAll(points);
                polyLineOptions.width(2);
                polyLineOptions.color(Color.BLUE);
            }

            mMap.addPolyline(polyLineOptions);
        }
    }
    public void deleteShop() {
        RequestParams params = new RequestParams();
        Supper supper = (retrieveSupper(supperList.arrayIndicator));
        int supID=supper.getSupID();
        params.put("supID", supID);
        invokeWS(params);
    }


    // method to call the Json data from the server
    public void invokeWS(RequestParams params) {
        System.out.println("shop deleted");
        AsyncHttpClient client = new AsyncHttpClient();

        client.get("http://192.168.1.5:8080/laNuitWS/SearchSupper/deleteShop", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
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



