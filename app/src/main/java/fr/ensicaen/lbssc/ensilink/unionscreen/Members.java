package fr.ensicaen.lbssc.ensilink.unionscreen;

/**
 * @author Marsel Arik
 * @version 1.0
 */

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import fr.ensicaen.lbssc.ensilink.R;
import fr.ensicaen.lbssc.ensilink.storage.School;
import fr.ensicaen.lbssc.ensilink.storage.Student;


public class Members extends SuperUnionFragment {

    private StudentAdapter _adapter;
    private View _view;

    public static Members newInstance(int unionId){
        Members members = new Members();
        SuperUnionFragment.newInstance(unionId, members);
        return members;
    }

    public Members() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _adapter = new StudentAdapter(getUnion().getStudents());
    }

    @Override
    protected void update() {
        if(_adapter != null) {
            _adapter.update(getUnion().getStudents());
        }
        if(_view != null){
            ImageView image = (ImageView) _view.findViewById(R.id.photo);
            image.setImageBitmap(getUnion().getPhoto());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        _view = inflater.inflate(R.layout.members_union, container, false);
        ListView list = (ListView) _view.findViewById(R.id.list_view_member);
        list.setAdapter(_adapter);
        update();
        return _view;
    }


    final class StudentAdapter extends BaseAdapter {

        Map<String, Student> _students;

        StudentAdapter(Map<String, Student> students){
            super();
            update(students);
        }

        void update(Map<String, Student> students){
            _students = students;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return _students.size();
        }

        @Override
        public Object getItem(int i) {
            //TODO return the good element
            return 0;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup parent) {
            if(view == null) {
                LayoutInflater inflater = (LayoutInflater) Members.this.getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.union_members_row, parent, false);
            }
            String position = _students.keySet().toArray()[i].toString();
            Student student = _students.get(position);
            TextView text = (TextView) view.findViewById(R.id.listview_union_membre_role);
            text.setText(position);
            text = (TextView) view.findViewById(R.id.listview_union_membre_nom);
            text.setText(student.getName() + " \"" + student.getNickname() + "\" " + student.getLastName());
            text = (TextView) view.findViewById(R.id.listview_union_membre_mail);
            text.setText(student.getEmail());
            return view;
        }
    }
}
