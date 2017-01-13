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
    private int _color;

    /**
     * The constructor
     * @param name the name of the union
     * @param logoFile a file with the path to the logo image
     * @param photoFile a file with the path to the photo image
     * @param color an integer representing the color of the union
     */
    Union(String name, File logoFile, File photoFile, int color){
        super(name, logoFile, photoFile);
        _clubs = new ArrayList<>();
        _color = color;
    }

    void addClub(Club newClub){
        _clubs.add(newClub);
    }

    public List<Club> getClubs(){
        return _clubs;
    }

    public Club getClub(int i){
        return _clubs.get(i);
    }

    public int getColor(){
        return _color;
    }
}
