package fr.ensicaen.lbssc.ensilink;

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
import android.widget.TextView;

import java.util.Map;

import fr.ensicaen.lbssc.ensilink.storage.Club;
import fr.ensicaen.lbssc.ensilink.storage.OnSchoolDataListener;
import fr.ensicaen.lbssc.ensilink.storage.School;
import fr.ensicaen.lbssc.ensilink.storage.Student;
import fr.ensicaen.lbssc.ensilink.storage.Union;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

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
        navigationView.setNavigationItemSelectedListener(this);
        displayInformation();
        refreshDrawer();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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
                                displayInformation();
                                refreshDrawer();
                            }
                        });
                    }
                });
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void displayInformation(){
        School school = School.getInstance();
        if(school.getUnions() != null) {
            TextView text = (TextView) findViewById(R.id.mainText);
            String str = "nombre de bureaux : " + school.getUnions().size() + "\n";
            for (Union union : school.getUnions()) {
                str += union.getName() + "\n";
                for(Map.Entry<String, Student> i : union.getStudents().entrySet()){
                    str += "\t" + i.getKey() + " : " + i.getValue().toString() + "\n";
                }
                for(Club c : union.getClubs()){
                    str += c.getName() + " :\n";
                    str += c.toString() + "\n";
                    for(Map.Entry<String, Student> i : c.getStudents().entrySet()){
                        str += "\t" + i.getKey() + " : " + i.getValue().toString() + "\n";
                    }
                }
            }
            text.setText(str);
        }
    }

    private void refreshDrawer(){
        Menu menu = ((NavigationView)findViewById(R.id.nav_view)).getMenu();
        menu.clear();
        for(Union i : School.getInstance().getUnions()){
            menu.add(i.getName()).setCheckable(true);
        }
    }
}
