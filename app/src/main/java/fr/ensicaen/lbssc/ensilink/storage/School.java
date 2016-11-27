package fr.ensicaen.lbssc.ensilink.storage;

import android.content.Context;
import android.util.Log;

import java.util.List;

public final class School {
    private static School _ourInstance = new School();
    private static List<Union> _unions;
    private OnSchoolDataListener _listener = null;

    public static School getInstance() {
        return _ourInstance;
    }

    private School() {
    }

    public void refreshData(Context context){
        DataLoader loader = new DataLoader(context);
        loader.setOnLoadingFinishListener(new OnLoadingFinishListener() {
            @Override
            public void OnLoadingFinish(DataLoader loader) {
                if(_listener != null){
                    _unions = loader.getUnions();
                    _listener.OnDataRefreshed(School.this);
                }
            }
        });
        loader.start();
    }
    public Union getUnion(int i){
        return _unions.get(i);
    }

    public List<Union> getUnions(){
        return _unions;
    }

    public void setOnSchoolDataListener(OnSchoolDataListener listener){
        _listener = listener;
    }
}
