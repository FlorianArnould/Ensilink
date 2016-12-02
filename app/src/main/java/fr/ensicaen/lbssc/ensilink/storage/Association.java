package fr.ensicaen.lbssc.ensilink.storage;

import java.util.HashMap;
import java.util.Map;


public abstract class Association {
    private String _name;
    private Map<String, Student> _students;
    Association(String name){
        _name = name;
        _students = new HashMap<>();
    }

    void addStudent(String position, Student student){
        _students.put(position, student);
    }

    public String getName(){
        return _name;
    }

    public Map<String, Student> getStudents(){
        return _students;
    }
}
