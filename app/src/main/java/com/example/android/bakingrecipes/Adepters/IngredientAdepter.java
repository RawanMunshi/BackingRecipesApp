package com.example.android.bakingrecipes.Adepters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingrecipes.Models.Ingredient;
import com.example.android.bakingrecipes.R;

import java.util.ArrayList;

public class IngredientAdepter extends RecyclerView.Adapter<IngredientAdepter.IngredientItemHolder> {

    private ArrayList<Ingredient> ingredientList ;

    @NonNull
    @Override
    public IngredientItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        int layoutId = R.layout.ingredient_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutId, viewGroup , false);
        IngredientItemHolder itemHolder = new IngredientItemHolder(view);
        return itemHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientItemHolder holder, int position) {

        // bind the data to the view
        Ingredient ingredient = ingredientList.get(position);
        holder.ingredientTv.setText(ingredient.getIngredient());
        holder.quantityTv.setText(String.valueOf(ingredient.getQuantity()));
        holder.quantityTv.append(" ");
        holder.quantityTv.append(ingredient.getMeasure());
    }

    @Override
    public int getItemCount() {
        if(ingredientList == null ){return 0;}
        return ingredientList.size();
    }

    public void setIngredientList(ArrayList<Ingredient> ingredientList) {
        this.ingredientList = ingredientList;
    }

    class IngredientItemHolder extends RecyclerView.ViewHolder {

        TextView ingredientTv;
        TextView quantityTv;
        public IngredientItemHolder(@NonNull View itemView) {
            super(itemView);
            ingredientTv = itemView.findViewById(R.id.ingredient);
            quantityTv = itemView.findViewById(R.id.quantity);
        }
    }
}
