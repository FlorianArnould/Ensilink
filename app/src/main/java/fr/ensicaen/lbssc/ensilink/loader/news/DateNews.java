package fr.ensicaen.lbssc.ensilink.loader.news;

import fr.ensicaen.lbssc.ensilink.storage.Date;

/**
 * @author Florian Arnould
 * @version 1.0
 */

/**
 * Store information from the modifications of the date of a club
 */
public class DateNews implements News {

    private final String _clubName;
    private final Date _newDate;

    public DateNews(String clubName, Date newDate){
        _clubName = clubName;
        _newDate = newDate;
    }

    @Override
    public String toNotificationString() {
        return "La prochaine réunion du club " + _clubName + "aura lieu le " + _newDate.toString();
    }
}
