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
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import static android.support.test.InstrumentationRegistry.getInstrumentation;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class DashboardActivityTest extends InstrumentationTestCase {



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
    public void testOnClickHaraButton() throws Exception {
        final DashboardActivity lA =  activityRule.launchActivity(new Intent(Intent.ACTION_MAIN));
        onView(withId(R.id.HaraButton)).perform(click());
    }

    public void testOnClickAbdominalButton() throws Exception {

    }

}