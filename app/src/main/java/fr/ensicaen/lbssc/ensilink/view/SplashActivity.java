package fr.ensicaen.lbssc.ensilink.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import fr.ensicaen.lbssc.ensilink.R;
import fr.ensicaen.lbssc.ensilink.storage.OnSchoolDataListener;
import fr.ensicaen.lbssc.ensilink.storage.School;

/**
 * @author Florian Arnould
 * @version 1.0
 */

/**
 * The Splash screen of the application
 */
public class SplashActivity extends Activity {

    private boolean isMainActivityLaunched;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        isMainActivityLaunched = false;
        School school = School.getInstance();
        school.refreshData(getApplicationContext(), new OnSchoolDataListener(){
            @Override
            public void OnDataRefreshed() {
                if(!isMainActivityLaunched) {
                    isMainActivityLaunched = true;
                    Thread thread = new Thread() {
                        @Override
                        public void run() {
                            try {
                                sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            Intent i = new Intent(SplashActivity.this, MainActivity.class);
                            startActivity(i);
                            finish();
                        }
                    };
                    thread.start();
                }
            }
        });
    }
}
