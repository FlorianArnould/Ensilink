package fr.ensicaen.lbssc.ensilink.loader;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ServiceTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import fr.ensicaen.lbssc.ensilink.R;
import fr.ensicaen.lbssc.ensilink.loader.news.DayNews;
import fr.ensicaen.lbssc.ensilink.loader.news.MailNotificationContainer;
import fr.ensicaen.lbssc.ensilink.loader.news.News;
import fr.ensicaen.lbssc.ensilink.loader.news.PlaceNews;
import fr.ensicaen.lbssc.ensilink.storage.Club;
import fr.ensicaen.lbssc.ensilink.storage.Union;

/**
 * @author Florian Arnould
 */
@RunWith(AndroidJUnit4.class)
public class UpdateServiceTest {
	@Rule
	public ServiceTestRule _serviceRule;

	@Before
	public void initializeServiceRule() {
		_serviceRule = new ServiceTestRule();
	}

	@Test
	public void updateInformationTest() throws Exception {
		Context context = InstrumentationRegistry.getTargetContext();
		SQLiteDatabase db = new LocalDatabaseManager(context).getWritableDatabase();
		new DatabaseCloner(db).clearDatabase();
		Intent intent = new Intent(context, UpdateService.class);
		UpdateService.ServiceBinder binder = (UpdateService.ServiceBinder)_serviceRule.bindService(intent);
		UpdateService service = binder.getServiceInstance();
		final CountDownLatch signal = new CountDownLatch(1);
		service.setListener(new OnServiceFinishedListener() {
			@Override
			public void onServiceFinished(boolean succeed, Map<String, Long> images) {
				Assert.assertTrue(succeed);
				Assert.assertTrue(images.size() > 0);
				signal.countDown();
			}
		});
		service.updateInformation();
		signal.await();
		service.removeListener();
		Cursor cursor = db.query("unions", null, null, null, null, null, null);
		Assert.assertTrue(cursor.getCount() > 0);
		cursor.close();
	}

