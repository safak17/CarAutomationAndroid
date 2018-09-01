package com.automation.CarAutomation.Model;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;

import com.automation.CarAutomation.Controller.App;
import com.automation.CarAutomation.Controller.BluetoothCommunicationThread;
import com.automation.CarAutomation.Controller.BluetoothDeviceAdapter;
import com.automation.CarAutomation.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class BluetoothContainer {

    /*  SINGLETON DESIGN PATTERN    */
    private static BluetoothContainer instance;

    public BluetoothAdapter bluetoothAdapter;
    private BluetoothContainer(){
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public static synchronized BluetoothContainer getInstance(){

        return ( instance == null ) ? (instance = new BluetoothContainer()) : instance;
    }


    private final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    public BluetoothDevice bluetoothDevice;
    public BluetoothSocket bluetoothSocket;

    public BluetoothCommunicationThread bluetoothCommunicationThread;
   // public Handler receivedBluetoothDataHandler;
    public final int receivedBluetoothDataHandlerID = 0;

    //  ListView Paired Devices
    public ArrayList<BluetoothDevice> pairedBluetoothDeviceList = new ArrayList<>();
    private BluetoothDeviceAdapter pairedBluetoothDeviceAdapter;

    //  ListView Discovered Devices
    public ArrayList<BluetoothDevice> availableBluetoothDeviceList = new ArrayList<>();
    public BluetoothDeviceAdapter availableBluetoothDeviceAdapter;



    // Android 6.0.0 needs to be granted for Location Permission in order to use Bluetooth
    public void requestLocationPermission(Activity currentActivity) {

        final int REQUEST_ID = 1;

        if (Build.VERSION.SDK_INT >= 23)
            ActivityCompat.requestPermissions(
                    currentActivity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_ID);

    }

    public final int REQUEST_ENABLE_BLUETOOTH = 1;
    public void requestTurnOnBluetoothOfDevice(Activity currentActivity) {

        Intent intentActionRequestEnable = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        currentActivity.startActivityForResult(intentActionRequestEnable, REQUEST_ENABLE_BLUETOOTH);
    }



    // Initializing IntentFilter for Bluetooth Communication Status
    // Registering it to calling activity's broadcast receiver.
    public void initIntentFilterForBluetooth(Activity currentActivity, BroadcastReceiver broadcastReceiver) {

        // Creating Intent Filters for Bluetooth
        IntentFilter filterBluetoothConnectionStatus = new IntentFilter();

        // Indicates a low level (ACL) connection has been established with a remote device.
        filterBluetoothConnectionStatus.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);

        // Indicates a low level (ACL) disconnection from a remote device.
        filterBluetoothConnectionStatus.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);

        // Indicates that a low level (ACL) disconnection has been requested for a remote device, and it will soon be disconnected.
        filterBluetoothConnectionStatus.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);

        // Indicates a change in the bond state of a remote device.
        filterBluetoothConnectionStatus.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);

        // Remote device discovered.
        filterBluetoothConnectionStatus.addAction(BluetoothDevice.ACTION_FOUND);

        // This intent is used to broadcast PAIRING REQUEST
        filterBluetoothConnectionStatus.addAction(BluetoothDevice.ACTION_PAIRING_REQUEST);

        // Now registering TabbedMainActivity to that intent filter.
        currentActivity.registerReceiver(broadcastReceiver, filterBluetoothConnectionStatus);
    }//end initIntentFilterForBluetooth()


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

                    String receivedMessage = (String) msg.obj;                                //  BluetoothCommunicationThread sends the message object.
                    String receivedCommandsList[] = receivedMessage.split(" ;\r\n");   //  Trim the end of lines characters.

            }
        }
    };













}
