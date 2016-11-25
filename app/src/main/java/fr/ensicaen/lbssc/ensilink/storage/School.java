package fr.ensicaen.lbssc.ensilink.storage;

import android.content.Context;

import java.util.List;

public final class School {
    private static School _ourInstance = new School();
    private static List<Union> _unions;

    public static School getInstance() {
        return _ourInstance;
    }

    private School() {
    }

    public void refreshData(Context context){
        _unions =  new DataLoader(context).getUnions();
    }
    public Union getUnion(int i){
        return _unions.get(i);
    }

    public List<Union> getUnions(){
        return _unions;
    }
}
