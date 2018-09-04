package com.automation.CarAutomation.Model;

import java.util.Locale;

public class Alarm {

    /*                  MASKS                   */
    private long MASK_ALARM_ID      =  0xFE000000 ;
    private long MASK_ENABLE        =  0x01000000 ;
    private long MASK_RELAY_4       =  0x00C00000 ;
    private long MASK_RELAY_3       =  0x00300000 ;
    private long MASK_RELAY_2       =  0x000C0000 ;
    private long MASK_RELAY_1       =  0x00030000 ;
    private long MASK_REPEAT        =  0x0000C000 ;
    private long MASK_MINUTE        =  0x00003F00 ;
    private long MASK_HOUR          =  0x000000F8 ;
    private long MASK_DAY_OF_WEEK   =  0x00000007 ;


    /*              DIGIT_VALUE                   */
    private long DIGIT_VALUE_ID            =  0x02000000;
    private long DIGIT_VALUE_ENABLE        =  0x01000000;
    private long DIGIT_VALUE_RELAY_4       =  0x00400000;
    private long DIGIT_VALUE_RELAY_3       =  0x00100000;
    private long DIGIT_VALUE_RELAY_2       =  0x00040000;
    private long DIGIT_VALUE_RELAY_1       =  0x00010000;
    private long DIGIT_VALUE_REPEAT        =  0x00004000;
    private long DIGIT_VALUE_MINUTE        =  0x00000100;
    private long DIGIT_VALUE_HOUR          =  0x00000008;
    private long DIGIT_VALUE_DAY_OF_WEEK   =  0x00000001;



    private long alarmDescription;
    private long id;
    private long enable;
    private long relay4;
    private long relay3;
    private long relay2;
    private long relay1;
    private long repeat;
    private long minute;
    private long hour;
    private long dayOfWeek;

    public Alarm(){}


    public Alarm(long alarmDescription) {
        this.alarmDescription = alarmDescription;
    }


    public String getAlarmSetCommand() {
        return "as " + String.valueOf(alarmDescription) + " ;";
    }

    public String getAlarmDisarmCommand(){
        return "ad " + id + " ;";
    }

    public String getDigitalClockFormat() {
        return String.format(Locale.getDefault(), "%02d:%02d", hour, minute);
    }

    public long id()                {  return ( alarmDescription & MASK_ALARM_ID  ) / DIGIT_VALUE_ID ; }
    public long isEnable()          {  return ( alarmDescription & MASK_ENABLE    ) / DIGIT_VALUE_ENABLE ; }

    public long relay4()            {  return ( alarmDescription & MASK_RELAY_4   ) / DIGIT_VALUE_RELAY_4 ; }
    public long relay3()            {  return ( alarmDescription & MASK_RELAY_3   ) / DIGIT_VALUE_RELAY_3 ; }
    public long relay2()            {  return ( alarmDescription & MASK_RELAY_2   ) / DIGIT_VALUE_RELAY_2 ; }
    public long relay1()            {  return ( alarmDescription & MASK_RELAY_1   ) / DIGIT_VALUE_RELAY_1 ; }

    public long repeat()            {  return ( alarmDescription & MASK_REPEAT    ) / DIGIT_VALUE_REPEAT ; }
    public long minute()            {  return ( alarmDescription & MASK_MINUTE    ) / DIGIT_VALUE_MINUTE ; }
    public long hour()              {  return ( alarmDescription & MASK_HOUR      ) / DIGIT_VALUE_HOUR ; }
    public long dayWeek()           {  return ( alarmDescription & MASK_DAY_OF_WEEK)/ DIGIT_VALUE_DAY_OF_WEEK ; }



    public void setId( long id ) {
        alarmDescription &= onesComplement( MASK_ALARM_ID );       //  Previous id is cleared.
        alarmDescription += id * DIGIT_VALUE_ID;                   //  The new one is set.
    }
    public void setEnable( long enable ) {
        alarmDescription &= onesComplement( MASK_ENABLE );
        alarmDescription += enable * DIGIT_VALUE_ENABLE;
    }




