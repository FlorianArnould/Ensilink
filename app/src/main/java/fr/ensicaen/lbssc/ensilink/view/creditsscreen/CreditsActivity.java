package fr.ensicaen.lbssc.ensilink.view.creditsscreen;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import fr.ensicaen.lbssc.ensilink.storage.Image;
import fr.ensicaen.lbssc.ensilink.storage.School;

/**
 * @author Florian Arnould
 * @version 1.0
 */

/**
 * Page to display developers and images creators attributions
 */
public final class CreditsActivity extends AppCompatActivity {

    @Override
    @SuppressWarnings("deprecation") //For retro compatibility
    protected void onCreate(Bundle savedStateInstance){
        super.onCreate(savedStateInstance);
        setContentView(R.layout.credits_activity);
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

    /**
     * Adapter to store the rows with the attributions
     */
    private final class CreditsAdapter extends BaseAdapter {

        private final List<Object> _rowContent;

        CreditsAdapter(){
            _rowContent = new ArrayList<>();
            _rowContent.add(getString(R.string.developers));
            String team[] = getResources().getStringArray(R.array.team_2016_2017);
            _rowContent.add(new Developer(R.drawable.ic_manager, getString(R.string.manager), team[0]));
            for(int i=1;i<team.length;i++){
                _rowContent.add(new Developer(R.drawable.ic_developer, getString(R.string.developer), team[i]));
            }
            _rowContent.add(getString(R.string.attributions));
            _rowContent.add(new Artist(getDrawable(R.mipmap.ic_launcher), getString(R.string.application_icon)));
            _rowContent.add(new Artist(getDrawable(R.drawable.ic_manager), getString(R.string.manager_icon)));
            _rowContent.add(new Artist(getDrawable(R.drawable.ic_developer), getString(R.string.developer_icon)));
            for(Image image : School.getInstance().getImages()){
                if(image.needsAttribution()){
                    _rowContent.add(new Artist(image));
                }
            }
        }

        @Override
        public int getCount() {
            return _rowContent.size();
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
        public int getItemViewType(int position){
            Log.d("Debug", String.valueOf(position));
            if(position == 0){
                return 0;
            }else if(position > 0 && position < 4){
                return 1;
            }else if(position == 4){
                return 0;
            }else{
                return 2;
            }
        }

        @Override
        public int getViewTypeCount(){
            return 3;
        }

        @Override
        public View getView(int i, View view, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            TextView text;
            ImageView image;
            Log.d("Debug", String.valueOf(getItemViewType(i)));
            switch (getItemViewType(i)){
                case 0:
                    view = inflater.inflate(R.layout.credits_title_row, parent, false);
                    text = (TextView) view.findViewById(R.id.title);
                    text.setText((String)_rowContent.get(i));
                    break;
                case 1:
                    view = inflater.inflate(R.layout.credits_developer_row, parent, false);
                    Developer dev = (Developer) _rowContent.get(i);
                    image = (ImageView)view.findViewById(R.id.icon);
                    image.setImageResource(dev.getDrawableId());
                    text = (TextView)view.findViewById(R.id.position);
                    text.setText(dev.getPosition());
                    text = (TextView)view.findViewById(R.id.name);
                    text.setText(dev.getName());
                    break;
                default:
                    view = inflater.inflate(R.layout.credits_attribution_row, parent, false);
                    Artist artist = (Artist) _rowContent.get(i);
                    image = (ImageView)view.findViewById(R.id.icon);
                    image.setImageDrawable(artist.getDrawable());
                    text = (TextView)view.findViewById(R.id.attribution);
                    text.setText(artist.getAttribution());
            }
            return view;
        }
    }
}
