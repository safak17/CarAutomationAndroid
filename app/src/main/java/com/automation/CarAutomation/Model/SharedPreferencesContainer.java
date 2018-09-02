package com.automation.CarAutomation.Model;

import android.content.SharedPreferences;

public class SharedPreferencesContainer {

    /*  SINGLETON DESIGN PATTERN    */
    private SharedPreferencesContainer(){}
    private static SharedPreferencesContainer instance;
    public static synchronized SharedPreferencesContainer getInstance(){
        return (instance == null) ? (instance = new SharedPreferencesContainer()) : instance;
    }

    /***** SHARED PREFERENCES *****/
    public SharedPreferences settings;
    public SharedPreferences.Editor editor;

    public Float get_a_value_of(String peripheralName){
        if(peripheralName.equals("temperature"))    return settings.getFloat("a_value_temperature", 1.00f);
        if(peripheralName.equals("current"))        return settings.getFloat("a_value_current", 1.00f);
        if(peripheralName.equals("voltage"))        return settings.getFloat("a_value_voltage", 1.00f);

        return 1.00f;
    }

    public Float get_b_value_of(String peripheralName){
        if(peripheralName.equals("temperature"))    return settings.getFloat("b_value_temperature", 0.00f);
        if(peripheralName.equals("current"))        return settings.getFloat("b_value_current", 0.00f);
        if(peripheralName.equals("voltage"))        return settings.getFloat("b_value_voltage", 0.00f);

        return 0.00f;
    }

    public String get_unit_of(String peripheralName){
        if(peripheralName.equals("temperature"))    return settings.getString("unit_temperature", "C");
        if(peripheralName.equals("current"))        return settings.getString("unit_current", "A");
        if(peripheralName.equals("voltage"))        return settings.getString("unit_voltage", "V");

        return "";
    }
}
