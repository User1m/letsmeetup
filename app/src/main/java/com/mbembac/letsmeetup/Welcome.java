package com.mbembac.letsmeetup;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;
import com.mbembac.letsmeetup.Classes.Friends;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class Welcome extends FragmentActivity implements
        GooglePlayServicesClient.OnConnectionFailedListener,
        GooglePlayServicesClient.ConnectionCallbacks,
        com.google.android.gms.location.LocationListener {

    // Declare Variable
    static Button logout;

    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    /*
     * Constants for location update parameters
     */
    // Milliseconds per second
    private static final int MILLISECONDS_PER_SECOND = 1000;

    // The update interval
    private static final int UPDATE_INTERVAL_IN_SECONDS = 5;

    // A fast interval ceiling
    private static final int FAST_CEILING_IN_SECONDS = 1;

    // Update interval in milliseconds
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = MILLISECONDS_PER_SECOND
            * UPDATE_INTERVAL_IN_SECONDS;

    // A fast ceiling of update intervals, used when the app is visible
    private static final long FAST_INTERVAL_CEILING_IN_MILLISECONDS = MILLISECONDS_PER_SECOND
            * FAST_CEILING_IN_SECONDS;

    /*
     * Constants for handling location results
     */
    // Conversion from feet to meters
    private static final float METERS_PER_FEET = 0.3048f;

    // Conversion from kilometers to meters
    private static final int METERS_PER_KILOMETER = 1000;

    // Initial offset for calculating the map bounds
    private static final double OFFSET_CALCULATION_INIT_DIFF = 1.0;

    // Accuracy for calculating the map bounds
    private static final float OFFSET_CALCULATION_ACCURACY = 0.01f;

    // Maximum results returned from a Parse query
    private static final int MAX_POST_SEARCH_RESULTS = 20;

    // Maximum post search radius for map in kilometers
    private static final int MAX_POST_SEARCH_DISTANCE = 100;

    private Location lastLocation;
    private Location currentLocation;

    // A request to connect to Location Services
    private LocationRequest locationRequest;

    // Stores the current instantiation of the location client in this object
    private LocationClient locationClient;

    static ParseUser currentUser;

    static Location myLoc;

    public static Friends myFriends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);

        //create global location object
        locationRequest = LocationRequest.create();
        //set update interval
        locationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        //use high accuracy
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        //set interval ceiling to 1 min
        locationRequest.setFastestInterval(FAST_INTERVAL_CEILING_IN_MILLISECONDS);
        //create new location client
        locationClient = new LocationClient(this, this, this);

        myLoc = (currentLocation == null) ? lastLocation : currentLocation;

        final ArrayList<ParseUser> user_list = new ArrayList<ParseUser>();
        final ArrayList<String> list = new ArrayList<String>();
        final ListView getUsers = (ListView) findViewById(R.id.friendslistView);

        Button friend_opt = (Button) findViewById(R.id.friend_option_button);
        friend_opt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(
                        Welcome.this,
                        FragmentMainPage.class);
                startActivity(intent);
            }
        });

        logout = (Button) findViewById(R.id.logout);

        LocationManager man =
                (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        if (!man.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "Please Enable GPS!", Toast.LENGTH_SHORT).show();
        }

        Typeface customFont = Typeface.createFromAsset(getResources().getAssets(), "BLACKJAR.ttf");

        // Retrieve current user from Parse.com
        currentUser = ParseUser.getCurrentUser();
        currentUser.put("isOnline", true);
        currentUser.saveInBackground();

        // Convert currentUser into String
        //String struser = currentUser.getUsername();
        String username = currentUser.get("first_name").toString();

        String firstLetter = username.substring(0, 1).toUpperCase();
        String restLetters = username.substring(1).toLowerCase();
        username = firstLetter + restLetters;

        // Locate TextView in welcome.xml
        TextView txtuser = (TextView) findViewById(R.id.txtuser);
        txtuser.setTypeface(customFont);

        // Set the currentUser String into TextView
        txtuser.setTextColor(Color.DKGRAY);
        txtuser.isOpaque();
        txtuser.setText("Welcome " + username + "!");


        ParseQuery<Friends> query_friends = Friends.getQuery();
        query_friends.whereEqualTo("user", ParseUser.getCurrentUser());
        query_friends.findInBackground(new FindCallback<Friends>() {
            @Override
            public void done(List<Friends> friend, ParseException e) {
                if (e == null) {
                    if (friend.isEmpty()) {
                        Friends addMe = new Friends();
                        addMe.addUser(ParseUser.getCurrentUser());
                        addMe.saveInBackground();
                        Toast.makeText(Welcome.this, "No Friends Yet! Click Friend Options and add friends.", Toast.LENGTH_LONG).show();
                        myFriends = null;
                    } else if (friend.size() == 1) {
                        myFriends = friend.get(0);
                    }
                }
            }
        });


        if (myFriends != null) {

            Log.d("MYFRIENDS", "NOT NULL");

            list.clear();

            final ParseRelation<ParseUser> friend_relation = myFriends.getFriends();

            ParseGeoPoint mylocation = (ParseGeoPoint) ParseUser.getCurrentUser().get("location");

            ParseQuery<ParseUser> query = friend_relation.getQuery();
            query.whereEqualTo("isOnline", true);
            query.whereNear("location", mylocation);
            Log.d(ParseApplication.APPTAG, " Querying my location");
//        query.setLimit(10);

            query.findInBackground(new FindCallback<ParseUser>() {
                                       @Override
                                       public void done(List<ParseUser> parseUsers, ParseException e) {
                                           if (e == null) {

                                               for (ParseUser user : parseUsers) {
                                                   list.add(user.getUsername());
                                                   user_list.add(user);
                                               }
                                           }
                                           ArrayAdapter<String> arrayAdapter =
                                                   new ArrayAdapter<String>(Welcome.this, R.layout.custom_layout, list);
                                           getUsers.setAdapter(arrayAdapter);
                                       }
                                   }
            );


            getUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    view.setSelected(true);
                    final Intent intent = new Intent(Welcome.this, FriendMapActivty.class);
                    String user_name = parent.getItemAtPosition(position).toString();

                    for (ParseUser user : user_list) {
                        if (user_name == user.get("username")) {

                            double lon = user.getParseGeoPoint("location").getLongitude();
                            double lat = user.getParseGeoPoint("location").getLatitude();

                            intent.putExtra("username", user.getUsername());
                            intent.putExtra("lat", lat);
                            intent.putExtra("lon", lon);

                            Log.d("SENT", "Sending user over");
                        }
                    }
                    startActivity(intent);
                }
            });

        }else{
            Log.d("MYFRIENDS", "IS NULL");
        }

        //register notifications
