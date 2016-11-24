package fr.ensicaen.lbssc.ensilink.storage;

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
}
