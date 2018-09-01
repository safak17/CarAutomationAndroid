package com.automation.CarAutomation.View.Fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.automation.CarAutomation.Controller.App;
import com.automation.CarAutomation.Model.BluetoothContainer;
import com.automation.CarAutomation.Model.SharedPreferencesContainer;
import com.automation.CarAutomation.R;


public class DashboardFragment extends Fragment {

    public DashboardFragment() { }

    public View rootView;

    SharedPreferencesContainer sharedPreferencesContainer   = SharedPreferencesContainer.getInstance();
    BluetoothContainer bluetoothContainer                   = BluetoothContainer.getInstance();

    public TextView tvVoltageValue;
    public TextView tvCurrentValue;
    public TextView tvTemperatureValue;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);

        tvVoltageValue = rootView.findViewById(R.id.tv_voltage_value);
        tvCurrentValue = rootView.findViewById(R.id.tv_current_value);
        tvTemperatureValue = rootView.findViewById(R.id.tv_temperature_value);

        initSwitchCompatOfRelays();
        initTextViewOfRelays();

        return rootView;
    } // onCreateView


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

    public void setRelaysTextView() {

        //  TODO: getId ne döndürüyor dene.
        for (int textViewId = R.id.tv_relay_1; textViewId <= R.id.tv_relay_4; textViewId++) {

            TextView tvRelayLabel = rootView.findViewById( textViewId );
            String sharedPreferenceKey = String.valueOf( textViewId );
            tvRelayLabel.setText(sharedPreferencesContainer.settings.getString(sharedPreferenceKey , "Relay" + textViewId));
        }

    }// setRelaysTextView()

    private void initTextViewOfRelays()
    {
        for (int textViewId = R.id.tv_relay_1; textViewId <= R.id.tv_relay_4; textViewId++) {

            TextView textView = rootView.findViewById( textViewId );

            final int finalTextViewId = textViewId;
            textView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    displayAlertDialogForEditingRelayLabel( finalTextViewId );
                    return true;
                }
            });
        }

        setRelaysTextView();
    }// initTextViewOfRelays

    private void displayAlertDialogForEditingRelayLabel(final int relayTextViewId)
    {

        final EditText etRelayLabel = new EditText( App.getContext() );

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(App.getContext());
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
                            Toast.makeText(getActivity().getApplicationContext(), "Enter Label Name !", Toast.LENGTH_LONG).show();

                        else {
                            sharedPreferencesContainer.editor.putString(String.valueOf(relayTextViewId), newLabel);
                            sharedPreferencesContainer.editor.apply();

                            Toast.makeText(getActivity().getApplicationContext(), "Saved Successfully", Toast.LENGTH_LONG).show();
                            ((TextView)rootView.findViewById( relayTextViewId )).setText(newLabel);
                            setRelaysTextView();
                            dialog.dismiss();
                        }
                    }});

        //  android.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialogBuilder.show();
    }// displayAlertDialogForEditingRelayLabel()

    private void enableSwitchCompat( boolean enable ){

        for(int switchCompatId=R.id.sw_relay_1; switchCompatId <= R.id.sw_relay_4; switchCompatId++) {

            SwitchCompat switchCompat = rootView.findViewById(switchCompatId);
            switchCompat.setEnabled(enable);

        }// for
    }

}// DashboardFragment
