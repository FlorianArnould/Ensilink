package fr.ensicaen.lbssc.ensilink.view;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
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
import java.util.concurrent.CountDownLatch;

import fr.ensicaen.lbssc.ensilink.R;
import fr.ensicaen.lbssc.ensilink.loader.DataManagerForTest;
import fr.ensicaen.lbssc.ensilink.storage.Event;
import fr.ensicaen.lbssc.ensilink.storage.OnSchoolDataListener;
import fr.ensicaen.lbssc.ensilink.storage.School;

@RunWith(AndroidJUnit4.class)
public class EventFragmentTest {
	@Rule
	public ActivityTestRule<MainActivity> _mainActivity = new ActivityTestRule<>(MainActivity.class);

	@BeforeClass
	public static void fillDatabase() throws Exception {
		DataManagerForTest.setDefaultLocalDatabase();
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
	public void fillListViewTest() throws Exception {
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

	@Test
	public void openEventTest() throws Exception {
		Espresso.onData(Matchers.anything()).inAdapterView(ViewMatchers.withId(android.R.id.list)).atPosition(3).perform(ViewActions.click());
		Espresso.onView(ViewMatchers.withId(R.id.eventTitle)).check(ViewAssertions.matches(ViewMatchers.withText("Caenpagne BDS 2017")));
	}
}
