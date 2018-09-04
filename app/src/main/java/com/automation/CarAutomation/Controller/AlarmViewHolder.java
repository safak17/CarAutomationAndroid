package com.automation.CarAutomation.Controller;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import com.automation.CarAutomation.Model.Alarm;
import com.automation.CarAutomation.Model.BluetoothContainer;
import com.automation.CarAutomation.Model.ArduinoVariableContainer;
import com.automation.CarAutomation.Model.SharedPreferencesContainer;
import com.automation.CarAutomation.R;
import com.automation.CarAutomation.View.Activity.TabbedActivity;

public class AlarmViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    ArduinoVariableContainer arduinoVariableContainer       = ArduinoVariableContainer.getInstance();
    BluetoothContainer bluetoothContainer                   = BluetoothContainer.getInstance();

    private TextView tvAlarmRelayInformation;
    private TextView tvDateTimeInformation;
    private ImageView ivDeleteIcon;
    private Switch swAlarmEnableDisable;
    public AlertDialog.Builder alertDialogBuilder;

    public AlarmViewHolder(View itemView) {
        super(itemView);

        tvAlarmRelayInformation = itemView.findViewById(R.id.tv_relay_information);
        tvDateTimeInformation   = itemView.findViewById(R.id.tv_date_time_information);
        ivDeleteIcon            = itemView.findViewById(R.id.iv_delete_alarm);
        ivDeleteIcon.setOnClickListener(this);
        swAlarmEnableDisable = itemView.findViewById(R.id.sw_alarm_enable_disable);
        swAlarmEnableDisable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!swAlarmEnableDisable.isChecked()){
                    tvAlarmRelayInformation.setTextColor(Color.parseColor("#888888"));
                    tvDateTimeInformation.setTextColor(Color.parseColor("#888888"));
                    ivDeleteIcon.setEnabled(false);
                }
                else {
                    tvAlarmRelayInformation.setTextColor(Color.parseColor("#FDFFFC"));
                    tvDateTimeInformation.setTextColor(Color.parseColor("#FDFFFC"));
                    ivDeleteIcon.setEnabled(true);
                }
            }
        });

        alertDialogBuilder = new AlertDialog.Builder( TabbedActivity.mContext );
    }

    public void setUserInterfaceData(Alarm alarmItem) {
        String dateTimeInformation  =   alarmItem.getDateTimeInformation();
        String relayInformation     =   alarmItem.getRelayInformation();

        tvDateTimeInformation.setText( dateTimeInformation );
        tvAlarmRelayInformation.setText( relayInformation );
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
