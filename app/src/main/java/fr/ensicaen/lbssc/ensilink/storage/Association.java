package fr.ensicaen.lbssc.ensilink.storage;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Florian Arnould
 * @version 1.0
 */


/**
 * Abstract class that represent a student association
 */
public abstract class Association {

    private String _name;
    private Map<String, Student> _students;

    /**
     * The constructor
     * @param name the name of the association
     */
    Association(String name){
        _name = name;
        _students = new HashMap<>();
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
}
