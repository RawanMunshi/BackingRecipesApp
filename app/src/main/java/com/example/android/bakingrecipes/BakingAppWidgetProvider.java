package com.example.android.bakingrecipes;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import static android.content.Context.MODE_PRIVATE;

/**
 * Implementation of App Widget functionality.
 */
public class BakingAppWidgetProvider extends AppWidgetProvider {

    private static final String RECIPE_NAME_EXTRA = "recipe_name_extra";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget_provider);
        Intent intent = new Intent(context,MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.appwidget_title, pendingIntent);


        // Used to load the saved widget data in case the user
        // clicks in add widget button before creating the screen widget
        SharedPreferences sharedPreferences = context.getSharedPreferences("widget_data", MODE_PRIVATE);
        String recipeName = sharedPreferences.getString(RECIPE_NAME_EXTRA,"");
        views.setTextViewText(R.id.appwidget_title, recipeName+" Ingredient");


        Intent serviceIntent = new Intent(context, BakingAppWidgetService.class);
        views.setRemoteAdapter(R.id.widget_grid_view, serviceIntent);
        views.setEmptyView(R.id.widget_grid_view,R.id.empty_view);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_grid_view);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created

    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

