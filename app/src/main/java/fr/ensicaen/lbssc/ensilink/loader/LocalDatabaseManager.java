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
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author Florian Arnould
 * @version 1.0
 */

/**
 * The class needed to open the local database and which creates it if it doesn't already exist
 */
final class LocalDatabaseManager extends SQLiteOpenHelper {
	private static final int VERSION = 1;
	private static final String DATABASE_NAME = "ProjectsDatabase.db";
	private static final String[] _tables = {"colors", "images", "students", "unions", "clubs", "students_club", "students_union", "events"};

	/**
	 * @param context an application context needed for the parent class
	 */
	LocalDatabaseManager(Context context) {
		super(context, DATABASE_NAME, null, VERSION);

	}

	/**
	 * @return a array of string with the name of the tables
	 */
	static String[] getTables() {
		return _tables;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE colors(" +
				"id serial NOT NULL PRIMARY KEY," +
				"red INTEGER NOT NULL CHECK (red>=0 and red<256)," +
				"green INTEGER NOT NULL CHECK (green>=0 and green<256)," +
				"blue INTEGER NOT NULL CHECK (blue>=0 and blue<256));");

		db.execSQL("CREATE TABLE images(" +
				"id INTEGER NOT NULL PRIMARY KEY," +
				"name VARCHAR(100) NOT NULL," +
				"attribution VARCHAR(500));");

		db.execSQL("CREATE TABLE unions(" +
				"id INTEGER NOT NULL PRIMARY KEY," +
				"name VARCHAR(50)," +
				"idlogo INTEGER NOT NULL REFERENCES images (id)," +
				"idphoto INTEGER NOT NULL REFERENCES images (id)," +
				"idcolor INTEGER NOT NULL REFERENCES colors (id)," +
				"email VARCHAR(50)," +
				"tags VARCHAR(100)," +
				"facebook VARCHAR(100));");

		db.execSQL("CREATE TABLE students(" +
				"id INTEGER NOT NULL PRIMARY KEY," +
				"lastname VARCHAR(50) NOT NULL," +
				"name VARCHAR(50) NOT NULL," +
				"nickname VARCHAR(50)," +
				"email VARCHAR(100) NOT NULL," +
				"login VARCHAR(50)," +
				"password VARCHAR(100));");

		db.execSQL("CREATE TABLE clubs(" +
				"id INTEGER NOT NULL PRIMARY KEY," +
				"idunion INTEGER NOT NULL REFERENCES unions (id)," +
				"idlogo INTEGER NOT NULL REFERENCES images (id)," +
				"idphoto INTEGER NOT NULL REFERENCES images (id)," +
				"name VARCHAR(50) NOT NULL," +
				"day INTEGER NOT NULL CHECK (day>0 and day<8)," +
				"date DATE," +
				"start_hour TIME," +
				"duration TIME," +
				"place VARCHAR(100) NOT NULL);");

		db.execSQL("CREATE TABLE students_union(" +
				"idunion INTEGER NOT NULL REFERENCES unions (id)," +
				"idstudent INTEGER NOT NULL REFERENCES students (id)," +
				"position VARCHAR(100) NOT NULL," +
				"PRIMARY KEY (idunion, idstudent));");

		db.execSQL("CREATE TABLE students_club(" +
				"idclub INTEGER NOT NULL REFERENCES clubs (id)," +
				"idstudent INTEGER NOT NULL REFERENCES students (id)," +
				"position VARCHAR(100) NOT NULL," +
				"PRIMARY KEY (idclub, idstudent));");

		db.execSQL("CREATE TABLE events(" +
				"id INTEGER NOT NULL PRIMARY KEY," +
				"idunion INTEGER NOT NULL REFERENCES unions (id)," +
				"idimage INTEGER REFERENCES images (id)," +
				"title VARCHAR(100) NOT NULL," +
				"text VARCHAR(1000) NOT NULL);");

		db.execSQL("CREATE TABLE mails(" +
				"id INTEGER PRIMARY KEY AUTOINCREMENT," +
				"date DATETIME);");

		db.execSQL("CREATE TABLE club_mails(" +
				"idclub INTEGER NOT NULL REFERENCES clubs (id)," +
				"idmail INTEGER NOT NULL REFERENCES mails (id)," +
				"PRIMARY KEY(idclub, idmail));");

		db.execSQL("CREATE TABLE union_mails(" +
				"idunion INTEGER NOT NULL REFERENCES unions (id)," +
				"idmail INTEGER NOT NULL REFERENCES mails (id)," +
				"PRIMARY KEY(idunion, idmail));");
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

	}

	// TODO: 02/09/17 Move the clearDatabase from DatabaseCloner to here
}
