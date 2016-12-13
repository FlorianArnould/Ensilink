package fr.ensicaen.lbssc.ensilink.storage;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Florian Arnould
 * @version 1.0
 */

/**
 * The class that load the information from the local database
 */
final class DataLoader extends Thread{

    private DatabaseCloner _cloner;
    private SQLiteDatabase _db;
    private LocalDatabaseManager _databaseManager;
    private List<Union> _unions;
    private List<Event> _events;
    private OnLoadingFinishListener _listener;
    private FileDownloader _downloader;
    private File _fileDir;

    /**
     * The constructor
     * @param context an activity context needed to open the local database with the database manager
     */
    DataLoader(Context context){
        _databaseManager = new LocalDatabaseManager(context);
        _cloner = new DatabaseCloner(context);
        _db = null;
        _downloader = new FileDownloader(context);
        _fileDir = context.getFilesDir();
    }

    /**
     * The main method of the thread
     */
    public void run(){
        openDatabase();
        if(!isDatabaseEmpty()){
            loadUnionsFromDatabase();
            if(_listener != null) {
                _listener.OnLoadingFinish(this);
            }
        }
        cloneDatabase();
        downloadImages();
        loadUnionsFromDatabase();
        if(_listener != null) {
            _listener.OnLoadingFinish(this);
        }
    }

    /**
     * Opens the local database
     * @return true if the database is opened
     */
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

    /**
     * clones the database with an instance of DatabaseCloner
     * @return true if the database was cloned successfully
     */
    private boolean cloneDatabase(){
        _cloner.cloneDatabase();
        return _cloner.succeed();
    }

    /**
     *
     * @return true if the local database is empty
     */
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

    /**
     * Loads unions and their information in a private attribute
     */
    private void loadUnionsFromDatabase(){
        _unions = new ArrayList<>();
        _events = new ArrayList<>();
        Cursor unionCursor = _db.rawQuery("SELECT u.id, u.name, l.name, p.name FROM unions AS u LEFT JOIN images AS l ON u.idlogo=l.id LEFT JOIN images AS p ON u.idphoto=p.id;", null, null);
        if(unionCursor.moveToFirst()) {
            do {
                Union union = new Union(unionCursor.getString(1), new File(_fileDir, unionCursor.getString(2)), new File(_fileDir, unionCursor.getString(3)));
                loadStudentsUnionFromDatabase(unionCursor, union);
                loadClubsFromDatabase(unionCursor, union);
                _unions.add(union);
                loadEventsFromUnion(unionCursor, union);
            } while (unionCursor.moveToNext());
        }
        unionCursor.close();
    }

    /**
     * Loads the students of an union
     * @param cursor a cursor from a query on the unions' table
     * @param union the union to which the students belong
     */
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

    /**
     * Loads the clubs of an union
     * @param cursor a cursor from a query on the unions' table
     * @param union the union to which the clubs belong
     */
    private void loadClubsFromDatabase(Cursor cursor, Union union){
        Cursor clubCursor = _db.rawQuery("SELECT c.id, c.name, c.day, c.date, c.start_hour, c.duration, c.place, l.name, p.name FROM clubs AS c LEFT JOIN images AS l ON c.idlogo=l.id LEFT JOIN images AS p ON c.idphoto=p.id where idunion=?;",
                new String[]{cursor.getString(0)});
        if(clubCursor.moveToFirst()) {
            do {
                Date date = null;
                if(!clubCursor.getString(3).isEmpty()){
                    date = new Date(clubCursor.getString(3));
                }
                Time time = null;
                if(!clubCursor.getString(4).isEmpty()){
                    time = new Time(clubCursor.getString(4));
                }
                Time duration = null;
                if(!clubCursor.getString(5).isEmpty()){
                    duration = new Time(clubCursor.getString(5));
                }
                Club club = new Club(clubCursor.getString(1), clubCursor.getInt(2), date, time, duration, clubCursor.getString(6), new File(_fileDir, clubCursor.getString(2)), new File(_fileDir, clubCursor.getString(3)));
                loadStudentsClubFromDatabase(clubCursor, club);
                union.addClub(club);
            } while (clubCursor.moveToNext());
        }
        clubCursor.close();
    }

    /**
     * Loads the students of a club
     * @param cursor a cursor from a query on the clubs' table
     * @param club the club to which the students belong
     */
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

    /**
     * Sets the listener
     * @param listener the listener to execute when data will be loaded
     */
    void setOnLoadingFinishListener(OnLoadingFinishListener listener){
        _listener = listener;
    }

    /**
     *
     * @return the previously loaded unions or null
     */
    List<Union> getUnions(){
        return _unions;
    }

    /**
     *
     * @return the previously loaded events or an empty list
     */
    List<Event> getEvents(){
        return _events;
    }

    /**
     *
     * @return true if all images were loaded successfully
     */
    private boolean downloadImages(){
        Cursor cursor = _db.query("images", null, null, null, null, null, null);
        boolean ok = true;
        if(cursor.moveToFirst()) {
            do {
                if(!_downloader.download(cursor.getString(1))){
                    ok = false;
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return ok;
    }

    /**
     *
     * @param unionCursor cursor with union information
     * @param union organiser union of the event
     */
    private void loadEventsFromUnion(Cursor unionCursor, Union union){
        Cursor cursor = _db.rawQuery("SELECT e.title, e.text, i.name FROM events AS e LEFT JOIN images AS i ON e.idimage=i.id WHERE idunion=?;", new String[]{unionCursor.getString(0)});
        if(cursor.moveToFirst()) {
            do {
                _events.add(new Event(cursor.getString(0), cursor.getString(1), new File(_fileDir, cursor.getString(2)), union));
            } while (cursor.moveToNext());
        }
        cursor.close();
    }
}

