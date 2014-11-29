package com.mbembac.letsmeetup;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.mbembac.letsmeetup.Classes.Meetups;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ClaudiusThaBeast on 11/18/14.
 */
public class MeetupRequest extends Fragment {

    Button refresh;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_meetup_request, container, false);

        refresh = (Button) v.findViewById(R.id.meetup_refresh_button);
        final ListView getlist = (ListView) v.findViewById(R.id.meetup_requests);

        getlist.setBackgroundColor(Color.argb(12, 24, 34, 23));
        getlist.setCacheColorHint(Color.DKGRAY);

        final ArrayList<String> list = new ArrayList<String>();

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                list.clear();

                final ParseQuery<Meetups> query = Meetups.getQuery();
                query.whereEqualTo("user_to", ParseUser.getCurrentUser());
                query.findInBackground(new FindCallback<Meetups>() {

                    @Override
                    public void done(List<Meetups> meets, ParseException e) {
                        if (e == null) {
                            if (meets.size() == 0) {
                                Toast.makeText(getActivity(), "Sorry No Meetup Requests Yet", Toast.LENGTH_LONG).show();
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

        return v;

    }
}