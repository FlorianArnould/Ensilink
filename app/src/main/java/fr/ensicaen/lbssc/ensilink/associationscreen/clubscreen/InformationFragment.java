package fr.ensicaen.lbssc.ensilink.associationscreen.clubscreen;

/**
 * @author Florian Arnould
 * @version 1.0
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import fr.ensicaen.lbssc.ensilink.R;
import fr.ensicaen.lbssc.ensilink.storage.Club;
import fr.ensicaen.lbssc.ensilink.storage.School;


public class InformationFragment extends Fragment {

    private int _unionId;
    private int _clubId;

    public static InformationFragment newInstance(int unionId, int clubId){
        InformationFragment informationFragment = new InformationFragment();
        Bundle args = new Bundle();
        args.putInt("UNION_ID", unionId);
        args.putInt("CLUB_ID", clubId);
        informationFragment.setArguments(args);
        return informationFragment;
    }

    public InformationFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _unionId = getArguments().getInt("UNION_ID");
        _clubId = getArguments().getInt("CLUB_ID");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.information_fragment, container, false);
        Club club = School.getInstance().getUnion(_unionId).getClub(_clubId);
        TextView text = (TextView) view.findViewById(R.id.place);
//        text.setText(club.getPlace());
//        text = (TextView) view.findViewById(R.id.date_club);
//        text.setText(club.getDate());
//        text = (TextView) view.findViewById(R.id.hours);
//        text.setText(club.getHours());
//        ImageView image = (ImageView) view.findViewById(R.id.logo);
//        image.setImageDrawable(club.getLogo());
//

//
//        _students=club.getStudents();
//
//        text = (TextView) view.findViewById(R.id.namePrez);
//        text.setText(_students[1].getName());
//
//
//        text = (TextView) view.findViewById(R.id.mailPrez);
//        text.setText(_students[1].getMail());
//
//        text = (TextView) view.findViewById(R.id.nameVicePrez);
//        text.setText(_students[2].getName());
//
//        text = (TextView) view.findViewById(R.id.mailVicePrez);
//        text.setText(_students[2].getName());

        return view;
    }
}
