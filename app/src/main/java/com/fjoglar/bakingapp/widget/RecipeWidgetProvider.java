/*
 * Copyright 2018 Felipe Joglar Santos
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fjoglar.bakingapp.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

import com.fjoglar.bakingapp.R;

/**
 * Implementation of App Widget functionality.
 *
 * App Widget Configuration implemented in {@link RecipeWidgetProviderConfigureActivity
 * RecipeWidgetProviderConfigureActivity}
 */
public class RecipeWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Get the widget data
        String widgetTitle = RecipeWidgetProviderConfigureActivity
                .loadTitlePref(context, appWidgetId);
        String widgetIngredients = RecipeWidgetProviderConfigureActivity
                .loadIngredientsPref(context, appWidgetId);

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_recipe_provider);
        views.setTextViewText(R.id.appwidget_title, widgetTitle);
        views.setTextViewText(R.id.appwidget_ingredients, widgetIngredients);

        // Instruct the com.fjoglar.bakingapp.widget manager to update the com.fjoglar.bakingapp.widget
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
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the com.fjoglar.bakingapp.widget, delete the preference
        // associated with it.
        for (int appWidgetId : appWidgetIds) {
            RecipeWidgetProviderConfigureActivity.deleteTitlePref(context, appWidgetId);
            RecipeWidgetProviderConfigureActivity.deleteIngredientsPref(context, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first com.fjoglar.bakingapp.widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last com.fjoglar.bakingapp.widget is disabled
    }
}

