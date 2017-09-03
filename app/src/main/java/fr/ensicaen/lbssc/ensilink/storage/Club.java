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
package fr.ensicaen.lbssc.ensilink.storage;

import android.content.Context;

import fr.ensicaen.lbssc.ensilink.R;

/**
 * @author Florian Arnould
 * @version 1.0
 */

/**
 * The class that represent a club of the school
 */
public final class Club extends Association {
	private final int _day;
	private final Date _date;
	private final Time _time;
	private final Time _duration;
	private final String _place;

	/**
	 * The constructor
	 *
	 * @param id       the id of the club
	 * @param name     the name of the club
	 * @param day      the day of the week as an integer between 1 and 7
	 * @param date     the date of the next event if it is not weekly
	 * @param time     the time of the club
	 * @param duration the duration of the club
	 * @param place    the place where it is
	 * @param logo     the logo image
	 * @param photo    the photo image
	 */
	public Club(int id, String name, int day, Date date, Time time, Time duration, String place, Image logo, Image photo) {
		super(id, name, logo, photo);
		_day = day;
		_date = date;
		_time = time;
		_duration = duration;
		_place = place;
	}

	/**
	 * @return a string with the day of the week of the meeting
	 */
	public String getDayOfWeek(Context context) {
		return context.getResources().getStringArray(R.array.days)[_day - 1];
	}

	/**
	 * @return the name of the place
	 */
	public String getPlace() {
		return _place;
	}

	/**
	 * @return the schedule time of the club
	 */
	public Time getTime() {
		return _time;
	}

	/**
	 * @return the duration of the meeting of the club
	 */
	public Time getDuration() {
		return _duration;
	}

	/**
	 * @return return the date of the meeting of the club if it hasn't weekly event
	 */
	public Date getDate() {
		return _date;
	}
}
