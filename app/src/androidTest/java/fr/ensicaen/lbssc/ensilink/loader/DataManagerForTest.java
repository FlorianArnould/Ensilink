package fr.ensicaen.lbssc.ensilink.loader;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.test.InstrumentationRegistry;

import org.junit.Assert;
import org.w3c.dom.Document;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;

import fr.ensicaen.lbssc.ensilink.storage.Club;
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

	public static void addMailToBDS() throws Exception {
		Context context = InstrumentationRegistry.getTargetContext();
		SQLiteDatabase db = new LocalDatabaseManager(context).getWritableDatabase();
		db.delete("club_mails", null, null);
		db.delete("union_mails", null, null);
		db.delete("mails", null, null);
		String from = "from";
		String subject = "subject";
		String content = "content";
		String date = "2017-09-01";
		String mailId = "1";

		Union union = new Union(2, "test", null, null, Color.WHITE, "", "");

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
	}

	public static void addMailToFootball() throws Exception {
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
	}
}
