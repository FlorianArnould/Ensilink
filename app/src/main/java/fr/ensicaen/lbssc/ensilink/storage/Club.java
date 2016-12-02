package fr.ensicaen.lbssc.ensilink.storage;


//TODO add the others fields of a club

public class Club extends Association{
    private int _day;
    private Date _date;
    private Time _time;
    private Time _duration;
    private String _place;
    private static String[] _daysOfWeek = {"Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi", "Dimanche"};

    Club(String name, int day, Date date, Time time, Time duration, String place){
        super(name);
        _day = day;
        _date = date;
        _time = time;
        _duration = duration;
        _place = place;
    }
    private String getDayOfWeek(){
        return _daysOfWeek[_day-1];
    }

    public String toString(){
        return getDayOfWeek() + " " + _time.toString() + " " + _duration.toString() + " " + _place;
    }

}
