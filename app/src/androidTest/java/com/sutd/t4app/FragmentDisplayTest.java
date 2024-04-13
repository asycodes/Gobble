package com.sutd.t4app;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import com.sutd.t4app.LoginSignUpActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
@RunWith(AndroidJUnit4.class)
public class FragmentDisplayTest {

    @Rule
    public ActivityScenarioRule<LoginSignUpActivity> activityRule = new ActivityScenarioRule<>(LoginSignUpActivity.class);

    @Test
    public void testFragmentInflation() {
        onView(withId(R.id.nav_host_fragment_login_signup)).check(matches(isDisplayed()));
        onView(withId(R.id.emailEditText)).check(matches(isDisplayed()));
    }
}