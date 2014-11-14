package com.mbembac.letsmeetup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class FriendMapActivty extends Activity {

    private MapFragment mMapFragment;
    private GoogleMap gMap;
    public final String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_map_activty);

//        mMapFragment = MapFragment.newInstance();
//        FragmentTransaction fragmentTransaction =
//                getFragmentManager().beginTransaction();
//        fragmentTransaction.add(R.id.gmap, mMapFragment);
//        fragmentTransaction.commit();


        Intent intent = getIntent();
        TextView name = (TextView) findViewById(R.id.friend_map_name);
        name.setText(intent.getStringExtra("ID"));

        final String username = intent.getStringExtra("ID");

        TextView distance = (TextView) findViewById(R.id.friend_map_miles);
        distance.setText(intent.getStringExtra("Distance") + " Feet Away");


        ParseQuery<ParseUser> query = ParseUser.getQuery();

        final List<ParseUser> userList = new ArrayList<ParseUser>();

        final ParseGeoPoint latLon;

        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> parseUsers, ParseException e) {

                if (e == null) {
                    if (username != null) {
                        for (ParseUser user : parseUsers) {
                            if (user.getUsername().contains(username)) {
                                userList.add(user);
                            }
                        }
                    }
                }
            }
        });

        double loc_lat = 39.9973749;
        double loc_long = -83.0092424;

        setUpMapIfNeeded();

        MarkerOptions marker = new MarkerOptions().position(new LatLng(loc_lat, loc_long)).title(username);
        gMap.addMarker(marker);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void setUpMapIfNeeded() {

        if (gMap == null) {
            gMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.gmap)).getMap();

            if (gMap != null) {
                setupMap();
            } else {
                Toast.makeText(this, "Google Maps not available",
                        Toast.LENGTH_LONG).show();
            }
        }

    }

    private void setupMap() {
//        gMap.addMarker(new MarkerOptions(new LatLng(0.0,0.0)).title("marker"));
        gMap.setMyLocationEnabled(true);

    }
}

