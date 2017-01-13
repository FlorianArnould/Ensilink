package fr.ensicaen.lbssc.ensilink.associationscreen;

/**
 * @author Marsel Arik
 * @version 1.0
 */

//TODO functionality will be implemented in the next released

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fr.ensicaen.lbssc.ensilink.R;

public class Emails extends AssociationFragment {

    public static Emails newInstance(int unionId) {
        Emails clubs = new Emails();
        AssociationFragment.newInstance(unionId, clubs);
        return clubs;
    }

    public Emails() {
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
        return inflater.inflate(R.layout.mails_union, container, false);
    }

    @Override
    public void update() {

    }
}
