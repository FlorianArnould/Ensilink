package fr.ensicaen.lbssc.ensilink.storage;

/**
 * @author Florian Arnould
 * @version 1.0
 */

import android.content.Context;

import java.io.File;

import fr.ensicaen.lbssc.ensilink.R;

/**
 * The class that represent a club of the school
 */
public class Club extends Association{

    private int _day;
    private Date _date;
    private Time _time;
    private Time _duration;
    private String _place;

    /**
     * The constructor
     * @param name the name of the club
     * @param day the day of the week as an integer between 1 and 7
     * @param date the date of the next event if it is not weekly
     * @param time the time of the club
     * @param duration the duration of the club
     * @param place the place where it is
     * @param logoFile a file with the path to the logo image
     * @param photoFile a file with the path to the photo image
     */
    Club(String name, int day, Date date, Time time, Time duration, String place, File logoFile, File photoFile){
        super(name, logoFile, photoFile);
        _day = day;
        _date = date;
        _time = time;
        _duration = duration;
        _place = place;
    }

    /**
     *
     * @return a string with the day of week
     */
    public String getDayOfWeek(Context context){
        return context.getResources().getStringArray(R.array.days)[_day-1];
    }

    /**
     *
     * @return the name of the place
     */
    public String getPlace(){
        return _place;
    }

    /**
     *
     * @return the schedule time of the club
     */
    public Time getTime(){
        return _time;
    }

    /**
     *
     * @return the duration of the club
     */
    public Time getDuration(){
        return _duration;
    }

    /**
     *
     * @return return the date of the club if it hasn't weekly event
     */
    public Date getDate(){
        return _date;
    }
}
