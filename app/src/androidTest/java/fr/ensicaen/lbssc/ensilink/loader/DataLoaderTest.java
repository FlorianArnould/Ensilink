package fr.ensicaen.lbssc.ensilink.loader;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import fr.ensicaen.lbssc.ensilink.storage.Association;
import fr.ensicaen.lbssc.ensilink.storage.Club;
import fr.ensicaen.lbssc.ensilink.storage.Event;
import fr.ensicaen.lbssc.ensilink.storage.Mail;
import fr.ensicaen.lbssc.ensilink.storage.Union;

/**
 * @author Florian Arnould
 */
@RunWith(AndroidJUnit4.class)
public class DataLoaderTest {
	@Test
	public void openDatabaseTest() throws Exception {
		DataLoader loader = new DataLoader(InstrumentationRegistry.getTargetContext(), true);
		Assert.assertTrue(loader.openDatabase());
		Assert.assertTrue(loader.openDatabase());
	}

	@Test
	public void databaseEmptyWithoutPreviousOpenTest() throws Exception {
		DataLoader loader = new DataLoader(InstrumentationRegistry.getTargetContext(), true);
		loader.isDatabaseEmpty();
	}

	@Test
	public void databaseEmptyTest() throws Exception {
		Context context = InstrumentationRegistry.getTargetContext();
		DatabaseCloner cloner = new DatabaseCloner(new LocalDatabaseManager(context).getWritableDatabase());
		cloner.clearDatabase();
		DataLoader loader = new DataLoader(context, true);
		Assert.assertTrue(loader.isDatabaseEmpty());
	}

	@Test
	public void databaseNotEmptyTest() throws Exception {
		Context context = InstrumentationRegistry.getTargetContext();
		DataManagerForTest.setDefaultLocalDatabase();
		DataLoader loader = new DataLoader(context, true);
		Assert.assertFalse(loader.isDatabaseEmpty());
	}

	@Test
	public void downloadFileTest() throws Exception {
		Context context = InstrumentationRegistry.getTargetContext();
		DatabaseCloner cloner = new DatabaseCloner(new LocalDatabaseManager(context).getWritableDatabase());
		Document doc = cloner.parseAnswer(InstrumentationRegistry.getContext().getResources().openRawResource(fr.ensicaen.lbssc.ensilink.test.R.raw.database));
		Assert.assertTrue(doc != null);
		NodeList list = doc.getElementsByTagName("last_update_image_file");
		String fileName = list.item(0).getAttributes().getNamedItem("file").getNodeValue();
		Map<String, Long> images = new HashMap<>();
		images.put(fileName, (long)0);
		File file = new File(context.getFilesDir(), fileName);
		if (file.exists()) {
			Assert.assertTrue(file.delete());
		}
		DataLoader loader = new DataLoader(context, true);
		loader.downloadFiles(images);
		Assert.assertTrue(file.exists());
	}

	@Test
	public void loadUnionsFromDatabase() throws Exception {
		Context context = InstrumentationRegistry.getTargetContext();
		SQLiteDatabase db = DataManagerForTest.setDefaultLocalDatabase();
		DataLoader loader = new DataLoader(context, true);
		loader.openDatabase();
		loader.loadUnionsFromDatabase();
		List<Union> unions = loader.getUnions();
		Assert.assertEquals(6, unions.size());
		List<Club> clubs = unions.get(1).getClubs();
		Assert.assertEquals(16, clubs.size());
		List<Event> events = loader.getEvents();
		Assert.assertEquals(6, events.size());
	}

	@Test
	public void runTest() throws Exception {
		Context context = InstrumentationRegistry.getTargetContext();
		DatabaseCloner cloner = new DatabaseCloner(new LocalDatabaseManager(context).getWritableDatabase());
		cloner.cloneDatabase();
		DataLoader loader = new DataLoader(context, true);
		final CountDownLatch signal = new CountDownLatch(1);
		loader.setOnLoadingFinishListener(new OnLoadingFinishListener() {
			@Override
			public void OnLoadingFinish(DataLoader loader) {
				signal.countDown();
			}
		});
		loader.run();
		signal.await();
		Assert.assertTrue(loader.getImages().size() > 0);
	}

