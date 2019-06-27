package com.example.android.bakingrecipes;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.util.ArraySet;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;

import com.example.android.bakingrecipes.Adepters.IngredientAdepter;
import com.example.android.bakingrecipes.Models.Ingredient;

import java.util.ArrayList;
import java.util.Set;

public class IngredientDetails extends AppCompatActivity {

    private static final String RECIPE_INGREDIENTS_EXTRA = "recipe_ingredients_extra";
    private static final String RECIPE_NAME_EXTRA = "recipe_name_extra";

    RecyclerView ingredientListRv;
    IngredientAdepter ingredientAdepter;
    ArrayList<Ingredient> ingredientList;
    String recipeName = " ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient_details);


        // setup the recycle view
        ingredientListRv = findViewById(R.id.ingredient_list_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        ingredientListRv.setLayoutManager(layoutManager);

        ingredientListRv.setHasFixedSize(true);
        ingredientAdepter = new IngredientAdepter();
        ingredientListRv.setAdapter(ingredientAdepter);

        // get the ingredients list and recipe name
        Intent intent = getIntent();
        if (intent.hasExtra("ingredient_list")) {
            ingredientList = intent.getParcelableArrayListExtra("ingredient_list");
            ingredientAdepter.setIngredientList(ingredientList);
        }
        if (intent.hasExtra("recipe_name")) {
            recipeName = intent.getStringExtra("recipe_name");
        }

        setTitle(recipeName + " Ingredient");

        // Button used to add the ingredient list as screen widget
        Button widgetButton = findViewById(R.id.widget_button);
        widgetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveIngredientPreferences();
            }
        });

    }

    private void SaveIngredientPreferences() {

        // Save the widget data in the sharedPreferences
        // in case the user clicks in add widget button before creating the screen widget
        SharedPreferences sharedPreferences = getSharedPreferences("widget_data", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Convert the ingredients list into ingredients set
        // in order to save it in the sharedPreferences
        Set<String> ingredientsSet = new ArraySet<>();
        for (int i = 0; i < ingredientList.size(); i++) {
            ingredientsSet.add((ingredientList.get(i).toString()));
        }

        editor.putStringSet(RECIPE_INGREDIENTS_EXTRA, ingredientsSet);
        editor.putString(RECIPE_NAME_EXTRA, recipeName);
        editor.commit();

        // Update the screen widget
        Context context = this;
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget_provider);
        ComponentName thisWidget = new ComponentName(context, BakingAppWidgetProvider.class);
        remoteViews.setTextViewText(R.id.appwidget_title, recipeName + " Ingredient");

        Intent serviceIntent = new Intent(context, BakingAppWidgetService.class);
        remoteViews.setRemoteAdapter(R.id.widget_grid_view, serviceIntent);
        remoteViews.setEmptyView(R.id.widget_grid_view, R.id.empty_view);

        int[] AppWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, BakingAppWidgetProvider.class));
        appWidgetManager.notifyAppWidgetViewDataChanged(AppWidgetIds, R.id.widget_grid_view);

        appWidgetManager.updateAppWidget(thisWidget, remoteViews);
    }
}
