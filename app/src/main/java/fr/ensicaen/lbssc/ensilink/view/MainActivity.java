package fr.ensicaen.lbssc.ensilink.view;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

import java.util.List;

import fr.ensicaen.lbssc.ensilink.R;
import fr.ensicaen.lbssc.ensilink.view.settingsscreen.SettingsActivity;
import fr.ensicaen.lbssc.ensilink.view.unionscreen.UnionFragment;
import fr.ensicaen.lbssc.ensilink.view.creditsscreen.CreditsActivity;
import fr.ensicaen.lbssc.ensilink.storage.OnSchoolDataListener;
import fr.ensicaen.lbssc.ensilink.storage.School;
import fr.ensicaen.lbssc.ensilink.storage.Union;
import fr.ensicaen.lbssc.ensilink.utils.ColorCreator;

/**
 * @author Florian Arnould
 * @version 1.0
 */

/**
 * The main activity of the application which manage the navigation drawer and the fragments
 */
public final class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnScrollListener{

    private UnionFragment _unionFragment;
    private Updatable _currentFragment;
    private SwipeRefreshLayout _refresher;
    private DrawerLayout _drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        _drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, _drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        _drawer.addDrawerListener(toggle);
        toggle.syncState();

        _refresher = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        _refresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);

        refreshDrawer();
        boolean fragmentSetted = false;
        if(getIntent() != null){
            int unionId = getIntent().getIntExtra("UNION_ID", -1);
            if(unionId != -1){
                initializeOrSetUnionFragment(unionId);
                fragmentSetted = true;
            }
        }
        if (savedInstanceState == null && !fragmentSetted) {
            changeFragment(new EventFragment());
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            moveTaskToBack(true);
        }
    }

    /**
     * Replace the drawer rows with the new ones
     */
    private void refreshDrawer(){
        final Menu menu = ((NavigationView)findViewById(R.id.nav_view)).getMenu();
        menu.clear();
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_kangaroo);
        drawable.mutate().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
        menu.add(getString(R.string.news)).setCheckable(true).setIcon(drawable);
        List<Union> list = School.getInstance().getUnions();
        for(Union u : list){
            MenuItem item = menu.add(u.getName());
            item.setIcon(u.getLogo());
            item.setCheckable(true);
        }
        MenuItem item;
        if(School.getInstance().isConnected()){
            item = menu.add(1, Menu.NONE, Menu.NONE, getString(R.string.logout));
        }else{
            item = menu.add(1, Menu.NONE, Menu.NONE, getString(R.string.login));
        }
        drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_beach_access);
        drawable.mutate().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
        item.setIcon(drawable);
        item = menu.add(1, Menu.NONE, Menu.NONE, getString(R.string.settings));
        drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_settings);
        drawable.mutate().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
        item.setIcon(drawable);
        item = menu.add(1, Menu.NONE, Menu.NONE, getString(R.string.credits));
        drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_library_books);
        drawable.mutate().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
        item.setIcon(drawable);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(_unionFragment != null){
            _unionFragment.resetPosition();
        }
        if(item.getTitle().equals(getString(R.string.news))){
            changeFragment(new EventFragment());
        }else if(item.getTitle().equals(getString(R.string.login))) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }else if(item.getTitle().equals(getString(R.string.logout))) {
            School.getInstance().logout(getApplicationContext());
        }else if(item.getTitle().equals(getString(R.string.settings))) {
            Intent intent_settings = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent_settings);
        }else if(item.getTitle().equals(getString(R.string.credits))) {
            Intent intent = new Intent(MainActivity.this, CreditsActivity.class);
            startActivity(intent);
        }else{
            List<Union> list = School.getInstance().getUnions();
            for (int i = 0; i < list.size(); i++) {
                if (item.toString().equals(list.get(i).getName())) {
                    initializeOrSetUnionFragment(i);
                }
            }
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Set the current fragment as a union fragment
     * @param unionId the position of the union
     */
    public void initializeOrSetUnionFragment(int unionId){
        if (_unionFragment == null) {
            _unionFragment = UnionFragment.newInstance(unionId);
        } else {
            _unionFragment.changeUnion(unionId);
        }
        changeFragment(_unionFragment);
        _unionFragment.changeColor(this, unionId);
    }

    /**
     * set the text displayed in the action bar
     * @param title the text to display
     */
    public void setActionBarTitle(String title){
        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle(title);
        }
    }

    /**
     * Set the color of the action bar
     * @param color the color to set
     */
    public void setApplicationColor(@ColorInt int color){
        if(getSupportActionBar() != null) {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(color));
        }
        if(_drawer != null) {
            _drawer.setStatusBarBackgroundColor(ColorCreator.darkerColor(color));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(color);
        }
        NavigationView nav = (NavigationView)findViewById(R.id.nav_view);
        nav.getHeaderView(0).findViewById(R.id.drawer_header).setBackgroundColor(color);
    }

    /**
     * Replace the main fragment by another
     * @param fragment the new fragment
     */
    private void changeFragment(Fragment fragment){
        _currentFragment = (Updatable) fragment;
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContent, fragment).commit();
    }

    /**
     * Reload all information in the application
     */
    private void refresh(){
        School.getInstance().refreshData(getApplicationContext(), new OnSchoolDataListener() {
            @Override
            public void OnDataRefreshed() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        refreshDrawer();
                        _currentFragment.update();
                        _refresher.setRefreshing(false);
                    }
                });
            }
        });
    }


    @Override
    public void onScrollStateChanged(AbsListView absListView, int state) {
        if(state == OnScrollListener.SCROLL_STATE_IDLE) {
            updateRefresherState(absListView);
        }
    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {
        //Need to be empty for optimisation reasons
    }

    /**
     * Update the state of the "swipe to refresh" action
     * @param absListView active listview
     */
    public void updateRefresherState(AbsListView absListView){
        if(_refresher != null) {
            setRefresherEnabled(!ViewCompat.canScrollVertically(absListView, -1));
        }
    }

    /**
     * Set the state of the "swipe to refresh" action
     * @param enabled the new state
     */
    public void setRefresherEnabled(boolean enabled){
        _refresher.setEnabled(enabled);
    }
}
