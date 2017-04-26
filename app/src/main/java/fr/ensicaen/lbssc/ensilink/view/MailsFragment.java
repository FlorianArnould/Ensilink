/**
 * This file is part of Ensilink.
 *
 * Ensilink is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * Ensilink is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Ensilink.
 * If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright, The Ensilink team :  ARNOULD Florian, ARIK Marsel, FILIPOZZI Jérémy,
 * ENSICAEN, 6 Boulevard du Maréchal Juin, 26 avril 2017
 *
 */

package fr.ensicaen.lbssc.ensilink.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import fr.ensicaen.lbssc.ensilink.R;
import fr.ensicaen.lbssc.ensilink.storage.Association;
import fr.ensicaen.lbssc.ensilink.storage.Mail;
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
public class MailsFragment extends ListFragment implements Updatable {

        private MailAdapter _adapter;
        private Association _association;
        private int _unionId;
        private int _clubId;

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
            mails._association = School.getInstance().getUnion(unionId);
            mails._unionId = unionId;
            mails._clubId = -1;
            return mails;
        }

        /**
         * create an instance of InformationFragment
         * @return return the list of the mails
         */
        public static MailsFragment newInstance(int unionId, int clubId) {
            MailsFragment mails = new MailsFragment();
            mails._association = School.getInstance().getUnion(unionId).getClub(clubId);
            mails._unionId = unionId;
            mails._clubId = clubId;
            return mails;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            _adapter = new MailAdapter(_association.getMails());
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
                    Intent myIntent = new Intent(view.getContext(), EmailsActivity.class);
                    myIntent.putExtra("UNION_ID", _unionId);
                    myIntent.putExtra("CLUB_ID", _clubId);
                    myIntent.putExtra("MAIL_ID", position);
                    startActivity(myIntent);
                }
            });
            if(getActivity() instanceof MainActivity) {
                list.setOnScrollListener((MainActivity) getActivity());
            }
            update();
        }

        public void changeAssociation(Association association){
            _association = association;
        }

        @Override
        public void update() {
            if (_adapter != null) {
                _adapter.update(_association.getMails());
            }
        }

        private final class MailAdapter extends BaseAdapter {

            List<Mail> _mails;

            /**
             * Constructor of the class
             * @param mails a list of mails
             */
            MailAdapter(List<Mail> mails){
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
                    LayoutInflater inflater = (LayoutInflater) MailsFragment.this.getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    view = inflater.inflate(R.layout.emails_row,parent,false);
                }
                Mail mail = _mails.get(i);
                TextView mailSubject = (TextView) view.findViewById(R.id.email_subject);
                mailSubject.setText(mail.getSubject());
                TextView mailSender = (TextView) view.findViewById(R.id.email_sender);
                mailSender.setText(mail.getTransmitter());
                TextView mailText = (TextView) view.findViewById(R.id.email_content);
                mailText.setText(mail.getDate());
                return view;
            }
        }
}
