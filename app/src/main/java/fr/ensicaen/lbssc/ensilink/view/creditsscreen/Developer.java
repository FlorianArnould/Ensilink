package fr.ensicaen.lbssc.ensilink.view.creditsscreen;

/**
 * @author Florian Arnould
 * @version 1.0
 */

/**
 * Used to store information about the developers for the credits
 */
final class Developer {

    private final int _drawableId;
    private final String _position;
    private final String _name;

    /**
     * @param drawableId resource id of the drawable
     * @param position position of the developer
     * @param name name of the developer
     */
    Developer(int drawableId, String position, String name){
        _drawableId = drawableId;
        _position = position;
        _name = name;
    }

    /**
     * @return the resource id of the position's icon
     */
    int getDrawableId(){
        return _drawableId;
    }

    /**
     * @return the position of the developer
     */
    String getPosition(){
        return _position;
    }

    /**
     * @return the name of the developer
     */
    String getName(){
        return _name;
    }
}
