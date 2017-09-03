package fr.ensicaen.lbssc.ensilink.loader;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.w3c.dom.Document;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import fr.ensicaen.lbssc.ensilink.loader.news.News;

/**
 * @author Florian Arnould
 */
@RunWith(AndroidJUnit4.class)
public class DatabaseClonerTest {
	private SQLiteDatabase _db;

	@Before
	public void getDatabase() {
		_db = new LocalDatabaseManager(InstrumentationRegistry.getTargetContext()).getWritableDatabase();
	}

	@After
	public void closeDatabase() {
		_db.close();
	}

	@Test
	public void clearDatabaseTest() throws Exception {
		ContentValues values = new ContentValues();
		values.put("id", 5000);
		values.put("red", 45);
		values.put("green", 24);
		values.put("blue", 12);
		_db.insertOrThrow("colors", null, values);
		Cursor cursor = _db.query("colors", new String[]{"id"}, null, null, null, null, null);
		Assert.assertTrue(cursor.getCount() > 0);
		cursor.close();
		DatabaseCloner cloner = new DatabaseCloner(_db);
		cloner.clearDatabase();
		Cursor cursor2 = _db.query("colors", new String[]{"id"}, null, null, null, null, null);
		Assert.assertEquals(0, cursor2.getCount());
		cursor2.close();
	}

	@Test
	public void connectTest() throws Exception {
		DatabaseCloner cloner = new DatabaseCloner(_db);
		Assert.assertFalse(cloner.connect() == null);
	}

	@Test
	public void parseAnswerTest() throws Exception {
		DatabaseCloner cloner = new DatabaseCloner(_db);
		InputStream in = InstrumentationRegistry.getContext().getResources().openRawResource(fr.ensicaen.lbssc.ensilink.test.R.raw.database);
		Document doc = cloner.parseAnswer(in);
		Assert.assertTrue(doc != null);
	}

	@Test
	public void parseAnswerCorruptedTest() throws Exception {
		DatabaseCloner cloner = new DatabaseCloner(_db);
		InputStream in = InstrumentationRegistry.getContext().getResources().openRawResource(fr.ensicaen.lbssc.ensilink.test.R.raw.corrupted_database);
		Document doc = cloner.parseAnswer(in);
		Assert.assertEquals(null, doc);
	}

	@Test
	public void updateDatabaseTest() throws Exception {
		DatabaseCloner cloner = new DatabaseCloner(_db);
		cloner.clearDatabase();
		InputStream in = InstrumentationRegistry.getContext().getResources().openRawResource(fr.ensicaen.lbssc.ensilink.test.R.raw.database);
		Document doc = cloner.parseAnswer(in);
		Assert.assertTrue(doc != null);
		cloner.updateDatabase(doc);
		Cursor cursor = _db.query("unions", null, null, null, null, null, null);
		Assert.assertEquals(6, cursor.getCount());
		cursor.close();
	}

	@Test
	public void throwDuringUpdateDatabaseTest() throws Exception {
		DatabaseCloner cloner = new DatabaseCloner(_db);
		cloner.clearDatabase();
		InputStream in = InstrumentationRegistry.getContext().getResources().openRawResource(fr.ensicaen.lbssc.ensilink.test.R.raw.non_insertable_database);
		Document doc = cloner.parseAnswer(in);
		Assert.assertTrue(doc != null);
		cloner.updateDatabase(doc);
		Cursor cursor = _db.query("unions", null, null, null, null, null, null);
		Assert.assertEquals(0, cursor.getCount());
		cursor.close();
	}

	@Test
	public void lastUpdateImagesTest() throws Exception {
		DatabaseCloner cloner = new DatabaseCloner(_db);
		cloner.clearDatabase();
		InputStream in = InstrumentationRegistry.getContext().getResources().openRawResource(fr.ensicaen.lbssc.ensilink.test.R.raw.database);
		Document doc = cloner.parseAnswer(in);
		Assert.assertTrue(doc != null);
		cloner.updateDatabase(doc);
		Map<String, Long> imagesTimestamps = cloner.lastUpdateImages();
		Assert.assertEquals(67, imagesTimestamps.size());
	}

	@Test
	public void checkNewsTest() throws Exception {
		DatabaseCloner cloner = new DatabaseCloner(_db);
		InputStream in = InstrumentationRegistry.getContext().getResources().openRawResource(fr.ensicaen.lbssc.ensilink.test.R.raw.database);
		Document doc = cloner.parseAnswer(in);
		Assert.assertTrue(doc != null);
		cloner.updateDatabase(doc);
		in = InstrumentationRegistry.getContext().getResources().openRawResource(fr.ensicaen.lbssc.ensilink.test.R.raw.new_database);
		doc = cloner.parseAnswer(in);
		Assert.assertTrue(doc != null);
		cloner.updateDatabase(doc);
		List<News> news = cloner.getModifications();
		Assert.assertEquals(4, news.size());
	}

	@Test
	public void cloneDatabaseTest() throws Exception {
		DatabaseCloner cloner = new DatabaseCloner(_db);
		cloner.clearDatabase();
		cloner.cloneDatabase();
		Cursor cursor = _db.query("unions", null, null, null, null, null, null);
		Assert.assertTrue(cursor.getCount() > 0);
		cursor.close();
		Assert.assertTrue(cloner.succeed());
	}
}
