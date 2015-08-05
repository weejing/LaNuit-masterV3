package com.example.asus.lanuit;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.facebook.FacebookSdk;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private Toolbar mToolbar;
    private ListView drawerListView;
    User userList;
    Supper supperList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_drawer);

        // Set up the drawer.
        mNavigationDrawerFragment.setup(R.id.fragment_drawer, (DrawerLayout) findViewById(R.id.drawer), mToolbar);

    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        //  Toast.makeText(this, "Menu item selected -> " + position, Toast.LENGTH_SHORT).show();
        Fragment fragment = null;
        if (userList.listOfUser.size() > 0) {
            if (userList.listOfUser.get(0).getRole().equals("U")) { //user
                switch (position) {
                    case 0:
                        fragment = new login();
                        break;
                    case 1:
                        fragment = new find();
                        break;
                }
            } else {
                switch (position) {
                    case 0:
                        fragment = new login();
                        break;
                    case 1:
                        getAllSupper();
                        fragment = new mainshop();
                        break;
                }
            }
        } else {
            switch (position) {
                case 0:
                    fragment = new login();
                    break;
                case 1:
                    fragment = new find();
            }
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }


    @Override
    public void onBackPressed() {
        if (mNavigationDrawerFragment.isDrawerOpen())
            mNavigationDrawerFragment.closeDrawer();
        else
            super.onBackPressed();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void getAllSupper() {
        AsyncHttpClient client = new AsyncHttpClient();
        // establish connection to webservice
        client.get("http://192.168.1.5:8080/laNuitWS/SearchSupper/getAllSupper", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                for (int counter = 0; counter < response.length(); counter++) {
                    try {
                      /*  if(supperList.listOfSupper.size()>0){
                            supperList.listOfSupper.clear();
                            supperList.supperName.clear();
                        }*/
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