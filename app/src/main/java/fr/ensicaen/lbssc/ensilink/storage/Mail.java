package fr.ensicaen.lbssc.ensilink.storage;

/**
 * @author Jérémy Filipozzi
 * @version 1.0
 */

/**
 * The class that stores all the useful informations regarding the mails
 */
public class Mail {

    private final String _transmitter;
    private final String _subject;
    private final String _text;
    private final String _date;

    /**
     * The constructor
     * @param transmitter person who sent the email
     * @param subject the subject of the mail
     * @param text the text sent with the email
     * @param date the date of the email reception
     */
    public Mail(String transmitter, String subject, String text, String date) {
        _transmitter = transmitter;
        _subject = subject;
        _text = text;
        _date = date;
    }

    /**
     * @return the name of the transmitter of the mail
     */
    public String get_transmitter() { return _transmitter; }

    /**
     * @return the subject of the mail which will be used to put them at the right place
     */
    public String get_subject() { return _subject; }

    /**
     * @return the text of the email which contains informations on an union or a club
     */
    public String get_text() { return _text; }

    /**
     * @return the date of the email which will be used to select the recent ones
     */
    public String get_date() { return _date; }

}
