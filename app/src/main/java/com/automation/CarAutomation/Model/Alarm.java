package com.automation.CarAutomation.Model;

import java.util.Locale;

public class Alarm {

    public int id;
    public int repeat;
    public int dayOfWeek;
    public int hour;
    public int minute;
    public int relayNumber;
    public int relayStatus;


    public Alarm(){}

    //  alarmFormat: "1 1 7 23 50 4 0"
    public Alarm(String alarmFormat)
    {
        String alarmProperties[] = alarmFormat.split(" ");        //   Alarm properties are split.

        this.id = Integer.valueOf(alarmProperties[0]);
        this.repeat = Integer.valueOf(alarmProperties[1]);
        this.dayOfWeek = Integer.valueOf(alarmProperties[2]);
        this.hour = Integer.valueOf(alarmProperties[3]);
        this.minute = Integer.valueOf(alarmProperties[4]);
        this.relayNumber = Integer.valueOf(alarmProperties[5]);
        this.relayStatus = Integer.valueOf(alarmProperties[6]);
    }


    public String getAlarmSetCommand() {
        return "as "
                + id + " "
                + repeat + " "
                + dayOfWeek + " "
                + hour + " "
                + minute + " "
                + relayNumber + " "
                + relayStatus + " ;";
    }

    public String getAlarmDisarmCommand(){
        return "ad " + id + " ;";
    }

    public String getDigitalClockFormat() {
        return String.format(Locale.getDefault(), "%02d:%02d", hour, minute);
    }
}
