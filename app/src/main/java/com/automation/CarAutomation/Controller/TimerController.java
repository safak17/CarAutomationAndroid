package com.automation.CarAutomation.Controller;

import android.os.Handler;
import com.automation.CarAutomation.Model.BluetoothContainer;
import java.util.Timer;
import java.util.TimerTask;

public class TimerController {

    /*  SINGLETON DESIGN PATTERN    */
    private static TimerController instance;

    public TimerController timerController;
    private TimerController(){ }

    public static synchronized TimerController getInstance(){

        return ( instance == null ) ? (instance = new TimerController()) : instance;
    }



    BluetoothContainer bluetoothContainer = BluetoothContainer.getInstance();

    Timer timer;
    TimerTask timerTask;
    public void startTimer(String arduinoCommand, int periodInMilliseconds) {
        timer = new Timer();

        initializeTimerTask(arduinoCommand);

        timer.schedule(timerTask, 0, periodInMilliseconds);
    }

    public void stoptimertask() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    //  We are going to use a handler to be able to run in our TimerTask
    final Handler handler = new Handler();
    public void initializeTimerTask(final String arduinoCommand) {

        timerTask = new TimerTask() {
            public void run() {

                handler.post(new Runnable() {
                    public void run() {
                        bluetoothContainer.bluetoothCommunicationThread.write(arduinoCommand);
                    }
                });
            }
        };
    }

}
