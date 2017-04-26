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

    private final int _id;
    private final String _name;
    private final Image _logo;
    private final Image _photo;
    private final List<Student> _students;
    private final List<Mail> _mails;

    /**
     * The constructor
     * @param name the name of the association
     * @param logo the logo image
     * @param photo the photo image
     */
    Association(int id, String name, Image logo, Image photo){
        _id = id;
        _name = name;
        _students = new ArrayList<>();
        _logo = logo;
        _photo = photo;
        _mails = new ArrayList<>();
    }

    /**
     * @return the id of the association
     */
    public int getId() {
        return _id;
    }

    /**
     * Adds a student to the association
     * @param student a student instance
     */
    public void addStudent(Student student){
        _students.add(student);
    }

    /**
     * Adds a mail to the association
     * @param mail a mail instance
     */
    public void addMail(Mail mail) {
        _mails.add(mail);
    }

    /**
     * @return the List of the mails
     */
    public List<Mail> getMails(){
        return _mails;
    }

    /**
     * @param position of the mail in the list
     * @return the mail instance
     */
    public Mail getMail(int position){
        return _mails.get(position);
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
