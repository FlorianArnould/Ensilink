package fr.ensicaen.lbssc.ensilink.creditsscreen;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fr.ensicaen.lbssc.ensilink.R;

/**
 * @author Florian Arnould
 * @version 1.0
 */

public final class CreditsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedStateInstance){
        super.onCreate(savedStateInstance);
        setContentView(R.layout.activity_credits);
        if(Build.VERSION.SDK_INT >= 23){
            getWindow().setStatusBarColor(getColor(R.color.colorPrimaryDark));
        }else if(Build.VERSION.SDK_INT >= 21){
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle(getString(R.string.credits));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        ListView list = (ListView) findViewById(R.id.list);
        list.setAdapter(new CreditsAdapter());
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

    private final class CreditsAdapter extends BaseAdapter {

        private final List<Integer> _rowLayout;
        private final List<Object> _rowContent;

        CreditsAdapter(){
            _rowLayout = new ArrayList<>();
            _rowContent = new ArrayList<>();
            _rowLayout.add(R.layout.credits_title_row);
            _rowContent.add(getString(R.string.developers));
            _rowLayout.add(R.layout.credits_developer_row);
            String team[] = getResources().getStringArray(R.array.team_2016_2017);
            _rowContent.add(new Developer(R.drawable.ic_manager, getString(R.string.manager), team[0]));
            for(int i=1;i<team.length;i++){
                _rowLayout.add(R.layout.credits_developer_row);
                _rowContent.add(new Developer(R.drawable.ic_developer, getString(R.string.developer), team[i]));
            }
            _rowLayout.add(R.layout.credits_title_row);
            _rowContent.add(getString(R.string.attributions));
            _rowLayout.add(R.layout.credits_attribution_row);
            _rowContent.add(new Artist(R.mipmap.ic_launcher, R.string.application_icon));
            _rowLayout.add(R.layout.credits_attribution_row);
            _rowContent.add(new Artist(R.drawable.ic_manager, R.string.manager_icon));
            _rowLayout.add(R.layout.credits_attribution_row);
            _rowContent.add(new Artist(R.drawable.ic_developer, R.string.developer_icon));
        }

        @Override
        public int getCount() {
            return _rowLayout.size();
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
        public View getView(int i, View view, ViewGroup parent) {
            if(view == null) {
                LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                view = inflater.inflate(_rowLayout.get(i), parent, false);
            }
            TextView text;
            ImageView image;
            switch (_rowLayout.get(i)){
                case R.layout.credits_title_row:
                    text = (TextView) view.findViewById(R.id.title);
                    text.setText((String)_rowContent.get(i));
                    break;
                case R.layout.credits_developer_row:
                    Developer dev = (Developer) _rowContent.get(i);
                    image = (ImageView)view.findViewById(R.id.icon);
                    image.setImageResource(dev.getDrawableId());
                    text = (TextView)view.findViewById(R.id.position);
                    text.setText(dev.getPosition());
                    text = (TextView)view.findViewById(R.id.name);
                    text.setText(dev.getName());
                    break;
                case R.layout.credits_attribution_row:
                    Artist artist = (Artist) _rowContent.get(i);
                    image = (ImageView)view.findViewById(R.id.icon);
                    image.setImageResource(artist.getDrawableId());
                    text = (TextView)view.findViewById(R.id.attribution);
                    text.setText(getText(artist.getAttribution()));
                    break;
            }
            return view;
        }
    }
}