    public void setRelay4(long relay4) {
        alarmDescription &= onesComplement( MASK_RELAY_4 );
        alarmDescription += relay4 * DIGIT_VALUE_RELAY_4;
    }
    public void setRelay3(long relay3) {
        alarmDescription &= onesComplement( MASK_RELAY_3 );
        alarmDescription += relay3 * DIGIT_VALUE_RELAY_3;
    }
    public void setRelay2(long relay2) {
        alarmDescription &= onesComplement( MASK_RELAY_2 );
        alarmDescription += relay2 * DIGIT_VALUE_RELAY_2;
    }
    public void setRelay1(long relay1) {
        alarmDescription &= onesComplement( MASK_RELAY_1 );
        alarmDescription += relay1 * DIGIT_VALUE_RELAY_1;
    }



    public void setRepeat(long repeat){
        alarmDescription &= onesComplement( MASK_REPEAT );
        alarmDescription += repeat * DIGIT_VALUE_REPEAT;
    }
    public void setMinute(long minute){
        alarmDescription &= onesComplement( MASK_MINUTE );
        alarmDescription += minute * DIGIT_VALUE_MINUTE;
    }
    public void setHour(long hour){
        alarmDescription &= onesComplement( MASK_HOUR );
        alarmDescription += hour * DIGIT_VALUE_HOUR;
    }
    public void setDayOfWeek(long dayWeek){
        alarmDescription &= onesComplement( MASK_DAY_OF_WEEK );
        alarmDescription += dayWeek * DIGIT_VALUE_DAY_OF_WEEK;
    }

    public String getDescription()
    {
        return ("id: " +          String.valueOf( id()                ) + " " +
                "isEnable: " +    String.valueOf( isEnable()          ) + " " +
                "relay4: " +      String.valueOf( relay4()            ) + " " +
                "relay3: " +      String.valueOf( relay3()            ) + " " +
                "relay2: " +      String.valueOf( relay2()            ) + " " +
                "relay1: " +      String.valueOf( relay1()            ) + " " +
                "repeat: " +      String.valueOf( repeat()            ) + " " +
                "minute: " +      String.valueOf( minute()            ) + " " +
                "hour: " +        String.valueOf( hour()              ) + " " +
                "dayWeek: " +     String.valueOf( dayWeek()           ));
    }

    public String getRepeatText(){
        if( repeat == 0 ) return "Bir kez";
        if( repeat == 1 ) return "Günlük";
        if( repeat == 2 ) return "Haftalık";

        return "-1";
    }

    public String getAlarmDay() {

        if( dayOfWeek == 1 )    return "Pazartesi";
        if( dayOfWeek == 2 )    return "Salı";
        if( dayOfWeek == 3 )    return "Çarşamba";
        if( dayOfWeek == 4 )    return "Perşembe";
        if( dayOfWeek == 5 )    return "Cuma";
        if( dayOfWeek == 6 )    return "Cumartesi";
        if( dayOfWeek == 7 )    return "Pazar";

        return "";
    }

    public String getDateTimeInformation(){
        return  getDigitalClockFormat() + "\n" +
                getRepeatText() + " " + getAlarmDay() ;
    }

    public String getRelayInformation(){
        SharedPreferencesContainer sharedPreferencesContainer = SharedPreferencesContainer.getInstance();
        return  sharedPreferencesContainer.get_name_of_relay("1") + " " + getInformationOfRelay("1") + "\n" +
                sharedPreferencesContainer.get_name_of_relay("2") + " " + getInformationOfRelay("2") + "\n" +
                sharedPreferencesContainer.get_name_of_relay("3") + " " + getInformationOfRelay("3") + "\n" +
                sharedPreferencesContainer.get_name_of_relay("4") + " " + getInformationOfRelay("4") ;

    }

    private String getInformationOfRelay(String relayNumber){
        if( relayNumber.equals("1"))    return getRelayText(relay1);
        if( relayNumber.equals("2"))    return getRelayText(relay2);
        if( relayNumber.equals("3"))    return getRelayText(relay3);
        if( relayNumber.equals("4"))    return getRelayText(relay4);

        return "";
    }

    private String getRelayText(long relayStatus){
        if( relayStatus == 0)   return "KAPA";
        if( relayStatus == 1)   return "AÇ";
        if( relayStatus == 2)   return "DEĞİŞ";
        if( relayStatus == 3)   return "-";

        return "";
    }

    long onesComplement(long number){ return (0xFFFFFFFF - number) ;}

}
