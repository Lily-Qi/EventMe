package edu.usc.eventme;

import android.app.Activity;
import android.view.KeyEvent;
import android.view.View;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.fragment.app.FragmentTransaction;
import androidx.test.InstrumentationRegistry;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.pressKey;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import java.lang.Thread;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ExploreFragmetnAndroidTestEspresso {
    @Rule public ActivityScenarioRule<MainActivity> activityScenarioRule
            = new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void openexplore() throws UiObjectNotFoundException, InterruptedException {
        ExploreFragment exp = new ExploreFragment();
        activityScenarioRule.getScenario().onActivity(activity -> {
            FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentContainerView, exp);
            transaction.commit();
        });
        Thread.sleep(2000);
    }

    @Test
    public void Test_ArtCategory() throws UiObjectNotFoundException, InterruptedException {
        onView(withId(R.id.image_food)).perform(click());
        Thread.sleep(2000);
        onView(ViewMatchers.withId(R.id.recyclerView))
                // scrollTo will fail the test if no item matches.
                .perform(RecyclerViewActions.scrollTo(
                        ViewMatchers.hasDescendant(withText("Food truck for snack"))
                ));
        onView(withText("Food truck for snack")).check(matches(isDisplayed()));
        onView(withText("Chloë Bass: Wayfinding")).check(doesNotExist());
        //onView(withText("Food truck for snack")).perform(click());
    }

    @Test
    public void Test_SearchbyKey() throws UiObjectNotFoundException, InterruptedException {
        onView(withId(R.id.searchView)).perform(click());
        onView(withId(R.id.searchView)).perform(typeText("Food truck for snack"),pressKey(
                KeyEvent.KEYCODE_ENTER));
        Thread.sleep(2000);

        onView(withText("Food truck for snack")).check(matches(isDisplayed()));
        onView(withId(R.id.recyclerView)).check(new RecyclerViewItemCountAssertion(1));
        //onView(withText("Chloë Bass: Wayfinding")).check(doesNotExist());
        //onView(withText("Food truck for snack")).perform(click());
    }
}
