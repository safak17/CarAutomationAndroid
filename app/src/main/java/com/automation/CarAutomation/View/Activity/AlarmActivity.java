package com.automation.CarAutomation.View.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.automation.CarAutomation.Controller.App;
import com.automation.CarAutomation.Model.Alarm;
import com.automation.CarAutomation.Model.ArduinoVariableContainer;
import com.automation.CarAutomation.Model.BluetoothContainer;
import com.automation.CarAutomation.Model.SharedPreferencesContainer;
import com.automation.CarAutomation.R;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

public class AlarmActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    ArduinoVariableContainer arduinoVariableContainer = ArduinoVariableContainer.getInstance();
    BluetoothContainer bluetoothContainer = BluetoothContainer.getInstance();

    CardView cvTimePicker;
    TextView tvAlarmTime;

    Spinner spinnerDaysOfWeek;

    RadioGroup radioGroupRelay1;
    RadioGroup radioGroupRelay2;
    RadioGroup radioGroupRelay3;
    RadioGroup radioGroupRelay4;

    RadioGroup radioGroupRepeat;

    CardView cvSaveAlarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        spinnerDaysOfWeek = findViewById(R.id.spinner_days_of_week);

        cvTimePicker = findViewById(R.id.cv_time_picker);
        tvAlarmTime = findViewById(R.id.tv_alarm_time);

        radioGroupRelay1 = findViewById(R.id.rg_relay1);
        radioGroupRelay2 = findViewById(R.id.rg_relay2);
        radioGroupRelay3 = findViewById(R.id.rg_relay3);
        radioGroupRelay4 = findViewById(R.id.rg_relay4);

        radioGroupRepeat = findViewById(R.id.rg_repeat);

//        radioGroupRepeat.getCheckedRadioButtonId();


        initRelayNameForTextView();
        cvSaveAlarm = findViewById(R.id.cv_save_alarm);

        cvTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(App.getContext(),"time picker",Toast.LENGTH_LONG).show();
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
                timePickerDialog.show(getFragmentManager(), "Time Picker");

            }
        });

        cvSaveAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Alarm alarm         = new Alarm();

                alarm.setId( generateRandomAlarmID() );
                alarm.setEnable( 1 );

                alarm.setRelay4( getCheckedRadioButtonTag(radioGroupRelay4));
                alarm.setRelay3( getCheckedRadioButtonTag(radioGroupRelay3));
                alarm.setRelay2( getCheckedRadioButtonTag(radioGroupRelay2));
                alarm.setRelay1( getCheckedRadioButtonTag(radioGroupRelay1));

                alarm.setRepeat( getCheckedRadioButtonTag(radioGroupRepeat));
                alarm.setMinute( Long.valueOf( tvAlarmTime.getText().toString().substring(3)));
                alarm.setHour(Long.valueOf( tvAlarmTime.getText().toString().substring(0,2)));
                alarm.setDayOfWeek( Long.valueOf(spinnerDaysOfWeek.getSelectedItemPosition() + 1));

                arduinoVariableContainer.alarmList.add(alarm);

                try {
                    bluetoothContainer.bluetoothCommunicationThread.write(alarm.getAlarmSetCommand());
                    Toast.makeText(getBaseContext(), "Alarm Saved Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }catch (Exception e){ }

            } // OnClick
        }); // btnSaveClicked
    } // OnCreate

    private long getCheckedRadioButtonTag(RadioGroup radioGroup){
        return Long.valueOf(((RadioButton) findViewById(radioGroup.getCheckedRadioButtonId())).getTag().toString());
    }

    public int generateRandomAlarmID() {

        int randomID = getRandomNumberBetween(0, 255);
        while (arduinoVariableContainer.alarmList.contains(randomID))
            randomID = getRandomNumberBetween(0, 255);

        return randomID;
    }

    private int getRandomNumberBetween(int minNumber, int maxNumber) {
        return new Random().nextInt((maxNumber - minNumber) + 1) + minNumber;
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        tvAlarmTime.setText(String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute));
    }

    private void initRelayNameForTextView() {

        String nameRelay1 = SharedPreferencesContainer.getInstance().get_name_of_relay("1");
        ((TextView)findViewById(R.id.tv_alarm_activity_relay1_name)).setText(nameRelay1);

        String nameRelay2 = SharedPreferencesContainer.getInstance().get_name_of_relay("2");
        ((TextView)findViewById(R.id.tv_alarm_activity_relay2_name)).setText(nameRelay2);

        String nameRelay3 = SharedPreferencesContainer.getInstance().get_name_of_relay("3");
        ((TextView)findViewById(R.id.tv_alarm_activity_relay3_name)).setText(nameRelay3);

        String nameRelay4 = SharedPreferencesContainer.getInstance().get_name_of_relay("4");
        ((TextView)findViewById(R.id.tv_alarm_activity_relay4_name)).setText(nameRelay4);
    }
} // AlarmActivity
