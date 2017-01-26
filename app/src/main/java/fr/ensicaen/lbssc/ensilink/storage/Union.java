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

    /**
     * The constructor
     * @param name the name of the union
     * @param logo the logo image
     * @param photo the photo image
     * @param color an integer representing the color of the union
     */
    Union(String name, Image logo, Image photo, int color){
        super(name, logo, photo);
        _clubs = new ArrayList<>();
        _color = color;
    }

    /**
     * Add the club to this union
     * @param newClub the club to add
     */
    void addClub(Club newClub){
        _clubs.add(newClub);
    }

    /**
     * @return the list of the clubs of the union
     */
    public List<Club> getClubs(){
        return _clubs;
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
}
