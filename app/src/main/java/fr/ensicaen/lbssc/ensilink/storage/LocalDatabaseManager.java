package fr.ensicaen.lbssc.ensilink.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

final class LocalDatabaseManager extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "ProjectsDatabase.db";
    private static final String[] _tables = {"etudiants", "bureaux", "clubs", "etudiants_club", "etudiants_bureau"};

    LocalDatabaseManager(Context context){
        super(context, DATABASE_NAME, null, VERSION);

    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE bureaux(" +
                "id INTEGER NOT NULL PRIMARY KEY," +
                "nom VARCHAR(50));");
        db.execSQL("CREATE TABLE etudiants(" +
                "id INTEGER NOT NULL PRIMARY KEY," +
                "nom VARCHAR(50) NOT NULL," +
                "prenom VARCHAR(50) NOT NULL," +
                "surnom VARCHAR(50)," +
                "email VARCHAR(100) NOT NULL);");

        db.execSQL("CREATE TABLE clubs(" +
                "id INTEGER NOT NULL PRIMARY KEY," +
                "idbureau INTEGER NOT NULL REFERENCES bureaux (id)," +
                "nom VARCHAR(50) NOT NULL," +
                "jour INTEGER NOT NULL CHECK (jour>0 and jour<8)," +
                "date DATE," +
                "heure_debut TIME," +
                "duree TIME," +
                "lieu VARCHAR(100) NOT NULL);");

        db.execSQL("CREATE TABLE etudiants_bureau(" +
                "idbureau INTEGER NOT NULL REFERENCES bureaux (id)," +
                "idetudiant INTEGER NOT NULL REFERENCES etudiants (id)," +
                "poste VARCHAR(100) NOT NULL," +
                "PRIMARY KEY (idbureau, idetudiant));");

        db.execSQL("CREATE TABLE etudiants_club(" +
                "idclub INTEGER NOT NULL REFERENCES clubs (id)," +
                "idetudiant INTEGER NOT NULL REFERENCES etudiants (id)," +
                "poste VARCHAR(100) NOT NULL," +
                "PRIMARY KEY (idclub, idetudiant));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    static String[] getTables(){
        return _tables;
    }
}
