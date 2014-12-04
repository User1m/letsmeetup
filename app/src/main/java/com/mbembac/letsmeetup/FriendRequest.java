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

import com.mbembac.letsmeetup.Classes.FriendRequests;
import com.mbembac.letsmeetup.Classes.Friends;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class FriendRequest extends Fragment {

    Button acceptFriend;
    Button rejectFriend;
    Button refresh;
    public ParseUser clickedUser = ParseUser.getCurrentUser();
    public boolean clickedUserSelected = false;
    protected String friend_name;
    static ParseUser currentFriend;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_friend_request, container, false);

        acceptFriend = (Button) v.findViewById(R.id.accept_request_button);
        rejectFriend = (Button) v.findViewById(R.id.reject_request_button);
        refresh = (Button) v.findViewById(R.id.friends_refresh_button);

        final ArrayList<ParseUser> user_list = new ArrayList<ParseUser>();
        final ArrayList<String> user_list_names = new ArrayList<String>();
        final ListView getlist = (ListView) v.findViewById(R.id.friend_requests_results);
        getlist.setBackgroundColor(Color.argb(12, 24, 34, 23));
        getlist.setCacheColorHint(Color.DKGRAY);

        refresh.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                Log.d("HERE", "Getting requests");
                //Assume getting ready to fetch new requests
                final ParseQuery<FriendRequests> query = FriendRequests.getQuery();
                query.whereEqualTo("user_to", ParseUser.getCurrentUser());
                query.whereEqualTo("accepted", false);

                query.findInBackground(new FindCallback<FriendRequests>() {
                    @Override
                    public void done(List<FriendRequests> requests, ParseException e) {
                        if (e == null) {

                            user_list.clear();
                            user_list_names.clear();

                            if (requests.isEmpty()) {
                                Toast.makeText(getActivity(), "Sorry No Friend Requests Yet", Toast.LENGTH_SHORT).show();
                            } else {
                                for (FriendRequests request : requests) {
                                    try {
                                        currentFriend = request.getFriend().fetch();
                                    } catch (ParseException e1) {
                                        e1.printStackTrace();
                                    }
//                                    Log.d("Friend", currentFriend.getUsername());
                                    if(!user_list_names.contains(currentFriend.getUsername())){
                                        user_list_names.add(currentFriend.getUsername());
                                    }
                                    user_list.add(currentFriend);
                                }
                            }
                        }

                        ArrayAdapter<String> arrayAdapter =
                                new ArrayAdapter<String>(getActivity(), R.layout.custom_layout, user_list_names);
                        getlist.setAdapter(arrayAdapter);
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

                for (ParseUser friend : user_list) {
                    if (friend.getUsername().equals(name)) {
                        clickedUser = friend;
                    }
                }

                friend_name = clickedUser.getUsername();
            }
        });

        acceptFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                String check = ParseUser.getCurrentUser().getUsername().toString();
                String check2 = clickedUser.getUsername().toString();

                if (!check.equals(check2)) {
                    ParseQuery<Friends> query_me = Friends.getQuery();
                    query_me.whereEqualTo("user", ParseUser.getCurrentUser());
                    query_me.findInBackground(new FindCallback<Friends>() {
                        @Override
                        public void done(List<Friends> friends, ParseException e) {
                            if (e == null) {
                                if (friends.size() == 1) {
                                    friends.get(0).addFriend(clickedUser);
                                    friends.get(0).saveInBackground();

                                } else if (friends.isEmpty()) {

                                    Friends newFriends = new Friends();
                                    newFriends.addUser(ParseUser.getCurrentUser());
                                    newFriends.addFriend(clickedUser);
                                    newFriends.saveInBackground();
                                }
                            }
                        }
                    });

                    ParseQuery<Friends> query_friend = Friends.getQuery();
                    query_friend.whereEqualTo("user", clickedUser);
                    query_friend.findInBackground(new FindCallback<Friends>() {
                        @Override
                        public void done(List<Friends> friends, ParseException e) {
                            if (e == null) {
                                if (friends.size() == 1) {
                                    friends.get(0).addFriend(ParseUser.getCurrentUser());
                                    friends.get(0).saveInBackground();

                                } else if (friends.isEmpty()) {
                                    Friends newFriends = new Friends();
                                    newFriends.addUser(clickedUser);
                                    newFriends.addFriend(ParseUser.getCurrentUser());
                                    newFriends.saveInBackground();
                                }
                            }
                        }
                    });

                    ParseQuery<ParseObject> query_requests = ParseQuery.getQuery("FriendRequests");
                    query_requests.whereEqualTo("user_from", clickedUser);
                    query_requests.whereEqualTo("user_to", ParseUser.getCurrentUser());
                    query_requests.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> requests, ParseException e) {
                            if (e == null) {
                                Log.d("ACCEPTING", Integer.toString(requests.size()) + " requests");
                                for (ParseObject request : requests) {
                                    request.deleteInBackground(new DeleteCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            if (e == null) {
                                                int c = 0;
                                                for (String name : user_list_names) {
                                                    if (name.equals(friend_name)) {
                                                        user_list_names.remove(c);
                                                        break;
                                                    }
                                                    c++;
                                                }
                                                Toast.makeText(getActivity(), friend_name + " has been added!", Toast.LENGTH_SHORT).show();
                                            } else {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                }
                            }
                        }
                    });
                } else {
                    Toast.makeText(getActivity(), "No user is selected to Accept!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        rejectFriend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                ParseQuery<ParseObject> query = ParseQuery.getQuery("FriendRequests");
                query.whereEqualTo("user_from", clickedUser);
                query.whereEqualTo("user_to", ParseUser.getCurrentUser());
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> requests, ParseException e) {
                        if (e == null) {
                            Log.d("DELETING", Integer.toString(requests.size()) + " requests");
                            for (ParseObject request : requests) {
                                Log.d("HERE", "Delete");
                                request.deleteInBackground(new DeleteCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e == null) {
                                            int c = 0;
                                            for (String name : user_list_names) {
                                                if (name.equals(friend_name)) {
                                                    user_list_names.remove(c);
                                                    break;
                                                }
                                                c++;
                                            }
                                            Toast.makeText(getActivity(), "Request has been removed. Please Refresh.", Toast.LENGTH_SHORT).show();
                                        } else {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }
                        }
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