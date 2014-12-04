package com.mbembac.letsmeetup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mbembac.letsmeetup.Classes.Meetups;
import com.parse.FindCallback;
import com.parse.ParseException;
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
        final String username = intent.getStringExtra("username");

        TextView name = (TextView) findViewById(R.id.friend_map_name);
        name.setText(username);

        TextView distance = (TextView) findViewById(R.id.friend_map_miles);
        distance.setText("Within 100 Miles");

        double loc_lat = intent.getDoubleExtra("lat", 0);
        double loc_long = intent.getDoubleExtra("lon", 0);

        setUpMapIfNeeded();

        MarkerOptions marker = new MarkerOptions().position(new LatLng(loc_lat, loc_long)).title(username);
        gMap.addMarker(marker);


        final List<ParseUser> user_list = new ArrayList<ParseUser>();

        Button send_request = (Button) findViewById(R.id.send_request_meetup);

        final EditText whereText = (EditText) findViewById(R.id.where);
        final EditText whenText = (EditText) findViewById(R.id.when);

        //assume user means to send meetup
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("username",username);
        query.findInBackground(new FindCallback<ParseUser>() {

            @Override
            public void done(List<ParseUser> parseUsers, ParseException e) {
                if (e == null) {
                    int c = 0;
                    for (ParseUser user : parseUsers) {
                        if (user.getUsername().equals(username)) {
//                            user_list.clear();
                            user_list.add(parseUsers.get(c));
                            break;
                        }
                        c++;
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });

        send_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String where = whereText.getText().toString();
                String when = whenText.getText().toString();
                ParseUser friend = user_list.get(0);
//                Log.d("SEND INVITE TO", friend.getUsername());
                String msg = ParseUser.getCurrentUser().getUsername() + " wants to meet @ " + when + " at " + where;

                Meetups meet = new Meetups();
                meet.setUserFrom(ParseUser.getCurrentUser());
                meet.setUserTo(friend);
                meet.setMessage(msg);
                meet.saveInBackground();

                Intent intent = new Intent(v.getContext(), Welcome.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(),
                        "You're message has been sent!!",
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        });
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

