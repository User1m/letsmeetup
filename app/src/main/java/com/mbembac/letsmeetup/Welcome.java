package com.mbembac.letsmeetup;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseUser;


public class Welcome extends Fragment {

    // Declare Variable
    Button logout;
    //Button friendme;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.welcome, container, false);

        LocationManager man =
                (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);

        if(!man.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            Toast.makeText(getActivity(),"Please Enable GPS!", Toast.LENGTH_LONG).show();
        }

        Button map = (Button) v.findViewById(R.id.map_button);
        map.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent friendMap;
                friendMap = new Intent(getActivity(), FriendMapActivty.class);
                startActivity(friendMap);
            }
        });


//        friendme = (Button) v.findViewById(R.id.friendme_button);
        logout = (Button) v.findViewById(R.id.logout);


        Typeface customFont = Typeface.createFromAsset(getResources().getAssets(), "BLACKJAR.ttf");

        // Retrieve current user from Parse.com
        ParseUser currentUser = ParseUser.getCurrentUser();

        // Convert currentUser into String
        //String struser = currentUser.getUsername();
        String username = currentUser.get("first_name").toString();

        String firstLetter = username.substring(0, 1).toUpperCase();
        String restLetters = username.substring(1).toLowerCase();
        username = firstLetter + restLetters;

        // Locate TextView in welcome.xml
        TextView txtuser = (TextView) v.findViewById(R.id.txtuser);
        txtuser.setTypeface(customFont);

        // Set the currentUser String into TextView
        txtuser.setTextColor(Color.DKGRAY);
        txtuser.isOpaque();
        txtuser.setText("Welcome " + username + "!");


//        // Find Friends Button Click Listener
//        friendme.setOnClickListener(new OnClickListener() {
//            public void onClick(View arg0) {
//
//                Intent intent = new Intent(getActivity(),
//                        FriendActivity.class);
//                startActivity(intent);
//                //finish();
//            }
//        });

        // Logout Button Click Listener
        logout.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                // Logout current user
                ParseUser.logOut();
                getActivity().finish();
            }
        });

        return v;
    }

//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//
//        int titleId = getResources().getIdentifier("action_bar_title", "id",
//                "android");
//
//        TextView yourTextView = (TextView) findViewById(titleId);
//        Typeface customFont = Typeface.createFromAsset(getAssets(), "Chi-TownNF.ttf");
//        yourTextView.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
//        yourTextView.setTypeface(customFont);
//
//        MenuInflater inflater = this.getMenuInflater();
//        inflater.inflate(R.menu.main_activity_actions, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                // app icon in action bar clicked; goto parent activity.
//                this.finish();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

}