package com.automation.CarAutomation.Controller;

import android.bluetooth.BluetoothSocket;
import com.automation.CarAutomation.Model.BluetoothContainer;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class BluetoothCommunicationThread extends Thread {

    private InputStream inputStream = null;
    private OutputStream outputStream = null;

    BluetoothContainer bluetoothContainer = BluetoothContainer.getInstance();

    public BluetoothCommunicationThread(BluetoothSocket bluetoothSocket) {
        try {
            inputStream = bluetoothSocket.getInputStream();
            outputStream = bluetoothSocket.getOutputStream();
        } catch (IOException e) {}
    }

    public void run() {
        byte[] buffer = new byte[1024];
        int bytes;

        String readMessage;
        String receivedArduinoMessage ="";
        while (true) {
            try {

                bytes = inputStream.read(buffer);
                readMessage = new String(buffer, 0, bytes);
                receivedArduinoMessage += readMessage;

                // Send the obtained bytes to the UI Activity via handler
                if (receivedArduinoMessage.endsWith(";\r\n")) {
                    bluetoothContainer.receivedBluetoothDataHandler.obtainMessage(
                            bluetoothContainer.receivedBluetoothDataHandlerID,
                            bytes,
                            -1,
                            receivedArduinoMessage).sendToTarget();
                    receivedArduinoMessage = "";
                }
            } catch (IOException e) {}
        }// end while( true )
    }// end run()

    public boolean write(String input) {

        byte[] messageBuffer = input.getBytes();

        try {
            outputStream.write(messageBuffer);
            return true;
        } catch (IOException e) { return false; }
    }//end write()

    // Closes the connect socket and causes the thread to finish.
    public boolean cancel() {
        try {
            bluetoothContainer.bluetoothSocket.close();
            return true;
        } catch (IOException e) { return false; }
    }
}