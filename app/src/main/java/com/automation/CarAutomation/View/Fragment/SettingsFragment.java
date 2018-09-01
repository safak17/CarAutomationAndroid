package com.automation.CarAutomation.View.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.automation.CarAutomation.Model.BluetoothContainer;
import com.automation.CarAutomation.Model.SharedPreferencesContainer;
import com.automation.CarAutomation.R;

import java.util.Calendar;

public class SettingsFragment extends Fragment {public SettingsFragment() { }

    View rootView;

    SharedPreferencesContainer sharedPreferencesContainer   = SharedPreferencesContainer.getInstance();
    BluetoothContainer bluetoothContainer                   = BluetoothContainer.getInstance();

    //  TODO: SharedPreferences kaydedilmesi.
    public TextView tvRealtimeClock;
    TextView tvTemperature, tvCurrent, tvVoltage;
    EditText editTextList[] = new EditText[9];
    Button btnSyncRealtimeClock;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        initTextViews();
        initEditTexts();

        btnSyncRealtimeClock = rootView.findViewById(R.id.btn_sync_rtc);
        btnSyncRealtimeClock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { syncRealtimeClock(); }
        });

        return rootView;
    } // onCreateView

    public void syncRealtimeClock() {

        //  Get the Android's time.
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

        for( final EditText editText : editTextList ){

            String sharedPreferencesKey = String.valueOf(editText.getId());

            if( editText.getTag().toString().startsWith("unit"))
                editText.setText(sharedPreferencesContainer.settings.getString(sharedPreferencesKey, "Unit"));
            else
                editText.setText(String.valueOf(sharedPreferencesContainer.settings.getFloat(sharedPreferencesKey, 0.0f)));
        }
    }

    private void initTextViews(){
        tvTemperature = rootView.findViewById(R.id.tv_settings_temperature);
        tvCurrent = rootView.findViewById(R.id.tv_settings_current);
        tvVoltage = rootView.findViewById(R.id.tv_settings_voltage);
        tvRealtimeClock = rootView.findViewById(R.id.tv_realtime_clock);
    }

    private void initEditTexts()
    {
        editTextList[0] = rootView.findViewById(R.id.et_a_value_temperature);
        editTextList[1] = rootView.findViewById(R.id.et_b_value_temperature);
        editTextList[2] = rootView.findViewById(R.id.editText_temperature_unit);

        editTextList[3] = rootView.findViewById(R.id.et_a_value_current);
        editTextList[4] = rootView.findViewById(R.id.et_b_value_current);
        editTextList[5] = rootView.findViewById(R.id.editText_current_unit);

        editTextList[6] = rootView.findViewById(R.id.et_a_value_voltage);
        editTextList[7] = rootView.findViewById(R.id.et_b_value_voltage);
        editTextList[8] = rootView.findViewById(R.id.editText_voltage_unit);


        for( final EditText editText : editTextList )
            editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean focus) {
                    if(!focus) {

                        String sharedPreferencesKey = String.valueOf(editText.getId());

                        if( editText.getTag().toString().startsWith("unit"))
                            sharedPreferencesContainer.editor.putString( sharedPreferencesKey, editText.getText().toString());
                        else
                            sharedPreferencesContainer.editor.putFloat( sharedPreferencesKey, Float.valueOf(editText.getText().toString()));

                        sharedPreferencesContainer.editor.commit();
                    }
                }
            });

        setEditTextFromSharedPreferences();
    }
}
