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
import fr.ensicaen.lbssc.ensilink.unionscreen.fragments.Clubs;
import fr.ensicaen.lbssc.ensilink.unionscreen.fragments.Mails;
import fr.ensicaen.lbssc.ensilink.unionscreen.fragments.Members;

/**
 * Created by florian on 15/12/16.
 */

public class UnionFragment extends Fragment {
    
    private Union _union;
    private TabLayout _tabLayout;
    private boolean _created;
    private View _view;

    public static UnionFragment newInstance(int unionId){
        UnionFragment fragment = new UnionFragment();
        Bundle args = new Bundle();
        args.putInt("UNION_ID", unionId);
        fragment.setArguments(args);
        return fragment;
    }

    public UnionFragment(){
        _created = false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            _union = School.getInstance().getUnion(getArguments().getInt("UNION_ID"));
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
            update();
            _created = true;
        }
        return _view;
    }

    private void update(){
        MainActivity activity = (MainActivity) getActivity();
        if( activity != null) {
            activity.setActionBarTitle(_union.getName());
        }

        TabLayout.Tab tab = _tabLayout.getTabAt(0);
        if(tab != null){
            tab.select();
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());
        adapter.addFragment(new Members(), "Membres");
        adapter.addFragment(new Clubs(), "Clubs");
        adapter.addFragment(new Mails(), "Mails");
        viewPager.setAdapter(adapter);
    }

    public void changeUnion(int unionId){
        _union = School.getInstance().getUnion(unionId);
        update();
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
