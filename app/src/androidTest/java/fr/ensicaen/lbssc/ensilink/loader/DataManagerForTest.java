package fr.ensicaen.lbssc.ensilink.loader;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;

import org.junit.Assert;
import org.w3c.dom.Document;

import java.util.List;

import fr.ensicaen.lbssc.ensilink.storage.Union;

/**
 * @author Florian Arnould
 */
public class DataManagerForTest {
	static List<Union> getTestUnions() {
		Context context = InstrumentationRegistry.getTargetContext();
		DatabaseCloner cloner = new DatabaseCloner(new LocalDatabaseManager(context).getWritableDatabase());
		Document doc = cloner.parseAnswer(InstrumentationRegistry.getContext().getResources().openRawResource(fr.ensicaen.lbssc.ensilink.test.R.raw.database));
		Assert.assertTrue(doc != null);
		cloner.updateDatabase(doc);
		DataLoader loader = new DataLoader(context, true);
		loader.openDatabase();
		loader.loadUnionsFromDatabase();
		return loader.getUnions();
	}

	public static SQLiteDatabase setDefaultLocalDatabase() {
		Context context = InstrumentationRegistry.getTargetContext();
		SQLiteDatabase db = new LocalDatabaseManager(context).getWritableDatabase();
		DatabaseCloner cloner = new DatabaseCloner(db);
		Document doc = cloner.parseAnswer(InstrumentationRegistry.getContext().getResources().openRawResource(fr.ensicaen.lbssc.ensilink.test.R.raw.database));
		Assert.assertTrue(doc != null);
		cloner.updateDatabase(doc);
		return db;
	}
}
