package fr.ensicaen.lbssc.ensilink.storage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;


/**
 * @author Florian Arnould
 * @version 1.0
 */

/**
 * The class that represents a big event
 */
public final class Event {

    private final String _title;
    private final String _text;
    private final File _imageFile;
    private final Union _union;

    /**
     * The constructor
     * @param title the title of the event
     * @param text the main text of the article
     * @param imageFile the header image of the article
     * @param union the organiser union of the event
     */
    public Event(String title, String text, File imageFile, Union union){
        _title = title;
        _text = text;
        _imageFile = imageFile;
        _union = union;
    }

    /**
     * @return the title of the event
     */
    public String getTitle(){
        return _title;
    }

    /**
     * @return the text of the event's article
     */
    public  String getMainText(){
        return _text;
    }

    /**
     * @return the bitmap image of the event
     */
    public Bitmap getImage(){
        return BitmapFactory.decodeFile(_imageFile.getAbsolutePath());
    }

    /**
     * @return the union which organise thus event
     */
    public Union getParentUnion(){
        return _union;
    }
}
