package fr.ensicaen.lbssc.ensilink.loader.news;

/**
 * @author Florian Arnould
 * @version 1.0
 */

/**
 * Store information from the modifications of the place of a club
 */
public class PlaceNews implements News {

    private final String _newPlace;
    private final String _clubName;

    public PlaceNews(String clubName, String newPlace){
        _newPlace = newPlace;
        _clubName = clubName;
    }

    @Override
    public String toNotificationString() {
        return "Nouveau lieu pour le club " + _clubName + " : " + _newPlace;
    }
}
