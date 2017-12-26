package fr.ensicaen.lbssc.ensilink.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.DrawerMatchers;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.view.ViewPager;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import fr.ensicaen.lbssc.ensilink.R;
import fr.ensicaen.lbssc.ensilink.loader.DataManagerForTest;
import fr.ensicaen.lbssc.ensilink.storage.OnRefreshListener;
import fr.ensicaen.lbssc.ensilink.storage.OnSchoolDataListener;
import fr.ensicaen.lbssc.ensilink.storage.School;
import fr.ensicaen.lbssc.ensilink.storage.Student;
import fr.ensicaen.lbssc.ensilink.storage.Union;
import fr.ensicaen.lbssc.ensilink.view.unionscreen.UnionFragment;

/**
 * @author Florian Arnould
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
	@Rule
	public ActivityTestRule<MainActivity> _rule = new ActivityTestRule<>(MainActivity.class);

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
	public void fillDrawerTest() throws Exception {
		Espresso.onView(ViewMatchers.withId(R.id.drawer_layout)).perform(DrawerActions.open());
		Espresso.onView(ViewMatchers.withId(R.id.drawer_layout)).check(ViewAssertions.matches(DrawerMatchers.isOpen()));
		List<Union> unions = School.getInstance().getUnions();
		for (Union union : unions) {
			Espresso.onView(ViewMatchers.withText(union.getName())).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
		}
	}

	@Test
	public void openUnionFromDrawerTest() throws Exception {
		Espresso.onView(ViewMatchers.withId(R.id.drawer_layout)).perform(DrawerActions.open());
		Union union = School.getInstance().getUnion(2);
		Espresso.onView(Matchers.allOf(ViewMatchers.withId(R.id.design_menu_item_text), ViewMatchers.withText(union.getName()))).perform(ViewActions.click());
		Student student = union.getStudents().get(0);
		String name = student.getName() + " \"" + student.getNickname() + "\" " + student.getLastName();
		Espresso.onView(ViewMatchers.withText(name)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
	}

	@Test
	public void changeUnionFromDrawerTest() throws Exception {
		Union union = School.getInstance().getUnion(2);
		Union union2 = School.getInstance().getUnion(3);
		Espresso.onView(ViewMatchers.withId(R.id.drawer_layout)).perform(DrawerActions.open());
		Espresso.onView(Matchers.allOf(ViewMatchers.withId(R.id.design_menu_item_text), ViewMatchers.withText(union.getName()))).perform(ViewActions.click());
		Espresso.onView(ViewMatchers.withId(R.id.drawer_layout)).perform(DrawerActions.open());
		Espresso.onView(Matchers.allOf(ViewMatchers.withId(R.id.design_menu_item_text), ViewMatchers.withText(union2.getName()))).perform(ViewActions.click());
		Student student = union2.getStudents().get(0);
		String name = student.getName() + " \"" + student.getNickname() + "\" " + student.getLastName();
		Espresso.onView(ViewMatchers.withText(name)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
	}

	@Test
	public void backPressToCloseDrawerTest() throws Exception {
		Espresso.onView(ViewMatchers.withId(R.id.drawer_layout)).perform(DrawerActions.open());
		Espresso.pressBack();
		Espresso.onView(ViewMatchers.withId(R.id.drawer_layout)).check(ViewAssertions.matches(DrawerMatchers.isClosed()));
	}

	@Test
	public void refreshTest() throws Exception {
		final CountDownLatch signal = new CountDownLatch(1);
		School.getInstance().setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				signal.countDown();
			}
		});
		Espresso.onView(ViewMatchers.withId(android.R.id.list)).perform(ViewActions.swipeDown());
		signal.await();
	}

	@Test
	public void changeToolbarTitleTest() throws Exception {
		final MainActivity activity = _rule.getActivity();
		final String title = "RandomTextForTest";
		final CountDownLatch signal = new CountDownLatch(1);
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				activity.setActionBarTitle(title);
				signal.countDown();
			}
		});
		signal.await();
		Espresso.onView(ViewMatchers.withText(title)).check(ViewAssertions.matches(ViewMatchers.withParent(ViewMatchers.withId(R.id.toolbar))));
	}

	@Test
	public void startMainActivityOnUnionTest() throws Exception {
		Union union = School.getInstance().getUnion(3);
		Intent intent = new Intent(InstrumentationRegistry.getTargetContext(), MainActivity.class);
		intent.putExtra("UNION_ID", union.getId() - 1);
		_rule.launchActivity(intent);
		Student student = union.getStudents().get(0);
		String name = student.getName() + " \"" + student.getNickname() + "\" " + student.getLastName();
		Espresso.onView(ViewMatchers.withText(name)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

	}

	@Test
	public void moveToNewsFragmentTest() throws Exception {
		Union union = School.getInstance().getUnion(3);
		Intent intent = new Intent(InstrumentationRegistry.getTargetContext(), MainActivity.class);
		intent.putExtra("UNION_ID", union.getId() - 1);
		_rule.launchActivity(intent);
		Espresso.onView(ViewMatchers.withId(R.id.drawer_layout)).perform(DrawerActions.open());
		Espresso.onView(Matchers.allOf(ViewMatchers.withId(R.id.design_menu_item_text), ViewMatchers.withText(R.string.news))).perform(ViewActions.click());
		Espresso.onData(Matchers.anything()).inAdapterView(ViewMatchers.withId(android.R.id.list)).atPosition(0).onChildView(ViewMatchers.withId(R.id.listview_item_title)).check(ViewAssertions.matches(ViewMatchers.withText("Les 40 ans de l'ENSICAEN")));
	}

	@Test
	public void openSettingsActivityTest() throws Exception {
		Espresso.onView(ViewMatchers.withId(R.id.drawer_layout)).perform(DrawerActions.open());
		Espresso.onView(ViewMatchers.withId(R.id.drawer_layout)).perform(ViewActions.swipeUp());
		Espresso.onView(Matchers.allOf(ViewMatchers.withId(R.id.design_menu_item_text), ViewMatchers.withText(R.string.settings))).perform(ViewActions.click());
		Espresso.onView(ViewMatchers.withText(R.string.settings)).check(ViewAssertions.matches(ViewMatchers.withParent(ViewMatchers.withId(R.id.action_bar))));
	}

	@Test
	public void openCreditsActivityTest() throws Exception {
		Espresso.onView(ViewMatchers.withId(R.id.drawer_layout)).perform(DrawerActions.open());
		Espresso.onView(ViewMatchers.withId(R.id.drawer_layout)).perform(ViewActions.swipeUp());
		Espresso.onView(Matchers.allOf(ViewMatchers.withId(R.id.design_menu_item_text), ViewMatchers.withText(R.string.credits))).perform(ViewActions.click());
		Espresso.onView(ViewMatchers.withText(R.string.credits)).check(ViewAssertions.matches(ViewMatchers.withParent(ViewMatchers.withId(R.id.action_bar))));
	}

	@Test
	public void openLoginActivityTest() throws Exception {
		Espresso.onView(ViewMatchers.withId(R.id.drawer_layout)).perform(DrawerActions.open());
		Espresso.onView(ViewMatchers.withId(R.id.drawer_layout)).perform(ViewActions.swipeUp());
		Espresso.onView(Matchers.allOf(ViewMatchers.withId(R.id.design_menu_item_text), ViewMatchers.withText(R.string.login))).perform(ViewActions.click());
		Espresso.onView(ViewMatchers.withId(R.id.input_email)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
	}

	@Test
	public void logoutInDrawerTest() throws Exception {
		Context context = InstrumentationRegistry.getTargetContext();
		SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
		preferences.edit().putString("email", "email@email.fr").putString("password", "somePassword").apply();
		final CountDownLatch signal = new CountDownLatch(1);
		School.getInstance().refreshData(InstrumentationRegistry.getTargetContext(), new OnSchoolDataListener() {
			@Override
			public void onDataRefreshed() {
				signal.countDown();
			}
		});
		signal.await();
		_rule.launchActivity(new Intent(context, MainActivity.class));
		Espresso.onView(ViewMatchers.withId(R.id.drawer_layout)).perform(DrawerActions.open());
		Espresso.onView(ViewMatchers.withId(R.id.drawer_layout)).perform(ViewActions.swipeUp());
		Espresso.onView(Matchers.allOf(ViewMatchers.withId(R.id.design_menu_item_text), ViewMatchers.withText(R.string.logout))).perform(ViewActions.click());
		Assert.assertFalse(School.getInstance().isConnected());
	}

	@Test
	public void slideToClubsAndRefreshTest() throws Exception {
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
		Espresso.onView(ViewMatchers.withId(R.id.photo)).perform(ViewActions.swipeLeft());
		final CountDownLatch signal2 = new CountDownLatch(1);
		School.getInstance().setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				signal2.countDown();
			}
		});
		signal.await();
		Espresso.onView(Matchers.allOf(ViewMatchers.withParent(ViewMatchers.withId(R.id.clubs_list_parent)), ViewMatchers.withId(android.R.id.list))).perform(ViewActions.swipeDown());
		signal2.await();
	}
}
