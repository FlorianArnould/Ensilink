package fr.ensicaen.lbssc.ensilink.storage;

import java.io.File;
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
     * @param logoFile a file with the path to the logo image
     * @param photoFile a file with the path to the photo image
     */
    Union(String name, File logoFile, File photoFile){
        super(name, logoFile, photoFile);
        _clubs = new ArrayList<>();
    }

    void addClub(Club newClub){
        _clubs.add(newClub);
    }

    public List<Club> getClubs(){
        return _clubs;
    }
}
