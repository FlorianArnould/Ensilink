package fr.ensicaen.lbssc.ensilink.storage;


public final class Date{
    private int _year;
    private int _month;
    private int _dayOfMonth;

    Date(String date){
        String[] strings = date.split(Character.toString('-'));
        _dayOfMonth = Integer.valueOf(strings[0]);
        _month = Integer.valueOf(strings[1]);
        _year = Integer.valueOf(strings[2]);
    }

    public String toString(){
        return String.valueOf(_dayOfMonth) + '-' + String.valueOf(_month) + '-' + String.valueOf(_year);
    }
}
