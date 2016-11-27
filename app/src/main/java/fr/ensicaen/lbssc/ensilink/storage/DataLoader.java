package fr.ensicaen.lbssc.ensilink.storage;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

final class DataLoader extends Thread{

    private DatabaseCloner _cloner;
    private SQLiteDatabase _db;
    private LocalDatabaseManager _databaseManager;
    private boolean _updated;
    private List<Union> _unions;
    private OnLoadingFinishListener _listener;

    DataLoader(Context context){
        _databaseManager = new LocalDatabaseManager(context);
        _cloner = new DatabaseCloner(context);
        _db = null;
        _updated = false;
    }

    public void run(){
        if(openDatabase() && (cloneDatabase() || !isDatabaseEmpty())){
            Log.d("D", String.valueOf(isDatabaseEmpty()));
            loadUnionsFromDatabase();
        }else {
            _unions = null;
        }
        if(_listener != null) {
            _listener.OnLoadingFinish(this);
        }
    }

    private boolean openDatabase(){
        if(_db != null){
            return true;
        }
        try{
            _db = _databaseManager.getWritableDatabase();
        }catch (SQLiteException e){
            Log.d("D", "Error when tried to open SQLite database : " + e.getMessage());
            return false;
        }
        return true;
    }

    //TODO : check speed issue with the timer on emulator
    private boolean cloneDatabase(){
        _cloner.start();
        try {
            if (isDatabaseEmpty()) {
                _cloner.join();
            } else {
                _cloner.join(5000);
            }
        } catch (InterruptedException e) {
            Log.d("D", "Problem with the cloner thread : " + e.getMessage());
            return false;
        }
        return _updated = _cloner.succeed();
    }

    private boolean isDatabaseEmpty(){
        if(_db == null){
            if(!openDatabase()){
                return true;
            }
        }
        String[] columns = {"name"};
        Cursor result = _db.query("unions", columns, null, null, null, null, null);
        boolean test = result.moveToFirst();
        result.close();
        return !test;
    }

    private void loadUnionsFromDatabase(){
        _unions = new ArrayList<>();
        Cursor unionCursor = _db.query("unions", null, null, null, null, null, null);
        if(unionCursor.moveToFirst()) {
            do {
                Union union = new Union(unionCursor.getString(1));
                loadStudentsUnionFromDatabase(unionCursor, union);
                loadClubsFromDatabase(unionCursor, union);
                _unions.add(union);
            } while (unionCursor.moveToNext());
        }
        unionCursor.close();
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

    public boolean wasUpdated(){
        return _updated;
    }

    void setOnLoadingFinishListener(OnLoadingFinishListener listener){
        _listener = listener;
    }

    List<Union> getUnions(){
        return _unions;
    }
}

