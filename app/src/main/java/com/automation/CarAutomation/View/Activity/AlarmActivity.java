package com.automation.CarAutomation.View.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

import com.automation.CarAutomation.Model.Alarm;
import com.automation.CarAutomation.Model.ArduinoVariableContainer;
import com.automation.CarAutomation.Model.BluetoothContainer;
import com.automation.CarAutomation.Model.SharedPreferencesContainer;
import com.automation.CarAutomation.R;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class AlarmActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    ArduinoVariableContainer arduinoVariableContainer       = ArduinoVariableContainer.getInstance();
    SharedPreferencesContainer sharedPreferencesContainer   = SharedPreferencesContainer.getInstance();
    BluetoothContainer bluetoothContainer                   = BluetoothContainer.getInstance();

    Button btnTimePicker;
    Spinner spinnerDaysOfWeek;
    CheckBox cbRepeatEveryWeek;

    Spinner spinnerRelayName;
    CheckBox cbRelayOnOff;

    Button btnSaveAlarm;
    List<String> listRelayLabels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);


        btnTimePicker           =   findViewById(R.id.btn_time_picker);
        spinnerDaysOfWeek       =   findViewById(R.id.spinner_days_of_week);
        cbRepeatEveryWeek       =   findViewById(R.id.cb_alarm_repeat);

        spinnerRelayName        =   findViewById(R.id.spinner_relay_name);
        cbRelayOnOff            =   findViewById(R.id.cb_relay_status);

        btnSaveAlarm = findViewById(R.id.btn_save_alarm);


        initRelayNameSpinner();

        btnTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar calendar = Calendar.getInstance();

                TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(
                        AlarmActivity.this,
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE),
                        true);
                timePickerDialog.setTitle("PICK A ALARM TIME");
                timePickerDialog.setOkText("OK");
                timePickerDialog.setCancelText("Cancel");
                timePickerDialog.setVersion(TimePickerDialog.Version.VERSION_2);
                timePickerDialog.setTimeInterval(1, 10);
                timePickerDialog.show(getFragmentManager(), "Time Picker");

            }
        });

        //  TODO: Bunu ternary ile yazmaya çalış.
        cbRelayOnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( cbRelayOnOff.isChecked())
                    cbRelayOnOff.setText("On");
                else
                    cbRelayOnOff.setText("Off");
            }
        });

        btnSaveAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Alarm alarm         = new Alarm();
                alarm.id            = generateRandomAlarmID();
                alarm.repeat        = (cbRepeatEveryWeek.isChecked()) ? 1 : 0;
                alarm.dayOfWeek     = spinnerDaysOfWeek.getSelectedItemPosition() + 1;
                alarm.hour          = Integer.valueOf( btnTimePicker.getText().toString().substring(0,2));
                alarm.minute        = Integer.valueOf( btnTimePicker.getText().toString().substring(3));
                alarm.relayNumber   = spinnerRelayName.getSelectedItemPosition() + 1;
                alarm.relayStatus   = (cbRelayOnOff.isChecked())? 1 : 0;


                arduinoVariableContainer.alarmList.add(alarm);

                //  TODO: Arduino'dan gelen cevaba göre listeye eklenmesi gerekir.
                try {
                    bluetoothContainer.bluetoothCommunicationThread.write(alarm.getAlarmSetCommand());
                    Toast.makeText(getBaseContext(), "Alarm Saved Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }catch (Exception e){ }

            } // OnClick
        }); // btnSaveClicked
    } // OnCreate


    public int generateRandomAlarmID() {

        List<Integer> alarmIDs = new ArrayList<>();

        for (Alarm alarm : arduinoVariableContainer.alarmList)
            alarmIDs.add(alarm.id);

        Collections.sort(alarmIDs);

        for (int availableAlarmID = 0; availableAlarmID < 256; availableAlarmID++)
            if (!alarmIDs.contains(availableAlarmID))
                return availableAlarmID;

        return -1;
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        btnTimePicker.setText(String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute));
    }

    private void initRelayNameSpinner()
    {
        spinnerRelayName = findViewById(R.id.spinner_relay_name);

        listRelayLabels = new ArrayList<>();

        for (int i = 0; i < 4; i++)
            listRelayLabels.add(sharedPreferencesContainer.settings.getString(String.valueOf(R.id.tv_relay_1  + i), null));

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(
                this,
                R.layout.spinner_relay_item,
                listRelayLabels);

        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_relay_item);
        spinnerRelayName.setAdapter(spinnerArrayAdapter);
    }
} // AlarmActivity
