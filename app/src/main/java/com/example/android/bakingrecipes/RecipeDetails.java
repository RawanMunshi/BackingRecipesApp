package com.example.android.bakingrecipes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.android.bakingrecipes.Adepters.StepAdepter;
import com.example.android.bakingrecipes.Models.Ingredient;
import com.example.android.bakingrecipes.Models.Recipe;
import com.example.android.bakingrecipes.Models.Step;

import java.util.ArrayList;
import java.util.List;

public class RecipeDetails extends AppCompatActivity
        implements StepAdepter.OnStepClickListener,
        StepDetailsFragment.PreviousButtonOnClickListener,
        StepDetailsFragment.NextButtonOnClickListener{

    private Recipe recipeDetails;
    private boolean isTablet;
    private RelativeLayout ingredientsButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        // get recipe details
        Intent intent = getIntent();
        if (intent.hasExtra("recipe_details")) {
            recipeDetails = (Recipe) intent.getParcelableExtra("recipe_details");
            setTitle(recipeDetails.getName());

        }

        // create the step list fragment
        StepFragment stepFragment = new StepFragment();
        stepFragment.setStepList(recipeDetails.getSteps());
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.step_list_fragment_view,stepFragment);
        fragmentTransaction.commit();

        // if the user use a tablet create the step details fragment
        // to apply Master Detail Flow
        isTablet = getResources().getBoolean(R.bool.isTablet);
        if(isTablet && savedInstanceState == null){
            StepDetailsFragment detailsFragment =
                    new StepDetailsFragment();
            if (recipeDetails != null) {
                detailsFragment.setStepDetails(recipeDetails.getSteps().get(0));
            }
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.step_details_container, detailsFragment)
                    .commit();
        }

        // button used to open ingredients list
        ingredientsButton = (RelativeLayout) findViewById(R.id.ingredients_button);
        Log.v("ingredientsButton",ingredientsButton+"");

        ingredientsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayIngredient();
            }
        });

    }

    @Override
    public void onStepSelected(int position) {
        if(isTablet){
            // If the user uses the tablet replace the step details fragment
            StepDetailsFragment detailsFragment =
                    new StepDetailsFragment();
            if (recipeDetails != null) {
                detailsFragment.setStepDetails(recipeDetails.getSteps().get(position));
            }
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.step_details_container, detailsFragment)
                    .commit();
        } else {
            // If the user not use the tablet open the step details activity
            // and send the step details
            Step step = recipeDetails.getSteps().get(position);
            ArrayList<Step> stepList = (ArrayList<Step>) recipeDetails.getSteps();
            Intent intent = new Intent(this, StepDetails.class);
            intent.putExtra("step_extra", step);
            intent.putParcelableArrayListExtra("step_list", stepList);
            startActivity(intent);
        }
    }

    /**
     * Function used to open Ingredient details activity
     * when the user click on ingredients button
     */
    public void displayIngredient(){
        Intent intent = new Intent(this, IngredientDetails.class);
        ArrayList<Ingredient> ingredientList = (ArrayList<Ingredient>) recipeDetails.getIngredients();
        intent.putParcelableArrayListExtra("ingredient_list", ingredientList);
        intent.putExtra("recipe_name", recipeDetails.getName());
        startActivity(intent);
    }

    @Override
    public void onPreviousButtonClicked(int id) {
        // if it is the first step
        if(id == 0) {
            Toast.makeText(RecipeDetails.this, "This is the first step", Toast.LENGTH_LONG).show();
        } else {
            displayFragment(id-1);
        }
    }

    @Override
    public void onNextButtonClicked(int id) {
        // if it is the last step
        if (id == recipeDetails.getSteps().size()-1){
            Toast.makeText(RecipeDetails.this, "This is the last step", Toast.LENGTH_LONG).show();
        } else {
            displayFragment(id+1);
        }
    }

    /**
     * Function used to replace the fragment with the
     * new fragment of Next/Previous step
     * @param id of the step
     */
    public void displayFragment (int id){
        List<Step> stepList = recipeDetails.getSteps();
        if(id >= 0 && id < stepList.size() ) {
            StepDetailsFragment detailsFragment =
                    new StepDetailsFragment();
            if (stepList != null) {
                detailsFragment.setStepDetails(stepList.get(id));
                setTitle(stepList.get(id).getShortDescription());
            }
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.step_details_container, detailsFragment)
                    .commit();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("doNoting","doNoting");
    }
}
