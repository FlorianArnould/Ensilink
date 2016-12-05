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
    private List<Club> _clubs;

    /**
     * The constructor
     * @param name the name of the union
     */
    Union(String name){
        super(name);
        _clubs = new ArrayList<>();
    }

    void addClub(Club newClub){
        _clubs.add(newClub);
    }

    public List<Club> getClubs(){
        return _clubs;
    }
}
