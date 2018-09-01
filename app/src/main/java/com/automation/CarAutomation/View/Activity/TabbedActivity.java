package com.automation.CarAutomation.View.Activity;

import com.automation.CarAutomation.Controller.App;
import com.automation.CarAutomation.Model.BluetoothContainer;
import com.automation.CarAutomation.R;
import com.automation.CarAutomation.View.Fragment.DashboardFragment;

import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;


public class TabbedActivity extends AppCompatActivity {


    BluetoothContainer bluetoothContainer = BluetoothContainer.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed);


        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        ViewPager mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        final FloatingActionButton fabAddAlarm = findViewById(R.id.fab_add_alarm);
        fabAddAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( TabbedActivity.this , AlarmActivity.class);
                startActivity(intent);
            }
        });

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                stoptimertask();

                if ( tab.getText().equals("Dashboard") ) {
                    startTimer("pg ;", 5000);
                }

                else if ( tab.getText().equals("Alarm")) {
                    fabAddAlarm.setVisibility(View.VISIBLE);
                }

                else if ( tab.getText().equals("Settıngs") )
                    startTimer("cg ;", 1000);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if( tab.getText().equals("Alarm"))
                    fabAddAlarm.setVisibility(View.GONE);

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });


        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));



    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("TABBED_ACTIVITY", "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("TABBED_ACTIVITY", "onStop");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("TABBED_ACTIVITY", "onRestart");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("TABBED_ACTIVITY", "onStart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("TABBED_ACTIVITY", "onDestroy");
        try {
            bluetoothContainer.bluetoothSocket.close();
        } catch (IOException e) { e.printStackTrace(); }
    }

    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() { }


        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_tabbed, container, false);
            return rootView;
        }
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return new DashboardFragment();
/*                case 1:
                    return new AlarmFragment();
                case 2:
                    return new SettingsFragment();*/
                default:
                    return null;
            }
        }

        @Override
        public int getCount() { return 3; }
    }

    //  TODO: Başka bir sınıf yazılabilir.
    Timer timer;
    TimerTask timerTask;
    public void startTimer(String arduinoCommand, int periodInMilliseconds) {
        timer = new Timer();

        initializeTimerTask(arduinoCommand);

        timer.schedule(timerTask, 0, periodInMilliseconds);
    }

    public void stoptimertask() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    //  We are going to use a handler to be able to run in our TimerTask
    final Handler handler = new Handler();
    public void initializeTimerTask(final String arduinoCommand) {

        timerTask = new TimerTask() {
            public void run() {

                handler.post(new Runnable() {
                    public void run() {
                        bluetoothContainer.bluetoothCommunicationThread.write(arduinoCommand);
                    }
                });
            }
        };
    }


}
