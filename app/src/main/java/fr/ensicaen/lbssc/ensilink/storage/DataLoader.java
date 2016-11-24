package fr.ensicaen.lbssc.ensilink.storage;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

final class DataLoader {

    private DatabaseCloner _cloner;
    private SQLiteDatabase _db;
    private LocalDatabaseManager _databaseManager;

    DataLoader(Context context){
        _cloner = new DatabaseCloner(context);
        _databaseManager = new LocalDatabaseManager(context);
        _db = null;
    }

    List<Union> getUnions(){
        boolean cloned = cloneDatabase();
        if(openDatabase() && (cloned || !isDatabaseEmpty())){
            return loadUnionsFromDatabase();
        }
        return null;
    }

    private boolean openDatabase(){
        try{
            _db = _databaseManager.getWritableDatabase();
        }catch (SQLiteException e){
            Log.d("D", "Error when tried to open SQLite database : " + e.getMessage());
            return false;
        }
        return true;
    }

    private boolean cloneDatabase(){
        _cloner.run();
        try{
            _cloner.join();
            return _cloner.succeed();
        } catch(InterruptedException e){
            Log.d("D", "Error with the cloner thread : " + e.getMessage());
        }
        return false;
    }

    private boolean isDatabaseEmpty(){
        String[] columns = {"nom"};
        Cursor result = _db.query("bureaux", columns, null, null, null, null, null);
        boolean test = result.moveToFirst();
        result.close();
        return test;
    }

    private List<Union> loadUnionsFromDatabase(){
        List<Union> unions = new ArrayList<>();
        Cursor unionCursor = _db.query("bureaux", null, null, null, null, null, null);
        while(unionCursor.moveToNext()){
            Union union = new Union(unionCursor.getString(1));
            Cursor studentCursor = _db.rawQuery("SELECT nom, prenom, surnom, email, poste " +
                                                "FROM etudiants_bureau LEFT JOIN etudiants ON id=idetudiant WHERE idbureau=?;",
                                                new String[] {unionCursor.getString(0)});
            while(studentCursor.moveToNext()){
                Student student = new Student(studentCursor.getString(0),
                                              studentCursor.getString(1),
                                              studentCursor.getString(2),
                                              studentCursor.getString(3));
                union.addStudent(studentCursor.getString(4), student);
            }
            studentCursor.close();
            Cursor clubCursor = _db.query("clubs", new String[] {"id", "nom", "jour", "date", "heure_debut", "duree", "lieu"},
                                            "idbureau = ?", new String[] {unionCursor.getString(0)}, null, null, null);
            while(clubCursor.moveToNext()){
                Club club = new Club(clubCursor.getString(1));
                //TODO set the others fields of the club
                Cursor studentClubCursor = _db.rawQuery("SELECT nom, prenom, surnom, email, poste " +
                                                        "FROM etudiants_club LEFT JOIN etudiants ON id=idetudiant WHERE idclub=?;",
                                                        new String[] {clubCursor.getString(0)});
                while(studentClubCursor.moveToNext()){
                    Student student = new Student(studentCursor.getString(0),
                                                  studentCursor.getString(1),
                                                  studentCursor.getString(2),
                                                  studentCursor.getString(3));
                    club.addStudent(studentCursor.getString(4), student);
                }
                studentClubCursor.close();
                union.addClub(club);
            }
            clubCursor.close();
            unions.add(union);
        }
        unionCursor.close();
        return unions;
    }
}

