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

    /**
     * the constructor
     * @param name his or her first name
     * @param lastName his or her last name
     * @param nickname his or her nickname
     * @param email his or her email address
     * @param position his or her position in the club or in the union
     */
    Student(String name, String lastName, String nickname, String email, String position){
        _name = name;
        _email = email;
        _lastName = lastName;
        _nickname = nickname;
        _position = position;
    }

    /**
     * @return the email address of the student
     */
    public String getEmail(){
        return _email;
    }

    /**
     * @return the first name of the student
     */
    public String getName(){
        return _name;
    }

    /**
     * @return the last name of the student
     */
    public String getLastName(){
        return _lastName;
    }

    /**
     * @return the nickname of the student
     */
    public String getNickname(){
        return  _nickname;
    }

    /**
     * @return the position of the student in the club or in the union
     */
    public String getPosition(){
        return _position;
    }

    /**
     * @return a string which sum up some information about the student
     */
    public String toString(){
        return _name + " " + _lastName + " " + _nickname + " " + _email;
    }
}
