package com.automation.CarAutomation.Controller;
import com.automation.CarAutomation.Model.BluetoothContainer;
import com.automation.CarAutomation.View.Activity.PairedDevicesActivity;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;


public class BluetoothBroadcastReceiver extends BroadcastReceiver {

    BluetoothContainer bluetoothContainer   = BluetoothContainer.getInstance();

    final String pairedDevicesActivity      = "com.automation.CarAutomation.View.Activity.PairedDevicesActivity";
    final String tabbedActivity             = "com.automation.CarAutomation.View.Activity.TabbedActivity";
    final String alarmActivity              = "com.automation.CarAutomation.View.Activity.AlarmActivity";

    @Override
    public void onReceive(Context context, Intent intent) {

        if ( "android.bluetooth.device.action.ACL_CONNECTED".equals(intent.getAction())){
            try {

                if( bluetoothContainer.bluetoothSocket.isConnected())
                    showActivity(context, tabbedActivity);
            }catch (Exception e){ Log.e("BroadcastReceiverERROR", "ACL_CONNECTED"); }
        }
        else if ( "android.bluetooth.device.action.ACL_DISCONNECTED".equals(intent.getAction())){
            try {
                Log.e("BroadcastReceiver", "ACL_DISCONNECTED");
                //  Neden bu çıkmıyor ?
                Toast.makeText(context,"Bluetooth is disconnected!", Toast.LENGTH_LONG).show();
                showActivity(context, pairedDevicesActivity);
            }catch (Exception e){ Log.e("BroadcastReceiverERROR", "ACL_DISCONNECTED"); }
        }
        else if ( "android.bluetooth.adapter.action.STATE_CHANGED".equals(intent.getAction())) {
            final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
            if ( state == BluetoothAdapter.STATE_OFF){
                Log.e("BroadcastReceiver", "STATE_OFF");
                try {
                    bluetoothContainer.bluetoothCommunicationThread.cancel();
                }catch (Exception e) {Log.e("BroadscastReceiver", "State_OFF_ERROR");}
                showActivity(context, pairedDevicesActivity);
            }
        }
    }

    private void showActivity(Context context, final String className){
        Intent i = new Intent();
        i.setClassName("com.automation.CarAutomation", className);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

}
