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
package fr.ensicaen.lbssc.ensilink.loader;

import android.content.Context;
import android.util.Log;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Florian Arnould
 * @version 1.0
 */

/**
 * Class which can download files in local directory as images
 */
final class FileDownloader {
	private final Context _context;

	/**
	 * @param context an activity context needed to open the local file directory
	 */
	FileDownloader(Context context) {
		_context = context;
	}

	/**
	 * Download one image from the network
	 *
	 * @param imageName the image name
	 */
	void download(String imageName) throws IOException {
		try {
			URL url = new URL("http://www.ecole.ensicaen.fr/~arnould/others/test/images/" + imageName);
			InputStream in = url.openStream();
			DataInputStream dis = new DataInputStream(in);
			byte[] buffer = new byte[1024];
			int length;

			FileOutputStream fos = new FileOutputStream(new File(_context.getFilesDir(), imageName + ".new"));
			while ((length = dis.read(buffer)) > 0) {
				fos.write(buffer, 0, length);
			}
			move(imageName + ".new", imageName);
		} catch (MalformedURLException e) {
			Log.w("download", "malformed url error : " + e.getMessage(), e);
		}
	}

	/**
	 * Rename the original file to the target
	 *
	 * @param original the name of the existing file
	 * @param target   the new name of the file
	 */
	void move(String original, String target) {
		File originalFile = new File(_context.getFilesDir(), original);
		try {
			InputStream in = new FileInputStream(originalFile);
			OutputStream out = new FileOutputStream(new File(_context.getFilesDir(), target));
			byte[] buffer = new byte[1024];
			int read;
			while ((read = in.read(buffer)) != -1) {
				out.write(buffer, 0, read);
			}
			in.close();
			out.flush();
			out.close();
			if (!originalFile.delete()) {
				Log.d("D", "File " + original + " was not removed correctly");
			}
		} catch (FileNotFoundException e) {
			Log.w("move", "File not found when moving image " + target + " : " + e.getMessage(), e);
		} catch (IOException e) {
			Log.w("move", "Read or write error when moving image " + target + " : " + e.getMessage(), e);
		}
	}
}
