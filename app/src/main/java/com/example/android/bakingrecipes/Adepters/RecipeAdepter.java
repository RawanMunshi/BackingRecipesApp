package com.example.android.bakingrecipes.Adepters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.bakingrecipes.Models.Recipe;
import com.example.android.bakingrecipes.R;

import java.util.List;

public class RecipeAdepter extends RecyclerView.Adapter<RecipeAdepter.RecipeItemHolder> {

    private List<Recipe> recipeList;
    private OnItemClickListener clickListener;

    public interface OnItemClickListener {
        void onListItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        clickListener = listener;
    }


    @NonNull
    @Override
    public RecipeItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        int layoutId = R.layout.recipe_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutId, viewGroup, false);
        RecipeItemHolder itemHolder = new RecipeItemHolder(view);
        return itemHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeItemHolder holder, int position) {

        // bind the data to the view
        Recipe recipe = recipeList.get(position);
        int id = recipe.getId();

        // bind the correct image
        switch (id) {
            case 1: {
                holder.recipeImage.setImageResource(R.drawable.nutellapie);
                break;
            }
            case 2: {
                holder.recipeImage.setImageResource(R.drawable.brownies);
                break;
            }
            case 3: {
                holder.recipeImage.setImageResource(R.drawable.yellowcake);
                break;
            }
            case 4: {
                holder.recipeImage.setImageResource(R.drawable.cheesecake);
                break;
            }
            default: {
                holder.recipeImage.setImageResource(R.drawable.nutellapie);
                break;
            }
        }
        holder.recipeLabel.setText(recipe.getName());
    }

    @Override
    public int getItemCount() {
        if (recipeList == null) {
            return 0;
        }
        return recipeList.size();
    }

    public List<Recipe> getRecipeList() {
        return recipeList;
    }

    public void setRecipeList(List<Recipe> recipeList) {
        this.recipeList = recipeList;
        notifyDataSetChanged();
    }

    class RecipeItemHolder extends RecyclerView.ViewHolder {

        ImageView recipeImage;
        TextView recipeLabel;

        public RecipeItemHolder(@NonNull View itemView) {
            super(itemView);
            recipeImage = itemView.findViewById(R.id.recipe_iv);
            recipeLabel = itemView.findViewById(R.id.recipe_label_tv);

            // set an onClick handler for the RecyclerView item
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (clickListener != null) {
                        int position = getAdapterPosition();
                        clickListener.onListItemClick(position);
                    }
                }
            });
        }
    }
}
