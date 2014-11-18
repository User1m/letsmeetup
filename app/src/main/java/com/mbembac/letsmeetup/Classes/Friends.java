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
        put("userObjectId", user);
    }

    public void setFriend(ParseUser friend){
        put("friendObjectId", friend);
    }

    public void setAccepted(){
        put("accepted", false);
    }

    public boolean getAccepted(){
        return getBoolean("accepted");
    }

    public ParseUser getUser(){
        return getParseUser("userObjectId");
    }

    public ParseUser getFriend(){
        return  getParseUser("friendObjectId");
    }

    public static ParseQuery<Friends> getQuery(){
        return ParseQuery.getQuery(Friends.class);
    }

}
