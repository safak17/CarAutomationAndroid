package com.automation.CarAutomation.View.Activity;

import com.automation.CarAutomation.Controller.BluetoothCommunicationThread;
import com.automation.CarAutomation.Model.BluetoothContainer;
import com.automation.CarAutomation.Model.SharedPreferencesContainer;
import com.automation.CarAutomation.R;
import com.automation.CarAutomation.View.Fragment.AlarmFragment;
import com.automation.CarAutomation.View.Fragment.DashboardFragment;
import com.automation.CarAutomation.View.Fragment.SettingsFragment;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
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
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;


public class TabbedActivity extends AppCompatActivity {


    BluetoothContainer bluetoothContainer = BluetoothContainer.getInstance();
    SharedPreferencesContainer sharedPreferencesContainer = SharedPreferencesContainer.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed);

        bluetoothContainer.bluetoothCommunicationThread = new BluetoothCommunicationThread(bluetoothContainer.bluetoothSocket);
        bluetoothContainer.bluetoothCommunicationThread.start();

        final FloatingActionButton fabAddAlarm = findViewById(R.id.fab_add_alarm);
        fabAddAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( TabbedActivity.this , AlarmActivity.class);
                startActivity(intent);
            }
        });

        TabLayout tabLayout = findViewById(R.id.tabs);
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        ViewPager mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                stoptimertask();

                if ( tab.getText().equals("Dashboard") ) {
                    startTimer("pg ;", 5000);
                }

                else if ( tab.getText().equals("Alarm")) {
                    startTimer("al ;", 5000);
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

        sharedPreferencesContainer.settings = this.getSharedPreferences("settings", Context.MODE_PRIVATE);
        sharedPreferencesContainer.editor = sharedPreferencesContainer.settings.edit();
    }


    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        Log.e("TabbedActivity", "onAttachFragment");
        Log.e("onAttachFragmentSizeS", String.valueOf(getSupportFragmentManager().getFragments().size()));
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("TabbedActivity", "onPause");
    }


    @Override
    protected void onStop() {
        super.onStop();
        Log.e("TabbedActivity", "onStop");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("TabbedActivity", "onRestart");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("TabbedActivity", "onStart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("TabbedActivity", "onDestroy");

        try {
            bluetoothContainer.bluetoothCommunicationThread.cancel();
            Log.e("TabbedActivity", "onDestroyBtClose");
        }catch (Exception e) {}
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
                case 1:
                    return new AlarmFragment();
                case 2:
                    return new SettingsFragment();
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
