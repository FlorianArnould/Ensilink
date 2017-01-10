package fr.ensicaen.lbssc.ensilink;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import fr.ensicaen.lbssc.ensilink.associationscreen.unionscreen.UnionFragment;
import fr.ensicaen.lbssc.ensilink.storage.OnSchoolDataListener;
import fr.ensicaen.lbssc.ensilink.storage.School;
import fr.ensicaen.lbssc.ensilink.storage.Union;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    UnionFragment _unionFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragmentContent, new EventFragment()).commit();
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
                School.getInstance().refreshData(getApplicationContext(), new OnSchoolDataListener() {
                    @Override
                    public void OnDataRefreshed(School school) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                refreshDrawer();
                            }
                        });
                    }
                });
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void refreshDrawer(){
        final Menu menu = ((NavigationView)findViewById(R.id.nav_view)).getMenu();
        menu.clear();
        menu.add("Actualité").setCheckable(true);
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
        FragmentManager fragmentManager = getSupportFragmentManager();
        if(item.toString().equals("Actualité")){
            fragmentManager.beginTransaction().replace(R.id.fragmentContent, new EventFragment()).commit();
        }
        List<Union> list = School.getInstance().getUnions();
        for(int i=0;i<list.size();i++){
            if(item.toString().equals(list.get(i).getName())) {
                if (_unionFragment == null) {
                    _unionFragment = UnionFragment.newInstance(i);
                }else {
                    _unionFragment.changeUnion(i);
                }
                fragmentManager.beginTransaction().replace(R.id.fragmentContent, _unionFragment).commit();
                _unionFragment.postReplaced(this, i);
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

    public void setActionBarColor(Drawable color){
        if(getSupportActionBar() != null) {
            getSupportActionBar().setBackgroundDrawable(color);
        }
    }
}
