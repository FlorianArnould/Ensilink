package fr.ensicaen.lbssc.ensilink.view.unionscreen;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.intent.matcher.IntentMatchers;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.hamcrest.Matchers;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;

import fr.ensicaen.lbssc.ensilink.R;
import fr.ensicaen.lbssc.ensilink.loader.DataManagerForTest;
import fr.ensicaen.lbssc.ensilink.storage.OnSchoolDataListener;
import fr.ensicaen.lbssc.ensilink.storage.School;
import fr.ensicaen.lbssc.ensilink.storage.Student;
import fr.ensicaen.lbssc.ensilink.storage.Union;
import fr.ensicaen.lbssc.ensilink.view.MainActivity;

/**
 * @author Florian Arnould
 */
@RunWith(AndroidJUnit4.class)
public class MembersFragmentTest {
	@Rule
	public ActivityTestRule<MainActivity> _rule = new ActivityTestRule<>(MainActivity.class);

	@BeforeClass
	public static void fillDatabase() throws Exception {
		DataManagerForTest.setDefaultLocalDatabase();
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
	public void membersDisplayedTest() throws Exception {
		final Union union = School.getInstance().getUnion(1);
		Intent intent = new Intent(InstrumentationRegistry.getTargetContext(), MainActivity.class);
		intent.putExtra("UNION_ID", union.getId() - 1);
		_rule.launchActivity(intent);
		Student student = union.getStudents().get(0);
		String name = student.getName() + " \"" + student.getNickname() + "\" " + student.getLastName();
		Espresso.onView(ViewMatchers.withText(name)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
	}

	@Test
	public void moveToMembersTest() throws Exception {
		final Union union = School.getInstance().getUnion(1);
		Intent intent = new Intent(InstrumentationRegistry.getTargetContext(), MainActivity.class);
		intent.putExtra("UNION_ID", union.getId() - 1);
		_rule.launchActivity(intent);
		Espresso.onView(ViewMatchers.withText(R.string.clubs)).perform(ViewActions.click());
		Espresso.onView(ViewMatchers.withText(R.string.members)).perform(ViewActions.click());
		Student student = union.getStudents().get(0);
		String name = student.getName() + " \"" + student.getNickname() + "\" " + student.getLastName();
		Espresso.onView(ViewMatchers.withText(name)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
	}

	@Test
	public void clickOnEmailIconTest() throws Exception {
		Intents.init();
		final Union union = School.getInstance().getUnion(1);
		Intent intent = new Intent(InstrumentationRegistry.getTargetContext(), MainActivity.class);
		intent.putExtra("UNION_ID", union.getId() - 1);
		_rule.launchActivity(intent);
		Espresso.onData(Matchers.anything())
				.inAdapterView(Matchers.allOf(ViewMatchers.withId(android.R.id.list), ViewMatchers.withParent(ViewMatchers.withChild(ViewMatchers.withId(R.id.photo)))))
				.atPosition(0)
				.onChildView(ViewMatchers.withId(R.id.image_mail)).perform(ViewActions.click());
		Intents.intending(IntentMatchers.hasAction(Intent.ACTION_SENDTO));
		Intents.release();
	}

	@Test
	public void clickOnFacebookFabTest() throws Exception {
		Intents.init();
		final Union union = School.getInstance().getUnion(1);
		Intent intent = new Intent(InstrumentationRegistry.getTargetContext(), MainActivity.class);
		intent.putExtra("UNION_ID", union.getId() - 1);
		_rule.launchActivity(intent);
		Espresso.onView(ViewMatchers.withId(R.id.facebook)).perform(ViewActions.click());
		Intents.intending(IntentMatchers.hasAction(Intent.ACTION_VIEW));
		Intents.release();
	}
}
