package fr.ensicaen.lbssc.ensilink.creditsscreen;

/**
 * @author Florian Arnould
 * @version 1.0
 */

/**
 * Used to store information for credits activity
 */
final class Artist {

    private final int _drawableId;
    private final int _attributionId;

    /**
     *
     * @param drawableId resource id of the drawable
     * @param attribution attribution text for the creative commons license
     */
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
