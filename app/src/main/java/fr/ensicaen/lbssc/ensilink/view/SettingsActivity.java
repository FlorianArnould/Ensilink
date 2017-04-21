package fr.ensicaen.lbssc.ensilink.view;




import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;


import android.preference.MultiSelectListPreference;

import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;

import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;


import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;

import android.util.Log;
import android.view.MenuItem;

import fr.ensicaen.lbssc.ensilink.R;

import fr.ensicaen.lbssc.ensilink.storage.Club;
import fr.ensicaen.lbssc.ensilink.storage.School;
import fr.ensicaen.lbssc.ensilink.storage.Union;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;


/**
 * Created by marsel on 05/03/17.
 */

public class SettingsActivity extends AppCompatActivity {
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

    public static class PrefsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_activity);


            final PreferenceScreen screen = this.getPreferenceScreen();
            PreferenceCategory cat = (PreferenceCategory) findPreference("Notifs");
            screen.addPreference(cat);

            for (final Union union : School.getInstance().getUnions()) {

                final MultiSelectListPreference pref = new MultiSelectListPreference(screen.getContext());
                pref.setTitle(union.getName());
                pref.setDialogTitle(union.getName());

                int i=0;
                String entries[] = new String[union.getClubs().size()];
                CharSequence entryValues[] = new String[union.getClubs().size()];

                for (Club club : union.getClubs()) {
                    entries[i] = club.getName();
                    entryValues[i] = Integer.toString(i);
                    i++;
                }
                pref.setEntries(entries);
                pref.setEntryValues(entryValues);
                cat.addPreference(pref);

            }
        }


        @Override
        public void onResume() {
            super.onResume();
            getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {

        }
    }




    public ActionBar getSupportActionBar() {
        return getDelegate().getSupportActionBar();
    }

    public AppCompatDelegate getDelegate() {
        if (mDelegate == null) {
            mDelegate = AppCompatDelegate.create(this, null);
        }
        return mDelegate;
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