	@Test
	public void loadMailClubTest() throws Exception {
		Context context = InstrumentationRegistry.getTargetContext();
		SQLiteDatabase db = DataManagerForTest.setDefaultLocalDatabase();

		Club club = new Club(1, "foot", 2, null, null, null, "", null, null);
		String from = "from";
		String subject = "subject";
		String content = "content";
		String date = "2017-09-01";
		String mailId = "1";

		db.delete("club_mails", null, null);
		db.delete("mails", null, null);
		ContentValues mailValues = new ContentValues();
		mailValues.put("id", mailId);
		mailValues.put("date", date);
		db.insertOrThrow("mails", null, mailValues);
		ContentValues clubMailValues = new ContentValues();
		clubMailValues.put("idclub", String.valueOf(club.getId()));
		clubMailValues.put("idmail", mailId);
		db.insertOrThrow("club_mails", null, clubMailValues);

		DataLoader loader = new DataLoader(context, true);
		loader.openDatabase();

		File parent = context.getDir("emails", Context.MODE_PRIVATE);
		File file = new File(parent, mailId + ".txt");
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		writer.write(from);
		writer.newLine();
		writer.write(subject);
		writer.newLine();
		writer.write(content);
		writer.close();

		final CountDownLatch signal = new CountDownLatch(1);
		loader.loadMailsClubFromDatabase(club, new OnMailLoadedListener() {
			@Override
			public void onMailLoaded(Association association) {
				signal.countDown();
			}
		});
		signal.await();
		List<Mail> mails = club.getMails();
		Assert.assertEquals(1, mails.size());
		Mail mail = mails.get(0);
		Assert.assertEquals(date, mail.getDate());
		Assert.assertEquals(from, mail.getTransmitter());
		Assert.assertEquals(subject, mail.getSubject());
		Assert.assertTrue(mail.getText().contains(content));
	}

	@Test
	public void loadMailUnion() throws Exception {
		Context context = InstrumentationRegistry.getTargetContext();
		SQLiteDatabase db = DataManagerForTest.setDefaultLocalDatabase();

		Union union = new Union(1, "test", null, null, Color.WHITE, "", "");
		String from = "from";
		String subject = "subject";
		String content = "content";
		String date = "2017-09-01";
		String mailId = "1";

		db.delete("union_mails", null, null);
		db.delete("mails", null, null);
		ContentValues mailValues = new ContentValues();
		mailValues.put("id", mailId);
		mailValues.put("date", date);
		db.insertOrThrow("mails", null, mailValues);
		ContentValues unionMailValues = new ContentValues();
		unionMailValues.put("idunion", String.valueOf(union.getId()));
		unionMailValues.put("idmail", mailId);
		db.insertOrThrow("union_mails", null, unionMailValues);

		DataLoader loader = new DataLoader(context, true);
		loader.openDatabase();

		File parent = context.getDir("emails", Context.MODE_PRIVATE);
		File file = new File(parent, mailId + ".txt");
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		writer.write(from);
		writer.newLine();
		writer.write(subject);
		writer.newLine();
		writer.write(content);
		writer.close();

		final CountDownLatch signal = new CountDownLatch(1);
		loader.loadMailsUnionFromDatabase(union, new OnMailLoadedListener() {
			@Override
			public void onMailLoaded(Association association) {
				signal.countDown();
			}
		});
		signal.await();
		List<Mail> mails = union.getMails();
		Assert.assertEquals(1, mails.size());
		Mail mail = mails.get(0);
		Assert.assertEquals(date, mail.getDate());
		Assert.assertEquals(from, mail.getTransmitter());
		Assert.assertEquals(subject, mail.getSubject());
		Assert.assertTrue(mail.getText().contains(content));
	}

	// TODO: 05/09/17 Cover onServiceDisconnected method
}
