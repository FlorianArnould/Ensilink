package fr.ensicaen.lbssc.ensilink.storage;

import android.graphics.drawable.Drawable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
    private List<Student> _students;

    /**
     * The constructor
     * @param name the name of the association
     * @param logoFile a file with the path to the logo image
     * @param photoFile a file with the path to the photo image
     */
    Association(String name, File logoFile, File photoFile){
        _name = name;
        _students = new ArrayList<>();
        _logoFile = logoFile;
        _photoFile = photoFile;
    }

    /**
     * Adds a student to the association
     * @param student a student instance
     */
    void addStudent(Student student){
        _students.add(student);
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
    public List<Student> getStudents(){
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
     * @return a drawable of the logo of the club
     */
    public Drawable getLogo(){
        return Drawable.createFromPath(_logoFile.getAbsolutePath());
    }

    /**
     *
     * @param listener listener called when the image will be loaded
     */
    public void loadPhoto(OnImageLoadedListener listener){
        ImageLoadThread thread = new ImageLoadThread(_photoFile, listener);
        thread.start();
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
            _listener.OnImageLoaded(Drawable.createFromPath(_image.getAbsolutePath()));
        }
    }
}
