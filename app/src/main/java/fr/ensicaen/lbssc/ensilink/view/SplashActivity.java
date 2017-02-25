package fr.ensicaen.lbssc.ensilink.view;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;

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
        Thread progressThread = new Thread(){
            @Override
            public void run(){
                final ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressBar);
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                });
                while(progressBar.getMax() != progressBar.getProgress()){
                    final School school = School.getInstance();
                    progressBar.setMax(school.getMaxProgress()*10);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ObjectAnimator animation = ObjectAnimator.ofInt(progressBar, "progress", school.getProgress()*10);
                            animation.setDuration(500); // 0.5 second
                            animation.setInterpolator(new DecelerateInterpolator());
                            animation.start();
                        }
                    });
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Log.d("Debug", "progress Thread finished");
            }
        };
        progressThread.start();
    }
}
