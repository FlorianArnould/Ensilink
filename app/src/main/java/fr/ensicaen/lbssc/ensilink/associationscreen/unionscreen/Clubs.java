package fr.ensicaen.lbssc.ensilink.associationscreen.unionscreen;

/**
 * @author Marsel Arik
 * @version 1.0
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import fr.ensicaen.lbssc.ensilink.R;
import fr.ensicaen.lbssc.ensilink.associationscreen.SuperFragment;
import fr.ensicaen.lbssc.ensilink.associationscreen.clubscreen.ClubActivity;
import fr.ensicaen.lbssc.ensilink.storage.Club;

public class Clubs extends SuperFragment {

    ClubsAdapter _adapter;

    public static Clubs newInstance(int unionId) {
        Clubs clubs = new Clubs();
        SuperFragment.newInstance(unionId, clubs);
        return clubs;
    }

    public Clubs() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _adapter = new ClubsAdapter(getUnion().getClubs());
    }

    @Override
    protected void update() {
        if (_adapter != null) {
            _adapter.update(getUnion().getClubs());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.clubs_union, container, false);
        ListView list = (ListView) view.findViewById(R.id.list_view_club);
        list.setAdapter(_adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent myIntent = new Intent(view.getContext(), ClubActivity.class);
                myIntent.putExtra("UNION_ID", getUnionId());
                myIntent.putExtra("CLUB_ID", i);
                startActivity(myIntent);
            }
        });
        update();
        return view;
    }

    final class ClubsAdapter extends BaseAdapter {

        List<Club> _clubs;

        ClubsAdapter(List<Club> clubs) {
            super();
            update(clubs);
        }

        void update(List<Club> clubs) {
            _clubs = clubs;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return _clubs.size();
        }

        @Override
        public Object getItem(int i) {
            return _clubs.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup parent) {
            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) Clubs.this.getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.union_clubs_row, parent, false);
            }
            Club club = _clubs.get(i);
            TextView text = (TextView) view.findViewById(R.id.listview_union_name_club);
            text.setText(club.getName());

            Drawable image = club.getDrawableLogo();

            image.setBounds(0, 0, 150, 150);
            text.setCompoundDrawables(image, null, null, null);

            return view;
        }
    }
}

