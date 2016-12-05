package fr.ensicaen.lbssc.ensilink.storage;

/**
 * @author Florian Arnould
 * @version 1.0
 */

/**
 * The interface of a listener to listen the update of School
 */
public interface OnSchoolDataListener {
    /**
     * Executes this when School is updated
     * @param school the School instance itself
     */
    void OnDataRefreshed(School school);
}
