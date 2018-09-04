package com.automation.CarAutomation.Controller;



public class CommandParser {

    public static String trimResponseAndGetCommandFrom(String formattedCommand) {
        return (formattedCommand.startsWith("OK: ")) ? formattedCommand.substring(4) : formattedCommand.substring(7);
    }

    public static String[] trimEndOfLineAndGetCommandResponseList(Object receivedBluetoothMessageObject) {
        String receivedBluetoothMessage = (String) receivedBluetoothMessageObject;
        String receivedCommandsList[] = receivedBluetoothMessage.split(" ;\r\n");
        return receivedCommandsList;
    }

    //  relayOperateResponse            = "RELAY_OPERATE 3 1"
    public static String[] getRelayOperateData(String relayOperateResponse) {
        int indexOfRelayNumber = relayOperateResponse.indexOf(" ") + 1;
        relayOperateResponse = relayOperateResponse.substring(indexOfRelayNumber);                  //  "RELAY_OPERATE " is trimmed.
        String relayOperateData[] = relayOperateResponse.split(" ");
        return relayOperateData;
    }

    //  peripheralGetResponse           = "PERIPHERAL_GET 4 22 10 30"
    public static String[] getPeripheralData(String peripheralGetResponse) {
        int indexOfRelayStatus  = peripheralGetResponse.indexOf(" ") + 1;
        peripheralGetResponse   = peripheralGetResponse.substring(indexOfRelayStatus);              //  "PERIPHERAL_GET " is trimmed.
        String peripheralData[] = peripheralGetResponse.split(" ");
        return peripheralData;
    }

    //  alarmListSizeResponse           = "ALARM_LIST_SIZE 10"
    public static int getAlarmListSize(String alarmListSizeResponse) {
        int indexOfAlarmListSize = alarmListSizeResponse.indexOf(" ") + 1;
        String alarmListSize = alarmListSizeResponse.substring(indexOfAlarmListSize);               //  "ALARM_LIST_SIZE " is trimmed.
        return Integer.valueOf(alarmListSize);
    }

    // alarmListItemResponse            = "ALARM_LIST_ITEM alarmDescription"
    public static long getAlarmListItemData(String alarmListItemResponse) {
        int indexOfAlarmDescription = alarmListItemResponse.indexOf(" ") + 1;
        long alarmListItemData = Long.valueOf(alarmListItemResponse.substring(indexOfAlarmDescription));                 //  "ALARM_LIST_ITEM " is trimmed.
        return alarmListItemData;
    }


    //  alarmTriggeredResponse          = "ALARM_TRIGGERED 2"
    public static int getTriggeredAlarmId(String alarmTriggeredResponse) {
        int indexOfAlarmId = alarmTriggeredResponse.indexOf(" ") + 1;
        String alarmId = alarmTriggeredResponse.substring(indexOfAlarmId);                          //  "ALARM_TRIGGERED " is trimmed.
        return Integer.valueOf(alarmId);
    }

    //  alarmDisarmResponse             = "ALARM_DISARM 2"
    public static int getDisarmAlarmId(String alarmDisarmResponse) {
        int indexOfAlarmId = alarmDisarmResponse.indexOf(" ") + 1;
        String alarmId = alarmDisarmResponse.substring(indexOfAlarmId);                             //  "ALARM_DISARM " is trimmed.
        return Integer.valueOf(alarmId);
    }

    //  getClockResponse                = "CLOCK_GET 2018 9 2 14 50 0"
    public static String[] getClockDateTimeDataArray(String clockGetResponse) {
        int indexOfYear = clockGetResponse.indexOf(" ") + 1;
        String clockDateTime = clockGetResponse.substring(indexOfYear);                             //  "CLOCK_GET " is trimmed.
        String[] clockDateTimeDataArray = clockDateTime.split(" ");
        return clockDateTimeDataArray;
    }

}