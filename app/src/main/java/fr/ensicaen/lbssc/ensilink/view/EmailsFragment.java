package fr.ensicaen.lbssc.ensilink.view;



//TODO feature will be implemented in the next released

import android.graphics.Color;
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
 * The real implementation will come in the second version
 */
public class EmailsFragment extends AssociationFragment {

    /**
     * create an instance of InformationFragment
     * @return return the list of the mails
     */
    public static EmailsFragment newInstance(int unionId) {
        EmailsFragment mails = new EmailsFragment();
        AssociationFragment.newInstance(unionId, mails);
        return mails;
    }

    /**
     * Required empty public constructor
     */
    public EmailsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mails_union, container, false);
        MainActivity activity = (MainActivity) getActivity();
        activity.setActionBarTitle("Mails");
        return view;

    }

    @Override
    public void update() {

    }
}
