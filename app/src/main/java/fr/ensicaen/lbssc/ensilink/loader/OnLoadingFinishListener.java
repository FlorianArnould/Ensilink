package fr.ensicaen.lbssc.ensilink.loader;

/**
 * @author Florian Arnould
 * @version 1.0
 */

/**
 * The interface of a listener to listen that the DataLoader finish its work
 */
public interface OnLoadingFinishListener {
    /**
     * Executes this when loading is finished
     * @param loader the DataLoader instance itself
     */
    void OnLoadingFinish(DataLoader loader);
}
