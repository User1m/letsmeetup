package com.mbembac.letsmeetup.Classes;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

/**
 * Created by ClaudiusThaBeast on 11/28/14.
 */
@ParseClassName("Friends")
public class Friends extends ParseObject {

    public void addUser(ParseUser user){
        put("user", user);
    }

    public void addFriend(ParseUser friend){
        getFriends().add(friend);
    }

    public ParseUser getUser(){
        return (ParseUser) get("user");
    }

    public ParseRelation<ParseUser> getFriends(){
        return getRelation("user_friends");
    }

    public static ParseQuery<Friends> getQuery(){
        return ParseQuery.getQuery(Friends.class);
    }
}
