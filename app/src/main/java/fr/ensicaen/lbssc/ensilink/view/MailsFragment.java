package fr.ensicaen.lbssc.ensilink.view;

import android.content.Context;
import android.content.Intent;
import android.os.*;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import javax.mail.*;
import javax.mail.Message;

import java.io.IOException;
import java.util.List;

import fr.ensicaen.lbssc.ensilink.R;
import fr.ensicaen.lbssc.ensilink.storage.School;


/**
 * @author Marsel Arik
 * @author Jérémy Filipozzi
 * @version 2.0
 */

/**
 * Class which display the screen of a the mails of an union
 * The real implementation will come in the second version
 */
public class MailsFragment extends AssociationFragment implements  Updatable {

        private MailAdapter _adapter;

        /**
         * Required empty public constructor
         */
        public MailsFragment() {

        }

        /**
         * create an instance of InformationFragment
         * @return return the list of the mails
         */
        public static MailsFragment newInstance(int unionId) {
            MailsFragment mails = new MailsFragment();
            AssociationFragment.newInstance(unionId, mails);
            return mails;
        }


        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            _adapter = new MailAdapter(School.getInstance().getMails());
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.emails_union, container, false);
        }

        @Override
        public void onActivityCreated(Bundle savedStateInstance){
            super.onActivityCreated(savedStateInstance);
            ListView list = getListView();
            list.setAdapter(_adapter);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent myIntent = new Intent(view.getContext(), LoginActivity.class);
                    myIntent.putExtra("MAIL_ID", position);
                    startActivity(myIntent);
                }
            });
            list.setOnScrollListener((MainActivity)getActivity());
            update();
        }

        @Override
        public void update() { _adapter.update(School.getInstance().getMails());}

        private final class MailAdapter extends BaseAdapter {

            List<Message> _mails;

            /**
             * Constructor of the class
             * @param mails a list of mails
             */
            MailAdapter(List<Message> mails){
                super();
                update(mails);
            }

            /**
             * Replace the old list of mails by adding the new ones
             * @param mails a list mails to add to the ListView
             */
            void update(List<Message> mails){
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
                    LayoutInflater inflater = (LayoutInflater) MailsFragment.this.getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    view = inflater.inflate(R.layout.emails_row,parent,false);
                }
                Message mail = _mails.get(i);
                TextView mailSubject = (TextView) view.findViewById(R.id.email_subject);
                try {
                    mailSubject.setText(mail.getSubject());//À compléter
                } catch (MessagingException e) {
                    Log.d("DEBUG","Problème avec la récupération du sujet");
                }
                TextView mailSender = (TextView) view.findViewById(R.id.email_sender);
                try {
                    String sender = mail.getFrom().toString();
                    mailSender.setText(sender);
                } catch (MessagingException e) {
                    Log.d("DEBUG","Problème avec la récupération du nom de l'expéditeur");
                }
                TextView mailText = (TextView) view.findViewById(R.id.email_content);
                String mailContent = null;
                try {
                    mailContent = mail.getContent().toString();
                } catch (IOException e) {
                    Log.d("DEBUG","Problème avec le contenu du message");
                } catch (MessagingException e) {
                    Log.d("DEBUG","Problème avec la récupération du contenu du mail");
                }
                if (mailContent.length() > 35){
                    mailContent = mailContent.substring(0,35) + "...";
                }
                mailText.setText(mailContent);
                return view;
            }
        }
}
