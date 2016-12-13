package fr.ensicaen.lbssc.ensilink;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;


import fr.ensicaen.lbssc.ensilink.storage.Event;
import fr.ensicaen.lbssc.ensilink.storage.School;

public class MainActivity extends DrawerActivity{

    EventAdapter _adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("D", String.valueOf(School.getInstance().getEvents().size()));
        _adapter = new EventAdapter(School.getInstance().getEvents());
        ListView list = (ListView) findViewById(R.id.list_view);
        list.setAdapter(_adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent myIntent = new Intent(view.getContext(), ListItemActivity2.class);
                startActivity(myIntent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            moveTaskToBack(true);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()){
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDataRefreshed() {
        _adapter.update(School.getInstance().getEvents());
    }

    final class EventAdapter extends BaseAdapter {

        List<Event> _events;

        EventAdapter(List<Event> events){
            super();
            update(events);
        }

        void update(List<Event> events){
            _events = events;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return _events.size();
        }

        @Override
        public Object getItem(int i) {
            return _events.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup parent) {
            if(view == null) {
                LayoutInflater inflater = (LayoutInflater) MainActivity.this.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.main_event_row, parent, false);
            }
            Event event = _events.get(i);
            ImageView image = (ImageView) view.findViewById(R.id.listview_image);
            image.setImageBitmap(event.getParentUnion().getLogo());
            TextView title = (TextView) view.findViewById(R.id.listview_item_title);
            title.setText(event.getTitle());
            TextView description = (TextView) view.findViewById(R.id.listview_item_short_description);
            String shortDescription = event.getMainText();
            if(shortDescription.length() > 20){
                shortDescription = shortDescription.substring(0,20) + "...";
            }
            description.setText(shortDescription);
            return view;
        }
    }


}
