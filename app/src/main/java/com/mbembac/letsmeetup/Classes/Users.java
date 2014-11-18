package com.mbembac.letsmeetup.Classes;

import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * User model class
 */
@ParseClassName("Users")
public class Users extends ParseObject{

    public void setLocation(ParseGeoPoint point){
        put("location", point);
    }

    public ParseGeoPoint getLocation(){
        return getParseGeoPoint("location");
    }

    public String getUsername(){
        return getString("username");
    }

    public String getEmail(){
        return getString("email");
    }

    public String getLastName(){
        return getString("last_name");
    }

    public String getFirstName(){
        return getString("first_name");
    }

    public static ParseQuery<Users> getQuery(){
        return ParseQuery.getQuery(Users.class);
    }
}
