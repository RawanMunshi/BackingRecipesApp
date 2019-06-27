package com.example.android.bakingrecipes.Models;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface BakingRecipesAPI {

    @GET("59121517_baking/baking.json")
    Call<List<Recipe>> getRecipes ();
}
