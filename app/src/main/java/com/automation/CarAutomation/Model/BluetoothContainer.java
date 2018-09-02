package com.automation.CarAutomation.Model;

import android.annotation.SuppressLint;

import android.support.v4.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;

import com.automation.CarAutomation.Controller.BluetoothCommunicationThread;
import com.automation.CarAutomation.Controller.BluetoothDeviceAdapter;
import com.automation.CarAutomation.Controller.CommandParser;
import com.automation.CarAutomation.R;
import com.automation.CarAutomation.View.Fragment.AlarmFragment;
import com.automation.CarAutomation.View.Fragment.DashboardFragment;
import com.automation.CarAutomation.View.Fragment.SettingsFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class BluetoothContainer extends AppCompatActivity {

    /*  SINGLETON DESIGN PATTERN    */
    private static BluetoothContainer instance;

    public BluetoothAdapter bluetoothAdapter;
    private BluetoothContainer(){
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public static synchronized BluetoothContainer getInstance(){

        return ( instance == null ) ? (instance = new BluetoothContainer()) : instance;
    }


    ArduinoVariableContainer arduinoVariableContainer = ArduinoVariableContainer.getInstance();
    SharedPreferencesContainer sharedPreferencesContainer = SharedPreferencesContainer.getInstance();

    private final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    public BluetoothDevice bluetoothDevice;
    public BluetoothSocket bluetoothSocket;

    public BluetoothCommunicationThread bluetoothCommunicationThread;

    public final int receivedBluetoothDataHandlerID = 0;

    public ArrayList<BluetoothDevice> pairedBluetoothDeviceList = new ArrayList<>();
    private BluetoothDeviceAdapter pairedBluetoothDeviceAdapter;

    public ArrayList<BluetoothDevice> availableBluetoothDeviceList = new ArrayList<>();
    public BluetoothDeviceAdapter availableBluetoothDeviceAdapter;


    public boolean connectKnownBluetoothDevice( String bondedDeviceMacAddress ) {

        bluetoothDevice = bluetoothAdapter.getRemoteDevice( bondedDeviceMacAddress );
        try {
            bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(myUUID);
            bluetoothSocket.connect();

            return bluetoothSocket.isConnected();
        } catch (IOException e) { return false; }
    }//end connectKnownBluetoothDevice()


    public BluetoothDeviceAdapter getPairedBluetoothDeviceAdapter(Context context) {
        pairedBluetoothDeviceList.clear();
        pairedBluetoothDeviceList.addAll( bluetoothAdapter.getBondedDevices() );
        return (pairedBluetoothDeviceAdapter == null )? new BluetoothDeviceAdapter(context, pairedBluetoothDeviceList) : pairedBluetoothDeviceAdapter;
    }



    @SuppressLint("HandlerLeak")
    public Handler receivedBluetoothDataHandler = new Handler() {

        public void handleMessage(Message msg) {

            if (msg.what == receivedBluetoothDataHandlerID) {

                String[] commandResponseList = CommandParser.trimEndOfLineAndGetCommandResponseList(msg.obj);

                for( String commandResponse : commandResponseList ){

                    if(commandResponse.startsWith("OK")){
                        String command = CommandParser.trimResponseAndGetCommandFrom(commandResponse);

                        if( command.startsWith("PERIPHERAL_GET")) {


                            //Fragment fragment = getFragmentManager().findFragmentByTag("fragment_dashboard");


                            DashboardFragment dashboardFragment = (DashboardFragment) getSupportFragmentManager().findFragmentByTag("fragment_dashboard");


                            String[] peripheralData = CommandParser.getPeripheralData(command);
                            Float a, measurement, b;
                            String unit;

                            a                   = SharedPreferencesContainer.getInstance().get_a_value_of("temperature");
                            measurement         = Float.valueOf( peripheralData[1] );
                            b                   = SharedPreferencesContainer.getInstance().get_b_value_of("temperature");
                            unit                = SharedPreferencesContainer.getInstance().get_unit_of("temperature");
                            String temperature  = String.valueOf(a * measurement + b) + " " + unit;
                            dashboardFragment.tvTemperatureValue.setText(String.format(Locale.getDefault(),"%.2f", temperature));

                            a                   = SharedPreferencesContainer.getInstance().get_a_value_of("current");
                            measurement         = Float.valueOf( peripheralData[1] );
                            b                   = SharedPreferencesContainer.getInstance().get_b_value_of("current");
                            unit                = SharedPreferencesContainer.getInstance().get_unit_of("current");
                            String current      = String.valueOf(a * measurement + b) + " " + unit;
                            dashboardFragment.tvTemperatureValue.setText(String.format(Locale.getDefault(),"%.2f", current));

                            a                   = SharedPreferencesContainer.getInstance().get_a_value_of("voltage");
                            measurement         = Float.valueOf( peripheralData[1] );
                            b                   = SharedPreferencesContainer.getInstance().get_b_value_of("voltage");
                            unit                = SharedPreferencesContainer.getInstance().get_unit_of("voltage");
                            String voltage      = String.valueOf(a * measurement + b) + " " + unit;
                            dashboardFragment.tvTemperatureValue.setText(String.format(Locale.getDefault(),"%.2f", voltage));

                        }
                        else if( command.startsWith("CLOCK_GET")){
                            SettingsFragment settingsFragment   = (SettingsFragment) getSupportFragmentManager().findFragmentByTag("fragment_settings");

                            String[] clockDateTimeDataArray     = CommandParser.getClockDateTimeDataArray(command);
                            String dateTimeOfRealTimeClock      =
                                    clockDateTimeDataArray[2] + "/"
                                            + clockDateTimeDataArray[1] + "/"
                                            + clockDateTimeDataArray[0] + "\n"
                                            + clockDateTimeDataArray[3] + ":"
                                            + clockDateTimeDataArray[4] + ":"
                                            + clockDateTimeDataArray[5];

                            settingsFragment.tvRealtimeClock.setText(dateTimeOfRealTimeClock);
                        }
                        else if( command.startsWith("RELAY_OPERATE")) {
                            DashboardFragment dashboardFragment = (DashboardFragment) getSupportFragmentManager().findFragmentByTag("fragment_dashboard");

                            String[] relayOperateData           = CommandParser.getRelayOperateData(command);
                            int relayNumber                     = Integer.valueOf(relayOperateData[0]);
                            boolean relayStatus                 = relayOperateData[1].equals("ACTIVE");

                            SwitchCompat swRelay                = dashboardFragment.rootView.findViewById(R.id.sw_relay_1 + relayNumber -1);
                            swRelay.setChecked(relayStatus);
                        }
                        else if ( command.startsWith("ALARM_LIST_SIZE")){
                            //TODO: neden size alındı ?

                            List<Fragment> listOfFragments = getSupportFragmentManager().getFragments();
                            DashboardFragment dashboardFragment;

                            for (int i = 0; i < listOfFragments.size(); i++)
                                if (listOfFragments.get(i) instanceof DashboardFragment)
                                    dashboardFragment = (DashboardFragment) listOfFragments.get(i);


                            AlarmFragment alarmFragment;
                            for (int i = 0; i < listOfFragments.size(); i++)
                                if (listOfFragments.get(i) instanceof AlarmFragment) {
                                    alarmFragment = (AlarmFragment) listOfFragments.get(i);
                                    alarmFragment.alarmAdapter.notifyDataSetChanged();
                                    break;
                                }



                            arduinoVariableContainer.alarmListSize = CommandParser.getAlarmListSize(command);
                        }
                        else if( command.startsWith("ALARM_LIST_ITEM")){
                            Alarm alarm = new Alarm( CommandParser.getAlarmListItemData(command) );
                            arduinoVariableContainer.alarmList.add(alarm);
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

                            AlarmFragment alarmFragment = (AlarmFragment) getSupportFragmentManager().findFragmentByTag("fragment_alarm");
                            alarmFragment.alarmAdapter.notifyDataSetChanged();

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
