package fr.ensicaen.lbssc.ensilink.view.settingsscreen;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fr.ensicaen.lbssc.ensilink.R;
import fr.ensicaen.lbssc.ensilink.storage.Club;
import fr.ensicaen.lbssc.ensilink.storage.School;

/**
 * @author Florian Arnould
 * @version 1.0
 */

/**
 * Screen which display preferences for clubs
 */
public class SettingsClubActivity extends AppCompatActivity {

    private SharedPreferences _pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_club_activity);
        _pref = SettingsClubActivity.this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            getWindow().setStatusBarColor(getColor(R.color.colorPrimaryDark));
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setElevation(0);
        }
        ListView list = (ListView)findViewById(R.id.list);
        list.setAdapter(new ClubsAdapter());
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //TODO change The Switch position
            }
        });
        String unionName = School.getInstance().getUnion(getIntent().getIntExtra("UNION_POSITION", 0)).getName();
        TextView text = (TextView) findViewById(R.id.union_name);
        text.setText(unionName);
        Switch switchButton = (Switch)findViewById(R.id.union_switch);
        switchButton.setOnCheckedChangeListener(new OnCheckedChangeListener(unionName));
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

    /**
     * Adapter for the rows of the list view
     */
    private class ClubsAdapter extends BaseAdapter {

        private List<String> _clubs;
        private boolean _switchButtonsInitialized[];

        ClubsAdapter(){
            _clubs = new ArrayList<>();
            for(Club club : School.getInstance().getUnion(getIntent().getIntExtra("UNION_POSITION", 0)).getClubs()){
                _clubs.add(club.getName());
            }
            _switchButtonsInitialized = new boolean[_clubs.size()];
            Arrays.fill(_switchButtonsInitialized, false);
        }

        @Override
        public int getCount() {
            return _clubs.size();
        }

        @Override
        public Object getItem(int position) {
            return _clubs.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            if(view == null) {
                LayoutInflater inflater = (LayoutInflater) SettingsClubActivity.this.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.settings_club_row, parent, false);
            }
            TextView text = (TextView) view.findViewById(R.id.union_name);
            text.setText(_clubs.get(position));
            if(!_switchButtonsInitialized[position]) {
                SwitchCompat switchButton = (SwitchCompat) view.findViewById(R.id.switch_button);
                switchButton.setChecked(_pref.getBoolean(_clubs.get(position), false));
                switchButton.setOnCheckedChangeListener(new OnCheckedChangeListener(_clubs.get(position)));
            }
            return view;
        }
    }

    /**
     * listener use to store the associationName to change the shared preferences of the application
     */
    private class OnCheckedChangeListener implements CompoundButton.OnCheckedChangeListener{

        private String _associationName;

        OnCheckedChangeListener(String associationName){
            _associationName = associationName;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            _pref.edit().putBoolean(_associationName, isChecked).apply();
        }
    }
}
