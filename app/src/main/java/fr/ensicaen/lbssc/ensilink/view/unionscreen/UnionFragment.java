package fr.ensicaen.lbssc.ensilink.view.unionscreen;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fr.ensicaen.lbssc.ensilink.view.MainActivity;
import fr.ensicaen.lbssc.ensilink.R;
import fr.ensicaen.lbssc.ensilink.view.Updatable;
import fr.ensicaen.lbssc.ensilink.view.EmailsFragment;
import fr.ensicaen.lbssc.ensilink.view.ViewPagerAdapter;
import fr.ensicaen.lbssc.ensilink.storage.School;
import fr.ensicaen.lbssc.ensilink.storage.Union;

/**
 * @author Marsel Arik
 * @version 1.0
 */

/**
 * Fragment of an union with the members, the clubs and the mails
 */
public class UnionFragment extends Fragment implements Updatable {

    private TabLayout _tabLayout;
    private boolean _created;
    private View _view;
    private MembersFragment _membersFragment;
    private ClubsFragment _clubsFragment;
    private EmailsFragment _emailsFragment;
    private int _color;
    private int _unionId;

    /**
     * create an object
     * @return an union fragment
     */
    public static UnionFragment newInstance(int unionId){
        UnionFragment fragment = new UnionFragment();
        Bundle args = new Bundle();
        args.putInt("UNION_ID", unionId);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * the default constructor
     */
    public UnionFragment(){
        _created = false;
        _color = Color.BLACK;
        _unionId = 0;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null && _unionId == 0) {
            _unionId = getArguments().getInt("UNION_ID");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(!_created) {
            _view = inflater.inflate(R.layout.union_fragment, container, false);

            ViewPager viewPager = (ViewPager) _view.findViewById(R.id.viewpager);
            setupViewPager(viewPager);

            _tabLayout = (TabLayout) _view.findViewById(R.id.tabs);
            _tabLayout.setupWithViewPager(viewPager);
            _tabLayout.setBackgroundColor(_color);
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    //Do nothing because it is always called during animation
                }

                @Override
                public void onPageSelected(int position) {
                    ListFragment listFragment;
                    switch (position){
                        case 0:
                            listFragment = _membersFragment;
                            break;
                        default:
                            listFragment = _clubsFragment;
                    }
                    if(getActivity() != null) {
                        ((MainActivity) getActivity()).updateRefresherState(listFragment.getListView());
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    if(state == ViewPager.SCROLL_STATE_DRAGGING){
                        ((MainActivity)getActivity()).setRefresherEnabled(false);
                    }
                }
            });
            update();
            _created = true;
        }
        return _view;
    }

    @Override
    public void update(){
        _membersFragment.changeUnion(_unionId);
        _clubsFragment.changeUnion(_unionId);
        _emailsFragment.changeUnion(_unionId);
    }

    /**
     * Reset the selected fragment to the default
     */
    public void resetPosition(){
        TabLayout.Tab tab = _tabLayout.getTabAt(0);
        if(tab != null){
            tab.select();
        }
    }

    /**
     * Change the color of the top of the application
     */
    public void changeColor(MainActivity activity, int unionId){
        if( activity != null) {
            Union union = School.getInstance().getUnion(unionId);
            activity.setActionBarTitle(union.getName());
            _color = union.getColor();
            activity.setApplicationColor(_color);
            if(_view != null){
                TabLayout tabLayout = (TabLayout) _view.findViewById(R.id.tabs);
                tabLayout.setBackgroundColor(_color);
            }
        }
    }

    /**
     * Set the fragments of the page in the adapter
     */
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());
        _membersFragment = MembersFragment.newInstance(_unionId);
        _clubsFragment = ClubsFragment.newInstance(_unionId);
        _emailsFragment = EmailsFragment.newInstance(_unionId);
        adapter.addFragment(_membersFragment, getString(R.string.members));
        adapter.addFragment(_clubsFragment, getString(R.string.clubs));
        adapter.addFragment(_emailsFragment, getString(R.string.emails));
        viewPager.setAdapter(adapter);
    }

    /**
     * Change the displayed union in the fragment
     */
    public void changeUnion(int unionId){
        _unionId = unionId;
        update();
    }
}
