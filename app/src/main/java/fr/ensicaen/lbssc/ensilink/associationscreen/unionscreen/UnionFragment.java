package fr.ensicaen.lbssc.ensilink.associationscreen.unionscreen;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fr.ensicaen.lbssc.ensilink.MainActivity;
import fr.ensicaen.lbssc.ensilink.R;
import fr.ensicaen.lbssc.ensilink.associationscreen.Mails;
import fr.ensicaen.lbssc.ensilink.associationscreen.SuperFragment;
import fr.ensicaen.lbssc.ensilink.associationscreen.ViewPagerAdapter;
import fr.ensicaen.lbssc.ensilink.storage.School;

/**
 * @author Marsel Arik
 * @version 1.0
 */

public class UnionFragment extends SuperFragment {

    private TabLayout _tabLayout;
    private boolean _created;
    private View _view;
    private Members _members;
    private Clubs _clubs;
    private Mails _mails;
    private int _color;

    public static UnionFragment newInstance(int unionId){
        UnionFragment fragment = new UnionFragment();
        SuperFragment.newInstance(unionId, fragment);
        return fragment;
    }

    public UnionFragment(){
        _created = false;
        _color = Color.BLACK;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
            update();
            _created = true;
        }
        return _view;
    }

    protected void update(){
        _members.changeUnion(getUnionId());
        _clubs.changeUnion(getUnionId());
        //_mails.changeUnion(getUnionId());
        TabLayout.Tab tab = _tabLayout.getTabAt(0);
        if(tab != null){
            tab.select();
        }
    }

    public void postReplaced(MainActivity activity, int i){
        if( activity != null) {
            activity.setActionBarTitle(School.getInstance().getUnion(i).getName());
            if(i == 0){
                _color = Color.BLUE;
            }else if(i == 1){
                _color = Color.argb(255, 238, 33, 33);
            }else if(i == 2){
                _color = Color.argb(255, 46, 82, 68);
            }else if(i == 3){
                _color = Color.argb(255, 117, 4, 22);
            }else if(i == 4){
                _color = Color.argb(255, 15, 203, 170);
            }
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
        _members = Members.newInstance(getUnionId());
        _clubs = Clubs.newInstance(getUnionId());
        _mails = new Mails();
        adapter.addFragment(_members, "Membres");
        adapter.addFragment(_clubs, "Clubs");
        adapter.addFragment(_mails, "Mails");
        viewPager.setAdapter(adapter);
    }
}
