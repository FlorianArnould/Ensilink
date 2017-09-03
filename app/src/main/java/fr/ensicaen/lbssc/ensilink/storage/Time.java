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

/**
 * @author Florian Arnould
 * @version 1.0
 */

/**
 * A representation of a time
 */
public final class Time {
	private final int _hours;
	private final int _minutes;
	private final int _seconds;

	/**
	 * The constructor
	 *
	 * @param time a string to parse to get the time with this form : hour:minutes. Example: 03:15
	 */
	public Time(String time) {
		String[] strings = time.split(Character.toString(':'));
		_hours = Integer.valueOf(strings[0]);
		_minutes = Integer.valueOf(strings[1]);
		_seconds = Integer.valueOf(strings[2]);
	}

	private Time(int hour, int minutes, int seconds) {
		_hours = hour;
		_minutes = minutes;
		_seconds = seconds;
	}

	/**
	 * @return the time with this form : hour:minutes. Example: 03:15
	 */
	public String toString() {
		String str = "";
		if (_hours < 10) {
			str += "0";
		}
		str += _hours + ":";
		if (_minutes < 10) {
			str += "0";
		}
		return str + _minutes;
	}

	/**
	 * @param time to add
	 * @return the result of the sum
	 */
	public Time add(Time time) {
		int seconds = (time._seconds + _seconds);
		int minutes = (time._minutes + _minutes + seconds / 60);
		seconds %= 60;
		int hours = time._hours + _hours + minutes / 60;
		minutes %= 60;
		return new Time(hours, minutes, seconds);
	}
}
