package fr.ensicaen.lbssc.ensilink.associationscreen.unionscreen;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fr.ensicaen.lbssc.ensilink.MainActivity;
import fr.ensicaen.lbssc.ensilink.R;
import fr.ensicaen.lbssc.ensilink.Updatable;
import fr.ensicaen.lbssc.ensilink.associationscreen.Mails;
import fr.ensicaen.lbssc.ensilink.associationscreen.ViewPagerAdapter;
import fr.ensicaen.lbssc.ensilink.storage.School;
import fr.ensicaen.lbssc.ensilink.storage.Union;

/**
 * @author Marsel Arik
 * @version 1.0
 */

public class UnionFragment extends Fragment implements Updatable {

    private TabLayout _tabLayout;
    private boolean _created;
    private View _view;
    private Members _members;
    private Clubs _clubs;
    private Mails _mails;
    private int _color;
    private int _unionId;

    public static UnionFragment newInstance(int unionId){
        UnionFragment fragment = new UnionFragment();
        Bundle args = new Bundle();
        args.putInt("UNION_ID", unionId);
        fragment.setArguments(args);
        return fragment;
    }

    public UnionFragment(){
        _created = false;
        _color = Color.BLACK;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
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
                    //Do nothing always called during animation
                }

                @Override
                public void onPageSelected(int position) {
                    ListFragment listFragment;
                    switch (position){
                        case 0:
                            listFragment = _members;
                            break;
                        default:
                            listFragment = _clubs;
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

    public void update(){
        _members.changeUnion(_unionId);
        _clubs.changeUnion(_unionId);
        //_mails.changeUnion(_unionId);
    }

    public void resetPosition(){
        TabLayout.Tab tab = _tabLayout.getTabAt(0);
        if(tab != null){
            tab.select();
        }
    }

    public void postReplaced(MainActivity activity, int unionId){
        if( activity != null) {
            Union union = School.getInstance().getUnion(unionId);
            activity.setActionBarTitle(union.getName());
            _color = union.getColor();
            activity.setActionBarColor(new ColorDrawable(_color));
            if(android.os.Build.VERSION.SDK_INT >= 21){
                int darker = darkerColor(_color);
                activity.getWindow().setStatusBarColor(Color.argb(125, Color.red(darker), Color.green(darker), Color.blue(darker)));
            }
            if(_view != null){
                TabLayout tabLayout = (TabLayout) _view.findViewById(R.id.tabs);
                tabLayout.setBackgroundColor(_color);
            }
        }
    }

    private int darkerColor(int color){
        float HSVcolor[] = new float[3];
        Color.colorToHSV(color, HSVcolor);
        HSVcolor[2] *= 0.9;
        return Color.HSVToColor(HSVcolor);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());
        _members = Members.newInstance(_unionId);
        _clubs = Clubs.newInstance(_unionId);
        _mails = new Mails();
        adapter.addFragment(_members, "Membres");
        adapter.addFragment(_clubs, "Clubs");
        adapter.addFragment(_mails, "Mails");
        viewPager.setAdapter(adapter);
    }

    public void changeUnion(int unionId){
        _unionId = unionId;
        update();
    }
}
