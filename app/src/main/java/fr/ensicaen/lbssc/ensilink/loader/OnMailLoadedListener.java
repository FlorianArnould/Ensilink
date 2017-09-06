package fr.ensicaen.lbssc.ensilink.loader;

import fr.ensicaen.lbssc.ensilink.storage.Association;

/**
 * @author Florian Arnould
 */
interface OnMailLoadedListener {
	void onMailLoaded(Association association);
}
