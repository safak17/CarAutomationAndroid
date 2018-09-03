package com.automation.CarAutomation.Controller;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
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
import com.automation.CarAutomation.View.Activity.TabbedActivity;

public class AlarmViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    SharedPreferencesContainer sharedPreferencesContainer   = SharedPreferencesContainer.getInstance();
    ArduinoVariableContainer arduinoVariableContainer       = ArduinoVariableContainer.getInstance();
    BluetoothContainer bluetoothContainer                   = BluetoothContainer.getInstance();

    private TextView tvAlarmRelayName;
    private TextView tvDateTime;
    private ImageView ivDeleteIcon;
    public AlertDialog.Builder alertDialogBuilder;

    public AlarmViewHolder(View itemView) {
        super(itemView);

        tvAlarmRelayName    = itemView.findViewById(R.id.tv_relay_name);
        tvDateTime          = itemView.findViewById(R.id.tv_date_time);
        ivDeleteIcon        = itemView.findViewById(R.id.iv_delete_alarm);
        ivDeleteIcon.setOnClickListener(this);


        alertDialogBuilder = new AlertDialog.Builder( TabbedActivity.mContext );
    }

    public void setUserInterfaceData(Alarm alarmItem) {

        String dateTime = alarmItem.getDigitalClockFormat();
        String relayName = sharedPreferencesContainer.settings.getString( String.valueOf( R.id.tv_relay_1 + alarmItem.relayNumber -1 ), null);

        this.tvAlarmRelayName.setText(relayName);


        if (alarmItem.repeat == 1)  this.tvDateTime.setText(dateTime + "\nEvery " + getAlarmDay(alarmItem.dayOfWeek) );
        else                        this.tvDateTime.setText(dateTime + "\nOnce only " + getAlarmDay(alarmItem.dayOfWeek) );



        if (alarmItem.relayStatus == 1) tvAlarmRelayName.setBackgroundColor(Color.parseColor("#42BA16"));
        else                            tvAlarmRelayName.setBackgroundColor(Color.parseColor("#ED3510"));

    }

    private String getAlarmDay(int dayOfWeek) {

        if( dayOfWeek == 1 )    return "Monday";
        if( dayOfWeek == 2 )    return "Tuesday";
        if( dayOfWeek == 3 )    return "Wednesday";
        if( dayOfWeek == 4 )    return "Thursday";
        if( dayOfWeek == 5 )    return "Friday";
        if( dayOfWeek == 6 )    return "Saturday";
        if( dayOfWeek == 7 )    return "Sunday";

        return "null";
    }


    @Override
    public void onClick(View view) {

        if (bluetoothContainer.bluetoothSocket == null)
            Toast.makeText(App.getContext(), "Cannot Delete Item. First Connect BTDevice", Toast.LENGTH_SHORT).show();

        else
            displayAlertDialog();

    }// onClick

    private void displayAlertDialog() {

        //  ((ActivityManager) itemView.getContext().getSystemService(Context.ACTIVITY_SERVICE)).getRunningTasks(1).get(0).topActivity.getClassName()
        //  ((ActivityManager) itemView.getContext().getSystemService(Context.ACTIVITY_SERVICE)).getAppTasks().get(0).getTaskInfo().baseActivity

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
