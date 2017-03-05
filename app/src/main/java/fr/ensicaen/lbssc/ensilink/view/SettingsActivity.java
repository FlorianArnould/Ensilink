package fr.ensicaen.lbssc.ensilink.view;


import android.content.Context;

import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import android.widget.ListView;
import android.widget.TextView;



import java.util.ArrayList;
import java.util.List;

import fr.ensicaen.lbssc.ensilink.R;
import fr.ensicaen.lbssc.ensilink.storage.Association;
import fr.ensicaen.lbssc.ensilink.storage.Club;
import fr.ensicaen.lbssc.ensilink.storage.School;
import fr.ensicaen.lbssc.ensilink.storage.Union;


/**
 * Created by marsel on 05/03/17.
 */

public final class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedStateInstance){
        super.onCreate(savedStateInstance);
        setContentView(R.layout.settings_activity);

        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle(getString(R.string.credits));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        ListView list = (ListView) findViewById(R.id.list_parameters);
        list.setAdapter(new ClubsParametersAdapter());
    }


    final private class ClubsParametersAdapter extends BaseAdapter {

        private final List<Association> _rowContent;
        private final List<Integer> _positionUnion;
        ClubsParametersAdapter(){
            _rowContent = new ArrayList<>();
            _positionUnion=new ArrayList<>();
            int _position;
            _position=0;

            for(Union union : School.getInstance().getUnions()){

                _rowContent.add(union);
                _positionUnion.add(_position);
                Log.d("Debug", String.valueOf(_position));
                _position++;
                Log.d("Debug", union.getName()+" "+union.getClubs().size());
                for(Club club : union.getClubs()){
                    _rowContent.add(club);
                    _position++;
                }
            }
        }

        @Override
        public Object getItem(int i) {
            return _rowContent.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public int getCount() {
            return _rowContent.size();
        }

        @Override
        public int getItemViewType(int position){
            if(_positionUnion.contains(position)){
                return 1;
            }else
                return 2;
            }


        public View getView(int i, View view, ViewGroup parent) {
            TextView text;
            LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            Log.d("Debug", String.valueOf(i));
            switch (getItemViewType(i)) {
                case 1:
                    view = inflater.inflate(R.layout.settings_club_row, parent, false);
                    break;
                case 2:
                    view = inflater.inflate(R.layout.settings_club_row, parent, false);
                    break;
            }
            text = (TextView) view.findViewById(R.id.list_parameters_club);
            text.setText(_rowContent.get(i).getName());
            return view;
        }

    }
}
