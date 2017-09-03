package fr.ensicaen.lbssc.ensilink.loader;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Florian Arnould
 */
@RunWith(AndroidJUnit4.class)
public class LocalDatabaseManagerTest {

	@Test
	public void onCreateTest() throws Exception{
		InstrumentationRegistry.getTargetContext().deleteDatabase(LocalDatabaseManager.DATABASE_NAME);
		LocalDatabaseManager manager = new LocalDatabaseManager(InstrumentationRegistry.getTargetContext());
		Assert.assertTrue(manager.getWritableDatabase() != null);
	}

	// TODO: 03/09/17 Add an onUpgrade test 
}
