package fr.ensicaen.lbssc.ensilink.loader;

import java.util.Map;

/**
 * Created by florian on 19/03/17.
 */

interface OnServiceFinishedListener {
    void onServiceFinished(boolean succeed, Map<String, Long> images);
}
