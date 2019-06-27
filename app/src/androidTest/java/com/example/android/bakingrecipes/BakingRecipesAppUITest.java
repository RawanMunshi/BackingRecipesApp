package com.example.android.bakingrecipes;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class BakingRecipesAppUITest {
    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);
    private IdlingResource idlingResource;

    @Before
    public void registerIdlingResources() {
        idlingResource = activityRule.getActivity().getIdlingResource();
        IdlingRegistry.getInstance().register(idlingResource);
    }

    @Test
    public void checkRecipeList_IsDisplay() {

        onView(ViewMatchers.withId(R.id.recipe_list_rv))
                .perform(RecyclerViewActions.scrollToPosition(3));

        onView(withText("Cheesecake")).check(matches(isDisplayed()));
    }

    @Test
    public void checkRecipeDetails_IsDisplayed() {

        onView(ViewMatchers.withId(R.id.recipe_list_rv))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

        onView(withId(R.id.ingredients_button)).check(matches(isDisplayed()));

        onView(withId(R.id.recipe_steps_label)).check(matches(isDisplayed()));

        onView(ViewMatchers.withId(R.id.stepsRecyclerView))
                .perform(RecyclerViewActions.scrollToPosition(0))
                .check(matches(isDisplayed()));

    }

    @Test
    public void checkStepDetails_IsDisplayed() {

        onView(ViewMatchers.withId(R.id.recipe_list_rv))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

        onView(ViewMatchers.withId(R.id.stepsRecyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(ViewMatchers.withId(R.id.playerView)).check(matches(isDisplayed()));

        onView(ViewMatchers.withId(R.id.step_full_description)).check(matches(isDisplayed()));

        onView(ViewMatchers.withId(R.id.next_button)).check(matches(isDisplayed()));

        onView(ViewMatchers.withId(R.id.previous_button)).check(matches(isDisplayed()));
    }

    @Test
    public void checkIngredientDetails__IsDisplayed() {

        onView(ViewMatchers.withId(R.id.recipe_list_rv))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

        onView(ViewMatchers.withId(R.id.ingredients_button)).perform(click());

        onView(withId(R.id.widget_button)).check(matches(isDisplayed()));

        onView(ViewMatchers.withId(R.id.ingredient_list_rv))
                .perform(RecyclerViewActions.scrollToPosition(2));

        onView(withText("granulated sugar")).check(matches(isDisplayed()));
    }

    @After
    public void unregisterIdlingResource() {
        if (idlingResource != null) {
            IdlingRegistry.getInstance().unregister(idlingResource);

        }
    }
}
