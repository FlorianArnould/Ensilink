package fr.ensicaen.lbssc.ensilink.storage;

/**
 * @author Florian Arnould
 * @version 1.0
 */

/**
 * The class which store information about a student
 */
public final class Student {

    private String _email;
    private String _name;
    private String _lastName;
    private String _nickname;

    Student(String name, String lastName, String nickname, String email){
        _name = name;
        _email = email;
        _lastName = lastName;
        _nickname = nickname;
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
    public String toString(){
        return _name + " " + _lastName + " " + _nickname + " " + _email;
    }
}
