package com.mbembac.letsmeetup.Classes;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

/**
 * Data model for friend association
 */

@ParseClassName("Friends")
public class Friends extends ParseObject{

    public void setUser(ParseUser user){
        put("user_from", user);
    }

    public void setFriend(ParseUser friend){
        put("user_to", friend);
    }

    public void setAccepted(){
        put("accepted", false);
    }

    public boolean getAccepted(){
        return getBoolean("accepted");
    }

    public ParseUser getUser(){
        return getParseUser("user_to");
    }

    public ParseUser getFriend(){
        return  getParseUser("user_from");
    }

    public static ParseQuery<Friends> getQuery(){
        return ParseQuery.getQuery(Friends.class);
    }

}
