package fr.ensicaen.lbssc.ensilink;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import fr.ensicaen.lbssc.ensilink.storage.OnSchoolDataListener;
import fr.ensicaen.lbssc.ensilink.storage.School;
import fr.ensicaen.lbssc.ensilink.storage.Union;
import fr.ensicaen.lbssc.ensilink.unionscreen.UnionScreenActivity;



public abstract class DrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    @Override
    protected void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        refreshDrawer();
    }

    private void refreshDrawer(){
        final Menu menu = ((NavigationView)findViewById(R.id.nav_view)).getMenu();
        menu.clear();
        List<Union> list = School.getInstance().getUnions();
        for(int i=0;i<list.size();i++){
            final int id = i;
            menu.add(list.get(i).getName()).setCheckable(true).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    Intent intent = new Intent(DrawerActivity.this, UnionScreenActivity.class);
                    intent.putExtra("UNION_ID", id);
                    startActivity(intent);
                    if(!(DrawerActivity.this instanceof MainActivity)){
                        finish();
                    }else{
                        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                        drawer.closeDrawer(GravityCompat.START);
                        //TODO do this
                        menuItem.setChecked(false);
                    }
                    return true;
                }
            });
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if(item.getItemId() == R.id.action_refresh){
            School.getInstance().refreshData(getApplicationContext(), new OnSchoolDataListener() {
                @Override
                public void OnDataRefreshed(School school) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            refreshDrawer();
                            onDataRefreshed();
                        }
                    });
                }
            });
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected abstract void onDataRefreshed();

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
