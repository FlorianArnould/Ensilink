/**
 * This file is part of Ensilink.
 * <p>
 * Ensilink is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 * <p>
 * Ensilink is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public
 * License along with Ensilink.
 * If not, see <http://www.gnu.org/licenses/>.
 * <p>
 * Copyright, The Ensilink team :  ARNOULD Florian, ARIK Marsel, FILIPOZZI Jérémy,
 * ENSICAEN, 6 Boulevard du Maréchal Juin, 26 avril 2017
 */
package fr.ensicaen.lbssc.ensilink.loader;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import fr.ensicaen.lbssc.ensilink.R;
import fr.ensicaen.lbssc.ensilink.loader.news.DayNews;
import fr.ensicaen.lbssc.ensilink.loader.news.MailNotificationContainer;
import fr.ensicaen.lbssc.ensilink.loader.news.News;
import fr.ensicaen.lbssc.ensilink.view.MainActivity;
import fr.ensicaen.lbssc.ensilink.view.SplashActivity;
import fr.ensicaen.lbssc.ensilink.view.clubscreen.ClubActivity;

/**
 * @author Florian Arnould
 * @version 1.0
 */

/**
 * Service used to refresh in background and send notification if it's needed
 */
public class UpdateService extends Service {
	private static ScheduledExecutorService _serviceExecutor;
	private IBinder _binder;
	private OnServiceFinishedListener _listener;
	private int _notificationMailId;

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d("Debug", "Service is launching");
		_binder = new ServiceBinder();
		if (_serviceExecutor == null) {
			_serviceExecutor = Executors.newScheduledThreadPool(1);
			_serviceExecutor.scheduleAtFixedRate(new Runnable() {
				@Override
				public void run() {
					getBaseContext().startService(new Intent(UpdateService.this, UpdateService.class));
				}
			}, 2, 2, TimeUnit.MINUTES);
		}
		_notificationMailId = 2;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		new WorkThread().execute();
		return START_STICKY;
	}

	/**
	 * Set the listener, it will be removed after being called.
	 *
	 * @param listener listener called after the refresh
	 */
	public void setListener(OnServiceFinishedListener listener) {
		_listener = listener;
	}

	/**
	 * Remove the current listener
	 */
	public void removeListener() {
		_listener = null;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return _binder;
	}

	/**
	 * Refresh the database, get the mails and create a notification
	 */
	void updateInformation() {
		LocalDatabaseManager manager = new LocalDatabaseManager(getBaseContext());
		SQLiteDatabase db;
		try {
			db = manager.getWritableDatabase();
			if (db != null) {
				DatabaseCloner cloner = new DatabaseCloner(db);
				cloner.cloneDatabase();
				ZimbraConnection zimbra = new ZimbraConnection();
				try {
					zimbra.connect(getBaseContext());
					zimbra.updateDatabase(db, getBaseContext());
					// TODO: 07/06/17 check to change this catch
				} catch (Exception e) {
					Log.e("updateInformation", "Connection to the zimbra server is not possible : " + e.getMessage(), e);
				}
				if (_listener != null) {
					_listener.onServiceFinished(cloner.succeed(), cloner.lastUpdateImages());
					_listener = null;
				}
				db.close();
				NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
				createNotification(cloner.getModifications(), notificationManager);
				createMailNotification(zimbra.getNewMails(), notificationManager);
			}
		} catch (SQLiteException e) {
			Log.e("updateInformation", "Error when tried to open SQLite database : " + e.getMessage(), e);
		}
	}

	/**
	 * Send a notification if it is needed
	 *
	 * @param news list of the news which needs a notifications
	 */
	void createNotification(List<News> news, NotificationManager notificationManager) {
		String text = "";
		if (!news.isEmpty()) {
			SharedPreferences pref = getBaseContext().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
			for (int i = 0; i < news.size() - 1; i++) {
				if (pref.getBoolean(news.get(i).getClubName(), false)) {
					// TODO: 03/09/17 Use an Enumeration instead of an intanceof to store the type in the News class
					if (news.get(i) instanceof DayNews) {
						((DayNews)news.get(i)).setDaysArray(getResources().getStringArray(R.array.days));
					}
					text += news.get(i).toNotificationString() + "\n";
				}
			}
			if (pref.getBoolean(news.get(news.size() - 1).getClubName(), false)) {
				text += news.get(news.size() - 1).toNotificationString();
			}
			if (!text.isEmpty()) {
				NotificationCompat.BigTextStyle notificationStyle = new
						NotificationCompat.BigTextStyle();
				notificationStyle.bigText(text);
				Intent intent;
				if (news.size() >= 2) {
					intent = new Intent(this, SplashActivity.class);
				} else {
					intent = new Intent(this, ClubActivity.class);
					intent.putExtra("UNION_ID", news.get(0).getUnionIndex());
					intent.putExtra("CLUB_ID", news.get(0).getClubIndex());
				}
				PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
						intent, PendingIntent.FLAG_UPDATE_CURRENT);
				NotificationCompat.Builder builder =
						new NotificationCompat.Builder(UpdateService.this.getApplicationContext())
								.setContentIntent(contentIntent)
								.setAutoCancel(true)
								.setSmallIcon(R.drawable.ic_kangaroo)
								.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_kangaroo))
								.setContentTitle("News")
								.setContentText(text)
								.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
								.setLights(Color.rgb(63, 81, 181), 3000, 3000)
								.setVibrate(new long[]{0, 500})
								.setStyle(notificationStyle);
				builder.setColor(Color.rgb(63, 81, 181));
				notificationManager.notify(1, builder.build());
			}
		}
	}

	/**
	 * Send a notification if it is needed
	 *
	 * @param newMails list of the new mails
	 */
	void createMailNotification(List<MailNotificationContainer> newMails, NotificationManager notificationManager) {
		SharedPreferences pref = getBaseContext().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
		for (MailNotificationContainer container : newMails) {
			boolean wantANotification;
			if (container.isForAClub()) {
				wantANotification = pref.getBoolean(container.getClub().getName(), false);
			} else {
				wantANotification = pref.getBoolean(container.getUnion().getName(), false);
			}
			if (wantANotification) {
				NotificationCompat.BigTextStyle notificationStyle = new
						NotificationCompat.BigTextStyle();
				notificationStyle.bigText(container.getText());
				Intent intent;
				if (container.isForAClub()) {
					intent = new Intent(this, ClubActivity.class);
					intent.putExtra("CLUB_ID", container.getUnion().getClubIndex(container.getClub()));
				} else {
					intent = new Intent(this, MainActivity.class);
				}
				intent.putExtra("UNION_ID", container.getUnion().getId() - 1);
				PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
						intent, PendingIntent.FLAG_UPDATE_CURRENT);
				NotificationCompat.Builder builder =
						new NotificationCompat.Builder(UpdateService.this.getApplicationContext())
								.setContentIntent(contentIntent)
								.setAutoCancel(true)
								.setSmallIcon(R.drawable.ic_kangaroo)
								.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_kangaroo))
								.setContentTitle(container.getSubject())
								.setContentText(container.getText())
								.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
								.setLights(Color.rgb(63, 81, 181), 3000, 3000)
								.setVibrate(new long[]{0, 500})
								.setStyle(notificationStyle);
				builder.setColor(Color.rgb(63, 81, 181));
				notificationManager.notify(_notificationMailId, builder.build());
				_notificationMailId++;
				if (_notificationMailId < 2) {
					_notificationMailId = 2;
				}
			}
		}
	}

	/**
	 * Binder used to get the service instance from other contexts like activities.
	 */
	class ServiceBinder extends Binder {
		UpdateService getServiceInstance() {
			return UpdateService.this;
		}
	}

	/**
	 * AsyncTask which update information to avoid network on main thread errors
	 */
	private class WorkThread extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			updateInformation();
			return null;
		}
	}
}
