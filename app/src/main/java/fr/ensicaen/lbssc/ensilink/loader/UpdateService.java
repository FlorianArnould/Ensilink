package fr.ensicaen.lbssc.ensilink.loader;

import android.app.Service;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by florian on 19/03/17.
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

    public void setListener(OnServiceFinishedListener listener){
        _listener = listener;
    }

    public void removeListener(){
        _listener = null;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return _binder;
    }

    class ServiceBinder extends Binder {
        UpdateService getServiceInstance(){
            return UpdateService.this;
        }
    }

    private void updateInformation(){
        LocalDatabaseManager manager = new LocalDatabaseManager(getBaseContext());
        SQLiteDatabase db;
        try{
            db = manager.getWritableDatabase();
            if(db != null) {
                DatabaseCloner cloner = new DatabaseCloner(db);
                cloner.cloneDatabase();
                //and mails
                if(_listener != null) {
                    _listener.onServiceFinished(cloner.succeed(), cloner.lastUpdateImages());
                    _listener = null;
                }
                db.close();
            }
        }catch (SQLiteException e){
            Log.d("D", "Error when tried to open SQLite database : " + e.getMessage());
        }
    }

    private class WorkThread extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... params) {
            updateInformation();
            return null;
        }
    }
}
