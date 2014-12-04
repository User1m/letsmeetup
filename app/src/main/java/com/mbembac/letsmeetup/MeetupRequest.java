package com.mbembac.letsmeetup;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.mbembac.letsmeetup.Classes.Meetups;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class MeetupRequest extends Fragment {

    Button refresh;
    public String messageClicked;
    final ArrayList<String> list = new ArrayList<String>();
    String objectId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_meetup_request, container, false);

        refresh = (Button) v.findViewById(R.id.meetup_refresh_button);
        final ListView getlist = (ListView) v.findViewById(R.id.meetup_requests);

        getlist.setBackgroundColor(Color.argb(12, 24, 34, 23));
        getlist.setCacheColorHint(Color.DKGRAY);


        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                list.clear();
                while (!list.isEmpty()) {
                    list.remove(0);
                }

                final ParseQuery<Meetups> query = Meetups.getQuery();
                query.whereEqualTo("user_to", ParseUser.getCurrentUser());
                query.findInBackground(new FindCallback<Meetups>() {

                    @Override
                    public void done(List<Meetups> meets, ParseException e) {
                        if (e == null) {
                            if (meets.size() == 0) {
                                Toast.makeText(getActivity(), "Sorry No Meetup Requests Yet", Toast.LENGTH_SHORT).show();
                            } else {
                                for (Meetups meet : meets) {
                                    String message = meet.getMessage();
                                    if (!list.contains(message)) {
                                        list.add(message);
                                    }
                                }
                            }
                        }

                        ArrayAdapter<String> arrayAdapter =
                                new ArrayAdapter<String>(getActivity(), R.layout.custom_layout, list);
                        getlist.setAdapter(arrayAdapter);
                    }
                });
            }
        });

        //get into list, find item selected, and delete when clicked.

        getlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.setSelected(true);
                String msg = parent.getItemAtPosition(position).toString();
                messageClicked = msg;
                showSimplePopUp();
            }
        });

        return v;

    }

    private void showSimplePopUp() {

        AlertDialog.Builder helpBuilder = new AlertDialog.Builder(getActivity());
        helpBuilder.setTitle("Delete?");
        helpBuilder.setMessage("Are you sure you want to delete this Meetup Message?");
        helpBuilder.setPositiveButton("Delete",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //delete message from list

                        ParseQuery<ParseObject> query = ParseQuery.getQuery("Meetups");
                        query.whereEqualTo("user_to", ParseUser.getCurrentUser());
                        query.findInBackground(new FindCallback<ParseObject>() {

                                                   @Override
                                                   final public void done(List<ParseObject> meets, ParseException e) {
                                                       if (e == null) {
                                                           for (ParseObject meet : meets) {
                                                               String msg = meet.getString("Message");

                                                               if (msg.equals(messageClicked)) {
                                                                   meet.deleteInBackground(new DeleteCallback() {
                                                                       @Override
                                                                       public void done(ParseException e) {
                                                                           if (e == null) {
                                                                               int c = 0;
                                                                               for (String found : list) {
                                                                                   if (found.equals(messageClicked)) {
                                                                                       list.remove(c);
                                                                                       break;
                                                                                   }
                                                                                   c++;
                                                                               }
                                                                               Toast.makeText(getActivity(), "Meetup has been removed. Please Refresh.", Toast.LENGTH_SHORT).show();
                                                                           } else {
                                                                               e.printStackTrace();
                                                                           }
                                                                       }
                                                                   });
                                                               }

                                                           }
                                                       }
                                                   }
                                               }
                        );
                    }
                }
        );
        helpBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener()

                {
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