package com.example.asus.lanuit;

import android.app.Activity;
//import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by ASUS on 28/6/2015.
 */
public class Result extends Fragment {
    View rootview;
    Supper supper;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        rootview = inflater.inflate(R.layout.result_ui, container, false);
        ListAdapter supperList = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, supper.supperName);
        ListView supperListView = (ListView)rootview.findViewById(R.id.supperList);
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

        return rootview;
    }

}
