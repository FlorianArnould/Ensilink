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
 * @author Jérémy Filipozzi
 * @version 1.0
 */

/**
 * The class that stores all the useful informations regarding the mails
 */
public final class Mail {
	private final String _transmitter;
	private final String _subject;
	private final String _text;
	private final String _date;

	/**
	 * The constructor
	 * @param transmitter person who sent the email
	 * @param subject the subject of the mail
	 * @param text the text sent with the email
	 * @param date the date of the email reception
	 */
	public Mail(String transmitter, String subject, String text, String date) {
		_transmitter = transmitter;
		_subject = subject;
		_text = text;
		_date = date;
	}

	/**
	 * @return the name of the transmitter of the mail
	 */
	public String getTransmitter() {
		return _transmitter;
	}

	/**
	 * @return the subject of the mail which will be used to put them at the right place
	 */
	public String getSubject() {
		return _subject;
	}

	/**
	 * @return the text of the email which contains informations on an union or a club
	 */
	public String getText() {
		return _text;
	}

	/**
	 * @return the date of the email which will be used to select the recent ones
	 */
	public String getDate() {
		return _date;
	}

}
