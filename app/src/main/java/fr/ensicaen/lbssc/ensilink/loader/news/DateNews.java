package fr.ensicaen.lbssc.ensilink.loader.news;

import fr.ensicaen.lbssc.ensilink.storage.Date;

/**
 * @author Florian Arnould
 * @version 1.0
 */

/**
 * Store information from the modifications of the date of a club
 */
public class DateNews extends News {

    private final Date _newDate;

    public DateNews(int unionId, String clubName, Date newDate){
        super(unionId, clubName);
        _newDate = newDate;
    }

    @Override
    public String toNotificationString() {
        return "La prochaine réunion du club " + getClubName() + "aura lieu le " + _newDate.toString();
    }
}
