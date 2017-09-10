package fr.ensicaen.lbssc.ensilink.view;

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
import fr.ensicaen.lbssc.ensilink.storage.OnSchoolDataListener;
import fr.ensicaen.lbssc.ensilink.storage.School;
import fr.ensicaen.lbssc.ensilink.storage.Union;

/**
 * @author Florian Arnould
 */
@RunWith(AndroidJUnit4.class)
public class EmailsActivityTest {
	@Rule
	public ActivityTestRule<MainActivity> _rule = new ActivityTestRule<>(MainActivity.class);

	@BeforeClass
	public static void fillDatabase() throws Exception {
		DataManagerForTest.setDefaultLocalDatabase();
		DataManagerForTest.addMailToBDS();
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
	public void fillDataTest() throws Exception {
		final Union union = School.getInstance().getUnion(1);
		Intent intent = new Intent(InstrumentationRegistry.getTargetContext(), MainActivity.class);
		intent.putExtra("UNION_ID", union.getId() - 1);
		_rule.launchActivity(intent);
		Espresso.onView(ViewMatchers.withText(R.string.emails)).perform(ViewActions.click());
		Espresso.onView(ViewMatchers.withText("from")).perform(ViewActions.click());
		Espresso.onView(ViewMatchers.withText("subject")).check(ViewAssertions.matches(ViewMatchers.withParent(ViewMatchers.withId(R.id.action_bar))));
		Espresso.onView(ViewMatchers.withId(R.id.mailTransmitter)).check(ViewAssertions.matches(ViewMatchers.withText("from")));
		Espresso.onView(ViewMatchers.withId(R.id.mail_date)).check(ViewAssertions.matches(ViewMatchers.withText("2017-09-01")));
		Espresso.onView(ViewMatchers.withId(R.id.mailText)).check(ViewAssertions.matches(ViewMatchers.withText("content\n")));
	}

	@Test
	public void closeWithHomeAsUpTest() throws Exception {
		final Union union = School.getInstance().getUnion(1);
		Intent intent = new Intent(InstrumentationRegistry.getTargetContext(), MainActivity.class);
		intent.putExtra("UNION_ID", union.getId() - 1);
		_rule.launchActivity(intent);
		Espresso.onView(ViewMatchers.withText(R.string.emails)).perform(ViewActions.click());
		Espresso.onView(ViewMatchers.withText("from")).perform(ViewActions.click());
		Espresso.onView(ViewMatchers.withContentDescription(R.string.abc_action_bar_up_description)).perform(ViewActions.click());
		Espresso.onView(ViewMatchers.withText(R.string.emails)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
	}
}
