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
package fr.ensicaen.lbssc.ensilink.utils;

import android.graphics.Color;

/**
 * @author Florian Arnould
 * @version 1.0
 */

/**
 * Class to modify colors
 */
public final class ColorCreator {
	private ColorCreator() {
		throw new UnsupportedOperationException("Cannot create an instance of ColorCreator");
	}

	/**
	 * @param color the color to modify
	 * @return a 15% darker color
	 */
	public static int darkerColor(int color) {
		float HSVColor[] = new float[3];
		Color.colorToHSV(color, HSVColor);
		HSVColor[2] *= 0.85;
		return Color.HSVToColor(HSVColor);
	}
}
