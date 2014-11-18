package com.mbembac.letsmeetup;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class FriendRequest extends Fragment {


    Button acceptFriend;
    Button rejectFriend;
    Button refresh;
    public ParseUser clickedUser;
    public boolean clickedUserSelected = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_friend_request, container, false);

        acceptFriend = (Button) v.findViewById(R.id.accept_request_button);
        rejectFriend = (Button) v.findViewById(R.id.reject_request_button);
        refresh = (Button) v.findViewById(R.id.refresh_button);

        final ArrayList<String> list = new ArrayList<String>();
        final ListView getlist = (ListView) v.findViewById(R.id.friendresults);
        getlist.setBackgroundColor(Color.argb(12, 24, 34, 23));
        getlist.setCacheColorHint(Color.DKGRAY);

        refresh.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                refresh(getlist, list);
            }
        });

        getlist.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                view.setSelected(true);
                String name = parent.getItemAtPosition(position).toString();

                ParseQuery<ParseUser> query = ParseUser.getQuery();

                query.whereEqualTo("username", name);
                query.findInBackground(new FindCallback<ParseUser>() {
                    @Override
                    public void done(List<ParseUser> parseUsers, ParseException e) {
                        if (e == null) {
                            if (parseUsers.size() == 1) {
                                clickedUser = parseUsers.get(0);
                                Log.d("HERE", clickedUser.getUsername());
                                clickedUserSelected = true;
                            }

                        }
                    }
                });
            }
        });

        acceptFriend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                ParseQuery<ParseObject> query = ParseQuery.getQuery("Friends");
                query.whereEqualTo("userObjectId", ParseUser.getCurrentUser());
                query.whereEqualTo("friendObjectId", clickedUser);
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if (e == null) {
                            if (objects.size() == 1) {
                                if (clickedUserSelected) {
                                    ParseObject currentObject = objects.get(0);
                                    currentObject.put("accepted", true);
                                    currentObject.saveInBackground(
                                            new SaveCallback() {
                                                @Override
                                                public void done(ParseException e) {
                                                    clickedUserSelected = false;
                                                    Log.d("FriendshipAcceptance", " SAVED");
                                                    if (e == null) {
                                                        Log.d("HERE", "Success");
                                                    } else {
                                                        Log.d("HERE", "Error");
                                                    }
                                                }
                                            }
                                    );
                                    clickedUserSelected = false;
                                }
                            }
                        }
                    }
                });
            }

        });
        rejectFriend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Friends");
                query.whereEqualTo("userObjectId", ParseUser.getCurrentUser());
                query.whereEqualTo("friendObjectId", clickedUser);
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if (e == null) {
                            if (objects.size() == 1) {
                                if (clickedUserSelected) {
                                    ParseObject currentObject = objects.get(0);
                                    currentObject.deleteInBackground();
                                    clickedUserSelected = false;
                                }
                            }

                        }
                    }
                });
            }
        });

        return v;

    }

    private void refresh(final ListView getlist, final ArrayList<String> list) {
        //Assume getting ready to fetch new requests
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Friends");
        query.whereEqualTo("userObjectId", ParseUser.getCurrentUser());
        query.whereEqualTo("accepted", false);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    for (ParseObject aObject : objects) {
                        ParseObject currentFriendUser = aObject.getParseObject("friendObjectId");

                        //String username = currentFriendUser.getString("username");
                        Log.d("USERNAME", currentFriendUser.getString("username"));

                        if (!list.contains(currentFriendUser.getString("username"))) {
                            list.add(currentFriendUser.getString("username"));
                        }
                    }
                    ArrayAdapter<String> arrayAdapter =
                            new ArrayAdapter<String>(getActivity(), R.layout.custom_layout, list);
                    getlist.setAdapter(arrayAdapter);
                }
            }
        });
    }
}