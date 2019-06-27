package com.example.android.bakingrecipes;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static org.hamcrest.core.IsNot.not;

@RunWith(AndroidJUnit4.class)
public class IntentTest {
    @Rule
    public IntentsTestRule<MainActivity> activityRule = new IntentsTestRule<>(
            MainActivity.class);

    private IdlingResource idlingResource;

    @Before
    public void registerIdlingResources() {
        idlingResource = activityRule.getActivity().getIdlingResource();
        IdlingRegistry.getInstance().register(idlingResource);
    }

    @Before
    public void stubAllExternalIntents() {
        intending(not(isInternal())).respondWith(
                new Instrumentation.ActivityResult(Activity.RESULT_OK, null));
    }

    @Test
    public void clickItemInRecipeList_OpensRecipeDetailsActivity() {

        // Let the UI load completely first
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        onView(ViewMatchers.withId(R.id.recipe_list_rv))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

        intended(hasComponent(
                RecipeDetails.class.getName()));
    }

    /**
     * In_RecipeDetailsActivity_
     */

    @Test
    public void clickItemInStepList_OpensStepDetails() {

        boolean isTablet = InstrumentationRegistry.getTargetContext()
                .getResources().getBoolean(R.bool.isTablet);

        onView(ViewMatchers.withId(R.id.recipe_list_rv))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

        onView(ViewMatchers.withId(R.id.stepsRecyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        if(!isTablet) {
            intended(hasComponent(
                    StepDetails.class.getName()));
        }
    }

    @Test
    public void clickIngredientButton_OpensIngredientDetails() {

        // Let the UI load completely first
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        onView(ViewMatchers.withId(R.id.recipe_list_rv))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

        onView(ViewMatchers.withId(R.id.ingredients_button)).perform(click());

        intended(hasComponent(
                IngredientDetails.class.getName()));
    }

    @After
    public void unregisterIdlingResource() {
        if (idlingResource != null) {
            IdlingRegistry.getInstance().unregister(idlingResource);

        }
    }
}
