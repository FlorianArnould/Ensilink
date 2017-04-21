package fr.ensicaen.lbssc.ensilink.view;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fr.ensicaen.lbssc.ensilink.R;
import fr.ensicaen.lbssc.ensilink.storage.School;
import fr.ensicaen.lbssc.ensilink.storage.Union;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            getWindow().setStatusBarColor(getColor(R.color.colorPrimaryDark));
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        ListView list = (ListView)findViewById(R.id.list);
        list.setAdapter(new UnionsAdapter());
    }

    private class UnionsAdapter extends BaseAdapter{

        private List<String> _unions;

        UnionsAdapter(){
            _unions = new ArrayList<>();
            for(Union union : School.getInstance().getUnions()){
                _unions.add(union.getName());
            }
        }

        @Override
        public int getCount() {
            return _unions.size();
        }

        @Override
        public Object getItem(int position) {
            return _unions.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            if(view == null) {
                LayoutInflater inflater = (LayoutInflater) SettingsActivity.this.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.settings_union_row, parent, false);
            }
            TextView text = (TextView) view.findViewById(R.id.union_name);
            text.setText(_unions.get(position));
            return view;
        }
    }
}
