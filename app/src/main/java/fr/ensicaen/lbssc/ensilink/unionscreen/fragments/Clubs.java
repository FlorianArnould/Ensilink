package fr.ensicaen.lbssc.ensilink.unionscreen.fragments;

/**
 * Created by marsel on 27/11/16.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fr.ensicaen.lbssc.ensilink.R;

public class Clubs extends Fragment {



    public Clubs() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.clubs_union, container, false);
    }

}