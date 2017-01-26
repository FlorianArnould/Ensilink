package fr.ensicaen.lbssc.ensilink;

/**
 * @author Florian Arnould
 * @version 1.0
 */

/**
 * Define a updatable object to refresh the views when we change the information
 */
public interface Updatable {

    /**
     * Method called to apply the previous modifications
     */
    void update();
}
