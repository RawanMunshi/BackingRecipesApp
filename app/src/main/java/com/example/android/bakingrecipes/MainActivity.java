package com.example.android.bakingrecipes;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.bakingrecipes.Adepters.RecipeAdepter;
import com.example.android.bakingrecipes.IdlingResource.BakingAppIdlingResource;
import com.example.android.bakingrecipes.Models.BakingRecipesAPI;
import com.example.android.bakingrecipes.Models.Client;
import com.example.android.bakingrecipes.Models.Recipe;

import org.jetbrains.annotations.Nullable;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private List<Recipe> recipes;
    private RecipeAdepter recipeAdepter;
    private RecyclerView recipeListRv;
    private TextView errorMessage;
    private ProgressBar progressBar;

    @Nullable
    private BakingAppIdlingResource idlingResource;

    // Using for app test
    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (idlingResource == null) {
            idlingResource = new BakingAppIdlingResource();
        }
        return idlingResource;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        idlingResource = (BakingAppIdlingResource) getIdlingResource();

        loadBakingRecipes();

        // binding layout views
        recipeListRv = findViewById(R.id.recipe_list_rv);
        errorMessage = findViewById(R.id.error_message_tv);
        progressBar = findViewById(R.id.list_loading_pb);
        showProgressBar();

        // check if the user device is a tablet
        boolean isTablet = getResources().getBoolean(R.bool.isTablet);
        if(isTablet){
            recipeListRv.setLayoutManager(new GridLayoutManager(this, 3));
        }
        else{
            recipeListRv.setLayoutManager(new GridLayoutManager(this, 1));
        }

        // setup the recycle view
        recipeListRv.setHasFixedSize(true);
        recipeAdepter = new RecipeAdepter();
        recipeListRv.setAdapter(recipeAdepter);
        recipeAdepter.setOnItemClickListener(new RecipeAdepter.OnItemClickListener() {
            @Override
            public void onListItemClick(int position) {

                // if the user click on a recipe item open the recipe details
                Intent intent = new Intent(MainActivity.this, RecipeDetails.class);
                Recipe recipe = recipeAdepter.getRecipeList().get(position);
                for (int i = 0 ; i < recipe.getSteps().size() ; i++){
                    recipe.getSteps().get(i).setId(i);
                }
                intent.putExtra("recipe_details", recipe);
                startActivity(intent);
            }
        });
    }

    /**
     * A Function used to load the baking recipes
     */
    public void loadBakingRecipes(){

        BakingRecipesAPI recipesAPI = Client.getClient().create(BakingRecipesAPI.class);
        Call<List<Recipe>> listCall = recipesAPI.getRecipes();
        listCall.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {

                if(!response.isSuccessful()){
                    showErrorMessage();
                }
                hideErrorMessage();
                hideProgressBar();

                List<Recipe> recipeList = response.body();
                recipeAdepter.setRecipeList(recipeList);
                recipes = recipeList;
                if (idlingResource != null) {
                    idlingResource.setIdleState(true);
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                showErrorMessage();
            }
        });
    }

    /**
     * A Function used to show the error message and hide the recycler view
     */
    private void showErrorMessage() {
        recipeListRv.setVisibility(View.INVISIBLE);
        errorMessage.setText(R.string.error_message_no_connection);
        errorMessage.setVisibility(View.VISIBLE);
        hideProgressBar();
    }

    /**
     * A Function used to hide the error message and display the recycler view
     */
    private void hideErrorMessage() {
        errorMessage.setVisibility(View.INVISIBLE);
        recipeListRv.setVisibility(View.VISIBLE);
    }

    /**
     * A Function used to show the progress bar and hide the recycler view
     */
    private void showProgressBar() {
        recipeListRv.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }

    /**
     * A Function used to hide the progress bar and display the recycler view
     */
    private void hideProgressBar() {
        progressBar.setVisibility(View.INVISIBLE);
        recipeListRv.setVisibility(View.VISIBLE);
    }
}
