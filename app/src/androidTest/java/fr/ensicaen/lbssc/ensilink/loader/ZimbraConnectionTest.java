package fr.ensicaen.lbssc.ensilink.loader;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;

import fr.ensicaen.lbssc.ensilink.storage.Association;
import fr.ensicaen.lbssc.ensilink.storage.Club;
import fr.ensicaen.lbssc.ensilink.storage.Mail;
import fr.ensicaen.lbssc.ensilink.storage.Union;

/**
 * @author Florian Arnould
 */
@RunWith(AndroidJUnit4.class)
public class ZimbraConnectionTest {
	@Test
	public void getContentFromPlainMultipartTest() throws Exception {
		Message message = Mockito.mock(Message.class);
		Multipart multipart = Mockito.mock(Multipart.class);
		Mockito.when(message.getContent()).thenReturn(multipart);
		Mockito.when(multipart.getCount()).thenReturn(1);
		BodyPart bodyPart = Mockito.mock(BodyPart.class);
		Mockito.when(multipart.getBodyPart(0)).thenReturn(bodyPart);
		String text = "test";
		Mockito.when(bodyPart.getContentType()).thenReturn("text/plain");
		Mockito.when(bodyPart.getContent()).thenReturn(text);
		ZimbraConnection zimbra = new ZimbraConnection();
		String textFound = zimbra.getContentFromMultipart(message);
		Assert.assertEquals(text, textFound);
	}

	@Test
	public void getContentFromHtmlMultipartTest() throws Exception {
		Message message = Mockito.mock(Message.class);
		Multipart multipart = Mockito.mock(Multipart.class);
		Mockito.when(message.getContent()).thenReturn(multipart);
		Mockito.when(multipart.getCount()).thenReturn(1);
		BodyPart bodyPart = Mockito.mock(BodyPart.class);
		Mockito.when(multipart.getBodyPart(0)).thenReturn(bodyPart);
		Mockito.when(bodyPart.getContentType()).thenReturn("text/html");
		Mockito.when(bodyPart.getContent()).thenReturn("text");
		ZimbraConnection zimbra = new ZimbraConnection();
		String textFound = zimbra.getContentFromMultipart(message);
		Assert.assertTrue(textFound.isEmpty());
	}

	@Test
	public void parseAndInsertMailAndSaveFromUnionTest() throws Exception {
		Context context = InstrumentationRegistry.getTargetContext();
		SQLiteDatabase db = DataManagerForTest.setDefaultLocalDatabase();
		db.delete("mails", null, null);
		db.delete("union_mails", null, null);
		db.delete("club_mails", null, null);

		DataLoader loader = new DataLoader(context, false);
		loader.openDatabase();
		loader.loadUnionsFromDatabase();
		List<Union> unions = loader.getUnions();

		Union union = unions.get(3);
		String from = "from " + union.getEmail();
		String subject = "subject";
		String date = "2017-09-01";
		String email = union.getEmail();

		ZimbraConnection zimbra = new ZimbraConnection();
		zimbra.setMailFolder(context.getDir("emails", Context.MODE_PRIVATE));
		final CountDownLatch zimbraSignal = new CountDownLatch(1);
		zimbra.parseAndInsertMailAndSave(from, subject, date, email, db, unions, new OnMailSavedListener() {
			@Override
			public void onMailSaved() {
				zimbraSignal.countDown();
			}
		});
		zimbraSignal.await();

		Cursor cursor = db.query("mails", null, null, null, null, null, null);
		Assert.assertEquals(1, cursor.getCount());
		Assert.assertTrue(cursor.moveToFirst());
		int mailId = cursor.getInt(0);
		Assert.assertEquals(date, cursor.getString(1));
		cursor.close();
		cursor = db.query("union_mails", null, null, null, null, null, null);
		Assert.assertEquals(1, cursor.getCount());
		cursor.moveToFirst();
		Assert.assertEquals(union.getId(), cursor.getInt(0));
		Assert.assertEquals(mailId, cursor.getInt(1));

		final CountDownLatch loaderSignal = new CountDownLatch(1);
		loader.loadMailsUnionFromDatabase(union, new OnMailLoadedListener() {
			@Override
			public void onMailLoaded(Association association) {
				loaderSignal.countDown();
			}
		});
		loaderSignal.await();
		List<Mail> mails = union.getMails();
		Assert.assertEquals(1, mails.size());
		Mail mail = mails.get(0);
		Assert.assertEquals(date, mail.getDate());
		Assert.assertEquals(subject, mail.getSubject());
	}

