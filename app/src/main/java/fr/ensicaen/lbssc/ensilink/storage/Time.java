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

    /**
     * The constructor
     * @param time a string to parse to get the time with this form : hour:minutes. Example: 03:15
     */
    Time(String time){
        String[] strings = time.split(Character.toString(':'));
        _hours = Integer.valueOf(strings[0]);
        _minutes = Integer.valueOf(strings[1]);
    }

    private Time(int hour, int minutes){
        _hours = hour;
        _minutes = minutes;
    }

    /**
     *
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
     *
     * @param time to add
     * @return the result of the sum
     */
    public Time add(Time time){
        int minutes = (time._minutes + _minutes);
        int hours = time._hours + _hours + minutes/60;
        minutes %= 60;
        return new Time(hours, minutes);
    }
}
