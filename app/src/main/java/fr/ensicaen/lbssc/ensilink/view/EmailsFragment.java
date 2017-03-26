package fr.ensicaen.lbssc.ensilink.view;



//TODO feature will be implemented in the next released

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import fr.ensicaen.lbssc.ensilink.R;
import fr.ensicaen.lbssc.ensilink.storage.Mail;


/**
 * @author Marsel Arik
 * @author Jérémy Filipozzi
 * @version 2.0
 */

/**
 * Class which display the screen of a the mails of an union
 * The real implementation will come in the second version
 */
public class EmailsFragment extends AssociationFragment {

    private MailAdapter _adapter;

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
    public void update() { /*IN LINE*/}

    private final class MailAdapter extends BaseAdapter {

        List<Mail> _mails;

        /**
         * Constructor of the class
         * @param mails a list of mails
         */
        MailAdapter(List<Mail> mails){
            super();
            update(mails);
        }

        /**
         * Replace the old list of mails by adding the new ones
         * @param mails a list mails to add to the ListView
         */
        void update(List<Mail> mails){
            _mails = mails;
            notifyDataSetChanged();
        }

        @Override
        public int getCount(){ return _mails.size(); }

        @Override
        public Object getItem(int i) { return _mails.get(i); }

        @Override
        public long getItemId(int i) { return 0; }

        @Override
        public View getView(int i, View view, ViewGroup parent) {
            if(view == null){
                LayoutInflater inflater = (LayoutInflater) EmailsFragment.this.getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.mails_union,parent,false);
            }
            return view;
        }
    }
}
