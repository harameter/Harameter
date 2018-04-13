package com.example.harameter.harameter;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.support.test.filters.LargeTest;
import android.support.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.InstrumentationTestCase;

import android.test.TouchUtils;
import android.widget.Button;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.core.StringContains.containsString;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class DashboardActivityTest3 extends InstrumentationTestCase {



    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    public void tearDown() throws Exception {
        super.tearDown();
    }

    public void testOnCreate() throws Exception {
    }

    @Rule
    public ActivityTestRule<DashboardActivity> activityRule
            = new ActivityTestRule<>(
            DashboardActivity.class,
            true,     // initialTouchMode
            false);   // launchActivity. False to customize the intent



    @Test
    public void textOnClickSelection() throws Exception {
        final DashboardActivity lA =  activityRule.launchActivity(new Intent(Intent.ACTION_MAIN));
        onView(withId(R.id.select)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Beginner"))).perform(click());
        onView(withId(R.id.select)).check(matches(withSpinnerText(containsString("Beginner"))));
        activityRule.finishActivity();
    }

    @Test
    public void textOnClickSelection2() throws Exception {
        final DashboardActivity lA =  activityRule.launchActivity(new Intent(Intent.ACTION_MAIN));
        onView(withId(R.id.select)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Advanced"))).perform(click());
        onView(withId(R.id.select)).check(matches(withSpinnerText(containsString("Advanced"))));
        activityRule.finishActivity();
    }

}