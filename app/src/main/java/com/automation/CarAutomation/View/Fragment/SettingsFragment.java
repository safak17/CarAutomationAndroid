package com.automation.CarAutomation.View.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.automation.CarAutomation.Model.ArduinoVariableContainer;
import com.automation.CarAutomation.Model.BluetoothContainer;
import com.automation.CarAutomation.Model.SharedPreferencesContainer;
import com.automation.CarAutomation.R;

import java.util.Calendar;

public class SettingsFragment extends Fragment {public SettingsFragment() { }

    View rootView;

    SharedPreferencesContainer sharedPreferencesContainer   = SharedPreferencesContainer.getInstance();
    BluetoothContainer bluetoothContainer                   = BluetoothContainer.getInstance();

    public TextView tvRealtimeClock;
    public TextView tvTemperature, tvCurrent, tvVoltage;
    public EditText editTextList[] = new EditText[9];
    public CardView cvSyncRealtimeClock;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        getTextViewsFromLayout();
        initEditTexts();

        cvSyncRealtimeClock = rootView.findViewById(R.id.cv_sync_rtc);
        cvSyncRealtimeClock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { syncRealtimeClock(); }
        });

        return rootView;
    } // onCreateView

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e(" SF_ onActivityCreated", "SettingsFragment");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(" SF_onResume", "SettingsFragment");
    }

    public void syncRealtimeClock() {

        //  TODO:   unixtime almayı bir dene.
        //  TODO:   Gün, Pazar'dan başlıyor 1.
        Calendar calendar = Calendar.getInstance();

        String setClockMessage = "cs "
                + calendar.get(Calendar.YEAR) + " "
                + (calendar.get(Calendar.MONTH) + 1) + " "
                + calendar.get(Calendar.DATE) + " "
                + calendar.get(Calendar.HOUR_OF_DAY) + " "
                + calendar.get(Calendar.MINUTE) + " "
                + calendar.get(Calendar.SECOND) + " ;";

        bluetoothContainer.bluetoothCommunicationThread.write(setClockMessage);
    }

    public void setEditTextFromSharedPreferences() {
        editTextList[0] = rootView.findViewById(R.id.et_a_value_temperature);
        editTextList[0].setText(String.valueOf(sharedPreferencesContainer.get_a_value_of("temperature")));

        editTextList[1] = rootView.findViewById(R.id.et_b_value_temperature);
        editTextList[1].setText(String.valueOf(sharedPreferencesContainer.get_b_value_of("temperature")));

        editTextList[2] = rootView.findViewById(R.id.et_temperature_unit);
        editTextList[2].setText(String.valueOf(sharedPreferencesContainer.get_unit_of("temperature")));

        editTextList[3] = rootView.findViewById(R.id.et_a_value_current);
        editTextList[3].setText(String.valueOf(sharedPreferencesContainer.get_a_value_of("current")));

        editTextList[4] = rootView.findViewById(R.id.et_b_value_current);
        editTextList[4].setText(String.valueOf(sharedPreferencesContainer.get_b_value_of("current")));

        editTextList[5] = rootView.findViewById(R.id.et_current_unit);
        editTextList[5].setText(String.valueOf(sharedPreferencesContainer.get_unit_of("current")));

        editTextList[6] = rootView.findViewById(R.id.et_a_value_voltage);
        editTextList[6].setText(String.valueOf(sharedPreferencesContainer.get_a_value_of("voltage")));

        editTextList[7] = rootView.findViewById(R.id.et_b_value_voltage);
        editTextList[7].setText(String.valueOf(sharedPreferencesContainer.get_b_value_of("voltage")));

        editTextList[8] = rootView.findViewById(R.id.et_voltage_unit);
        editTextList[8].setText(String.valueOf(sharedPreferencesContainer.get_unit_of("voltage")));
    }

    private void getTextViewsFromLayout(){
        tvTemperature   = rootView.findViewById(R.id.tv_settings_temperature);
        tvCurrent       = rootView.findViewById(R.id.tv_settings_current);
        tvVoltage       = rootView.findViewById(R.id.tv_settings_voltage);
        tvRealtimeClock = rootView.findViewById(R.id.tv_realtime_clock);
    }

    private void initEditTexts() {

        setEditTextFromSharedPreferences();

        for( final EditText editText : editTextList )
            editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean focus) {

                    if(!focus)  sharedPreferencesContainer.set_peripheral(editText.getTag().toString(), editText.getText().toString());

                }
            });
    }
}