	@Test
	public void parseAndInsertMailAndSaveFromUnionTagsTest() throws Exception {
		Context context = InstrumentationRegistry.getTargetContext();
		SQLiteDatabase db = DataManagerForTest.setDefaultLocalDatabase();
		db.delete("mails", null, null);
		db.delete("union_mails", null, null);
		db.delete("club_mails", null, null);

		DataLoader loader = new DataLoader(context, false);
		loader.openDatabase();
		loader.loadUnionsFromDatabase();
		List<Union> unions = loader.getUnions();

		Union union = unions.get(3);
		String from = "from";
		String subject = "subject " + union.getTags();
		String date = "2017-09-01";
		String email = "test@test.test";

		ZimbraConnection zimbra = new ZimbraConnection();
		zimbra.setMailFolder(context.getDir("emails", Context.MODE_PRIVATE));
		final CountDownLatch zimbraSignal = new CountDownLatch(1);
		zimbra.parseAndInsertMailAndSave(from, subject, date, email, db, unions, new OnMailSavedListener() {
			@Override
			public void onMailSaved() {
				zimbraSignal.countDown();
			}
		});
		zimbraSignal.await();

		Cursor cursor = db.query("mails", null, null, null, null, null, null);
		Assert.assertEquals(1, cursor.getCount());
		Assert.assertTrue(cursor.moveToFirst());
		int mailId = cursor.getInt(0);
		Assert.assertEquals(date, cursor.getString(1));
		cursor.close();
		cursor = db.query("union_mails", null, null, null, null, null, null);
		Assert.assertEquals(1, cursor.getCount());
		cursor.moveToFirst();
		Assert.assertEquals(union.getId(), cursor.getInt(0));
		Assert.assertEquals(mailId, cursor.getInt(1));

		final CountDownLatch loaderSignal = new CountDownLatch(1);
		loader.loadMailsUnionFromDatabase(union, new OnMailLoadedListener() {
			@Override
			public void onMailLoaded(Association association) {
				loaderSignal.countDown();
			}
		});
		loaderSignal.await();
		List<Mail> mails = union.getMails();
		Assert.assertEquals(1, mails.size());
		Mail mail = mails.get(0);
		Assert.assertEquals(date, mail.getDate());
		Assert.assertEquals(subject, mail.getSubject());
	}

	@Test
	public void parseAndInsertMailAndSaveFromClubTest() throws Exception {
		Context context = InstrumentationRegistry.getTargetContext();
		SQLiteDatabase db = DataManagerForTest.setDefaultLocalDatabase();
		db.delete("mails", null, null);
		db.delete("union_mails", null, null);
		db.delete("club_mails", null, null);

		DataLoader loader = new DataLoader(context, false);
		loader.openDatabase();
		loader.loadUnionsFromDatabase();
		List<Union> unions = loader.getUnions();

		Club club = unions.get(1).getClub(0);
		String from = "from";
		String subject = "subject " + club.getName();
		String date = "2017-09-01";
		String email = "somthing@test.test";

		ZimbraConnection zimbra = new ZimbraConnection();
		zimbra.setMailFolder(context.getDir("emails", Context.MODE_PRIVATE));
		final CountDownLatch zimbraSignal = new CountDownLatch(1);
		zimbra.parseAndInsertMailAndSave(from, subject, date, email, db, unions, new OnMailSavedListener() {
			@Override
			public void onMailSaved() {
				zimbraSignal.countDown();
			}
		});
		zimbraSignal.await();

		Cursor cursor = db.query("mails", null, null, null, null, null, null);
		Assert.assertEquals(1, cursor.getCount());
		Assert.assertTrue(cursor.moveToFirst());
		int mailId = cursor.getInt(0);
		Assert.assertEquals(date, cursor.getString(1));
		cursor.close();
		cursor = db.query("club_mails", null, null, null, null, null, null);
		Assert.assertEquals(1, cursor.getCount());
		cursor.moveToFirst();
		Assert.assertEquals(club.getId(), cursor.getInt(0));
		Assert.assertEquals(mailId, cursor.getInt(1));

		final CountDownLatch loaderSignal = new CountDownLatch(1);
		loader.loadMailsClubFromDatabase(club, new OnMailLoadedListener() {
			@Override
			public void onMailLoaded(Association association) {
				loaderSignal.countDown();
			}
		});
		loaderSignal.await();
		List<Mail> mails = club.getMails();
		Assert.assertEquals(1, mails.size());
		Mail mail = mails.get(0);
		Assert.assertEquals(date, mail.getDate());
		Assert.assertEquals(subject, mail.getSubject());
	}

}
