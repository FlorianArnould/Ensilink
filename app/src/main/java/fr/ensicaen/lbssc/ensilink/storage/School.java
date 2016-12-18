package fr.ensicaen.lbssc.ensilink.storage;

import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * @author Florian Arnould
 * @version 1.0
 */

/**
 * A singleton to store the information of associations
 */
public final class School {

    private static School _ourInstance = new School();
    private static List<Union> _unions;
    private static List<Event> _events;

    /**
     *
     * @return the school instance
     */
    public static School getInstance() {
        return _ourInstance;
    }

    /**
     * The private constructor
     */
    private School() {
    }

    /**
     * Updates the information of the unions
     * @param context an application context
     * @param listener a listener to get when the school will be updated
     */
    public void refreshData(Context context, final OnSchoolDataListener listener){
        DataLoader loader = new DataLoader(context);
        loader.setOnLoadingFinishListener(new OnLoadingFinishListener() {
            @Override
            public void OnLoadingFinish(DataLoader loader) {
                _unions = loader.getUnions();
                _events = loader.getEvents();
                if(listener != null){
                    listener.OnDataRefreshed(School.this);
                }
            }
        });
        loader.start();
    }

    /**
     *
     * @param i the union index
     * @return return the corresponding union
     */
    public Union getUnion(int i){
        return _unions.get(i);
    }

    /**
     *
     * @return a List with all unions
     */
    public List<Union> getUnions(){
        return _unions;
    }

    /**
     *
     * @return a List with all events
     */
    public List<Event> getEvents(){
        return _events;
    }
}