package com.automation.CarAutomation.View.Fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.automation.CarAutomation.Model.BluetoothContainer;
import com.automation.CarAutomation.Model.SharedPreferencesContainer;
import com.automation.CarAutomation.R;

import org.w3c.dom.Text;


public class DashboardFragment extends Fragment {

    public DashboardFragment() { }

    public View rootView;

    SharedPreferencesContainer sharedPreferencesContainer   = SharedPreferencesContainer.getInstance();
    BluetoothContainer bluetoothContainer                   = BluetoothContainer.getInstance();

    public TextView tvVoltageValue;
    public TextView tvCurrentValue;
    public TextView tvTemperatureValue;
    public TextView tvConnectedDeviceName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);

        tvVoltageValue          = rootView.findViewById(R.id.tv_voltage_value);
        tvCurrentValue          = rootView.findViewById(R.id.tv_current_value);
        tvTemperatureValue      = rootView.findViewById(R.id.tv_temperature_value);
        tvConnectedDeviceName   = rootView.findViewById(R.id.tv_connected_device_name);


        initConnectedDeviceName();
        initTextViewOfRelays();
        initSwitchCompatOfRelays();

        return rootView;
    } // onCreateView

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e(" DF_onActivityCreated",String.valueOf(getActivity().getSupportFragmentManager().getFragments().size()));
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(" DF_onResume",String.valueOf(getActivity().getSupportFragmentManager().getFragments().size()));
        bluetoothContainer.bluetoothCommunicationThread.write("pg ;");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e(" DF_onStart",String.valueOf(getActivity().getSupportFragmentManager().getFragments().size()));
    }

    private void initConnectedDeviceName(){
        tvConnectedDeviceName.setText(bluetoothContainer.bluetoothSocket.getRemoteDevice().getName().toString());
    }

    private void initSwitchCompatOfRelays(){

        for(int switchCompatId=R.id.sw_relay_1; switchCompatId <= R.id.sw_relay_4; switchCompatId++) {

            SwitchCompat switchCompat = rootView.findViewById(switchCompatId);

            switchCompat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    SwitchCompat switchCompat = (SwitchCompat) view;

                    String relayOperateCommand = "ro " + String.valueOf(switchCompat.getTag());

                    relayOperateCommand += (switchCompat.isChecked()) ? " 1 ;" : " 0 ;";
                    switchCompat.toggle();

                    bluetoothContainer.bluetoothCommunicationThread.write(relayOperateCommand);

                }// public void onClick(View view)
            });// View.OnClickListener()
        }// for
    } // initSwitchCompatOfRelays()

    private void initTextViewOfRelays() {

        final TextView relay1     =   (TextView)rootView.findViewById(R.id.tv_relay_1);
        relay1.setText(sharedPreferencesContainer.get_name_of_relay("1"));
        relay1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                displayAlertDialogForEditingRelayLabel("1");
                return true;
            }
        });

        final TextView relay2     =   (TextView)rootView.findViewById(R.id.tv_relay_2);
        relay2.setText(sharedPreferencesContainer.get_name_of_relay("2"));
        relay2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                displayAlertDialogForEditingRelayLabel("2");
                return true;
            }
        });

        final TextView relay3     =   (TextView)rootView.findViewById(R.id.tv_relay_3);
        relay3.setText(sharedPreferencesContainer.get_name_of_relay("3"));
        relay3.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                displayAlertDialogForEditingRelayLabel("3");
                return true;
            }
        });

        final TextView relay4     =   (TextView)rootView.findViewById(R.id.tv_relay_4);
        relay4.setText(sharedPreferencesContainer.get_name_of_relay("4"));
        relay4.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                displayAlertDialogForEditingRelayLabel("4");
                return true;
            }
        });

    }// initTextViewOfRelays


    private void displayAlertDialogForEditingRelayLabel(final String relayNumber)
    {

        final EditText etRelayLabel = new EditText( getContext() );

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder( getContext() );
        alertDialogBuilder.setTitle("Edit Relay Label!")
                .setView(etRelayLabel)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }})
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String newLabel = etRelayLabel.getText().toString();

                        if (TextUtils.isEmpty( newLabel ))
                            Toast.makeText( getContext(), "Enter Label Name !", Toast.LENGTH_LONG).show();

                        else {

                            sharedPreferencesContainer.set_name_of_relay(relayNumber, newLabel);
                            initTextViewOfRelays();
                            Toast.makeText( getContext(), "Saved Successfully", Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                        }
                    }});

        //  android.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialogBuilder.show();
    }// displayAlertDialogForEditingRelayLabel()

}// DashboardFragment
