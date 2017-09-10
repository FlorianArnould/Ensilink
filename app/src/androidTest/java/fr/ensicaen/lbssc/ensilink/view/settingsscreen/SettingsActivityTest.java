package fr.ensicaen.lbssc.ensilink.view.settingsscreen;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

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
import fr.ensicaen.lbssc.ensilink.storage.Club;
import fr.ensicaen.lbssc.ensilink.storage.OnSchoolDataListener;
import fr.ensicaen.lbssc.ensilink.storage.School;
import fr.ensicaen.lbssc.ensilink.storage.Union;
import fr.ensicaen.lbssc.ensilink.view.MainActivity;

/**
 * @author Florian Arnould
 */
@RunWith(AndroidJUnit4.class)
public class SettingsActivityTest {
	@Rule
	public ActivityTestRule<SettingsActivity> _rule = new ActivityTestRule<>(SettingsActivity.class);
	@Rule
	public ActivityTestRule<MainActivity> _mainRule;

	@BeforeClass
	public static void initialize() throws Exception {
		DataManagerForTest.setDefaultLocalDatabase();
		final CountDownLatch signal = new CountDownLatch(1);
		School.getInstance().loadLocalData(InstrumentationRegistry.getTargetContext(), new OnSchoolDataListener() {
			@Override
			public void OnDataRefreshed() {
				signal.countDown();
			}
		});
		signal.await();
		Context context = InstrumentationRegistry.getTargetContext();
		List<Union> unions = School.getInstance().getUnions();
		SharedPreferences.Editor pref = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE).edit();
		for (Union union : unions) {
			pref.remove(union.getName());
			for (Club club : union.getClubs()) {
				pref.remove(club.getName());
			}
		}
		pref.apply();
	}

	@Test
	public void saveNewUnionPreference() throws Exception {
		Union union = School.getInstance().getUnion(1);
		Espresso.onView(ViewMatchers.withText(union.getName())).perform(ViewActions.click());
		Espresso.onView(ViewMatchers.withId(R.id.union_switch)).perform(ViewActions.click());
		Context context = InstrumentationRegistry.getTargetContext();
		SharedPreferences pref = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
		Assert.assertTrue(pref.getBoolean(union.getName(), false));
	}

	@Test
	public void removeExistingUnionPreference() throws Exception {
		Union union = School.getInstance().getUnion(2);
		Espresso.onView(ViewMatchers.withText(union.getName())).perform(ViewActions.click());
		Espresso.onView(ViewMatchers.withId(R.id.union_switch)).perform(ViewActions.click());
		Espresso.onView(ViewMatchers.withId(R.id.union_switch)).perform(ViewActions.click());
		Context context = InstrumentationRegistry.getTargetContext();
		SharedPreferences pref = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
		Assert.assertFalse(pref.getBoolean(union.getName(), false));
	}

	@Test
	public void saveNewClubPreference() throws Exception {
		Union union = School.getInstance().getUnion(1);
		Club club = union.getClub(0);
		Espresso.onView(ViewMatchers.withText(union.getName())).perform(ViewActions.click());
		Espresso.onView(Matchers.allOf(ViewMatchers.withParent(ViewMatchers.withChild(ViewMatchers.withText(club.getName()))), ViewMatchers.withId(R.id.switch_button))).perform(ViewActions.click());
		Context context = InstrumentationRegistry.getTargetContext();
		SharedPreferences pref = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
		Assert.assertTrue(pref.getBoolean(club.getName(), false));
	}

	@Test
	public void removeExistingClubPreference() throws Exception {
		Union union = School.getInstance().getUnion(1);
		Club club = union.getClub(1);
		Espresso.onView(ViewMatchers.withText(union.getName())).perform(ViewActions.click());
		Espresso.onView(Matchers.allOf(ViewMatchers.withParent(ViewMatchers.withChild(ViewMatchers.withText(club.getName()))), ViewMatchers.withId(R.id.switch_button))).perform(ViewActions.click());
		Espresso.onView(Matchers.allOf(ViewMatchers.withParent(ViewMatchers.withChild(ViewMatchers.withText(club.getName()))), ViewMatchers.withId(R.id.switch_button))).perform(ViewActions.click());
		Context context = InstrumentationRegistry.getTargetContext();
		SharedPreferences pref = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
		Assert.assertFalse(pref.getBoolean(club.getName(), false));
	}

	@Test
	public void closeWithHomeAsUpClubTest() throws Exception {
		Union union = School.getInstance().getUnion(1);
		Espresso.onView(ViewMatchers.withText(union.getName())).perform(ViewActions.click());
		Espresso.onView(ViewMatchers.withContentDescription(R.string.abc_action_bar_up_description)).perform(ViewActions.click());
		Espresso.onView(ViewMatchers.withText(School.getInstance().getUnion(2).getName())).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
	}

	@Test
	public void closeWithHomeAsUpUnionTest() throws Exception {
		Context context = InstrumentationRegistry.getTargetContext();
		Intent intent = new Intent(context, MainActivity.class);
		_mainRule = new ActivityTestRule<>(MainActivity.class);
		_mainRule.launchActivity(intent);
		Espresso.onView(ViewMatchers.withId(R.id.drawer_layout)).perform(DrawerActions.open());
		Espresso.onView(ViewMatchers.withText(R.string.settings)).perform(ViewActions.click());
		Espresso.onView(ViewMatchers.withContentDescription(R.string.abc_action_bar_up_description)).perform(ViewActions.click());
		Espresso.onView(ViewMatchers.withText(School.getInstance().getEvents().get(0).getTitle())).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
	}
}