	@Test
	public void createNotificationTest() throws Exception {
		List<Union> unions = DataManagerForTest.getTestUnions();
		final String clubName = unions.get(1).getClub(0).getName();
		final NotificationManager notificationManager = Mockito.mock(NotificationManager.class);
		final CountDownLatch signal = new CountDownLatch(1);
		Mockito.doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
				Notification notification = invocationOnMock.getArgument(1);
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
					Assert.assertTrue(notification.extras.getString("android.text", "").contains(clubName));
				}
				// TODO: 05/09/17 Make an assert for API before kitkat (notification.tickerView.mActions.get(4))
				signal.countDown();
				return null;
			}
		}).when(notificationManager).notify(Mockito.anyInt(), Mockito.any(Notification.class));
		Intent intent = new Intent(InstrumentationRegistry.getTargetContext(), UpdateService.class);
		UpdateService.ServiceBinder binder = (UpdateService.ServiceBinder)_serviceRule.bindService(intent);
		UpdateService service = binder.getServiceInstance();
		List<News> news = new ArrayList<>();
		news.add(new DayNews(2, 1, clubName, 1));
		Context realContext = InstrumentationRegistry.getTargetContext();
		SharedPreferences preferences = realContext.getSharedPreferences(realContext.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putBoolean(clubName, true);
		editor.apply();
		service.createNotification(news, notificationManager);
		signal.await();
	}

	@Test
	public void createMailNotificationClubTest() throws Exception {
		List<Union> unions = DataManagerForTest.getTestUnions();
		final Union union = unions.get(1);
		final Club club = union.getClub(0);
		final String subject = "subject";
		final String text = "text";
		final NotificationManager notificationManager = Mockito.mock(NotificationManager.class);
		final CountDownLatch signal = new CountDownLatch(1);
		Mockito.doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
				Notification notification = invocationOnMock.getArgument(1);
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
					Assert.assertTrue(notification.extras.getString("android.title", "").equals(subject));
					Assert.assertTrue(notification.extras.getString("android.text", "").equals(text));
				}
				// TODO: 05/09/17 Make an assert for API before kitkat (notification.tickerView.mActions.get(4))
				signal.countDown();
				return null;
			}
		}).when(notificationManager).notify(Mockito.anyInt(), Mockito.any(Notification.class));
		Intent intent = new Intent(InstrumentationRegistry.getTargetContext(), UpdateService.class);
		UpdateService.ServiceBinder binder = (UpdateService.ServiceBinder)_serviceRule.bindService(intent);
		UpdateService service = binder.getServiceInstance();
		List<MailNotificationContainer> news = new ArrayList<>();
		news.add(new MailNotificationContainer(subject, text, union, club));
		Context realContext = InstrumentationRegistry.getTargetContext();
		SharedPreferences preferences = realContext.getSharedPreferences(realContext.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putBoolean(club.getName(), true);
		editor.apply();
		service.createMailNotification(news, notificationManager);
		signal.await();
	}

	@Test
	public void createMultipleNotificationsTest() throws Exception {
		List<Union> unions = DataManagerForTest.getTestUnions();
		final String clubName = unions.get(1).getClub(0).getName();
		final NotificationManager notificationManager = Mockito.mock(NotificationManager.class);
		final CountDownLatch signal = new CountDownLatch(1);
		Mockito.doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
				Notification notification = invocationOnMock.getArgument(1);
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
					Assert.assertTrue(notification.extras.getString("android.text", "").contains(clubName));
				}
				// TODO: 05/09/17 Make an assert for API before kitkat (notification.tickerView.mActions.get(4))
				signal.countDown();
				return null;
			}
		}).when(notificationManager).notify(Mockito.anyInt(), Mockito.any(Notification.class));
		Intent intent = new Intent(InstrumentationRegistry.getTargetContext(), UpdateService.class);
		UpdateService.ServiceBinder binder = (UpdateService.ServiceBinder)_serviceRule.bindService(intent);
		UpdateService service = binder.getServiceInstance();
		List<News> news = new ArrayList<>();
		news.add(new DayNews(2, 1, clubName, 1));
		news.add(new PlaceNews(2, 1, clubName, "coucou"));
		Context realContext = InstrumentationRegistry.getTargetContext();
		SharedPreferences preferences = realContext.getSharedPreferences(realContext.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putBoolean(clubName, true);
		editor.apply();
		service.createNotification(news, notificationManager);
		signal.await();
	}

	@Test
	public void createMailNotificationUnionTest() throws Exception {
		List<Union> unions = DataManagerForTest.getTestUnions();
		final Union union = unions.get(1);
		final String subject = "subject";
		final String text = "text";
		final NotificationManager notificationManager = Mockito.mock(NotificationManager.class);
		final CountDownLatch signal = new CountDownLatch(1);
		Mockito.doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
				Notification notification = invocationOnMock.getArgument(1);
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
					Assert.assertTrue(notification.extras.getString("android.title", "").equals(subject));
					Assert.assertTrue(notification.extras.getString("android.text", "").equals(text));
				}
				// TODO: 05/09/17 Make an assert for API before kitkat (notification.tickerView.mActions.get(4))
				signal.countDown();
				return null;
			}
		}).when(notificationManager).notify(Mockito.anyInt(), Mockito.any(Notification.class));
		Intent intent = new Intent(InstrumentationRegistry.getTargetContext(), UpdateService.class);
		UpdateService.ServiceBinder binder = (UpdateService.ServiceBinder)_serviceRule.bindService(intent);
		UpdateService service = binder.getServiceInstance();
		List<MailNotificationContainer> news = new ArrayList<>();
		news.add(new MailNotificationContainer(subject, text, union));
		Context realContext = InstrumentationRegistry.getTargetContext();
		SharedPreferences preferences = realContext.getSharedPreferences(realContext.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putBoolean(union.getName(), true);
		editor.apply();
		service.createMailNotification(news, notificationManager);
		signal.await();
	}
}
