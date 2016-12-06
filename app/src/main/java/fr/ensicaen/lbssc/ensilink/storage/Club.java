package fr.ensicaen.lbssc.ensilink.storage;


//TODO get the days from strings.xml

/**
 * @author Florian Arnould
 * @version 1.0
 */

/**
 * The class that represent a club of the school
 */
public class Club extends Association{

    private int _day;
    private Date _date;
    private Time _time;
    private Time _duration;
    private String _place;
    private static String[] _daysOfWeek = {"Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi", "Dimanche"};

    /**
     * The constructor
     * @param name the name of the club
     * @param day the day of the week as an integer between 1 and 7
     * @param date the date of the next event if it is not weekly
     * @param time the time of the club
     * @param duration the duration of the club
     * @param place the place where it is
     */
    Club(String name, int day, Date date, Time time, Time duration, String place){
        super(name);
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
    private String getDayOfWeek(){
        return _daysOfWeek[_day-1];
    }

    /**
     *
     * @return a string that sum up the club information
     */
    public String toString(){
        return getDayOfWeek() + " " + _time.toString() + " " + _duration.toString() + " " + _place;
    }

}
