package fr.ensicaen.lbssc.ensilink.associationscreen.unionscreen;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import fr.ensicaen.lbssc.ensilink.MainActivity;
import fr.ensicaen.lbssc.ensilink.R;
import fr.ensicaen.lbssc.ensilink.associationscreen.AssociationFragment;
import fr.ensicaen.lbssc.ensilink.storage.OnImageLoadedListener;
import fr.ensicaen.lbssc.ensilink.storage.Student;

/**
 * @author Marsel Arik
 * @version 1.0
 */

/**
 * Fragment used to display the screen of a the members of the union
 */
public class MembersFragment extends AssociationFragment {

    private StudentAdapter _adapter;
    private View _view;

    /**
     * Create an object MembersFragment
     * @return the list of members of an union
     */
    public static MembersFragment newInstance(int unionId){
        MembersFragment membersFragment = new MembersFragment();
        AssociationFragment.newInstance(unionId, membersFragment);
        return membersFragment;
    }
    /**
     * Required empty public constructor
     */
    public MembersFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _adapter = new StudentAdapter(getUnion().getStudents());
    }

    @Override
    public void update() {
        if(_adapter != null) {
            _adapter.update(getUnion().getStudents());
        }
        if(_view != null){
            final ImageView imageView = (ImageView) _view.findViewById(R.id.photo);
            getUnion().loadPhoto(new OnImageLoadedListener() {
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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        _view = inflater.inflate(R.layout.members_fragment, container, false);
        return _view;
    }

    @Override
    public void onActivityCreated(Bundle savedStateInstance){
        super.onActivityCreated(savedStateInstance);
        ListView list = getListView();
        list.setAdapter(_adapter);
        list.setOnScrollListener((MainActivity)getActivity());
        update();
    }

    /**
     *  Class which store the students to show in the ListView
     */
    final class StudentAdapter extends BaseAdapter {

        List<Student> _students;

        /**
         * Fill the adapter
         * @param students a list with all the students
         */
        StudentAdapter(List<Student> students){
            super();
            update(students);
        }

        /**
         * Replace the list of the students
         * @param students a list with all the students
         */
        void update(List<Student> students){
            _students = students;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return _students.size();
        }

        @Override
        public Object getItem(int i) {
            return _students.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup parent) {
            if(view == null) {
                LayoutInflater inflater = (LayoutInflater) MembersFragment.this.getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.members_fragment_row, parent, false);
            }
            Student student = _students.get(i);
            TextView text = (TextView) view.findViewById(R.id.listview_union_membre_role);
            text.setText(student.getPosition());
            text = (TextView) view.findViewById(R.id.listview_union_membre_nom);
            text.setText(student.getName() + " \"" + student.getNickname() + "\" " + student.getLastName());
            text = (TextView) view.findViewById(R.id.listview_union_membre_mail);
            text.setText(student.getEmail());
            return view;
        }
    }
}
