package fr.ensicaen.lbssc.ensilink.storage;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Florian Arnould
 * @version 1.0
 */

/**
 * A representation of a union of the school
 */
public final class Union extends Association{

    private final List<Club> _clubs;
    private final int _color;
    private final String _facebookUrl;
    private final String _email;
    private final List<String> _tags;

    /**
     * The constructor
     * @param name the name of the union
     * @param logo the logo image
     * @param photo the photo image
     * @param color an integer representing the color of the union
     * @param facebookUrl the link to the facebook page of the union
     * @param email the email address of the club
     */
    public Union(String name, Image logo, Image photo, int color, String facebookUrl, String email) {
        super(name, logo, photo);
        _clubs = new ArrayList<>();
        _tags = new ArrayList<>();
        _color = color;
        _facebookUrl = facebookUrl;
        _email = email;
    }

    /**
     * Add the club to this union
     * @param newClub the club to add
     */
    public void addClub(Club newClub){
        _clubs.add(newClub);
    }

    /**
     * Add the tag to this union to parse the emails
     * @param tag the tag to add
     */
    public void addTag(String tag) {
        _tags.add(tag);
    }

    /**
     * @return the list of the clubs of the union
     */
    public List<Club> getClubs(){
        return _clubs;
    }

    /**
     * @return the list of the tags of the union to parse the emails
     */
    public List<String> getTags() {
        return _tags;
    }

    /**
     * @param i the club index
     * @return the corresponding club
     */
    public Club getClub(int i){
        return _clubs.get(i);
    }

    /**
     * @return the color of the union
     */
    public int getColor(){
        return _color;
    }

    /**
     * @return the url of the facebook page of the union
     */
    public String getFacebookUrl() {
        return _facebookUrl;
    }

    /**
     * @return the email address of the union
     */
    public String getEmail() {
        return _email;
    }
}
