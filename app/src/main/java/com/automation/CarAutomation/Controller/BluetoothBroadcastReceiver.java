package com.automation.CarAutomation.Controller;
import com.automation.CarAutomation.Model.BluetoothContainer;

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

            bluetoothContainer.bluetoothCommunicationThread = new BluetoothCommunicationThread(bluetoothContainer.bluetoothSocket);
            bluetoothContainer.bluetoothCommunicationThread.start();
            bluetoothContainer.bluetoothCommunicationThread.write("al ;");
        }
        //  TODO: ACL_DISCONNECTED çalışma mantığı. Bluetooth kapatıldığında.
        else if ( "android.bluetooth.device.action.ACL_DISCONNECTED".equals(intent.getAction())){
            Log.e("DISCONNECTED", "KAPANDI");
            bluetoothContainer.bluetoothCommunicationThread.cancel();
        }
        else if ( "android.bluetooth.adapter.action.STATE_CHANGED".equals(intent.getAction())) {
            Log.e("STATE_CHANGED", "ACILDI");


            final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
                    BluetoothAdapter.ERROR);

            if ( state == BluetoothAdapter.STATE_OFF){
                Log.e("STATE_OFF", "1");
                Toast.makeText(App.getContext(), "Bluetooth must be enabled !", Toast.LENGTH_LONG).show();
            }

        }

    }
}
