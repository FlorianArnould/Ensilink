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
	private boolean _isMainActivityLaunched;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_activity);
		_isMainActivityLaunched = false;
		School school = School.getInstance();
		school.refreshData(getApplicationContext(), new OnSchoolDataListener() {
			@Override
			public void onDataRefreshed() {
				if (!_isMainActivityLaunched) {
					_isMainActivityLaunched = true;
					Thread thread = new Thread() {
						@Override
						public void run() {
							try {
								sleep(1000);
							} catch (InterruptedException e) {
								Log.w("Launch MainActivity", "the 1 seconds wait was interrupted : " + e.getMessage(), e);
								finish();
								Thread.currentThread().interrupt();
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
		Thread progressThread = new Thread() {
			@Override
			public void run() {
				final ProgressBar progressBar = findViewById(R.id.progressBar);
				try {
					sleep(3000);
				} catch (InterruptedException e) {
					Log.w("Progress Thread", "the 3 seconds wait was interrupted : " + e.getMessage(), e);
					Thread.currentThread().interrupt();
				}
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						progressBar.setVisibility(View.VISIBLE);
					}
				});
				while (progressBar.getMax() != progressBar.getProgress()) {
					final School school = School.getInstance();
					progressBar.setMax(school.getMaxProgress() * 10);
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							ObjectAnimator animation = ObjectAnimator.ofInt(progressBar, "progress", school.getProgress() * 10);
							animation.setDuration(500); // 0.5 second
							animation.setInterpolator(new DecelerateInterpolator());
							animation.start();
						}
					});
					try {
						sleep(1000);
					} catch (InterruptedException e) {
						Log.w("Progress", "the 1 seconds wait was interrupted : " + e.getMessage(), e);
						Thread.currentThread().interrupt();
					}
				}
			}
		};
		progressThread.start();
	}
}
