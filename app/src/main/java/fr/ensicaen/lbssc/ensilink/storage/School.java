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
import android.content.SharedPreferences;

import java.util.List;

import fr.ensicaen.lbssc.ensilink.R;
import fr.ensicaen.lbssc.ensilink.loader.DataLoader;
import fr.ensicaen.lbssc.ensilink.loader.OnLoadingFinishListener;

/**
 * @author Florian Arnould
 * @version 1.0
 */

/**
 * A singleton to store the information of associations
 */
public final class School {
	private static final School _ourInstance = new School();
	private static List<Union> _unions;
	private static List<Event> _events;
	private static List<Image> _images;
	private static boolean _neverUpdated;
	private static boolean _isConnected;
	private DataLoader _loader;

	/**
	 * The private constructor
	 */
	private School() {
		_neverUpdated = true;
		_loader = null;
		_isConnected = false;
	}

	/**
	 * @return the school instance
	 */
	public static School getInstance() {
		return _ourInstance;
	}

	/**
	 * Updates the information of the unions
	 *
	 * @param context  an application context
	 * @param listener a listener to get when the school will be updated
	 */
	public void refreshData(Context context, final OnSchoolDataListener listener) {
		_loader = new DataLoader(context, _neverUpdated);
		_loader.setOnLoadingFinishListener(new OnLoadingFinishListener() {
			@Override
			public void OnLoadingFinish(DataLoader loader) {
				_unions = loader.getUnions();
				_events = loader.getEvents();
				_images = loader.getImages();
				if (listener != null) {
					listener.OnDataRefreshed();
				}
				_neverUpdated = false;
			}
		});
		_loader.start();
		SharedPreferences pref = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
		_isConnected = !pref.getString("email", "").isEmpty() && !pref.getString("password", "").isEmpty();
	}

	/**
	 * @param i the union index
	 * @return the corresponding union
	 */
	public Union getUnion(int i) {
		return _unions.get(i);
	}

	/**
	 * @return a List with all unions
	 */
	public List<Union> getUnions() {
		return _unions;
	}

	/**
	 * @return a List with all events
	 */
	public List<Event> getEvents() {
		return _events;
	}

	/**
	 * @param i the union index
	 * @return the corresponding event
	 */
	public Event getEvent(int i) {
		return _events.get(i);
	}

	/**
	 * @return a List with all images of the application
	 */
	public List<Image> getImages() {
		return _images;
	}

	/**
	 * @return the progress of the current update
	 */
	public int getProgress() {
		if (_loader != null) {
			return _loader.getProgress();
		}
		return 0;
	}

	/**
	 * @return the max value of the progress of the current update
	 */
	public int getMaxProgress() {
		if (_loader != null) {
			return _loader.getMaxProgress();
		}
		return 0;
	}

	/**
	 * @return if the user is connected with an Ensicaen email account
	 */
	public boolean isConnected() {
		return _isConnected;
	}

	/**
	 * logout from the Ensicaen email account
	 */
	public void logout(Context context) {
		SharedPreferences pref = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
		pref.edit().remove("email").remove("password").apply();
		_isConnected = false;
	}

	/**
	 * Set the user state as connected to the zimbra server of the school
	 */
	public void setConnected() {
		_isConnected = true;
	}
}
