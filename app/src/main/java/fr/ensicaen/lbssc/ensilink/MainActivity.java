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
import android.view.View;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.ensicaen.lbssc.ensilink.storage.Club;
import fr.ensicaen.lbssc.ensilink.storage.OnSchoolDataListener;
import fr.ensicaen.lbssc.ensilink.storage.School;
import fr.ensicaen.lbssc.ensilink.storage.Student;
import fr.ensicaen.lbssc.ensilink.storage.Union;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // Array of strings for ListView Title
    String[] listViewTitle = new String[]{
            "Victoire au CAS 2016", "ListView Title 2", "ListView Title 3", "ListView Title 4",
            "ListView Title 5", "ListView Title 6", "ListView Title 7", "ListView Title 8",
    };

    int[] listViewImage = new int[]{
            R.drawable.bds, R.drawable.bds, R.drawable.bds, R.drawable.bds, R.drawable.bds,
            R.drawable.bds, R.drawable.bds, R.drawable.bds, R.drawable.bds,
    };

    String[] listViewDescription = new String[]{
            "Une de plus pour l'ENSI", "Android Description", "Android Description", "Android Description",
            "Android Description", "Android Description", "Android Description", "Android Description",
    };

    ListView androidListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        refreshDrawer();

        List<HashMap<String,String>> aList = new ArrayList<HashMap<String, String>>();

        for (int i=0; i<8 ; i++){
            HashMap<String,String> hm = new HashMap<String, String>();
            hm.put("listview_title", listViewTitle[i]);
            hm.put("listview_description", listViewDescription[i]);
            hm.put("listview_image", Integer.toString(listViewImage[i]));
            aList.add(hm);
        }

        String[] from = {"listview_image", "listview_title", "listview_description"};
        int[] to = {R.id.listview_image, R.id.listview_item_title, R.id.listview_item_short_description};

        SimpleAdapter simpleAdapter = new SimpleAdapter(getBaseContext(), aList, R.layout.listview_activity_1, from, to);
        androidListView = (ListView) findViewById(R.id.list_view);
        androidListView.setAdapter(simpleAdapter);

        androidListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent myIntent = new Intent(view.getContext(), ListItemAcitivty2.class);
                startActivity(myIntent);
            }
        });

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


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void refreshDrawer(){
        Menu menu = ((NavigationView)findViewById(R.id.nav_view)).getMenu();
        menu.clear();
        for(Union i : School.getInstance().getUnions()){
            menu.add(i.getName()).setCheckable(true);
        }
    }
}
