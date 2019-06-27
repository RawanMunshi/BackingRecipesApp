package com.example.android.bakingrecipes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.Set;

public class BakingAppWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new BakingAppWidgetItemFactory(getApplicationContext(), intent);
    }

    class BakingAppWidgetItemFactory implements RemoteViewsFactory {

        private static final String RECIPE_INGREDIENTS_EXTRA = "recipe_ingredients_extra";
        private Context context;
        private int appWidgetId;
        private String[] ingredientList;

        public BakingAppWidgetItemFactory(Context context, Intent intent) {
            this.context = context;
        }

        @Override
        public void onCreate() {
            // Loading the ingredients list from the sharedPreferences
            SharedPreferences sharedPreferences = context.getSharedPreferences("widget_data", MODE_PRIVATE);
            Set<String> ingredientsSet = sharedPreferences.getStringSet(RECIPE_INGREDIENTS_EXTRA, null);

            // Convert the ingredientsSet into Array
            if (ingredientsSet != null) {
                ingredientList = new String[ingredientsSet.size()];
                int i = 0;
                for (String s : ingredientsSet) {
                    ingredientList[i++] = s;
                }
            }
        }

        @Override
        public void onDataSetChanged() {

            // Updating the ingredients list from the sharedPreferences
            SharedPreferences sharedPreferences = context.getSharedPreferences("widget_data", MODE_PRIVATE);
            Set<String> ingredientsSet = sharedPreferences.getStringSet(RECIPE_INGREDIENTS_EXTRA, null);

            // Convert the ingredientsSet into Array
            if (ingredientsSet != null) {
                ingredientList = new String[ingredientsSet.size()];
                int i = 0;
                for (String s : ingredientsSet) {
                    ingredientList[i++] = s;
                }
            }
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            if (ingredientList == null) {
                return 0;
            }
            return ingredientList.length;
        }

        @Override
        public RemoteViews getViewAt(int position) {
            // bind the views
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widget_item);
            if (ingredientList != null) {
                views.setTextViewText(R.id.appwidget_text, (position + 1) + "- " + ingredientList[position]);
            }
            return views;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
