package com.automation.CarAutomation.Controller;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.automation.CarAutomation.Model.Alarm;
import com.automation.CarAutomation.Model.BluetoothContainer;
import com.automation.CarAutomation.Model.ArduinoVariableContainer;
import com.automation.CarAutomation.Model.SharedPreferencesContainer;
import com.automation.CarAutomation.R;

public class AlarmViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    SharedPreferencesContainer sharedPreferencesContainer = SharedPreferencesContainer.getInstance();
    ArduinoVariableContainer arduinoVariableContainer = ArduinoVariableContainer.getInstance();
    BluetoothContainer bluetoothContainer = BluetoothContainer.getInstance();

    private TextView tvAlarmTime;
    private TextView tvAlarmLabel;
    private TextView tvAlarmDay;
    private TextView tvAlarmRelayLabel;
    private TextView tvAlarmRelayStatus;

    private ImageView ivRepeatedIcon;
    private ImageView ivDeleteIcon;


    public AlarmViewHolder(View itemView) {
        super(itemView);

        tvAlarmTime = itemView.findViewById(R.id.tv_alarm_time);
        tvAlarmLabel = itemView.findViewById(R.id.tv_alarm_label);
        tvAlarmDay = itemView.findViewById(R.id.tv_alarm_day);
        tvAlarmRelayLabel = itemView.findViewById(R.id.tv_alarm_relay_label);
        tvAlarmRelayStatus = itemView.findViewById(R.id.tv_alarm_relay_status);

        ivRepeatedIcon = itemView.findViewById(R.id.iv_repeated_icon);
        ivDeleteIcon = itemView.findViewById(R.id.iv_delete_item);
        ivDeleteIcon.setOnClickListener(this);
    }

    public void setUserInterfaceData(Alarm selectedAlarm) {

        this.tvAlarmTime.setText(selectedAlarm.getDigitalClockFormat());
        this.tvAlarmLabel.setText(selectedAlarm.alarmLabel);

        if (selectedAlarm.repeat == 1)  ivRepeatedIcon.setImageResource(R.drawable.ic_repeat);
        else                            ivRepeatedIcon.setImageResource(R.drawable.ic_non_repeated);


        setTextViewAlarmDay(selectedAlarm.dayOfWeek);
        tvAlarmRelayLabel.setText(sharedPreferencesContainer.settings.getString(String.valueOf(selectedAlarm.relayNumber-1), null));


        if (selectedAlarm.relayStatus == 1)
            tvAlarmRelayStatus.setBackground(App.getContext().getResources().getDrawable(R.drawable.status_of_bluetooth_circle_connected));
        else
            tvAlarmRelayStatus.setBackground(App.getContext().getResources().getDrawable(R.drawable.status_of_bluetooth_circle_disconnected));

    }
    private void setTextViewAlarmDay(int dayOfWeek) {
        switch (dayOfWeek) {
            case 1:
                tvAlarmDay.setText("Monday");
                break;
            case 2:
                tvAlarmDay.setText("Tuesday");
                break;
            case 3:
                tvAlarmDay.setText("Wednesday");
                break;
            case 4:
                tvAlarmDay.setText("Thursday");
                break;
            case 5:
                tvAlarmDay.setText("Friday");
                break;
            case 6:
                tvAlarmDay.setText("Saturday");
                break;
            case 7:
                tvAlarmDay.setText("Sunday");
                break;
        }
    }


    @Override
    public void onClick(View view) {

        if (bluetoothContainer.bluetoothSocket == null)
            Toast.makeText(App.getContext(), "Cannot Delete Item. First Connect BTDevice", Toast.LENGTH_SHORT).show();

        else
            displayAlertDialog();

    }// onClick

    private void displayAlertDialog() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(App.getContext());

        alertDialogBuilder.setTitle("Warning!");

        alertDialogBuilder
                .setMessage("Are you sure you want to delete this alarm?")
                .setCancelable(false)
                .setIcon(R.drawable.ic_warning_black)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Alarm clickedAlarm = arduinoVariableContainer.alarmList.get(getAdapterPosition());
                        bluetoothContainer.bluetoothCommunicationThread.write(clickedAlarm.getAlarmDisarmCommand());
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
