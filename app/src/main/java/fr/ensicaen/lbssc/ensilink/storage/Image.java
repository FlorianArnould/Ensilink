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

package fr.ensicaen.lbssc.ensilink.storage;

import java.io.File;

/**
 * @author Florian Arnould
 * @version 1.0
 */

/**
 * Represent an image with its possible attribution
 */
public class Image {

    private final String _attribution;
    private final File _file;

    /**
     * The constructor
     * @param file with the path to the image
     * @param attribution the possible attribution of this image
     */
    public Image(File file, String attribution){
        _file = file;
        _attribution = attribution;
    }

    /**
     * @return the path of the image file
     */
    public String getAbsolutePath(){
        return _file.getAbsolutePath();
    }

    /**
     * @return true if this is a creative commons image that needs an attribution
     */
    public boolean needsAttribution(){
        return !_attribution.isEmpty();
    }

    /**
     * @return a string with the attribution for this image
     */
    public String getAttribution(){
        return _attribution;
    }
}
