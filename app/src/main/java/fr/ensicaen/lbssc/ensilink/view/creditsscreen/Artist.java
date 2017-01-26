package fr.ensicaen.lbssc.ensilink.view.creditsscreen;

import android.graphics.drawable.Drawable;

import fr.ensicaen.lbssc.ensilink.storage.Image;

/**
 * @author Florian Arnould
 * @version 1.0
 */

/**
 * Used to store information about the images creators for credits activity
 */
final class Artist {

    private final Drawable _drawable;
    private final String _attribution;

    /**
     * @param drawableId resource id of the drawable
     * @param attribution attribution text for the creative commons license
     */
    Artist(Drawable drawableId, String attribution){
        _drawable = drawableId;
        _attribution = attribution;
    }

    /**
     * @param image which needs an attribution
     */
    Artist(Image image){
        _drawable = Drawable.createFromPath(image.getAbsolutePath());
        _attribution = image.getAttribution();
    }

    /**
     * @return a resource id to the drawable that the artist created
     */
    Drawable getDrawable(){
        return _drawable;
    }

    /**
     * @return a resource id to the string of the attribution
     */
    String getAttribution(){
        return _attribution;
    }
}
