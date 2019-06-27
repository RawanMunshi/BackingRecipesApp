package com.example.android.bakingrecipes.Adepters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingrecipes.Models.Step;
import com.example.android.bakingrecipes.R;

import java.util.List;

public class StepAdepter extends RecyclerView.Adapter<StepAdepter.StepViewHolder> {

    private List<Step> steps;
    private OnStepClickListener callback;

    public void setCallback(OnStepClickListener callback) {
        this.callback = callback;
    }

    @NonNull
    @Override
    public StepViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        int layoutId = R.layout.step_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutId, viewGroup , false);
        StepViewHolder itemHolder = new StepViewHolder(view);
        return itemHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull StepViewHolder holder, int position) {
        Step step = steps.get(position);
        holder.stepShortDescriptionTv.setText(step.getShortDescription());
    }

    @Override
    public int getItemCount() {
        if(steps == null) {return 0;}
        return steps.size();
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public interface OnStepClickListener {
        void onStepSelected(int position);
    }

    class StepViewHolder extends RecyclerView.ViewHolder {

        TextView stepShortDescriptionTv;

        public StepViewHolder(@NonNull View itemView) {
            super(itemView);
            stepShortDescriptionTv = itemView.findViewById(R.id.step_short_description);

            // set an onClick handler for the RecyclerView item
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (callback != null) {
                        int position = getAdapterPosition();
                        callback.onStepSelected(position);
                    }
                }
            });
        }
    }

}
