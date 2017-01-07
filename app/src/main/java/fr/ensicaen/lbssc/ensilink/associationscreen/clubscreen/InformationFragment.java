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

import fr.ensicaen.lbssc.ensilink.R;


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
        return view;
    }
}
