package com.automation.CarAutomation.Controller;

import android.app.Fragment;
import android.os.Handler;
import com.automation.CarAutomation.Model.BluetoothContainer;
import java.util.Timer;
import java.util.TimerTask;

public class TimerController {

    /*  SINGLETON DESIGN PATTERN    */
    private static TimerController instance;
    private TimerController(){ }

    public static synchronized TimerController getInstance(){

        return ( instance == null ) ? (instance = new TimerController()) : instance;
    }



    BluetoothContainer bluetoothContainer = BluetoothContainer.getInstance();



}
