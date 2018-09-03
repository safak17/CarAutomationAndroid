package com.automation.CarAutomation.View.Activity;

import com.automation.CarAutomation.Controller.BluetoothCommunicationThread;
import com.automation.CarAutomation.Controller.CommandParser;
import com.automation.CarAutomation.Model.Alarm;
import com.automation.CarAutomation.Model.ArduinoVariableContainer;
import com.automation.CarAutomation.Model.BluetoothContainer;
import com.automation.CarAutomation.Model.SharedPreferencesContainer;
import com.automation.CarAutomation.R;
import com.automation.CarAutomation.View.Fragment.AlarmFragment;
import com.automation.CarAutomation.View.Fragment.DashboardFragment;
import com.automation.CarAutomation.View.Fragment.SettingsFragment;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class TabbedActivity extends AppCompatActivity {

    ArduinoVariableContainer arduinoVariableContainer = ArduinoVariableContainer.getInstance();
    BluetoothContainer bluetoothContainer = BluetoothContainer.getInstance();
    SharedPreferencesContainer sharedPreferencesContainer = SharedPreferencesContainer.getInstance();

    ViewPager mViewPager;
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
        mViewPager = findViewById(R.id.container);
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

                else if ( tab.getText().equals("Settıngs") ){
                    startTimer("cg ;", 1000);
                }


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if( tab.getText().equals("Alarm"))
                    fabAddAlarm.setVisibility(View.GONE);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                Log.e("TA_onTabReselected", "onTabReselected");
            }
        });

        sharedPreferencesContainer.settings = this.getSharedPreferences("settings", Context.MODE_PRIVATE);
        sharedPreferencesContainer.editor = sharedPreferencesContainer.settings.edit();
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.e(" TA_onStart",String.valueOf((getSupportFragmentManager().getFragments().size())));
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(" TA_onResume",String.valueOf((getSupportFragmentManager().getFragments().size())));
        initBluetoothCommunicationHandler();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(" TA_onPause",String.valueOf((getSupportFragmentManager().getFragments().size())));
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(" TA_onStop",String.valueOf((getSupportFragmentManager().getFragments().size())));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(" TA_onDestroy",String.valueOf((getSupportFragmentManager().getFragments().size())));

        try {
            bluetoothContainer.bluetoothCommunicationThread.cancel();
            Log.e("TA", "onDestroyBtClose");
        }catch (Exception e) {}
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

    public void UpdateDashboardUI(){
        DashboardFragment dashboardFragment = (DashboardFragment) FindFragment("fragment_dashboard");

        dashboardFragment.tvTemperatureValue.setText(String.format(Locale.getDefault(),"%.2f", arduinoVariableContainer.temperature));
        dashboardFragment.tvCurrentValue.setText(String.format(Locale.getDefault(),"%.2f", arduinoVariableContainer.current));
        dashboardFragment.tvVoltageValue.setText(String.format(Locale.getDefault(),"%.2f", arduinoVariableContainer.voltage));
    }

    private void UpdateSettingsUI(){
        SettingsFragment settingsFragment = (SettingsFragment) FindFragment("fragment_settings");
        settingsFragment.tvRealtimeClock.setText(arduinoVariableContainer.dateTimeOfRealTimeClock);
    }

    private void UpdateAlarmUI(){
        AlarmFragment alarmFragment = (AlarmFragment) FindFragment("fragment_alarm");
        alarmFragment.alarmAdapter.notifyDataSetChanged();
    }

    private Fragment FindFragment(String fragmentName){
        List<Fragment> listOfFragments = getSupportFragmentManager().getFragments();


        if( fragmentName.equals("fragment_dashboard")){
            for (int i = 0; i < listOfFragments.size(); i++)
                if (listOfFragments.get(i) instanceof DashboardFragment)
                    return listOfFragments.get(i);
        }

        if( fragmentName.equals("fragment_alarm")){
            for (int i = 0; i < listOfFragments.size(); i++)
                if (listOfFragments.get(i) instanceof AlarmFragment)
                    return listOfFragments.get(i);
        }

        if( fragmentName.equals("fragment_settings")){
            for (int i = 0; i < listOfFragments.size(); i++)
                if (listOfFragments.get(i) instanceof SettingsFragment)
                    return listOfFragments.get(i);
        }

        return null;
    }


    @SuppressLint("HandlerLeak")
    private void initBluetoothCommunicationHandler() {
        bluetoothContainer.receivedBluetoothDataHandler = new Handler() {

            public void handleMessage(Message msg) {

                if (msg.what == bluetoothContainer.receivedBluetoothDataHandlerID) {

                    String[] commandResponseList = CommandParser.trimEndOfLineAndGetCommandResponseList(msg.obj);

                    for( String commandResponse : commandResponseList ){

                        if(commandResponse.startsWith("OK")){
                            String command = CommandParser.trimResponseAndGetCommandFrom(commandResponse);

                            if( command.startsWith("PERIPHERAL_GET")) {

                                String[] peripheralData = CommandParser.getPeripheralData(command);

                                Float temperatureMeasurement                = Float.valueOf(peripheralData[1]);
                                arduinoVariableContainer.temperatureA       = sharedPreferencesContainer.get_a_value_of("temperature");
                                arduinoVariableContainer.temperatureB       = sharedPreferencesContainer.get_b_value_of("temperature");
                                arduinoVariableContainer.temperatureUnit    = sharedPreferencesContainer.get_unit_of("temperature");
                                arduinoVariableContainer.temperature        = arduinoVariableContainer.temperature * temperatureMeasurement + arduinoVariableContainer.temperatureB;

                                Float currentMeasurement                    = Float.valueOf(peripheralData[2]);
                                arduinoVariableContainer.currentA           = sharedPreferencesContainer.get_a_value_of("current");
                                arduinoVariableContainer.currentB           = sharedPreferencesContainer.get_b_value_of("current");
                                arduinoVariableContainer.currentUnit        = sharedPreferencesContainer.get_unit_of("current");
                                arduinoVariableContainer.current            = arduinoVariableContainer.currentA * currentMeasurement + arduinoVariableContainer.currentB;

                                Float voltageMeasurement                    = Float.valueOf(peripheralData[3]);
                                arduinoVariableContainer.voltageA           = sharedPreferencesContainer.get_a_value_of("voltage");
                                arduinoVariableContainer.voltageB           = sharedPreferencesContainer.get_b_value_of("voltage");
                                arduinoVariableContainer.voltageUnit        = sharedPreferencesContainer.get_unit_of("voltage");
                                arduinoVariableContainer.voltage            = arduinoVariableContainer.voltageA * voltageMeasurement + arduinoVariableContainer.voltageB;

                                UpdateDashboardUI();
                            }
                            else if( command.startsWith("CLOCK_GET")){

                                String[] clockDateTimeDataArray     = CommandParser.getClockDateTimeDataArray(command);
                                String dateTimeOfRealTimeClock      =
                                        clockDateTimeDataArray[2] + "/"
                                                + clockDateTimeDataArray[1] + "/"
                                                + clockDateTimeDataArray[0] + "\n"
                                                + clockDateTimeDataArray[3] + ":"
                                                + clockDateTimeDataArray[4] + ":"
                                                + clockDateTimeDataArray[5];

                                arduinoVariableContainer.dateTimeOfRealTimeClock = dateTimeOfRealTimeClock;
                                UpdateSettingsUI();
                            }
                            else if( command.startsWith("RELAY_OPERATE")) {

                                Fragment page = getSupportFragmentManager().findFragmentByTag("android:switcher:" + mViewPager.getId() + ":" + mViewPager.getCurrentItem());

                                String[] relayOperateData           = CommandParser.getRelayOperateData(command);
                                int relayNumber                     = Integer.valueOf(relayOperateData[0]);
                                boolean relayStatus                 = relayOperateData[1].equals("ACTIVE");

                                if (mViewPager.getCurrentItem() == 0 && page != null) {
                                    SwitchCompat swRelay = ((DashboardFragment)page).rootView.findViewById(R.id.sw_relay_1 + relayNumber - 1);
                                    swRelay.setChecked(relayStatus);
                                }
                            }
                            else if ( command.startsWith("ALARM_LIST_SIZE")){
                                arduinoVariableContainer.alarmListSize = CommandParser.getAlarmListSize(command);
                            }
                            else if( command.startsWith("ALARM_LIST_ITEM")){
                                Alarm alarm = new Alarm( CommandParser.getAlarmListItemData(command) );
                                arduinoVariableContainer.alarmList.add(alarm);
                                UpdateAlarmUI();
                            }
                            else if( command.startsWith("ALARM_TRIGGERED")){
                                int triggeredAlarmId = CommandParser.getTriggeredAlarmId(command);

                                for (Alarm alarm : arduinoVariableContainer.alarmList)
                                    if (alarm.id == triggeredAlarmId && alarm.repeat != 1) {
                                        arduinoVariableContainer.alarmList.remove(alarm);
                                        sharedPreferencesContainer.editor.remove( String.valueOf( "alarmId" + triggeredAlarmId ) );
                                        sharedPreferencesContainer.editor.commit();
                                        break;
                                    }

                                UpdateAlarmUI();
                            }
                            else if( command.startsWith("ALARM_DISARM")){

                                int alarmID = CommandParser.getDisarmAlarmId(command);

                                for (Alarm alarm : arduinoVariableContainer.alarmList)
                                    if (alarm.id == alarmID) {
                                        arduinoVariableContainer.alarmList.remove(alarm);
                                        sharedPreferencesContainer.editor.remove( String.valueOf( "alarmId" + alarmID ) );
                                        sharedPreferencesContainer.editor.commit();
                                        break;
                                    }


                                UpdateAlarmUI();

                            } // if (command.startsWith("ALARM_DISARM"))
                        }// if( commandResponse.startsWith("OK")

                        else {
                            Log.e("ERROR COMMAND ", "else");
                        }
                    }// for( String command : commandResponseList )
                }
            }
        };
    }

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