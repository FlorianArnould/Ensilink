package fr.ensicaen.lbssc.ensilink.storage;

import android.content.Context;

import java.util.List;

/**
 * @author Florian Arnould
 * @version 1.0
 */

/**
 * A singleton to store the information of associations
 */
public final class School {

    private static final School _ourInstance = new School();
    private static List<Union> _unions;
    private static List<Event> _events;
    private static List<Image> _images;
    private static boolean _neverUpdated;

    /**
     * @return the school instance
     */
    public static School getInstance() {
        return _ourInstance;
    }

    /**
     * The private constructor
     */
    private School() {
        _neverUpdated = true;
    }

    /**
     * Updates the information of the unions
     * @param context an application context
     * @param listener a listener to get when the school will be updated
     */
    public void refreshData(Context context, final OnSchoolDataListener listener){
        DataLoader loader = new DataLoader(context, _neverUpdated);
        loader.setOnLoadingFinishListener(new OnLoadingFinishListener() {
            @Override
            public void OnLoadingFinish(DataLoader loader) {
                _unions = loader.getUnions();
                _events = loader.getEvents();
                _images = loader.getImages();
                if(listener != null){
                    listener.OnDataRefreshed();
                }
                _neverUpdated = false;
            }
        });
        loader.start();
    }

    /**
     * @param i the union index
     * @return the corresponding union
     */
    public Union getUnion(int i){
        return _unions.get(i);
    }

    /**
     * @return a List with all unions
     */
    public List<Union> getUnions(){
        return _unions;
    }

    /**
     * @return a List with all events
     */
    public List<Event> getEvents(){
        return _events;
    }

    /**
     * @param i the union index
     * @return the corresponding event
     */
    public Event getEvent(int i) {
        return _events.get(i);
    }

    /**
     * @return a List with all images of the application
     */
    public List<Image> getImages(){
        return _images;
    }
}
