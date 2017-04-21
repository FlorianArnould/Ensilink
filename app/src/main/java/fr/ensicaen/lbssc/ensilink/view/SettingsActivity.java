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

import java.util.List;

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
    private PrefsFragment fragment;

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
        fragment = new PrefsFragment();
        getFragmentManager().beginTransaction().replace(android.R.id.content, fragment).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        if(fragment.isOnClubs()){
            fragment.setOnUnion();
        }else{
            finish();
        }
    }

    public static class PrefsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

        private PreferenceScreen unionPreference;
        private boolean isOnClubs;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_activity);

            final PreferenceScreen screen = this.getPreferenceScreen();
            PreferenceCategory cat = (PreferenceCategory) findPreference("Notifs");
            screen.addPreference(cat);

            for (final Union union : School.getInstance().getUnions()) {

                final MultiSelectListPreference pref = new MultiSelectListPreference(screen.getContext());
            isOnClubs = false;
            unionPreference = this.getPreferenceScreen();
            PreferenceCategory cat = (PreferenceCategory) findPreference("Notifs");
            unionPreference.addPreference(cat);
            for (final Union union : School.getInstance().getUnions()) {
                final Preference pref = new Preference(unionPreference.getContext());
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
                        isOnClubs = true;
                        return true;
                    }
                });
            }
        }

        boolean isOnClubs(){
            return isOnClubs;
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

        void setOnUnion(){
            setPreferenceScreen(unionPreference);
            isOnClubs = false;
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



