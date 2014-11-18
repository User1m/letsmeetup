package com.mbembac.letsmeetup;

/**
 * Created by amnakhan on 10/16/14.
 */

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.mbembac.letsmeetup.Classes.Friends;
import com.mbembac.letsmeetup.Classes.Meetups;
import com.mbembac.letsmeetup.Classes.Users;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class ParseApplication extends Application {

    //public vars
    public static final boolean APPDEBUG = false;

    public static final String APPTAG = "LETSMEETUP";

    public static final String INTENT_EXTRA_LOCATION = "location";

    private static final String SEARCH_DISTANCE = "searchDistance";

    private static final float DEFAULT_SEARCH_DISTANCE = 250.0f;

    private static SharedPreferences preferences;


    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Friends.class);
        ParseObject.registerSubclass(Users.class);
        ParseObject.registerSubclass(Meetups.class);

        // Add your initialization code here
        Parse.initialize(this, "HAX3UEY7MZ1nfP7UhrkwvWH7FFutZ1vdf0fKzmP7", "sqT98AlebubQTHlwvv5GCpmgk66LiLYOBZBJhMCJ");

        ParseInstallation.getCurrentInstallation().saveInBackground();

        preferences = getSharedPreferences("com.mbembac.letsmeetup", Context.MODE_PRIVATE);

        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();

        // If you would like all objects to be private by default, remove this
        // line.
        defaultACL.setPublicReadAccess(true);

        ParseACL.setDefaultACL(defaultACL, true);
    }

    public static float getSearchDistance() {
        return preferences.getFloat(SEARCH_DISTANCE, DEFAULT_SEARCH_DISTANCE);
    }

    public static void setSearchDistance(float value) {
        preferences.edit().putFloat(SEARCH_DISTANCE, value).commit();
    }
}
