/**
 * This file is part of Ensilink.
 *
 * Ensilink is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * Ensilink is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Ensilink.
 * If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright, The Ensilink team :  ARNOULD Florian, ARIK Marsel, FILIPOZZI Jérémy,
 * ENSICAEN, 6 Boulevard du Maréchal Juin, 26 avril 2017
 *
 */

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
