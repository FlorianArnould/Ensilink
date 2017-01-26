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
        return inflater.inflate(R.layout.mails_union, container, false);
    }

    @Override
    public void update() {

    }
}
