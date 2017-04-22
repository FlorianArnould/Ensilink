package fr.ensicaen.lbssc.ensilink.loader.news;

/**
 * @author Florian Arnould
 * @version 1.0
 */

/**
 * Store information from the modifications of the hour of a club
 */
public class HourNews extends News {

    private final String _newHour;

    public HourNews(String clubName, String newHour){
        super(clubName);
        _newHour = newHour;
    }

    @Override
    public String toNotificationString() {
        return "Le club " + getClubName() + "aura maintenant lieu à " + _newHour;
    }
}
