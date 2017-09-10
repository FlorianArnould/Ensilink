package fr.ensicaen.lbssc.ensilink.storage;

import android.support.annotation.VisibleForTesting;

/**
 * @author Florian Arnould
 */
@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
public interface OnRefreshListener {
	void onRefresh();
}
