package com.automation.CarAutomation.Model;

import java.util.ArrayList;

public class ArduinoVariableContainer {
    /*  SINGLETON DESIGN PATTERN    */
    private ArduinoVariableContainer(){}
    private static ArduinoVariableContainer instance;
    public static synchronized ArduinoVariableContainer getInstance(){
        return (instance == null) ? (instance = new ArduinoVariableContainer()) : instance;
    }

    //  In order to see the unit when app first installed.
    public float temperatureA = 1;
    public float temperatureB = 0;
    public String temperatureUnit = "°C";

    //  In order to see the unit when app first installed.
    public float currentA = 1;
    public float currentB = 0;
    public String currentUnit = "A";

    //  In order to see the unit when app first installed.
    public float voltageA = 1;
    public float voltageB = 0;
    public String voltageUnit = "V";

    public int alarmListSize = -1;
    public ArrayList<Alarm> alarmList = new ArrayList<>();
}