//        ParsePush.subscribeInBackground("", new SaveCallback() {
//            @Override
//            public void done(ParseException e) {
//                if (e == null) {
//                    Log.d("com.parse.push", "successfully subscribed to the broadcast channel.");
//                } else {
//                    Log.e("com.parse.push", "failed to subscribe for push", e);
//                }
//            }
//        });


        // Logout Button Click Listener
        logout.setOnClickListener(new

                                          View.OnClickListener() {

                                              public void onClick(View arg0) {
                                                  currentUser.put("isOnline", false);
                                                  currentUser.saveInBackground();
                                                  // Logout current user
                                                  currentUser.logOut();
//                                                  Welcome.this.finish();
                                                  Intent intent = new Intent(Welcome.this, LoginSignupActivity.class);
                                                  startActivity(intent);
                                                  finish();
                                              }
                                          }
        );

        if (myLoc == null) {
            Toast.makeText(Welcome.this,
                    "Please wait until GPS is connected.", Toast.LENGTH_LONG).show();
            return;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        locationClient.connect();
    }

    @Override
    protected void onStop() {
        if (locationClient.isConnected()) {
            stopPeriodicUpdates();
        }

        locationClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Toast.makeText(this, "Connected!", Toast.LENGTH_SHORT).show();

        currentLocation = getLocation();
        startPeriodicUpdates();
    }

    @Override
    public void onDisconnected() {
        Toast.makeText(this, "Disconnected. Please re-connect.", Toast.LENGTH_SHORT).show();

    }

    private void startPeriodicUpdates() {
        locationClient.requestLocationUpdates(locationRequest, (com.google.android.gms.location.LocationListener) this);
    }

    private void stopPeriodicUpdates() {
        locationClient.removeLocationUpdates((com.google.android.gms.location.LocationListener) this);
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;
//        String msg = "Update location: " + Double.toHexString(location.getLatitude()) + ", " + Double.toString(location.getLongitude());
//        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        if (lastLocation != null
                && geoPointFromLocation(location)
                .distanceInKilometersTo(geoPointFromLocation(lastLocation)) < 0.01) {
            // If the location hasn't changed by more than 10 meters, ignore it.
            return;
        } else {
            ParseUser user = ParseUser.getCurrentUser();
            user.put("location", geoPointFromLocation(currentLocation));
            user.saveInBackground();
        }

        lastLocation = location;
    }

    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        // Decide what to do based on the original request code
        switch (requestCode) {

            // If the request code matches the code sent in onConnectionFailed
            case CONNECTION_FAILURE_RESOLUTION_REQUEST:

                switch (resultCode) {
                    // If Google Play services resolved the problem
                    case Activity.RESULT_OK:

                        if (ParseApplication.APPDEBUG) {
                            // Log the result
                            Log.d(ParseApplication.APPTAG, "Connected to Google Play services");
                        }

                        break;

                    // If any other result was returned by Google Play services
                    default:
                        if (ParseApplication.APPDEBUG) {
                            // Log the result
                            Log.d(ParseApplication.APPTAG, "Could not connect to Google Play services");
                        }
                        break;
                }

                // If any other request code was received
            default:
                if (ParseApplication.APPDEBUG) {
                    // Report that this Activity received an unknown requestCode
                    Log.d(ParseApplication.APPTAG, "Unknown request code received for the activity");
                }
                break;
        }
    }

    private boolean servicesConnected() {
        // Check that Google Play services is available
        int resultCode =
                GooglePlayServicesUtil.
                        isGooglePlayServicesAvailable(this);
        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            // In debug mode, log the status
            Log.d("Location Updates",
                    "Google Play services is available.");
            // Continue
            return true;
            // Google Play services was not available for some reason.
            // resultCode holds the error code.
        } else {
            // Get the error dialog from Google Play services
            Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
                    resultCode,
                    this,
                    CONNECTION_FAILURE_RESOLUTION_REQUEST);

            // If Google Play services can provide an error dialog
            if (errorDialog != null) {
                // Create a new DialogFragment for the error dialog
                ErrorDialogFragment errorFragment =
                        new ErrorDialogFragment();
                // Set the dialog in the DialogFragment
                errorFragment.setDialog(errorDialog);
                // Show the error dialog in the DialogFragment
                errorFragment.show(getSupportFragmentManager(),
                        "Location Updates");
            }
            return false;
        }
    }

    /*
  * Get the current location
  */
    private Location getLocation() {
        // If Google Play Services is available
        if (servicesConnected()) {
            // Get the current location
            return locationClient.getLastLocation();
        } else {
            return null;
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                Log.d(ParseApplication.APPTAG, "An error occurred when connecting to location services.", e);
                e.printStackTrace();
            }
        } else {
//            Toast.makeText(this, connectionResult.getErrorCode(), Toast.LENGTH_SHORT).show();
            showErrorDialog(connectionResult.getErrorCode());
        }

    }

    private ParseGeoPoint geoPointFromLocation(Location loc) {
        return new ParseGeoPoint(loc.getLatitude(), loc.getLongitude());
    }

    /*
  * Show a dialog returned by Google Play services for the connection error code
  */
    private void showErrorDialog(int errorCode) {
        // Get the error dialog from Google Play services
        Dialog errorDialog =
                GooglePlayServicesUtil.getErrorDialog(errorCode, this,
                        CONNECTION_FAILURE_RESOLUTION_REQUEST);

        // If Google Play services can provide an error dialog
        if (errorDialog != null) {

            // Create a new DialogFragment in which to show the error dialog
            ErrorDialogFragment errorFragment = new ErrorDialogFragment();

            // Set the dialog in the DialogFragment
            errorFragment.setDialog(errorDialog);

            // Show the error dialog in the DialogFragment
            errorFragment.show(getSupportFragmentManager(), ParseApplication.APPTAG);
        }
    }

    // Define a DialogFragment that displays the error dialog
    public static class ErrorDialogFragment extends android.support.v4.app.DialogFragment {
        // Global field to contain the error dialog
        private Dialog mDialog;

        // Default constructor. Sets the dialog field to null
        public ErrorDialogFragment() {
            super();
            mDialog = null;
        }

        // Set the dialog to display
        public void setDialog(Dialog dialog) {
            mDialog = dialog;
        }

        // Return a Dialog to the DialogFragment.
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return mDialog;
        }

    }
}