package fr.ensicaen.lbssc.ensilink.associationscreen;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> _FragmentList;
    private final List<String> _TitleList;

    public ViewPagerAdapter(FragmentManager manager) {
        super(manager);
        _FragmentList = new ArrayList<>();
        _TitleList = new ArrayList<>();
    }

    @Override
    public Fragment getItem(int position) {
        return _FragmentList.get(position);
    }

    @Override
    public int getCount() {
        return _FragmentList.size();
    }

    public void addFragment(Fragment fragment, String title) {
        _FragmentList.add(fragment);
        _TitleList.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return _TitleList.get(position);
    }
}