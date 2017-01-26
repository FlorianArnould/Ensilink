package fr.ensicaen.lbssc.ensilink.loader;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import fr.ensicaen.lbssc.ensilink.storage.Club;
import fr.ensicaen.lbssc.ensilink.storage.Date;
import fr.ensicaen.lbssc.ensilink.storage.Event;
import fr.ensicaen.lbssc.ensilink.storage.Image;
import fr.ensicaen.lbssc.ensilink.storage.Student;
import fr.ensicaen.lbssc.ensilink.storage.Time;
import fr.ensicaen.lbssc.ensilink.storage.Union;

/**
 * @author Florian Arnould
 * @version 1.0
 */

/**
 * The class that load the information from the local database
 */
final public class DataLoader extends Thread{

    private SQLiteDatabase _db;
    private final LocalDatabaseManager _databaseManager;
    private List<Union> _unions;
    private List<Event> _events;
    private final List<Image> _images;
    private OnLoadingFinishListener _listener;
    private final FileDownloader _downloader;
    private final File _fileDir;
    private final boolean _preload;

    /**
     * @param context an activity context needed to open the local database with the database manager
     * @param preload is true if we want to preload the local information during download
     */
    public DataLoader(Context context, boolean preload){
        _databaseManager = new LocalDatabaseManager(context);
        _db = null;
        _downloader = new FileDownloader(context);
        _fileDir = context.getFilesDir();
        _preload = preload;
        _images = new ArrayList<>();
    }

    /**
     * The main method of the thread
     */
    public void run(){
        openDatabase();
        if(_preload && !isDatabaseEmpty()){
            loadUnionsFromDatabase();
            if(_listener != null) {
                _listener.OnLoadingFinish(this);
            }
        }
        if(cloneDatabaseAndDownloadFiles()) {
            loadUnionsFromDatabase();
            if (_listener != null) {
                _listener.OnLoadingFinish(this);
            }
        }
        _db.close();
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
     * clones the database with an instance of DatabaseCloner and download images if it is necessary
     * @return true if the database was cloned successfully
     */
    private boolean cloneDatabaseAndDownloadFiles(){
        DatabaseCloner cloner = new DatabaseCloner(_db);
        cloner.cloneDatabase();
        indexImages();
        if(_fileDir.list().length == 0 || cloner.lastUpdateOfImageFolder() > _fileDir.lastModified()/1000){
            updateImages();
        }
        return cloner.succeed();
    }

    /**
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
        Cursor unionCursor = _db.rawQuery("SELECT u.id, u.name, l.name, l.attribution, p.name, p.attribution, c.red, c.green, c.blue FROM unions AS u LEFT JOIN images AS l ON u.idlogo=l.id LEFT JOIN images AS p ON u.idphoto=p.id LEFT JOIN colors AS c ON u.idcolor=c.id;", null, null);
        if(unionCursor.moveToFirst()) {
            do {
                Union union = new Union(
                        unionCursor.getString(1),
                        new Image(new File(_fileDir, unionCursor.getString(2)), unionCursor.getString(3)),
                        new Image(new File(_fileDir, unionCursor.getString(4)), unionCursor.getString(5)),
                        Color.rgb(unionCursor.getInt(6), unionCursor.getInt(7), unionCursor.getInt(8)));
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
                        studentCursor.getString(3),
                        studentCursor.getString(4));
                union.addStudent(student);
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
        Cursor clubCursor = _db.rawQuery("SELECT c.id, c.name, c.day, c.date, c.start_hour, c.duration, c.place, l.name, l.attribution, p.name, p.attribution FROM clubs AS c LEFT JOIN images AS l ON c.idlogo=l.id LEFT JOIN images AS p ON c.idphoto=p.id where idunion=?;",
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
                Club club = new Club(
                        clubCursor.getString(1),
                        clubCursor.getInt(2),
                        date,
                        time,
                        duration,
                        clubCursor.getString(6),
                        new Image(new File(_fileDir, clubCursor.getString(7)), clubCursor.getString(8)),
                        new Image(new File(_fileDir, clubCursor.getString(9)), clubCursor.getString(10)));
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
                        "FROM students_club LEFT JOIN students ON id=idstudent WHERE idclub=? ORDER BY position;",
                new String[]{cursor.getString(0)});
        if(studentClubCursor.moveToFirst()) {
            do {
                Student student = new Student(studentClubCursor.getString(0),
                        studentClubCursor.getString(1),
                        studentClubCursor.getString(2),
                        studentClubCursor.getString(3),
                        studentClubCursor.getString(4));
                club.addStudent(student);
            } while (studentClubCursor.moveToNext());
        }
        studentClubCursor.close();
    }

    /**
     * Sets the listener
     * @param listener the listener to execute when data will be loaded
     */
    public void setOnLoadingFinishListener(OnLoadingFinishListener listener){
        _listener = listener;
    }

    /**
     * @return the previously loaded unions or null
     */
    public List<Union> getUnions(){
        return _unions;
    }

    /**
     * @return the previously loaded events or an empty list
     */
    public List<Event> getEvents(){
        return _events;
    }

    /**
     * @return the images used in the application
     */
    public List<Image> getImages(){
        return _images;
    }

    /**
     * Download All images from network and remove the unused files
     */
    private void updateImages(){
        Cursor cursor = _db.query("images", null, null, null, null, null, null);
        List<String> list = new ArrayList<>();
        if(cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }
        cursor.close();
        _downloader.downloadImages(list);
        removeUnusedFiles(list);
    }

    /**
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

    /**
     * Remove the unused files to free some memory
     * @param filesUsed list of the files really used in the application
     */
    private void removeUnusedFiles(List<String> filesUsed){
        for (String file : _fileDir.list()){
            if(!filesUsed.contains(file)){
                if(! new File(file).delete()){
                    Log.d("Error", "Unused file wasn't deleted");
                }
            }
        }
    }

    /**
     * Fill the list of images
     */
    private void indexImages(){
        Cursor cursor = _db.query("images", null, null, null, null, null, null);
        if(cursor.moveToFirst()) {
            do {
                _images.add(new Image(new File(_fileDir, cursor.getString(1)), cursor.getString(2)));
            } while (cursor.moveToNext());
        }
        cursor.close();
    }
}

