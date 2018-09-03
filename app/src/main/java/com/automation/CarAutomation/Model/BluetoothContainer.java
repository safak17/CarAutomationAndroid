package com.automation.CarAutomation.Model;

import android.annotation.SuppressLint;

import android.app.Application;
import android.content.pm.ActivityInfo;
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

import com.automation.CarAutomation.Controller.App;
import com.automation.CarAutomation.Controller.BluetoothCommunicationThread;
import com.automation.CarAutomation.Controller.BluetoothDeviceAdapter;
import com.automation.CarAutomation.Controller.CommandParser;
import com.automation.CarAutomation.R;
import com.automation.CarAutomation.View.Activity.TabbedActivity;
import com.automation.CarAutomation.View.Fragment.AlarmFragment;
import com.automation.CarAutomation.View.Fragment.DashboardFragment;
import com.automation.CarAutomation.View.Fragment.SettingsFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

//  AppCompatActivity
//  App
public class BluetoothContainer{

    /*  SINGLETON DESIGN PATTERN    */
    private static BluetoothContainer instance;
    public BluetoothAdapter bluetoothAdapter;
    private BluetoothContainer(){ bluetoothAdapter = BluetoothAdapter.getDefaultAdapter(); }
    public static synchronized BluetoothContainer getInstance(){
        return ( instance == null ) ? (instance = new BluetoothContainer()) : instance;
    }


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

    public Handler receivedBluetoothDataHandler;
}