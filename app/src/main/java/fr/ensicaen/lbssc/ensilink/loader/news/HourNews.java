package fr.ensicaen.lbssc.ensilink.loader.news;

/**
 * @author Florian Arnould
 * @version 1.0
 */

/**
 * Store information from the modifications of the hour of a club
 */
public class HourNews implements News {

    private final String _clubName;
    private final String _newHour;

    public HourNews(String clubName, String newHour){
        _clubName = clubName;
        _newHour = newHour;
    }

    @Override
    public String toNotificationString() {
        return "Le club " + _clubName + "aura maintenant lieu à " + _newHour;
    }
}
