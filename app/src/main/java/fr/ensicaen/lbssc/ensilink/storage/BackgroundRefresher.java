package fr.ensicaen.lbssc.ensilink.storage;


import android.content.Context;

final class BackgroundRefresher extends Thread {
    private Context _context;

    BackgroundRefresher(Context context){
        _context = context;
    }

    public void run(){
        new DataLoader(_context).getUnions();
    }

}
