package fr.ensicaen.lbssc.ensilink.storage;

/**
 * @author Florian Arnould
 * @version 1.0
 */

/**
 * A representation of a time
 */
public final class Time {

    private final int _hours;
    private final int _minutes;
    private final int _seconds;

    /**
     * The constructor
     * @param time a string to parse to get the time with this form : hour:minutes. Example: 03:15
     */
    public Time(String time){
        String[] strings = time.split(Character.toString(':'));
        _hours = Integer.valueOf(strings[0]);
        _minutes = Integer.valueOf(strings[1]);
        _seconds = Integer.valueOf(strings[2]);
    }

    private Time(int hour, int minutes, int seconds){
        _hours = hour;
        _minutes = minutes;
        _seconds = seconds;
    }

    /**
     * @return the time with this form : hour:minutes. Example: 03:15
     */
    public String toString(){
        String str = "";
        if(_hours < 10){
            str += "0";
        }
        str += _hours + ":";
        if(_minutes < 10){
            str += "0";
        }
        return  str + _minutes;
    }

    /**
     * @param time to add
     * @return the result of the sum
     */
    public Time add(Time time){
        int seconds = (time._seconds + _seconds);
        int minutes = (time._minutes + _minutes + seconds/60);
        seconds %= 60;
        int hours = time._hours + _hours + minutes/60;
        minutes %= 60;
        return new Time(hours, minutes, seconds);
    }
}
