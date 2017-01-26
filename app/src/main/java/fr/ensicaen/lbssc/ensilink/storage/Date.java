package fr.ensicaen.lbssc.ensilink.storage;

/**
 * @author Florian Arnould
 * @version 1.0
 */

/**
 * A representation of a date
 */
public final class Date{
    private final int _year;
    private final int _month;
    private final int _dayOfMonth;

    /**
     * The constructor
     * @param date a string to parse to get the date with this form : day-month-year. Example: 22-05-2016
     */
    public Date(String date){
        String[] strings = date.split(Character.toString('-'));
        _dayOfMonth = Integer.valueOf(strings[0]);
        _month = Integer.valueOf(strings[1]);
        _year = Integer.valueOf(strings[2]);
    }

    /**
     * @return the date with this form : day-month-year. Example: 22-05-2016
     */
    public String toString(){
        return String.valueOf(_dayOfMonth) + '-' + String.valueOf(_month) + '-' + String.valueOf(_year);
    }
}
