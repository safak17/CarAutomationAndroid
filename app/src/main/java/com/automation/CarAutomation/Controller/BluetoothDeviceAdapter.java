package com.automation.CarAutomation.Controller;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import com.automation.CarAutomation.R;


public class BluetoothDeviceAdapter extends ArrayAdapter<BluetoothDevice> {


    public BluetoothDeviceAdapter(Context context, ArrayList<BluetoothDevice> bluetoothDeviceList) {
        super(context, 0, bluetoothDeviceList);
    }


    @Override
    public View getView(int position, View listItemView, ViewGroup parent) {

        /*  TODO: LayoutInflater açıklamasına bak. Android Guide. */
        if (listItemView == null)
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.listview_item_paired_bluetooth_device, parent, false);


        BluetoothDevice bluetoothDeviceData = getItem(position);

        TextView tvBluetoothDeviceName = listItemView.findViewById(R.id.tv_device_name);
        tvBluetoothDeviceName.setText( bluetoothDeviceData.getName() );

        return listItemView;
    }//end getView()
}
