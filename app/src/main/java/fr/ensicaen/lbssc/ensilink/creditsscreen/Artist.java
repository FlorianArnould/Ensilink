package fr.ensicaen.lbssc.ensilink.creditsscreen;

/**
 * @author Florian Arnould
 * @version 1.0
 */

class Artist {

    private final int _drawableId;
    private final int _attributionId;

    Artist(int drawableId, int attribution){
        _drawableId = drawableId;
        _attributionId = attribution;
    }

    int getDrawableId(){
        return _drawableId;
    }

    int getAttribution(){
        return _attributionId;
    }
}
