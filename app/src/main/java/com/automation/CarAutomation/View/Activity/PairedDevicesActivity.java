package com.automation.CarAutomation.View.Activity;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.automation.CarAutomation.Controller.App;
import com.automation.CarAutomation.Model.BluetoothContainer;
import com.automation.CarAutomation.R;

public class PairedDevicesActivity extends Activity {

    static BluetoothContainer bluetoothContainer = BluetoothContainer.getInstance();
    ListView lvPairedDevicesList;
    Button btnPairedDevicesList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paired_devices);

        lvPairedDevicesList = findViewById(R.id.lv_paired_devices_list);
        btnPairedDevicesList = findViewById(R.id.btn_paired_devices_list);

        btnPairedDevicesList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( bluetoothContainer.bluetoothAdapter.isEnabled() )
                    showPairedDevicesList();
                else
                    finish();
            }
        });

        lvPairedDevicesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                BluetoothDevice bluetoothDevice = (BluetoothDevice) lvPairedDevicesList.getItemAtPosition(position);

                if( bluetoothContainer.connectKnownBluetoothDevice(bluetoothDevice.getAddress()) ){
                    Log.e("PairedDeviceActivity", "CONNECTED");
                    Toast.makeText(App.getContext(), "Connection established.", Toast.LENGTH_LONG).show();
                }
                else{
                    Log.e("PairedDeviceActivity", "CONNECTION_FAILED");
                    Toast.makeText(App.getContext(), "Connection failed.", Toast.LENGTH_LONG).show();
                }
            }
        });

        Log.e("PairedDeviceActivity", "onCreate");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("PairedDeviceActivity", "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.e("PairedDeviceActivity", "onResume");

        if( ! bluetoothContainer.bluetoothAdapter.isEnabled() )
            displayAlertDialog();

        showPairedDevicesList();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("PairedDeviceActivity", "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("PairedDeviceActivity", "onStop");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("PairedDeviceActivity", "onRestart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("PairedDeviceActivity", "onDestroy");
        try {
            bluetoothContainer.bluetoothCommunicationThread.cancel();
            Log.e("PairedDeviceActivity", "onDestroyBtClose");
        }catch (Exception e) {}
    }

    private void showPairedDevicesList() {
        lvPairedDevicesList.setAdapter(bluetoothContainer.getPairedBluetoothDeviceAdapter(this));
    }


    public void displayAlertDialog() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder( this );

        alertDialogBuilder.setTitle("Warning!");

        alertDialogBuilder
                .setMessage("Bluetooth must be enabled !")
                .setCancelable(true)
                .setIcon(R.drawable.ic_warning_black)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
