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
 * Store information from the modifications of the day of a club
 */
public class DayNews extends News {
	private final int _newDayId;
	private String _newDay;

	public DayNews(int unionId, int clubId, String clubName, int newDayId) {
		super(unionId, clubId, clubName);
		_newDayId = newDayId;
	}

	/**
	 * Set the name of the day associate to the day ID
	 *
	 * @param days the array of the name of the days
	 */
	public void setDaysArray(String[] days) {
		_newDay = days[_newDayId - 1];
	}

	@Override
	public String toNotificationString() {
		return "Le club " + getClubName() + " aura maintenant lieu le " + _newDay;
	}
}
