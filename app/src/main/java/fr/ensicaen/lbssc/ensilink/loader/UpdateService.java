package fr.ensicaen.lbssc.ensilink.loader;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.mail.MessagingException;

import fr.ensicaen.lbssc.ensilink.R;
import fr.ensicaen.lbssc.ensilink.loader.news.DayNews;
import fr.ensicaen.lbssc.ensilink.loader.news.News;
import fr.ensicaen.lbssc.ensilink.view.SplashActivity;

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

    @Override
    public void onCreate(){
        super.onCreate();
        Log.d("Debug", "Service is launching");
        _binder = new ServiceBinder();
        if(_serviceExecutor == null) {
            _serviceExecutor = Executors.newScheduledThreadPool(1);
            _serviceExecutor.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    getBaseContext().startService(new Intent(UpdateService.this, UpdateService.class));
                }
            }, 2, 2, TimeUnit.MINUTES);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        new WorkThread().execute();
        return START_STICKY;
    }

    /**
     * Set the listener, it will be removed after being called.
     * @param listener listener called after the refresh
     */
    public void setListener(OnServiceFinishedListener listener){
        _listener = listener;
    }

    /**
     * Remove the current listener
     */
    public void removeListener(){
        _listener = null;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return _binder;
    }

    /**
     * Binder used to get the service instance from other contexts like activities.
     */
    class ServiceBinder extends Binder {
        UpdateService getServiceInstance(){
            return UpdateService.this;
        }
    }

    /**
     * Refresh the database, get the mails and create a notification
     */
    private void updateInformation(){
        LocalDatabaseManager manager = new LocalDatabaseManager(getBaseContext());
        SQLiteDatabase db;
        try{
            db = manager.getWritableDatabase();
            if(db != null) {
                DatabaseCloner cloner = new DatabaseCloner(db);
                cloner.cloneDatabase();
                ZimbraConnection zimbra = new ZimbraConnection();
                try {
                    zimbra.connect(getBaseContext());
                    Log.d("DEBUG", "coucou");
                    zimbra.updateDatabase(db, getBaseContext());
                    zimbra.close();
                } catch(MessagingException ex) {
                    Log.e("ERROR", "Connection to the zimbra server is not possible : "+ ex.getMessage());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(_listener != null) {
                    _listener.onServiceFinished(cloner.succeed(), cloner.lastUpdateImages());
                    _listener = null;
                }
                db.close();
                createNotification(cloner.getModifications());
            }
        }catch (SQLiteException e){
            Log.e("ERROR", "Error when tried to open SQLite database : " + e.getMessage());
        }
    }

    /**
     * Send a notification if it is needed
     * @param news list of the news which needs a notifications
     */
    private void createNotification(List<News> news){
        String text = "";
        if(!news.isEmpty()) {
            for (int i=0;i<news.size()-1;i++) {
                if(news.get(i) instanceof DayNews){
                    ((DayNews) news.get(i)).setDaysArray(getResources().getStringArray(R.array.days));
                }
                text += news.get(i).toNotificationString() + "\n";
            }
            text += news.get(news.size()-1).toNotificationString();
            NotificationCompat.BigTextStyle notificationStyle = new
                    NotificationCompat.BigTextStyle();
            notificationStyle.bigText(text);
            Intent intent = new Intent(this, SplashActivity.class);
            PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                    intent, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder builder =
                    new NotificationCompat.Builder(UpdateService.this.getApplicationContext())
                            .setContentIntent(contentIntent)
                            .setSmallIcon(R.drawable.ic_kangaroo)
                            .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_kangaroo))
                            .setContentTitle("News")
                            .setContentText(text)
                            .setStyle(notificationStyle);
            builder.setColor(Color.rgb(63,81,181));
            NotificationManager notifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notifyMgr.notify(1, builder.build());
        }
        Log.d("Debug", "News : " + text);
    }

    /**
     * AsyncTask which update information to avoid network on main thread errors
     */
    private class WorkThread extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... params) {
            updateInformation();
            return null;
        }
    }
}
