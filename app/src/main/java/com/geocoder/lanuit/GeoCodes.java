package com.geocoder.lanuit;


import android.app.Activity;
import android.content.Context;
import android.location.Address;


import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;


import android.location.Geocoder;
import android.os.Bundle;

/**
 * Created by ASUS on 25/6/2015.
 */
public class GeoCodes extends Activity {

    Geocoder geocoder;
    @Override
    protected void onCreate(Bundle hihi){
        super.onCreate(hihi);
         geocoder = new Geocoder(this);
    }

    public double [] convertLocationName(String districtLocation)
    {
        //districtLocation += "Singapore";
        double [] convertedLocation = new double[2];

        try {
            List<Address> locationLatLog = geocoder.getFromLocationName(districtLocation, 1);
            System.out.println(locationLatLog.size());

            while(locationLatLog.size() ==0)
            {
                locationLatLog = geocoder.getFromLocationName(districtLocation, 1);
            }
            if(locationLatLog.size()>1)
            {
                convertedLocation[0] = locationLatLog.get(0).getLatitude();
                convertedLocation[1] = locationLatLog.get(0).getLongitude();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return convertedLocation;
    }

}
