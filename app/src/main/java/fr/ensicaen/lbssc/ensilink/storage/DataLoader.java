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
            Log.d("D", String.valueOf(isDatabaseEmpty()));
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
        _cloner.start();
        try{
            _cloner.join();
            return _cloner.succeed();
        } catch(InterruptedException e){
            Log.d("D", "Error with the cloner thread : " + e.getMessage());
        }
        return false;
    }

    private boolean isDatabaseEmpty(){
        String[] columns = {"name"};
        Cursor result = _db.query("unions", columns, null, null, null, null, null);
        boolean test = result.moveToFirst();
        result.close();
        return !test;
    }

    private List<Union> loadUnionsFromDatabase(){
        List<Union> unions = new ArrayList<>();
        Cursor unionCursor = _db.query("unions", null, null, null, null, null, null);
        if(unionCursor.moveToFirst()) {
            do {
                Union union = new Union(unionCursor.getString(1));
                loadStudentsUnionFromDatabase(unionCursor, union);
                loadClubsFromDatabase(unionCursor, union);
                unions.add(union);
            } while (unionCursor.moveToNext());
        }
        unionCursor.close();
        return unions;
    }

    private void loadStudentsUnionFromDatabase(Cursor cursor, Union union){
        Cursor studentCursor = _db.rawQuery("SELECT lastname, name, nickname, email, position " +
                        "FROM students_union LEFT JOIN students ON id=idstudent WHERE idunion=?;",
                new String[]{cursor.getString(0)});
        if(studentCursor.moveToFirst()) {
            do {
                Student student = new Student(studentCursor.getString(0),
                        studentCursor.getString(1),
                        studentCursor.getString(2),
                        studentCursor.getString(3));
                union.addStudent(studentCursor.getString(4), student);
            } while (studentCursor.moveToNext());
        }
        studentCursor.close();
    }

    private void loadClubsFromDatabase(Cursor cursor, Union union){
        Cursor clubCursor = _db.query("clubs", new String[]{"id", "name", "day", "date", "start_hour", "duration", "place"},
                "idunion = ?", new String[]{cursor.getString(0)}, null, null, null);
        if(clubCursor.moveToFirst()) {
            do {
                Club club = new Club(clubCursor.getString(1));
                //TODO set the others fields of the club
                loadStudentsClubFromDatabase(clubCursor, club);
                union.addClub(club);
            } while (clubCursor.moveToNext());
        }
        clubCursor.close();
    }

    private void loadStudentsClubFromDatabase(Cursor cursor, Club club){
        Cursor studentClubCursor = _db.rawQuery("SELECT lastname, name, nickname, email, position " +
                        "FROM students_club LEFT JOIN students ON id=idstudent WHERE idclub=?;",
                new String[]{cursor.getString(0)});
        if(studentClubCursor.moveToFirst()) {
            do {
                Student student = new Student(studentClubCursor.getString(0),
                        studentClubCursor.getString(1),
                        studentClubCursor.getString(2),
                        studentClubCursor.getString(3));
                club.addStudent(studentClubCursor.getString(4), student);
            } while (studentClubCursor.moveToNext());
        }
        studentClubCursor.close();
    }
}

