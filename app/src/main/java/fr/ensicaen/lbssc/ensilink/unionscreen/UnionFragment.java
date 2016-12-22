package fr.ensicaen.lbssc.ensilink.unionscreen;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import fr.ensicaen.lbssc.ensilink.MainActivity;
import fr.ensicaen.lbssc.ensilink.R;
import fr.ensicaen.lbssc.ensilink.storage.School;
import fr.ensicaen.lbssc.ensilink.storage.Union;

/**
 * @author Marsel Arik
 * @version 1.0
 */

public class UnionFragment extends SuperUnionFragment {

    private TabLayout _tabLayout;
    private boolean _created;
    private View _view;
    private Members _members;
    private Clubs _clubs;
    private Mails _mails;

    public static UnionFragment newInstance(int unionId){
        UnionFragment fragment = new UnionFragment();
        SuperUnionFragment.newInstance(unionId, fragment);
        return fragment;
    }

    public UnionFragment(){
        _created = false;
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
            update();
            _created = true;
        }
        return _view;
    }

    protected void update(){
        MainActivity activity = (MainActivity) getActivity();
        if( activity != null) {
            activity.setActionBarTitle(getUnion().getName());
        }
        _members.changeUnion(getUnionId());
        //_clubs.changeUnion(getUnionId());
        //_mails.changeUnion(getUnionId());
        TabLayout.Tab tab = _tabLayout.getTabAt(0);
        if(tab != null){
            tab.select();
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());
        _members = Members.newInstance(getUnionId());
        _clubs = new Clubs();
        _mails = new Mails();
        adapter.addFragment(_members, "Membres");
        adapter.addFragment(_clubs, "Clubs");
        adapter.addFragment(_mails, "Mails");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList;
        private final List<String> mFragmentTitleList;

        ViewPagerAdapter(FragmentManager manager) {
            super(manager);
            mFragmentList = new ArrayList<>();
            mFragmentTitleList = new ArrayList<>();
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
