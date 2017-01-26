package fr.ensicaen.lbssc.ensilink.associationscreen.clubscreen;

import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import fr.ensicaen.lbssc.ensilink.ColorCreator;
import fr.ensicaen.lbssc.ensilink.R;
import fr.ensicaen.lbssc.ensilink.associationscreen.EmailsFragment;
import fr.ensicaen.lbssc.ensilink.associationscreen.ViewPagerAdapter;
import fr.ensicaen.lbssc.ensilink.storage.School;
import fr.ensicaen.lbssc.ensilink.storage.Union;

/**
 * @author Marsel Arik
 * @version 1.0
 */

/**
 * Class which manage the screen of a club with all of the informations related to the club
 */
public class ClubActivity extends AppCompatActivity {

    private int _unionId;
    private int _clubId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.club_activity);
        _unionId = getIntent().getIntExtra("UNION_ID", 0);
        _clubId = getIntent().getIntExtra("CLUB_ID", 0);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setElevation(0);
        }
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        Union union = School.getInstance().getUnion(_unionId);
        int color = union.getColor();
        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle(union.getClub(_clubId).getName());
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(color));
        }
        if(Build.VERSION.SDK_INT >= 21){
            getWindow().setStatusBarColor(ColorCreator.darkerColor(union.getColor()));
        }
        tabLayout.setBackgroundColor(color);
    }

    /**
     * Set the fragments of the page in the adapter
     */
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        EmailsFragment emailsFragment = EmailsFragment.newInstance(_unionId);
        InformationFragment info = InformationFragment.newInstance(_unionId, _clubId);
        adapter.addFragment(info, getString(R.string.information));
        adapter.addFragment(emailsFragment, getString(R.string.emails));
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
