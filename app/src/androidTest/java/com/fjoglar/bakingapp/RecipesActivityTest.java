/*
 * Copyright 2018 Felipe Joglar Santos
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fjoglar.bakingapp;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.NoActivityResumedException;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.fjoglar.bakingapp.recipes.RecipesActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.Assert.fail;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class RecipesActivityTest {

    private static final String RECIPE_NAME = "Nutella Pie";

    /**
     * {@link RecipesActivity} is a JUnit {@link Rule @Rule} to launch your activity under test.
     * <p>
     * Rules are interceptors which are executed for each test method and are important building
     * blocks of Junit tests.
     */
    @Rule
    public ActivityTestRule<RecipesActivity> mTasksActivityTestRule =
            new ActivityTestRule<RecipesActivity>(RecipesActivity.class);

    /**
     * Prepare your test fixture for this test. In this case we register an IdlingResources with
     * Espresso. IdlingResource resource is a great way to tell Espresso when your app is in an
     * idle state. This helps Espresso to synchronize your test actions, which makes tests significantly
     * more reliable.
     */
    @Before
    public void setUp() throws Exception {
        IdlingRegistry.getInstance().register(
                mTasksActivityTestRule.getActivity().getCountingIdlingResource());
    }

    @Test
    public void recipesRecyclerViewIsDisplayed() {
        onView(withId(R.id.recyclerview_recipes)).check(matches(isDisplayed()));
    }

    @Test
    public void backFromTasksScreen_ExitsApp() {
        // From the recipes screen, press back should exit the app.
        assertPressingBackExitsApp();
    }

    /**
     * Unregister your Idling Resource so it can be garbage collected and does not leak any memory.
     */
    @After
    public void tearDown() throws Exception {
        IdlingRegistry.getInstance().unregister(
                mTasksActivityTestRule.getActivity().getCountingIdlingResource());
    }

    private void assertPressingBackExitsApp() {
        try {
            pressBack();
            fail("Should kill the app and throw an exception");
        } catch (NoActivityResumedException e) {
            // Test OK
        }
    }
}
