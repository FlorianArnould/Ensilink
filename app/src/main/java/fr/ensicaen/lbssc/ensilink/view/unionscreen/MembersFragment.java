package fr.ensicaen.lbssc.ensilink.view.unionscreen;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import fr.ensicaen.lbssc.ensilink.view.MainActivity;
import fr.ensicaen.lbssc.ensilink.R;
import fr.ensicaen.lbssc.ensilink.view.AssociationFragment;
import fr.ensicaen.lbssc.ensilink.storage.OnImageLoadedListener;
import fr.ensicaen.lbssc.ensilink.view.StudentAdapter;

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
        _adapter = new StudentAdapter(getUnion().getStudents(), getActivity());
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
}
