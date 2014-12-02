package com.mbembac.letsmeetup;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.TextView;
import android.graphics.Typeface;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.widget.Toast;
import com.parse.ParseACL;

public class FragmentMainPage extends FragmentActivity {
    ViewPager Tab;
    TabPagerAdapter TabAdapter;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe);
        TabAdapter = new TabPagerAdapter(getSupportFragmentManager());
        Tab = (ViewPager) findViewById(R.id.pager);
        Tab.setOnPageChangeListener(
                new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        actionBar = getActionBar();
                        actionBar.setSelectedNavigationItem(position);
                    }
                }
        );
        Tab.setAdapter(TabAdapter);
        actionBar = getActionBar();
        int titleId = getResources().getIdentifier("action_bar_title", "id",
                "android");
        TextView yourTextView = (TextView) findViewById(titleId);
        Typeface customFont = Typeface.createFromAsset(getAssets(), "Chi-TownNF.ttf");
        yourTextView.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
        yourTextView.setTypeface(customFont);
        //Enable Tabs on Action Bar
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        ActionBar.TabListener tabListener = new ActionBar.TabListener() {
            @Override
            public void onTabReselected(android.app.ActionBar.Tab tab,
                                        FragmentTransaction ft) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
                Tab.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(android.app.ActionBar.Tab tab,
                                        FragmentTransaction ft) {
                // TODO Auto-generated method stub
            }
        };
        //Add New Tab
//        actionBar.addTab(actionBar.newTab().setText("Home").setTabListener(tabListener));
        actionBar.addTab(actionBar.newTab().setText("Add Friends").setTabListener(tabListener));
        actionBar.addTab(actionBar.newTab().setText("Friend Requests").setTabListener(tabListener));
        actionBar.addTab(actionBar.newTab().setText("View Meetups").setTabListener(tabListener));


        ParseACL myAppACL = new ParseACL();
        myAppACL.setPublicReadAccess(true);
        myAppACL.setPublicWriteAccess(true);
        ParseACL.setDefaultACL(myAppACL, true);

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            Toast.makeText(this, "Network is Connected.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No Network Connection.", Toast.LENGTH_SHORT).show();
        }
    }
}