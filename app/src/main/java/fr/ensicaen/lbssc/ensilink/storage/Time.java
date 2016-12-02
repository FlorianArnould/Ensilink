package fr.ensicaen.lbssc.ensilink.storage;


public class Time {

    private int _hours;
    private int _minutes;

    Time(String time){
        String[] strings = time.split(Character.toString(':'));
        _hours = Integer.valueOf(strings[0]);
        _minutes = Integer.valueOf(strings[1]);
    }

    public String toString(){
        String str = "";
        if(_hours < 10){
            str += "0";
        }
        str += _hours + ":";
        if(_minutes < 10){
            str += "0";
        }
        return  str + _minutes;
    }
}
