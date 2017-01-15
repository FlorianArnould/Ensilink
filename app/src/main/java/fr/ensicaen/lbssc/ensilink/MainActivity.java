package fr.ensicaen.lbssc.ensilink;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

import java.util.List;

import fr.ensicaen.lbssc.ensilink.associationscreen.unionscreen.UnionFragment;
import fr.ensicaen.lbssc.ensilink.creditsscreen.CreditsActivity;
import fr.ensicaen.lbssc.ensilink.eventscreen.EventFragment;
import fr.ensicaen.lbssc.ensilink.storage.OnSchoolDataListener;
import fr.ensicaen.lbssc.ensilink.storage.School;
import fr.ensicaen.lbssc.ensilink.storage.Union;

/**
 * @author Florian Arnould
 * @version 1.0
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
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        _drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        _drawer.setStatusBarBackgroundColor(Color.RED);
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

        if (savedInstanceState == null) {
            changeFragment(new EventFragment());
        }
        refreshDrawer();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()){
            case R.id.action_settings:
                return true;
            case R.id.action_refresh:
                _refresher.setRefreshing(true);
                refresh();
                return true;
            case R.id.action_credits:
                Intent intent = new Intent(MainActivity.this, CreditsActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void refreshDrawer(){
        final Menu menu = ((NavigationView)findViewById(R.id.nav_view)).getMenu();
        menu.clear();
        menu.add(getString(R.string.news)).setCheckable(true);
        List<Union> list = School.getInstance().getUnions();
        for(Union u : list){
            MenuItem item = menu.add(u.getName());
            item.setIcon(u.getLogo());
            item.setCheckable(true);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        if(item.getTitle().equals(getString(R.string.news))){
            changeFragment(new EventFragment());
        }else {
            List<Union> list = School.getInstance().getUnions();
            for (int i = 0; i < list.size(); i++) {
                if (item.toString().equals(list.get(i).getName())) {
                    if (_unionFragment == null) {
                        _unionFragment = UnionFragment.newInstance(i);
                    } else {
                        _unionFragment.changeUnion(i);
                        //TODO return to default position after visited Events page
                        _unionFragment.resetPosition();
                    }
                    changeFragment(_unionFragment);
                    _unionFragment.postReplaced(this, i);
                }
            }
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setActionBarTitle(String title){
        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle(title);
        }
    }

    public void setApplicationColor(@ColorInt int color){
        if(getSupportActionBar() != null) {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(color));
        }
        if(_drawer != null) {
            _drawer.setStatusBarBackgroundColor(ColorCreator.darkerColor(color));
        }
    }

    private void changeFragment(Fragment fragment){
        _currentFragment = (Updatable) fragment;
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContent, fragment).commit();
    }


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

    }

    public void updateRefresherState(AbsListView absListView){
        if(_refresher != null) {
            setRefresherEnabled(!ViewCompat.canScrollVertically(absListView, -1));
        }
    }

    public void setRefresherEnabled(boolean enabled){
        _refresher.setEnabled(enabled);
    }
}
