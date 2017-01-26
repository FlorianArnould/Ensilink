package fr.ensicaen.lbssc.ensilink.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import fr.ensicaen.lbssc.ensilink.R;
import fr.ensicaen.lbssc.ensilink.storage.Student;

/**
 * @author Florian Arnould
 * @version 1.0
 */

/**
 *  Class which store the students to show in the ListView
 */
public final class StudentAdapter extends BaseAdapter {

    private List<Student> _students;
    private final Context _context;

    /**
     * Fill the adapter
     * @param students a list with all the students
     */
    public StudentAdapter(List<Student> students, Context context){
        super();
        _context = context;
        update(students);
    }

    /**
     * Replace the list of the students
     * @param students a list with all the students
     */
    public void update(List<Student> students){
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
            LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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