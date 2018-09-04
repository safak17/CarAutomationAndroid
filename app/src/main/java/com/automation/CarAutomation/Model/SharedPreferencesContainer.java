package com.automation.CarAutomation.Model;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

public class SharedPreferencesContainer extends AppCompatActivity {

    /*  SINGLETON DESIGN PATTERN    */
    private SharedPreferencesContainer(){ }
    private static SharedPreferencesContainer instance;
    public static synchronized SharedPreferencesContainer getInstance(){
        return (instance == null) ? (instance = new SharedPreferencesContainer()) : instance;
    }

    /***** SHARED PREFERENCES *****/
    public SharedPreferences settings;
    public SharedPreferences.Editor editor;



    public void set_name_of_relay(String relayNumber, String name){
        editor.putString("name_relay"+relayNumber, name);
        editor.commit();
    }

    public String get_name_of_relay(String relayNumber){
        return settings.getString("name_relay"+relayNumber, "röle"+relayNumber);
    }

    public void set_peripheral(String peripheralName, String value ){

        if(peripheralName.startsWith("unit"))   editor.putString(peripheralName, value);
        else                                    editor.putFloat(peripheralName, Float.valueOf(value));

        editor.commit();
    }

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
        if(peripheralName.equals("temperature"))    return settings.getString("unit_temperature", "°C");
        if(peripheralName.equals("current"))        return settings.getString("unit_current", "A");
        if(peripheralName.equals("voltage"))        return settings.getString("unit_voltage", "V");

        return "";
    }
}
