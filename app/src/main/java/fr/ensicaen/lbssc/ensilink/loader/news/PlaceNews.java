package fr.ensicaen.lbssc.ensilink.loader.news;

/**
 * @author Florian Arnould
 * @version 1.0
 */

/**
 * Store information from the modifications of the place of a club
 */
public class PlaceNews extends News {

    private final String _newPlace;

    public PlaceNews(String clubName, String newPlace){
        super(clubName);
        _newPlace = newPlace;
    }

    @Override
    public String toNotificationString() {
        return "Nouveau lieu pour le club " + getClubName() + " : " + _newPlace;
    }
}
