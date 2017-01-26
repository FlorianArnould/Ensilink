package fr.ensicaen.lbssc.ensilink.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Florian Arnould
 * @version 1.0
 */

/**
 * A adapter used to store the fragment of a page
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> _FragmentList;
    private final List<String> _TitleList;

    /**
     * The constructor
     * @param manager the FragmentManager of the parent activity or the parent fragment
     */
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

    /**
     * Add a fragment to the adapter (in order they will be shown from left to right)
     * @param fragment the fragment to add
     * @param title the title to show in the TabLayout
     */
    public void addFragment(Fragment fragment, String title) {
        _FragmentList.add(fragment);
        _TitleList.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return _TitleList.get(position);
    }
}