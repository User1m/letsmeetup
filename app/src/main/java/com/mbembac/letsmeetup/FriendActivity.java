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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.mbembac.letsmeetup.Classes.Friends;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class FriendActivity extends Fragment {

    Button addfriend;
    Button findfriend;
    // Button goback;

    String finduser;
    EditText findusertxt;
    ParseUser clickedUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.activity_find_friends, container, false);


//        ActionBar actionBar = getActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);

//        setContentView(R.layout.activity_find_friends);

        addfriend = (Button) v.findViewById(R.id.addfriend_button);
        findfriend = (Button) v.findViewById(R.id.findfriend_button);
        //goback = (Button) v.findViewById(R.id.go_back_friends);


        findusertxt = (EditText) v.findViewById(R.id.find_user_box);

        final ArrayList<String> list = new ArrayList<String>();
        final ListView getlist = (ListView) v.findViewById(R.id.friendresults);
        ParseUser selectedUser;
        getlist.setBackgroundColor(Color.argb(12, 24, 34, 23));
        getlist.setCacheColorHint(Color.DKGRAY);


        // Add Friend Button Click Listener
        findfriend.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {

                finduser = findusertxt.getText().toString();

                ParseQuery<ParseUser> query = ParseUser.getQuery();

                query.findInBackground(new FindCallback<ParseUser>() {
                    @Override
                    public void done(List<ParseUser> objects, ParseException e) {

                        if (e == null) {
                            if (!finduser.equals("")) {
                                String x = finduser;
                                for (ParseUser oneObject : objects) {
                                    if (oneObject.getUsername().contains(x)) {
                                        if (!list.contains(oneObject.getUsername())) {
                                            list.add(oneObject.getUsername());
                                        }
                                    }
                                }
                                ArrayAdapter<String> arrayAdapter =
                                        new ArrayAdapter<String>(getActivity(), R.layout.custom_layout, list);
                                getlist.setAdapter(arrayAdapter);
                            }
                            if (finduser.equals("")) {
                                Toast.makeText(getActivity(),
                                        "Enter a username to search.",
                                        Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(getActivity(),
                                    "An error occurred.",
                                    Toast.LENGTH_LONG).show();
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

                ParseQuery<ParseUser> query = ParseUser.getQuery();

                query.whereEqualTo("username", name);
                query.findInBackground(new FindCallback<ParseUser>() {
                    @Override
                    public void done(List<ParseUser> parseUsers, ParseException e) {
                        if (e == null) {
                            if (parseUsers.size() == 1) {
                                clickedUser = parseUsers.get(0);
                                Log.d("HERE", clickedUser.getUsername());
                            }

                        }
                    }
                });
            }
        });

        // Add Friend Button Click Listener
        addfriend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Friends newFriendship = new Friends();
                newFriendship.setUser(ParseUser.getCurrentUser());
                newFriendship.setFriend(clickedUser);
                newFriendship.setAccepted();
                newFriendship.saveInBackground(
                        new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                Log.d("Friendship", " SAVED");
                                if (e == null)
//                            completitionCallback.success();
                                    Log.d("HERE", "Success");
                                else
//                            completitionCallback.error(e);
                                    Log.d("HERE", "Error");
//                                e.printStackTrace();

                            }
                        }
                );
            }
        });


//        // Go Back to Home Page Click Listener
//        goback.setOnClickListener(new View.OnClickListener() {
//
//            public void onClick(View arg0) {
//
//                Intent intent = new Intent(getActivity(),
//                        Welcome.class);
//                startActivity(intent);
//                //finish();
//
//            }
//        });

        return v;
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//
//        int titleId = getResources().getIdentifier("action_bar_title", "id",
//                "android");
//
//        TextView yourTextView = (TextView) findViewById(titleId);
//        Typeface customFont = Typeface.createFromAsset(getAssets(), "Chi-TownNF.ttf");
//        yourTextView.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
//        yourTextView.setTypeface(customFont);
//
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.main_activity_actions, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                // app icon in action bar clicked; goto parent activity.
//                this.finish();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

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