package com.mbembac.letsmeetup.Classes;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

/**
 * Created by Jon Ruben on 10/17/2014.
 */
@ParseClassName("Meetups")
public class Meetups extends ParseObject {

    public void setUserFrom(ParseUser user){
        put("user_from", user);
    }

    public void setUserTo(ParseUser user){
        put("user_to", user);
    }

    public void setMessage(String msg){
        put("Message", msg);
    }

    public ParseUser getUserFrom(){
        return getParseUser("user_from");
    }

    public ParseUser getUserTo(){
        return getParseUser("user_to");
    }

    public String getMessage(){
        return getString("Message");
    }

    public static ParseQuery<Meetups> getQuery(){
        return ParseQuery.getQuery(Meetups.class);
    }

}
