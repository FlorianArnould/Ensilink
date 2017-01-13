package fr.ensicaen.lbssc.ensilink.creditsscreen;

/**
 * @author Florian Arnould
 * @version 1.0
 */

/**
 * Used to store information for credits activity
 */
final class Developer {

    private final int _drawableId;
    private final String _position;
    private final String _name;

    /**
     *
     * @param drawableId resource id of the drawable
     * @param position position of the developer
     * @param name name of the developer
     */
    Developer(int drawableId, String position, String name){
        _drawableId = drawableId;
        _position = position;
        _name = name;
    }

    int getDrawableId(){
        return _drawableId;
    }

    String getPosition(){
        return _position;
    }

    String getName(){
        return _name;
    }
}
