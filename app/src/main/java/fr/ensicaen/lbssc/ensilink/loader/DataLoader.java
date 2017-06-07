/**
 * This file is part of Ensilink.
 *
 * Ensilink is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * Ensilink is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Ensilink.
 * If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright, The Ensilink team :  ARNOULD Florian, ARIK Marsel, FILIPOZZI Jérémy,
 * ENSICAEN, 6 Boulevard du Maréchal Juin, 26 avril 2017
 *
 */

package fr.ensicaen.lbssc.ensilink.loader;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fr.ensicaen.lbssc.ensilink.storage.Association;
import fr.ensicaen.lbssc.ensilink.storage.Club;
import fr.ensicaen.lbssc.ensilink.storage.Date;
import fr.ensicaen.lbssc.ensilink.storage.Event;
import fr.ensicaen.lbssc.ensilink.storage.Image;
import fr.ensicaen.lbssc.ensilink.storage.Mail;
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
    private int _progress;
    private int _maxProgress;
    private final Context _context;

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
        _progress = 0;
        _maxProgress = 100;
        _context = context;
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
        Intent serviceIntent = new Intent(_context, UpdateService.class);
        final ServiceConnection connection = new ServiceConnection() {

            UpdateService service;

            @Override
            public void onServiceConnected(ComponentName name, IBinder iBinder) {
                UpdateService.ServiceBinder binder = (UpdateService.ServiceBinder) iBinder;
                service = binder.getServiceInstance();
                service.setListener(new OnServiceFinishedListener() {
                    @Override
                    public void onServiceFinished(boolean succeed, Map<String, Long> images) {
                        if(succeed) {
                            downloadFiles(images);
                            loadUnionsFromDatabase();
                            if (_listener != null) {
                                _listener.OnLoadingFinish(DataLoader.this);
                            }
                        }
                        getAllImagesAttribution();
                        _db.close();
                    }
                });
                _context.startService(new Intent(_context, UpdateService.class));
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                if(service != null) {
                    service.removeListener();
                }
            }
        };
        _context.bindService(serviceIntent, connection, Context.BIND_AUTO_CREATE);
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
            Log.w("openDatabase", "Error when tried to open SQLite database : " + e.getMessage(), e);
            return false;
        }
        return true;
    }

    /**
     * Download the images
     * @param images map containing the images to download
     */
    private void downloadFiles(Map<String, Long> images){
        _progress++;
        updateImages(images);
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
        Cursor unionCursor = _db.rawQuery("SELECT u.id, u.name, l.name, l.attribution, p.name, p.attribution, c.red, c.green, c.blue, u.facebook, u.email, u.tags FROM unions AS u LEFT JOIN images AS l ON u.idlogo=l.id LEFT JOIN images AS p ON u.idphoto=p.id LEFT JOIN colors AS c ON u.idcolor=c.id;", null, null);
        if(unionCursor.moveToFirst()) {
            do {
                Union union = new Union(
                        unionCursor.getInt(0),
                        unionCursor.getString(1),
                        new Image(new File(_fileDir, unionCursor.getString(2)), unionCursor.getString(3)),
                        new Image(new File(_fileDir, unionCursor.getString(4)), unionCursor.getString(5)),
                        Color.rgb(unionCursor.getInt(6), unionCursor.getInt(7), unionCursor.getInt(8)),
                        unionCursor.getString(9),
                        unionCursor.getString(10));
                String tags[] = unionCursor.getString(11).split("-");
                for(String tag : tags){
                    union.addTag(tag);
                }
                loadStudentsUnionFromDatabase(unionCursor, union);
                loadClubsFromDatabase(unionCursor, union);
                _unions.add(union);
                loadEventsFromUnion(unionCursor, union);
                loadMailsUnionFromDatabase(union);
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
     * Loads the mails of an union
     * @param union the union to which the mail belong
     */
    private void loadMailsUnionFromDatabase(Union union) {
        Cursor cursor = _db.rawQuery("SELECT idmail, date FROM union_mails JOIN mails ON idmail=id WHERE idunion=?;", new String[]{String.valueOf(union.getId())});
        if(cursor.moveToFirst()){
            do {
                new MailLoader(union).execute(cursor.getString(0), cursor.getString(1));
            } while (cursor.moveToNext());
        }
        cursor.close();
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
                        clubCursor.getInt(0),
                        clubCursor.getString(1),
                        clubCursor.getInt(2),
                        date,
                        time,
                        duration,
                        clubCursor.getString(6),
                        new Image(new File(_fileDir, clubCursor.getString(7)), clubCursor.getString(8)),
                        new Image(new File(_fileDir, clubCursor.getString(9)), clubCursor.getString(10)));
                loadStudentsClubFromDatabase(clubCursor, club);
                loadMailsClubFromDatabase(club);
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
     * Loads the mails of a club
     * @param club the club to which the mail belong
     */
    private void loadMailsClubFromDatabase(Club club) {
        Cursor cursor = _db.rawQuery("SELECT idmail, date FROM club_mails JOIN mails ON idmail=id WHERE idclub=?;", new String[]{String.valueOf(club.getId())});
        if(cursor.moveToFirst()){
            do {
                new MailLoader(club).execute(cursor.getString(0), cursor.getString(1));
            } while (cursor.moveToNext());
        }
        cursor.close();
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
     * Download new images from network and remove the unused files
     */
    private void updateImages(Map<String, Long> timestamps){
        List<String> list = new ArrayList<>();
        List<String> localFiles = Arrays.asList(_fileDir.list());
        for(Map.Entry<String, Long> entry : timestamps.entrySet()){
            if(localFiles.contains(entry.getKey())){
                File file = new File(_fileDir, entry.getKey());
                if(file.lastModified()/1000 < entry.getValue()){
                    list.add(entry.getKey());
                }
            }else{
                list.add(entry.getKey());
            }

        }
        _maxProgress = list.size()+1;
        try{
            for (String imageName : list){
                _downloader.download(imageName);
                _progress++;
            }
        } catch (IOException e) {
            Log.d("updateImages", "io error : " + e.getMessage(), e);
        }
        removeUnusedFiles(timestamps.keySet());
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
    private void removeUnusedFiles(Set<String> filesUsed){
        for (String file : _fileDir.list()){
            if(!filesUsed.contains(file)){
                if(! new File(file).delete()){
                    Log.d("Error", "Unused file " + file + " wasn't deleted");
                }
            }
        }
    }

    /**
     * Set all images which need an attribution in credits
     */
    private void getAllImagesAttribution(){
        Cursor cursor = _db.query("images", null, null, null, null, null, null);
        if(cursor.moveToFirst()) {
            do {
                if(!cursor.getString(2).isEmpty()) {
                    _images.add(new Image(new File(_fileDir, cursor.getString(1)), cursor.getString(2)));
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    /**
     * @return the progress of the current update
     */
    public int getProgress(){
        return _progress;
    }

    /**
     * @return the max value of the progress of the current update
     */
    public int getMaxProgress(){
        return _maxProgress;
    }

    /**
     * AsyncTask to load mails from hard disk in background
     */
    class MailLoader extends AsyncTask<String, Void, Void>{

        private final Association _association;

        MailLoader(Association association){
            _association = association;
        }

        @Override
        protected Void doInBackground(String... params) {
            File parent = _context.getDir("emails", Context.MODE_PRIVATE);
            try {
                BufferedReader rd = new BufferedReader(new FileReader(new File(parent, params[0] + ".txt")));
                String from = rd.readLine();
                String subject = rd.readLine();
                String content = "";
                String line;
                while((line = rd.readLine()) != null){
                    content += line + "\n";
                }
                _association.addMail(new Mail(from, subject, content, params[1]));
            } catch(IOException e) {
                Log.w("doInBackground", "Mail not loaded : " + e.getMessage(), e);
            }
            return null;
        }
    }
}