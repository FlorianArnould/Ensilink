package fr.ensicaen.lbssc.ensilink.storage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Florian Arnould
 * @version 1.0
 */


/**
 * Abstract class that represent a student association
 */
abstract class Association {

    private String _name;
    private File _logoFile;
    private File _photoFile;
    //TODO choose another representation for members
    private Map<String, Student> _students;

    /**
     * The constructor
     * @param name the name of the association
     * @param logoFile a file with the path to the logo image
     * @param photoFile a file with the path to the photo image
     */
    Association(String name, File logoFile, File photoFile){
        _name = name;
        _students = new HashMap<>();
        _logoFile = logoFile;
        _photoFile = photoFile;
    }

    /**
     * Adds a student to the association
     * @param position the position of the student in the association
     * @param student a student instance
     */
    void addStudent(String position, Student student){
        _students.put(position, student);
    }

    /**
     *
     * @return the name of the association
     */
    public String getName(){
        return _name;
    }

    /**
     *
     * @return a Map with the students and their position as key
     */
    public Map<String, Student> getStudents(){
        return _students;
    }

    /**
     *
     * @param listener listener called when the image will be loaded
     */
    public void loadLogo(OnImageLoadedListener listener){
        ImageLoadThread thread = new ImageLoadThread(_logoFile, listener);
        thread.start();
    }

    /**
     *
     * @return the bitmap logo of the union
     */
    public Bitmap getLogo(){
        return BitmapFactory.decodeFile(_logoFile.getAbsolutePath());
    }

    /**
     *
     * @return the bitmap photo of the union
     */
    public Bitmap getPhoto(){
        return BitmapFactory.decodeFile(_photoFile.getAbsolutePath());
    }

    /**
     * Thread used to avoid UI lags when we loads the images
     */
    private class ImageLoadThread extends Thread{

        File _image;
        OnImageLoadedListener _listener;

        /**
         * The constructor
         * @param image a file object to find the image on the local disk
         * @param listener the listener to call when the image will be loaded
         */
        ImageLoadThread(File image, OnImageLoadedListener listener){
            _image = image;
            _listener = listener;
        }

        /**
         * Load the image and send it to the listener
         */
        @Override
        public void run(){
            _listener.OnImageLoaded(BitmapFactory.decodeFile(_logoFile.getAbsolutePath()));
        }
    }
}
