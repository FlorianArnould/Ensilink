package fr.ensicaen.lbssc.ensilink.associationscreen.clubscreen;

/**
 * @author Florian Arnould
 * @version 1.0
 */

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import fr.ensicaen.lbssc.ensilink.R;
import fr.ensicaen.lbssc.ensilink.storage.Club;
import fr.ensicaen.lbssc.ensilink.storage.OnImageLoadedListener;
import fr.ensicaen.lbssc.ensilink.storage.School;
import fr.ensicaen.lbssc.ensilink.storage.Student;


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
        text.setText(club.getPlace());
        text = (TextView) view.findViewById(R.id.date_club);
        if(club.getDayOfWeek(getContext()) != null) {
            text.setText(club.getDayOfWeek(getContext()));
        }else if(club.getDate() != null){
            text.setText(club.getDate().toString());
        }else{
            text.setText(getString(R.string.unknown));
        }
        text = (TextView) view.findViewById(R.id.hours);
        if(club.getTime() != null) {
            String str = club.getTime().toString() + "-";
            if(club.getDuration() != null){
                str += club.getTime().add(club.getDuration()).toString();
            }else{
                str += getString(R.string.unknown);
            }
            text.setText(str);
        }else{
            text.setText(getString(R.string.unknown));
        }
        final ImageView imageView = (ImageView) view.findViewById(R.id.logo);
        club.loadLogo(new OnImageLoadedListener() {
            @Override
            public void OnImageLoaded(final Drawable image) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        imageView.setImageDrawable(image);
                    }
                });
            }
        });


        List<Student> students = club.getStudents();

        text = (TextView) view.findViewById(R.id.namePrez);
        text.setText(students.get(0).getName());


        text = (TextView) view.findViewById(R.id.mailPrez);
        text.setText(students.get(0).getEmail());

        text = (TextView) view.findViewById(R.id.nameVicePrez);
        text.setText(students.get(1).getName());

        text = (TextView) view.findViewById(R.id.mailVicePrez);
        text.setText(students.get(1).getName());

        return view;
    }
}
