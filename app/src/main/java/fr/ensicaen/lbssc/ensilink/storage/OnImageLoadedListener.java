package fr.ensicaen.lbssc.ensilink.storage;

import android.graphics.Bitmap;

/**
 * @author Florian Arnould
 * @version 1.0
 */

/**
 * Listener to use when you want to load images without UI lags
 */
public interface OnImageLoadedListener {
    /**
     * method called when the image is loaded
     * @param image the loaded image
     */
    void OnImageLoaded(Bitmap image);
}
