package com.mbembac.letsmeetup;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.Toast;

import com.mbembac.letsmeetup.Classes.Friends;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class FriendRequest extends Fragment {

    Button acceptFriend;
    Button rejectFriend;
    Button refresh;
    public ParseUser clickedUser;
    public boolean clickedUserSelected = false;

    static ParseUser currentFriend;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_friend_request, container, false);

        acceptFriend = (Button) v.findViewById(R.id.accept_request_button);
        rejectFriend = (Button) v.findViewById(R.id.reject_request_button);
        refresh = (Button) v.findViewById(R.id.friends_refresh_button);

        final ArrayList<ParseUser> user_list_accepted = new ArrayList<ParseUser>();
        final ArrayList<ParseUser> user_list_unviewed = new ArrayList<ParseUser>();

        final ArrayList<String> user_list_names = new ArrayList<String>();
        final ListView getlist = (ListView) v.findViewById(R.id.friend_requests_results);
        getlist.setBackgroundColor(Color.argb(12, 24, 34, 23));
        getlist.setCacheColorHint(Color.DKGRAY);

        refresh.setOnClickListener(new View.OnClickListener() {


            public void onClick(View arg0) {
                Log.d("HERE", "Getting requests");
                //Assume getting ready to fetch new requests

                List<ParseQuery<Friends>> queries = new ArrayList<ParseQuery<Friends>>();

                final ParseQuery<Friends> query1 = Friends.getQuery();
                query1.whereEqualTo("user_to", ParseUser.getCurrentUser());

                final ParseQuery<Friends> query2 = Friends.getQuery();
                query2.whereEqualTo("user_from", ParseUser.getCurrentUser());

                queries.add(query1);
                queries.add(query2);

                ParseQuery<Friends> superQuery = ParseQuery.or(queries);

//                query.whereEqualTo("user_from", ParseUser.getCurrentUser());
                superQuery.findInBackground(new FindCallback<Friends>() {
                    @Override
                    public void done(List<Friends> requests, ParseException e) {
                        if (e == null) {

                            for (Friends request : requests) {

                                if (!request.getUser().equals(ParseUser.getCurrentUser())) {
                                    try {
                                        currentFriend = request.getFriend().fetch();
                                    } catch (ParseException e1) {
                                        e1.printStackTrace();
                                    }
                                    Log.d("Friend1", currentFriend.getUsername());
                                } else {
                                    try {
                                        currentFriend = request.getUser().fetch();
                                    } catch (ParseException e1) {
                                        e1.printStackTrace();
                                    }
                                    Log.d("Friend2", currentFriend.getUsername());
                                }

                                //make a list of accepted requests
                                if (request.getAccepted()) {
                                    user_list_accepted.add(currentFriend);
                                    request.deleteInBackground();

                                    //make a list of un-responded requests
                                } else {
                                    user_list_names.add(currentFriend.getUsername());
                                    user_list_unviewed.add(currentFriend);
                                }
                            }

                            if (user_list_unviewed.size() <= 0) {
                                Toast.makeText(getActivity(), "Sorry, No Friend Requests Right Now", Toast.LENGTH_LONG).show();
                            }

                            ArrayAdapter<String> arrayAdapter =
                                    new ArrayAdapter<String>(getActivity(), R.layout.custom_layout, user_list_names);
                            getlist.setAdapter(arrayAdapter);


                            if (user_list_accepted.size() > 0) {
                                ParseRelation relation = ParseUser.getCurrentUser().getRelation("friends");
                                for (ParseUser friendUser : user_list_accepted) {
                                    relation.add(friendUser);
                                }
                                ParseUser.getCurrentUser().saveInBackground();
                            }

                        }
                    }
                });
            }
        });


        getlist.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                view.setSelected(true);
                String name = parent.getItemAtPosition(position).toString();

//                Log.d("Selected User is", name);

                for (ParseUser friend : user_list_unviewed) {
                    if (friend.getUsername().equals(name)) {
                        clickedUser = friend;
                    }
                }
            }
        });

        acceptFriend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                final String friend_name = clickedUser.getUsername();

                ParseRelation user_relation = ParseUser.getCurrentUser().getRelation("friends");
                user_relation.add(clickedUser);
                ParseUser.getCurrentUser().saveInBackground();

                ParseQuery<ParseObject> query = ParseQuery.getQuery("Friends");
                query.whereEqualTo("user_from", clickedUser);
                query.whereEqualTo("user_to", ParseUser.getCurrentUser());
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> requests, ParseException e) {
                        if (e == null) {
                            //change accepted to true
                            Log.d("ACCEPTING", Integer.toString(requests.size()) + " requests");
                            for (ParseObject request : requests) {
                                Log.d("HERE", "accepting");
                                request.put("accepted", true);
                                request.saveInBackground();
                            }
                        }
                        Toast.makeText(getActivity(), friend_name + " has been added!", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        rejectFriend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Friends");
                query.whereEqualTo("user_from", clickedUser);
                query.whereEqualTo("user_to", ParseUser.getCurrentUser());
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> requests, ParseException e) {
                        if (e == null) {
                            Log.d("DELETING", Integer.toString(requests.size()) + " requests");
                            for (ParseObject request : requests) {
                                Log.d("HERE", "Delete");
                                try {
                                    request.delete();
                                } catch (ParseException e1) {
                                    e1.printStackTrace();
                                }
                            }
                        }

                        Toast.makeText(getActivity(), "Request has been removed", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        return v;

    }

    private void showSimplePopUp() {

        AlertDialog.Builder helpBuilder = new AlertDialog.Builder(getActivity());
        helpBuilder.setTitle("Add Friend?");
        helpBuilder.setMessage("Send friend request to");
        helpBuilder.setPositiveButton("Add Friend",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                    }
                }
        );
        helpBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Close window
                    }
                }
        );
        // Remember, create doesn't show the dialog
        AlertDialog helpDialog = helpBuilder.create();
        helpDialog.show();
    }

}