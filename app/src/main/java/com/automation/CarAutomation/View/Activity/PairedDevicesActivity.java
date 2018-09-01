package com.automation.CarAutomation.View.Activity;

import android.bluetooth.BluetoothDevice;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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

public class PairedDevicesActivity extends AppCompatActivity {

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

                if( bluetoothContainer.connectKnownBluetoothDevice(bluetoothDevice.getAddress()) )
                    Toast.makeText(App.getContext(), "Doğru", Toast.LENGTH_LONG);
                Log.e("asd", "2");
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.e("RESUME", "1");

        if( ! bluetoothContainer.bluetoothAdapter.isEnabled() )
            Toast.makeText(this, "Bluetooth must be enabled!", Toast.LENGTH_LONG).show();

        showPairedDevicesList();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("PAUSE", "2");
    }

    private void showPairedDevicesList() {
        lvPairedDevicesList.setAdapter(bluetoothContainer.getPairedBluetoothDeviceAdapter(this));
    }


    private void displayAlertDialog() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(App.getContext());

        alertDialogBuilder.setTitle("Warning!");

        alertDialogBuilder
                .setMessage("Bluetooth must be enabled !")
                .setCancelable(true)
                .setIcon(R.drawable.ic_warning_black);


        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}
