package fr.ensicaen.lbssc.ensilink.loader.news;

/**
 * @author Florian Arnould
 * @version 1.0
 */

/**
 * Store information from the modifications of the day of a club
 */
public class DayNews implements News {

    private final int _newDayId;
    private final String _clubName;
    private String _newDay;

    public DayNews(String clubName, int newDayId){
        _newDayId = newDayId;
        _clubName = clubName;
    }

    /**
     * Set the name of the day associate to the day ID
     * @param days the array of the name of the days
     */
    public void setDaysArray(String[] days){
        _newDay = days[_newDayId-1];
    }

    @Override
    public String toNotificationString() {
        return "Le club "+ _clubName + " aura maintenant lieu le " + _newDay;
    }
}
