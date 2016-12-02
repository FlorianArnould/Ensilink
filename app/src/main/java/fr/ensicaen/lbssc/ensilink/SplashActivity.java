package fr.ensicaen.lbssc.ensilink;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import fr.ensicaen.lbssc.ensilink.storage.OnSchoolDataListener;
import fr.ensicaen.lbssc.ensilink.storage.School;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        School school = School.getInstance();
        school.setOnSchoolDataListener(new OnSchoolDataListener(){
            @Override
            public void OnDataRefreshed(School school) {
                Thread thread = new Thread(){
                    @Override
                    public void run(){
                        try {
                            sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Intent i = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(i);
                        School.getInstance().setOnSchoolDataListener(null);
                        finish();
                    }
                };
                thread.start();
            }
        });
        school.refreshData(getApplicationContext());
    }
}
