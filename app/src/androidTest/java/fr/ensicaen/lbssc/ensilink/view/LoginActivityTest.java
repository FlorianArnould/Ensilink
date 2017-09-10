package fr.ensicaen.lbssc.ensilink.view;

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

/**
 * @author Florian Arnould
 */
@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {
	@Rule
	public ActivityTestRule<LoginActivity> _rule = new ActivityTestRule<>(LoginActivity.class);

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
	public void wrongEmailTest() throws Exception {
		Espresso.onView(ViewMatchers.withId(R.id.input_email)).perform(ViewActions.typeText("email@email.fr"));
		Espresso.onView(ViewMatchers.withId(R.id.login_btn)).perform(ViewActions.click());
		String errorMessage = InstrumentationRegistry.getTargetContext().getResources().getString(R.string.wrong_email_address_message);
		Espresso.onView(ViewMatchers.withId(R.id.input_email)).check(ViewAssertions.matches(ViewMatchers.hasErrorText(errorMessage)));
	}

	@Test
	public void wrongCredentialsTest() throws Exception {
		Espresso.onView(ViewMatchers.withId(R.id.input_email)).perform(ViewActions.typeText("email@ecole.ensicaen.fr"));
		Espresso.onView(ViewMatchers.withId(R.id.input_password)).perform(ViewActions.typeText("test"));
		Espresso.onView(ViewMatchers.withId(R.id.login_btn)).perform(ViewActions.click());
		Thread.sleep(10000); // TODO: 10/09/17 Remove the Thread.sleep
		Espresso.onView(ViewMatchers.withText(R.string.authentification_error)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
	}
}
