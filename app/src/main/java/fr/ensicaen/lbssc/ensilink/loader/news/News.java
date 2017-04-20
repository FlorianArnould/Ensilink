package fr.ensicaen.lbssc.ensilink.loader.news;

/**
 * @author Florian Arnould
 * @version 1.0
 */

/**
 * Interface of a News that represent a modification
 */
public interface News {
    /**
     * Create the string to show in the notification
     * @return the string
     */
    String toNotificationString();
}
