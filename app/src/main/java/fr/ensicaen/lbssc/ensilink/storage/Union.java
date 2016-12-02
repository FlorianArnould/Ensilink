package fr.ensicaen.lbssc.ensilink.storage;

import java.util.ArrayList;
import java.util.List;

public final class Union extends Association{
    private List<Club> _clubs;

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
