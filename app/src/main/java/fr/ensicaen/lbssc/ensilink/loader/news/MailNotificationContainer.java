package fr.ensicaen.lbssc.ensilink.loader.news;

import fr.ensicaen.lbssc.ensilink.storage.Club;
import fr.ensicaen.lbssc.ensilink.storage.Union;

/**
 * @author Florian Arnould
 * @version 1.0
 */

public class MailNotificationContainer {

    private final String _subject;
    private final String _text;
    private final Union _union;
    private final Club _club;

    public MailNotificationContainer(String subject, String text, Union union, Club club){
        _subject = subject;
        _text = text;
        _union = union;
        _club = club;
    }

    public MailNotificationContainer(String subject, String text, Union union){
        _subject = subject;
        _text = text;
        _union = union;
        _club = null;
    }

    public String getSubject(){
        return _subject;
    }

    public String getText(){
        return _text;
    }

    public Union getUnion(){
        return _union;
    }

    public Club getClub(){
        return _club;
    }

    public boolean isForAClub(){
        return _club != null;
    }
}
