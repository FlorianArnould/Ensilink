package fr.ensicaen.lbssc.ensilink;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import fr.ensicaen.lbssc.ensilink.storage.Event;
import fr.ensicaen.lbssc.ensilink.storage.OnImageLoadedListener;
import fr.ensicaen.lbssc.ensilink.storage.School;

/**
 * @author Florian Arnould
 * @version 1.0
 */

public class EventFragment extends Fragment {

    EventAdapter _adapter;

    public EventFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _adapter = new EventAdapter(School.getInstance().getEvents());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.event_fragment, container, false);
        MainActivity activity = (MainActivity) getActivity();
        activity.setActionBarTitle(getActivity().getResources().getString(R.string.app_name));
        if(android.os.Build.VERSION.SDK_INT >= 23){
            activity.setActionBarColor(new ColorDrawable(getResources().getColor(R.color.colorPrimary, null)));
            activity.getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark, null));
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        }else{
            activity.setActionBarColor(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));
            if(android.os.Build.VERSION.SDK_INT >= 21){
                activity.getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
            }
        }

        ListView list = (ListView) view.findViewById(R.id.list_view);
        list.setAdapter(_adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent myIntent = new Intent(view.getContext(), EventActivity.class);
                startActivity(myIntent);
            }
        });

        return view;
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
                LayoutInflater inflater = (LayoutInflater) EventFragment.this.getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.event_row, parent, false);
            }
            Event event = _events.get(i);
            final ImageView image = (ImageView) view.findViewById(R.id.listview_image);
            event.getParentUnion().loadLogo(new OnImageLoadedListener() {
                @Override
                public void OnImageLoaded(final Drawable logo) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            image.setImageDrawable(logo);
                        }
                    });
                }
            });
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
