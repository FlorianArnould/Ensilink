package fr.ensicaen.lbssc.ensilink.view;




import android.os.Build;
import android.os.Bundle;

import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;

import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatDelegate;
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
    private AppCompatDelegate mDelegate;

    @Override
    protected void onCreate(Bundle savedStateInstance) {
        super.onCreate(savedStateInstance);
        if (Build.VERSION.SDK_INT >= 23) {
            getWindow().setStatusBarColor(getColor(R.color.colorPrimaryDark));
        } else if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        getSupportActionBar().setTitle("Paramètres");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new PrefsFragment()).commit();
    }

    public static class PrefsFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_activity);

            final PreferenceScreen screen = this.getPreferenceScreen();
            PreferenceCategory cat = (PreferenceCategory) findPreference("Notifs");
            screen.addPreference(cat);
            for (final Union union : School.getInstance().getUnions()) {
                final Preference pref = new Preference(screen.getContext());
                pref.setTitle(union.getName());
                cat.addPreference(pref);



                pref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    public boolean onPreferenceClick(Preference preference) {
                        PreferenceScreen screenClub = getPreferenceManager().createPreferenceScreen(getActivity());
                        setPreferenceScreen(screenClub);
                        final PreferenceCategory cate = new PreferenceCategory(screenClub.getContext());
                        cate.setTitle(union.getName());
                        screenClub.addPreference(cate);

                        for (Club club : union.getClubs()) {

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



    public ActionBar getSupportActionBar() {
        return getDelegate().getSupportActionBar();
    }

    private AppCompatDelegate getDelegate() {
        if (mDelegate == null) {
            mDelegate = AppCompatDelegate.create(this, null);
        }
        return mDelegate;
    }
}



