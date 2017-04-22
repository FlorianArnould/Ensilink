package fr.ensicaen.lbssc.ensilink.loader.news;

/**
 * @author Florian Arnould
 * @version 1.0
 */

/**
 * Interface of a News that represent a modification
 */
public abstract class News {

    private final String _clubName;

    News(String clubName) {
        _clubName = clubName;
    }

    /**
     * Create the string to show in the notification
     * @return the string
     */
    public abstract String toNotificationString();

    /**
     * @return the name of the club which was updated
     */
    public String getClubName() {
        return _clubName;
    }
}
