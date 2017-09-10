package fr.ensicaen.lbssc.ensilink.view;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.ListView;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matchers;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import fr.ensicaen.lbssc.ensilink.R;
import fr.ensicaen.lbssc.ensilink.loader.DataManagerForTest;
import fr.ensicaen.lbssc.ensilink.storage.Event;
import fr.ensicaen.lbssc.ensilink.storage.School;

/**
 * @author Florian Arnould
 */
@RunWith(AndroidJUnit4.class)
public class SplashActivityTest {
	@Rule
	public ActivityTestRule<SplashActivity> _rule = new ActivityTestRule<>(SplashActivity.class);

	@BeforeClass
	public static void fillDatabase() throws Exception {
		DataManagerForTest.setDefaultLocalDatabase();
	}

	@Test
	public void correctlyLaunchedTest() throws Exception {
		Thread.sleep(5000); // TODO: 10/09/17 Remove the Thread.sleep
		Espresso.onView(ViewMatchers.withId(android.R.id.list)).check(ViewAssertions.matches(new BaseMatcher<View>() {
			@Override
			public boolean matches(Object item) {
				ListView list = (ListView)item;
				return list.getAdapter().getCount() == 6;
			}

			@Override
			public void describeTo(Description description) {

			}
		}));
		List<Event> events = School.getInstance().getEvents();
		for (int i = 0; i < events.size(); i++) {
			Espresso.onData(Matchers.anything()).inAdapterView(ViewMatchers.withId(android.R.id.list)).atPosition(i).onChildView(ViewMatchers.withId(R.id.listview_item_title)).check(ViewAssertions.matches(ViewMatchers.withText(events.get(i).getTitle())));
		}
	}
}
