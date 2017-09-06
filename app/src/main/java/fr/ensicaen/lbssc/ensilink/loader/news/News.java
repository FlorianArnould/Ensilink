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
package fr.ensicaen.lbssc.ensilink.loader.news;

/**
 * @author Florian Arnould
 * @version 1.0
 */

/**
 * Interface of a News that represent a modification
 */
public abstract class News {
	private final String _clubName;
	private final int _unionIndex;
	private final int _clubIndex;

	News(int unionId, int clubId, String clubName) {
		_clubName = clubName;
		_unionIndex = unionId - 1;
		_clubIndex = clubId - 1;
	}

	/**
	 * Create the string to show in the notification
	 *
	 * @return the string
	 */
	public abstract String toNotificationString();

	/**
	 * @return the name of the club which was updated
	 */
	public String getClubName() {
		return _clubName;
	}

	/**
	 * @return the index of the union of the club
	 */
	public int getUnionIndex() {
		return _unionIndex;
	}

	/**
	 * @return the index of the club
	 */
	public int getClubIndex() {
		return _clubIndex;
	}
}
