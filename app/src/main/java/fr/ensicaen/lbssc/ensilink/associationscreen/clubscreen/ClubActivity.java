package fr.ensicaen.lbssc.ensilink.associationscreen.clubscreen;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import fr.ensicaen.lbssc.ensilink.R;
import fr.ensicaen.lbssc.ensilink.associationscreen.Mails;
import fr.ensicaen.lbssc.ensilink.associationscreen.ViewPagerAdapter;
import fr.ensicaen.lbssc.ensilink.storage.School;

public class ClubActivity extends AppCompatActivity {

    private int _unionId;
    private int _clubId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club);
        _unionId = getIntent().getIntExtra("UNION_ID", 0);
        _clubId = getIntent().getIntExtra("CLUB_ID", 0);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        int color = getColor();

        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle(School.getInstance().getUnion(_unionId).getClub(_clubId).getName());
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(color));
        }
        if(android.os.Build.VERSION.SDK_INT >= 21){
            getWindow().setStatusBarColor(darkerColor(color));
        }
        tabLayout.setBackgroundColor(color);
    }

    private int darkerColor(int color){
        float HSVcolor[] = new float[3];
        Color.colorToHSV(color, HSVcolor);
        HSVcolor[2] *= 0.9;
        return Color.HSVToColor(HSVcolor);
    }

    private int getColor(){
        int color = Color.BLACK;
        if(_unionId == 0){
            color = Color.BLUE;
        }else if(_unionId == 1){
            color = Color.argb(255, 238, 33, 33);
        }else if(_unionId == 2){
            color = Color.argb(255, 46, 82, 68);
        }else if(_unionId == 3){
            color = Color.argb(255, 117, 4, 22);
        }else if(_unionId == 4){
            color = Color.argb(255, 15, 203, 170);
        }
        return color;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        Mails mails = new Mails();
        InformationFragment info= InformationFragment.newInstance(_unionId, _clubId);
        adapter.addFragment(info, "Informations");
        adapter.addFragment(mails, "Mails");
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
