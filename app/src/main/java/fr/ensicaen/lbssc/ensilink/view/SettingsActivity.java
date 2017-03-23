package fr.ensicaen.lbssc.ensilink.view;




import android.os.Build;
import android.os.Bundle;

import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;

import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;

import android.util.Log;
import android.view.MenuItem;

import fr.ensicaen.lbssc.ensilink.R;

import fr.ensicaen.lbssc.ensilink.storage.Club;
import fr.ensicaen.lbssc.ensilink.storage.School;
import fr.ensicaen.lbssc.ensilink.storage.Union;


/**
 * Created by marsel on 05/03/17.
 */

public class SettingsActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedStateInstance) {
        super.onCreate(savedStateInstance);
        if (Build.VERSION.SDK_INT >= 23) {
            getWindow().setStatusBarColor(getColor(R.color.colorPrimaryDark));
        } else if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new PrefsFragment()).commit();

    }

        public static class PrefsFragment extends PreferenceFragment {

            @Override
            public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                addPreferencesFromResource(R.xml.settings_activity);


                final PreferenceScreen screen = this.getPreferenceScreen();

                PreferenceCategory cat = new PreferenceCategory(screen.getContext());
                cat.setTitle("Notifications");
                screen.addPreference(cat);

                for (final Union union : School.getInstance().getUnions()) {
                    final Preference pref = new Preference(screen.getContext());
                    pref.setTitle(union.getName());
                    cat.addPreference(pref);
                    pref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                        public boolean onPreferenceClick(Preference preference) {
                            Log.d("aaa","avant get preferenceScreen");
                            PreferenceScreen screenClub = getPreferenceManager().createPreferenceScreen(screen.getContext());
                            Log.d("aaa","apres get preferenceScreen");
                            final PreferenceCategory cate = new PreferenceCategory(screenClub.getContext());
                            Log.d("aaa",union.getName());
                            cate.setTitle(union.getName());
                            screenClub.addPreference(cate);


                            for (Club club : union.getClubs()) {
                                Log.d("ddd","dans le for");
                                Log.d("ddd",club.getName());
                                final SwitchPreference prefClub = new SwitchPreference(screenClub.getContext());
                                prefClub.setTitle(club.getName());
                                cate.addPreference(prefClub);

                            }
                            return true;
                        }
                    });
                }

            }
        }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    }