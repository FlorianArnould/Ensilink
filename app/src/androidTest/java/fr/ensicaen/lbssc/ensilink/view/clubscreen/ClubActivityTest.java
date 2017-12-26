package fr.ensicaen.lbssc.ensilink.view.clubscreen;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.view.ViewPager;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;

import fr.ensicaen.lbssc.ensilink.R;
import fr.ensicaen.lbssc.ensilink.loader.DataManagerForTest;
import fr.ensicaen.lbssc.ensilink.storage.Club;
import fr.ensicaen.lbssc.ensilink.storage.OnSchoolDataListener;
import fr.ensicaen.lbssc.ensilink.storage.School;
import fr.ensicaen.lbssc.ensilink.storage.Union;
import fr.ensicaen.lbssc.ensilink.view.MainActivity;
import fr.ensicaen.lbssc.ensilink.view.unionscreen.UnionFragment;

/**
 * @author Florian Arnould
 */
@RunWith(AndroidJUnit4.class)
public class ClubActivityTest {
	@Rule
	public ActivityTestRule<MainActivity> _rule = new ActivityTestRule<>(MainActivity.class);

	@BeforeClass
	public static void fillDatabase() throws Exception {
		DataManagerForTest.setDefaultLocalDatabase();
		DataManagerForTest.addMailToFootball();
		final CountDownLatch signal = new CountDownLatch(1);
		School.getInstance().loadLocalData(InstrumentationRegistry.getTargetContext(), new OnSchoolDataListener() {
			@Override
			public void onDataRefreshed() {
				signal.countDown();
			}
		});
		signal.await();
	}

	@Test
	public void checkEmailTest() throws Exception {
		final Union union = School.getInstance().getUnion(1);
		Intent intent = new Intent(InstrumentationRegistry.getTargetContext(), MainActivity.class);
		intent.putExtra("UNION_ID", union.getId() - 1);
		MainActivity activity = _rule.launchActivity(intent);
		UnionFragment fragment = activity.getUnionFragment();
		Assert.assertNotNull(fragment);
		final CountDownLatch signal = new CountDownLatch(1);
		fragment.setViewPagerListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageScrollStateChanged(int state) {
				if (state == ViewPager.SCROLL_STATE_IDLE) {
					signal.countDown();
				}
			}
		});
		Espresso.onView(ViewMatchers.withId(R.id.viewpager)).perform(ViewActions.swipeLeft());
		signal.await();
		Club club = union.getClub(0);
		Espresso.onView(ViewMatchers.withText(club.getName())).perform(ViewActions.click());
		Espresso.onView(ViewMatchers.withId(R.id.viewpager)).perform(ViewActions.swipeLeft());
		Espresso.onView(ViewMatchers.withText("from")).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
	}

	@Test
	public void openEmailTest() throws Exception {
		final Union union = School.getInstance().getUnion(1);
		Club club = union.getClub(0);
		Intent intent = new Intent(InstrumentationRegistry.getTargetContext(), ClubActivity.class);
		intent.putExtra("UNION_ID", union.getId() - 1);
		intent.putExtra("CLUB_ID", club.getId() - 1);
		ClubActivity activity = new ActivityTestRule<>(ClubActivity.class).launchActivity(intent);
		final CountDownLatch signal = new CountDownLatch(1);
		activity.setViewPagerListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageScrollStateChanged(int state) {
				if (state == ViewPager.SCROLL_STATE_IDLE) {
					signal.countDown();
				}
			}
		});
		Espresso.onView(ViewMatchers.withId(R.id.viewpager)).perform(ViewActions.swipeLeft());
		signal.await();
		Espresso.onView(ViewMatchers.withText("from")).perform(ViewActions.click());
		Espresso.onView(ViewMatchers.withText("subject")).check(ViewAssertions.matches(ViewMatchers.withParent(ViewMatchers.withId(R.id.action_bar))));
	}

	@Test
	public void closeWithHomeAsUpTest() throws Exception {
		final Union union = School.getInstance().getUnion(1);
		Intent intent = new Intent(InstrumentationRegistry.getTargetContext(), MainActivity.class);
		intent.putExtra("UNION_ID", union.getId() - 1);
		MainActivity activity = _rule.launchActivity(intent);
		final CountDownLatch signal = new CountDownLatch(1);
		UnionFragment fragment = activity.getUnionFragment();
		Assert.assertNotNull(fragment);
		fragment.setViewPagerListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageScrollStateChanged(int state) {
				if (state == ViewPager.SCROLL_STATE_IDLE) {
					signal.countDown();
				}
			}
		});
		Espresso.onView(ViewMatchers.withId(R.id.viewpager)).perform(ViewActions.swipeLeft());
		signal.await();
		Club club = union.getClub(0);
		Espresso.onView(ViewMatchers.withText(club.getName())).perform(ViewActions.click());
		Espresso.onView(ViewMatchers.withContentDescription(R.string.abc_action_bar_up_description)).perform(ViewActions.click());
		Espresso.onView(ViewMatchers.withText(R.string.clubs)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
	}
}
