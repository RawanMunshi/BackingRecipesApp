package com.example.android.bakingrecipes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.android.bakingrecipes.Models.Step;

import java.util.ArrayList;

public class StepDetails extends AppCompatActivity implements
        StepDetailsFragment.PreviousButtonOnClickListener,
        StepDetailsFragment.NextButtonOnClickListener {

    private Step step;
    private ArrayList<Step> stepList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_details);

        // get the step details
        Intent intent = getIntent();
        if (intent.hasExtra("step_extra")) {
            step = (Step) intent.getParcelableExtra("step_extra");
            setTitle(step.getShortDescription());
        }

        // get the step list to use it in fragment replace
        if (intent.hasExtra("step_list")) {
            stepList = intent.getParcelableArrayListExtra("step_list");
        }

        // create step details fragment
        StepDetailsFragment detailsFragment =
                new StepDetailsFragment();
        if (step != null) {
            detailsFragment.setStepDetails(step);
            setTitle(step.getShortDescription());
        }
        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .add(R.id.step_details_container, detailsFragment)
                .commit();
    }

    @Override
    public void onPreviousButtonClicked(int id) {
        // if it is the first step
        if (id == 0) {
            Toast.makeText(StepDetails.this, "This is the first step", Toast.LENGTH_LONG).show();
        } else {
            displayFragment(id - 1);
        }
    }

    @Override
    public void onNextButtonClicked(int id) {
        // if it is the last step
        if (id == stepList.size() - 1) {
            Toast.makeText(StepDetails.this, "This is the last step", Toast.LENGTH_LONG).show();
        } else {
            displayFragment(id + 1);
        }
    }

    /**
     * Function used to replace the fragment with the
     * new fragment of Next/Previous step
     *
     * @param id of the step
     */
    public void displayFragment(int id) {
        if (id >= 0 && id < stepList.size()) {
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
}
