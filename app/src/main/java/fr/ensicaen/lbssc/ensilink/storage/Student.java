package fr.ensicaen.lbssc.ensilink.storage;

/**
 * @author Florian Arnould
 * @version 1.0
 */

/**
 * The class which store information about a student
 */
public final class Student {

    private final String _email;
    private final String _name;
    private final String _lastName;
    private final String _nickname;
    private final String _position;

    Student(String name, String lastName, String nickname, String email, String position){
        _name = name;
        _email = email;
        _lastName = lastName;
        _nickname = nickname;
        _position = position;
    }

    public String getEmail(){
        return _email;
    }
    public String getName(){
        return _name;
    }
    public String getLastName(){
        return _lastName;
    }
    public String getNickname(){
        return  _nickname;
    }
    public String getPosition(){
        return _position;
    }
    public String toString(){
        return _name + " " + _lastName + " " + _nickname + " " + _email;
    }
}