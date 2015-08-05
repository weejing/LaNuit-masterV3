package com.example.asus.lanuit;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by shihui on 16/6/2015.
 */
public class home extends Fragment {
    View rootview;
    User userList;
    private TextView textView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.home_ui, container, false);
        TextView text = (TextView) rootview.findViewById(R.id.textView33);
        if (userList.listOfUser.size() > 0) {
            text.setText(userList.listOfUser.get(0).getName());
        } else {
            text.setText("Please LOGIN");
        }


        View.OnClickListener cameraListener = new View.OnClickListener() {
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.buttonFind:
                        Fragment fragment = new find();
                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.container, fragment)
                                .commit();
                        break;
                    case R.id.buttonChange:
                        //get current time
                        Calendar c = Calendar.getInstance();
                        System.out.println("Current time =&gt; " + c.getTime());
                        //  SimpleDateFormat df = new SimpleDateFormat("H:mm");
                        SimpleDateFormat df = new SimpleDateFormat("hh:mm");
                        String formattedDate = df.format(c.getTime());
                        //get current day
                        String weekDay;
                        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.US);
                        weekDay = dayFormat.format(c.getTime());

                        ((TextView) getView().findViewById(R.id.textView33)).setText(formattedDate + " " + weekDay);
                        break;
                }
            }
        };
        rootview.findViewById(R.id.buttonFind).setOnClickListener(cameraListener);
        rootview.findViewById(R.id.buttonChange).setOnClickListener(cameraListener);
        // String apple = userList.listOfUser.get(0).getName();
        //  Toast.makeText(getActivity().getApplicationContext(), apple + " " , Toast.LENGTH_LONG).show();
        //  User user = (retrieveUser(userList.arrayIndicator));

        return rootview;
    }


    public User retrieveUser(int position) {
        return userList.listOfUser.get(position);
    }


}
