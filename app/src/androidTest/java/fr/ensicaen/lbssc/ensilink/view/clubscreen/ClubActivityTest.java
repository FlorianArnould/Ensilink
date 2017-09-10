package fr.ensicaen.lbssc.ensilink.view.clubscreen;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

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
			public void OnDataRefreshed() {
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
		_rule.launchActivity(intent);
		Espresso.onView(ViewMatchers.withText(R.string.clubs)).perform(ViewActions.click());
		Club club = union.getClub(0);
		Espresso.onView(ViewMatchers.withText(club.getName())).perform(ViewActions.click());
		Espresso.onView(ViewMatchers.withText(R.string.emails)).perform(ViewActions.click());
		Espresso.onView(ViewMatchers.withText("from")).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
	}

	@Test
	public void openEmailTest() throws Exception {
		final Union union = School.getInstance().getUnion(1);
		Intent intent = new Intent(InstrumentationRegistry.getTargetContext(), MainActivity.class);
		intent.putExtra("UNION_ID", union.getId() - 1);
		_rule.launchActivity(intent);
		Espresso.onView(ViewMatchers.withText(R.string.clubs)).perform(ViewActions.click());
		Club club = union.getClub(0);
		Espresso.onView(ViewMatchers.withText(club.getName())).perform(ViewActions.click());
		Espresso.onView(ViewMatchers.withText(R.string.emails)).perform(ViewActions.click());
		Espresso.onView(ViewMatchers.withText("from")).perform(ViewActions.click());
		Espresso.onView(ViewMatchers.withText("subject")).check(ViewAssertions.matches(ViewMatchers.withParent(ViewMatchers.withId(R.id.action_bar))));
	}

	@Test
	public void closeWithHomeAsUpTest() throws Exception {
		final Union union = School.getInstance().getUnion(1);
		Intent intent = new Intent(InstrumentationRegistry.getTargetContext(), MainActivity.class);
		intent.putExtra("UNION_ID", union.getId() - 1);
		_rule.launchActivity(intent);
		Espresso.onView(ViewMatchers.withText(R.string.clubs)).perform(ViewActions.click());
		Club club = union.getClub(0);
		Espresso.onView(ViewMatchers.withText(club.getName())).perform(ViewActions.click());
		Espresso.onView(ViewMatchers.withContentDescription(R.string.abc_action_bar_up_description)).perform(ViewActions.click());
		Espresso.onView(ViewMatchers.withText(R.string.clubs)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
	}
}
