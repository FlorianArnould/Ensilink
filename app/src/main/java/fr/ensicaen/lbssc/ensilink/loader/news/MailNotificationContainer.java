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

import fr.ensicaen.lbssc.ensilink.storage.Club;
import fr.ensicaen.lbssc.ensilink.storage.Union;

/**
 * @author Florian Arnould
 * @version 1.0
 */

/**
 * Store informations to create a mail notifications
 */
public class MailNotificationContainer {
	private final String _subject;
	private final String _text;
	private final Union _union;
	private final Club _club;

	/**
	 * @param subject the subject of the mail
	 * @param text    the content of the mail
	 * @param union   the union of the club
	 * @param club    the club of the mail
	 */
	public MailNotificationContainer(String subject, String text, Union union, Club club) {
		_subject = subject;
		_text = text;
		_union = union;
		_club = club;
	}

	/**
	 * @param subject the subject of the mail
	 * @param text    the content of the mail
	 * @param union   the union of the mail
	 */
	public MailNotificationContainer(String subject, String text, Union union) {
		_subject = subject;
		_text = text;
		_union = union;
		_club = null;
	}

	public String getSubject() {
		return _subject;
	}

	public String getText() {
		return _text;
	}

	public Union getUnion() {
		return _union;
	}

	public Club getClub() {
		return _club;
	}

	/**
	 * @return true if this is a mail from a club
	 */
	public boolean isForAClub() {
		return _club != null;
	}
}
