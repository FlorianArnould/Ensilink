package fr.ensicaen.lbssc.ensilink.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

final class LocalDatabaseManager extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "ProjectsDatabase.db";
    private static final String[] _tables = {"students", "unions", "clubs", "students_club", "students_union"};

    LocalDatabaseManager(Context context){
        super(context, DATABASE_NAME, null, VERSION);

    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE unions(" +
                "id INTEGER NOT NULL PRIMARY KEY," +
                "name VARCHAR(50));");
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
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    static String[] getTables(){
        return _tables;
    }
}
