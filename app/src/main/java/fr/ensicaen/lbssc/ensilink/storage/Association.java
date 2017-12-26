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

import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Florian Arnould
 * @version 1.0
 */

/**
 * Abstract class that represent a student association
 */
public abstract class Association {
	private final int _id;
	private final String _name;
	private final Image _logo;
	private final Image _photo;
	private final List<Student> _students;
	private final List<Mail> _mails;

	/**
	 * The constructor
	 *
	 * @param name  the name of the association
	 * @param logo  the logo image
	 * @param photo the photo image
	 */
	Association(int id, String name, Image logo, Image photo) {
		_id = id;
		_name = name;
		_students = new ArrayList<>();
		_logo = logo;
		_photo = photo;
		_mails = new ArrayList<>();
	}

	/**
	 * @return the id of the association
	 */
	public int getId() {
		return _id;
	}

	/**
	 * Adds a student to the association
	 *
	 * @param student a student instance
	 */
	public void addStudent(Student student) {
		_students.add(student);
	}

	/**
	 * Adds a mail to the association
	 *
	 * @param mail a mail instance
	 */
	public void addMail(Mail mail) {
		_mails.add(mail);
	}

	/**
	 * @return the List of the mails
	 */
	public List<Mail> getMails() {
		return _mails;
	}

	/**
	 * @param position of the mail in the list
	 * @return the mail instance
	 */
	public Mail getMail(int position) {
		return _mails.get(position);
	}

	/**
	 * @return the name of the association
	 */
	public String getName() {
		return _name;
	}

	/**
	 * @return the List of the students
	 */
	public List<Student> getStudents() {
		return _students;
	}

	/**
	 * load the logo in a thread and after call the listener
	 *
	 * @param listener listener called when the image will be loaded
	 */
	public void loadLogo(final ImageView imageView, final OnImageLoadedListener listener) {
		imageView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
			@Override
			public boolean onPreDraw() {
				imageView.getViewTreeObserver().removeOnPreDrawListener(this);
				ImageLoadThread thread = new ImageLoadThread(_logo.getAbsolutePath(), imageView.getMeasuredWidth(), imageView.getMeasuredHeight(), listener);
				thread.start();
				return true;
			}
		});
	}

	/**
	 * Load the logo here and return it
	 *
	 * @return a drawable of the logo of the club
	 */
	public Drawable getLogo() {
		return Drawable.createFromPath(_logo.getAbsolutePath());
	}

	/**
	 * Load the photo in a thread and after call the listener
	 *
	 * @param listener listener called when the image will be loaded
	 */
	public void loadPhoto(final ImageView imageView, final OnImageLoadedListener listener) {
		imageView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
			@Override
			public boolean onPreDraw() {
				imageView.getViewTreeObserver().removeOnPreDrawListener(this);
				ImageLoadThread thread = new ImageLoadThread(_photo.getAbsolutePath(), imageView.getMeasuredWidth(), imageView.getMeasuredHeight(), listener);
				thread.start();
				return true;
			}
		});
	}

	/**
	 * Thread used to avoid UI lags when we loads the images
	 */
	private class ImageLoadThread extends Thread {
		private final String _path;
		private final OnImageLoadedListener _listener;
		private final int _width;
		private final int _height;

		/**
		 * The constructor
		 *
		 * @param path     to the local file
		 * @param listener the listener to call when the image will be loaded
		 */
		ImageLoadThread(String path, int width, int height, OnImageLoadedListener listener) {
			_path = path;
			_listener = listener;
			_width = width;
			_height = height;
		}

		/**
		 * Load the image and send it to the listener
		 */
		@Override
		public void run() {
			final BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(_path, options);
			options.inJustDecodeBounds = false;
			options.inSampleSize = calculateInSampleSize(options, _width, _height);
			_listener.onImageLoaded(BitmapFactory.decodeFile(_path, options));
		}

		/**
		 * Calculate the sample size for the Bitmap
		 *
		 * @param options   the options for the loading
		 * @param reqWidth  the width to load
		 * @param reqHeight the height to load
		 * @return inSampleSize for the options
		 */
		private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
			// Raw height and width of image
			final int height = options.outHeight;
			final int width = options.outWidth;
			int inSampleSize = 1;

			if (height > reqHeight || width > reqWidth) {

				final int halfHeight = height / 2;
				final int halfWidth = width / 2;

				// Calculate the largest inSampleSize value that is a power of 2 and keeps both
				// height and width larger than the requested height and width.
				while ((halfHeight / inSampleSize) >= reqHeight
						&& (halfWidth / inSampleSize) >= reqWidth) {
					inSampleSize *= 2;
				}
			}

			return inSampleSize;
		}
	}
}
