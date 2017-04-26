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

/**
 * @author Florian Arnould
 * @version 1.0
 */

/**
 * A representation of a date
 */
public final class Date{
    private final int _year;
    private final int _month;
    private final int _dayOfMonth;

    /**
     * The constructor
     * @param date a string to parse to get the date with this form : day-month-year. Example: 22-05-2016
     */
    public Date(String date){
        String[] strings = date.split(Character.toString('-'));
        _dayOfMonth = Integer.valueOf(strings[0]);
        _month = Integer.valueOf(strings[1]);
        _year = Integer.valueOf(strings[2]);
    }

    /**
     * @return the date with this form : day-month-year. Example: 22-05-2016
     */
    public String toString(){
        return String.valueOf(_dayOfMonth) + '-' + String.valueOf(_month) + '-' + String.valueOf(_year);
    }
}
