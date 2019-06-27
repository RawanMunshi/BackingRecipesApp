package com.example.android.bakingrecipes;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.bakingrecipes.Adepters.StepAdepter;
import com.example.android.bakingrecipes.Models.Step;

import java.util.List;

public class StepFragment extends Fragment{


    private List<Step> stepList;
    private StepAdepter.OnStepClickListener callback;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {

        // create a recycler view with fragment items
        View view = inflater.inflate(R.layout.steps_fragment, container, false);

        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.stepsRecyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        StepAdepter adepter = new StepAdepter();
        recyclerView.setAdapter(adepter);

            adepter.setCallback(new StepAdepter.OnStepClickListener() {
            @Override
            public void onStepSelected(int position) {
                callback.onStepSelected(position);
            }
            });

        adepter.setSteps(stepList);

        return view;
    }

    public void setStepList(List<Step> stepList) {
        this.stepList = stepList;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            callback = (StepAdepter.OnStepClickListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString());
        }
    }
}
