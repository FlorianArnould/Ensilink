package fr.ensicaen.lbssc.ensilink.storage;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Florian Arnould
 * @version 1.0
 */

/**
 * Abstract class that represent a student association
 */
public abstract class Association {

    private final String _name;
    private final Image _logo;
    private final Image _photo;
    private final List<Student> _students;

    /**
     * The constructor
     * @param name the name of the association
     * @param logo the logo image
     * @param photo the photo image
     */
    Association(String name, Image logo, Image photo){
        _name = name;
        _students = new ArrayList<>();
        _logo = logo;
        _photo = photo;
    }

    /**
     * Adds a student to the association
     * @param student a student instance
     */
    public void addStudent(Student student){
        _students.add(student);
    }

    /**
     * @return the name of the association
     */
    public String getName(){
        return _name;
    }

    /**
     * @return the List of the students
     */
    public List<Student> getStudents(){
        return _students;
    }

    /**
     * load the logo in a thread and after call the listener
     * @param listener listener called when the image will be loaded
     */
    public void loadLogo(OnImageLoadedListener listener){
        ImageLoadThread thread = new ImageLoadThread(_logo.getAbsolutePath(), listener);
        thread.start();
    }

    /**
     * Load the logo here and return it
     * @return a drawable of the logo of the club
     */
    public Drawable getLogo(){
        return Drawable.createFromPath(_logo.getAbsolutePath());
    }

    /**
     * Load the photo in a thread and after call the listener
     * @param listener listener called when the image will be loaded
     */
    public void loadPhoto(OnImageLoadedListener listener){
        ImageLoadThread thread = new ImageLoadThread(_photo.getAbsolutePath(), listener);
        thread.start();
    }

    /**
     * Thread used to avoid UI lags when we loads the images
     */
    private class ImageLoadThread extends Thread{

        private final String _path;
        private final OnImageLoadedListener _listener;

        /**
         * The constructor
         * @param path to the local file
         * @param listener the listener to call when the image will be loaded
         */
        ImageLoadThread(String path, OnImageLoadedListener listener){
            _path = path;
            _listener = listener;
        }

        /**
         * Load the image and send it to the listener
         */
        @Override
        public void run(){
            _listener.OnImageLoaded(Drawable.createFromPath(_path));
        }
    }
}
