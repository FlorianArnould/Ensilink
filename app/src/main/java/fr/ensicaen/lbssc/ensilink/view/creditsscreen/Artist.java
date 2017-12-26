/**
 * This file is part of Ensilink.
 * <p>
 * Ensilink is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 * <p>
 * Ensilink is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public
 * License along with Ensilink.
 * If not, see <http://www.gnu.org/licenses/>.
 * <p>
 * Copyright, The Ensilink team :  ARNOULD Florian, ARIK Marsel, FILIPOZZI Jérémy,
 * ENSICAEN, 6 Boulevard du Maréchal Juin, 26 avril 2017
 */
package fr.ensicaen.lbssc.ensilink.view.creditsscreen;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import fr.ensicaen.lbssc.ensilink.storage.Image;

/**
 * @author Florian Arnould
 * @version 1.0
 */

/**
 * Used to store information about the images creators for credits activity
 */
final class Artist {
	private final Drawable _drawable;
	private final String _attribution;

	/**
	 * @param drawableId  resource id of the drawable
	 * @param attribution attribution text for the creative commons license
	 */
	Artist(Drawable drawableId, String attribution) {
		_drawable = drawableId;
		_attribution = attribution;
	}

	/**
	 * @param image which needs an attribution
	 */
	Artist(Image image) {
		_drawable = BitmapDrawable.createFromPath(image.getAbsolutePath());
		_attribution = image.getAttribution();
	}

	/**
	 * @return a resource id to the drawable that the artist created
	 */
	Drawable getDrawable() {
		return _drawable;
	}

	/**
	 * @return a resource id to the string of the attribution
	 */
	String getAttribution() {
		return _attribution;
	}
}
