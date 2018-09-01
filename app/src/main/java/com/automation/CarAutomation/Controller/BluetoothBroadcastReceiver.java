package com.automation.CarAutomation.Controller;
import com.automation.CarAutomation.Model.BluetoothContainer;
import com.automation.CarAutomation.View.Activity.PairedDevicesActivity;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;


public class BluetoothBroadcastReceiver extends BroadcastReceiver {

    BluetoothContainer bluetoothContainer = BluetoothContainer.getInstance();

    @Override
    public void onReceive(Context context, Intent intent) {

        if ( "android.bluetooth.device.action.ACL_CONNECTED".equals(intent.getAction())){

            try {
                bluetoothContainer.bluetoothCommunicationThread = new BluetoothCommunicationThread(bluetoothContainer.bluetoothSocket);
                bluetoothContainer.bluetoothCommunicationThread.start();
                bluetoothContainer.bluetoothCommunicationThread.write("al ;");
            }catch (Exception e){
                Log.e("First Pair", "1");
            }

        }

        //  TODO: Bluetooth açık ama
        else if ( "android.bluetooth.device.action.ACL_DISCONNECTED".equals(intent.getAction())){

            try {
                bluetoothContainer.bluetoothCommunicationThread.cancel();
                Log.e("DISCONNECTED", "KAPANDI");
                Toast.makeText(context, "Bluetoth Disconnected ! ", Toast.LENGTH_LONG).show();
            }catch (Exception e) { }
        }
        else if ( "android.bluetooth.adapter.action.STATE_CHANGED".equals(intent.getAction())) {
            Log.e("STATE_CHANGED", "ACILDI");


            final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
                    BluetoothAdapter.ERROR);

            if ( state == BluetoothAdapter.STATE_OFF){
                Log.e("STATE_OFF", "1");
                showPairedDevicesActivityIntent(context);
            }
        }
    }

    private void showPairedDevicesActivityIntent(Context context){
        Intent i = new Intent();
        i.setClassName("com.automation.CarAutomation", "com.automation.CarAutomation.View.Activity.PairedDevicesActivity");
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
}
