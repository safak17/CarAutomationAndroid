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
}
