package com.automation.CarAutomation.View.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.automation.CarAutomation.Controller.App;
import com.automation.CarAutomation.Model.ArduinoVariableContainer;
import com.automation.CarAutomation.Model.BluetoothContainer;
import com.automation.CarAutomation.Model.SharedPreferencesContainer;
import com.automation.CarAutomation.R;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class AlarmActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    ArduinoVariableContainer arduinoVariableContainer = ArduinoVariableContainer.getInstance();
    SharedPreferencesContainer sharedPreferencesContainer = SharedPreferencesContainer.getInstance();
    BluetoothContainer bluetoothContainer = BluetoothContainer.getInstance();

    CardView cvTimePicker;
    TextView tvAlarmTime;

    Spinner spinnerDaysOfWeek;

    RadioGroup rdRelay1;
    RadioGroup rdRelay2;
    RadioGroup rdRelay3;
    RadioGroup rdRelay4;

    RadioGroup rdRepeat;

    CardView cvSaveAlarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        spinnerDaysOfWeek = findViewById(R.id.spinner_days_of_week);

        cvTimePicker = findViewById(R.id.cv_time_picker);
        tvAlarmTime = findViewById(R.id.tv_alarm_time);

        rdRelay1 = findViewById(R.id.rg_relay1);
        rdRelay2 = findViewById(R.id.rg_relay2);
        rdRelay3 = findViewById(R.id.rg_relay3);
        rdRelay4 = findViewById(R.id.rg_relay4);

        rdRepeat = findViewById(R.id.rg_repeat);


//        rdRelay1.getCheckedRadioButtonId();
//        rdRelay2.getCheckedRadioButtonId();
//        rdRelay3.getCheckedRadioButtonId();
//        rdRelay4.getCheckedRadioButtonId();

//        rdRepeat.getCheckedRadioButtonId();


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

//                // TODO: Yeni Modele göre Güncellenecek
                Toast.makeText(App.getContext(),"time picker",Toast.LENGTH_LONG).show();
//                Alarm alarm         = new Alarm();
//                alarm.id            = generateRandomAlarmID();
//                alarm.repeat        = (cbRepeatEveryWeek.isChecked()) ? 1 : 0;
//                alarm.dayOfWeek     = spinnerDaysOfWeek.getSelectedItemPosition() + 1;
//                alarm.hour          = Integer.valueOf( tvAlarmTime.getText().toString().substring(0,2));
//                alarm.minute        = Integer.valueOf( tvAlarmTime.getText().toString().substring(3));
//                alarm.relayNumber   = spinnerRelayName.getSelectedItemPosition() + 1;
//                alarm.relayStatus   = (cbRelayOnOff.isChecked())? 1 : 0;
//
//
//                arduinoVariableContainer.alarmList.add(alarm);
//
//                //  TODO: Arduino'dan gelen cevaba göre listeye eklenmesi gerekir.
//                try {
//                    bluetoothContainer.bluetoothCommunicationThread.write(alarm.getAlarmSetCommand());
//                    Toast.makeText(getBaseContext(), "Alarm Saved Successfully", Toast.LENGTH_SHORT).show();
//                    finish();
//                }catch (Exception e){ }

            } // OnClick
        }); // btnSaveClicked
    } // OnCreate


    public int generateRandomAlarmID() {

        int randomID = getRandomNumberBetween(0, 255);
        while (arduinoVariableContainer.alarmList.contains(randomID)) ;
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

        for (int i = R.id.tv_alarm_activity_relay1_name, index = 0; i <= R.id.tv_alarm_activity_relay4_name; i++) {

            TextView tvRelayName = findViewById(i);
            tvRelayName.setText(sharedPreferencesContainer.settings.getString(String.valueOf(R.id.tv_relay_1 + index), null));
            index++;
        }
    }
} // AlarmActivity
