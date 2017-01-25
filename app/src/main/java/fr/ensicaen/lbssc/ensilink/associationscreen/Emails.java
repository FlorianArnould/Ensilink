package fr.ensicaen.lbssc.ensilink.associationscreen;



//TODO functionality will be implemented in the next released

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fr.ensicaen.lbssc.ensilink.R;


/**
 * @author Marsel Arik
 * @version 1.0
 */

/**
 * Class which display the screen of a the mails of an union
 */

public class Emails extends AssociationFragment {

    /**
     * Create an object Emails
     * @return return the list of the mails
     */
    public static Emails newInstance(int unionId) {
        Emails mails = new Emails();
        AssociationFragment.newInstance(unionId, mails);
        return mails;
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
