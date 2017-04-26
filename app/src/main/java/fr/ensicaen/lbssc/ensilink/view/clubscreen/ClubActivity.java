/**
 * This file is part of Ensilink.
 *
 * Ensilink is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * Ensilink is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Ensilink.
 * If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright, The Ensilink team :  ARNOULD Florian, ARIK Marsel, FILIPOZZI Jérémy,
 * ENSICAEN, 6 Boulevard du Maréchal Juin, 26 avril 2017
 *
 */

package fr.ensicaen.lbssc.ensilink.view.clubscreen;

import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import fr.ensicaen.lbssc.ensilink.utils.ColorCreator;
import fr.ensicaen.lbssc.ensilink.R;
import fr.ensicaen.lbssc.ensilink.view.MailsFragment;
import fr.ensicaen.lbssc.ensilink.view.ViewPagerAdapter;
import fr.ensicaen.lbssc.ensilink.storage.School;
import fr.ensicaen.lbssc.ensilink.storage.Union;

/**
 * @author Marsel Arik
 * @version 1.0
 */

/**
 * Class which manage the screen of a club with all of the informations related to the club
 */
public class ClubActivity extends AppCompatActivity{

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
        MailsFragment emailsFragment = MailsFragment.newInstance(_unionId, _clubId);
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
