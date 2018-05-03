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

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fjoglar.bakingapp.R;
import com.fjoglar.bakingapp.data.model.Ingredient;
import com.fjoglar.bakingapp.data.model.Recipe;
import com.fjoglar.bakingapp.data.model.mapper.ModelDataMapper;
import com.fjoglar.bakingapp.data.source.RecipesRepository;
import com.fjoglar.bakingapp.data.source.local.RecipesLocalDataSource;
import com.fjoglar.bakingapp.data.source.local.db.RecipeDb;
import com.fjoglar.bakingapp.data.source.remote.RecipesRemoteDataSource;
import com.fjoglar.bakingapp.util.schedulers.SchedulerProvider;
import com.fjoglar.bakingapp.util.ui.UiUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * The configuration screen for the {@link RecipeWidgetProvider RecipeWidgetProvider} AppWidget.
 */
public class RecipeWidgetProviderConfigureActivity extends Activity
        implements RecipeWidgetContract.View, RecipeWidgetAdapter.WidgetRecipeClickListener {

    private static final String PREFS_NAME = "com.fjoglar.bakingapp.widget.RecipeWidgetProvider";
    private static final String PREF_TITLE_PREFIX_KEY = "title_appwidget_";
    private static final String PREF_INGREDIENTS_PREFIX_KEY = "ingredients_appwidget_";

    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    private RecipeWidgetContract.Presenter mRecipeWidgetPresenter;
    private RecipeWidgetAdapter mRecipeWidgetAdapter;

    @BindView(R.id.textview_widget_recipe_title)
    TextView mTextViewWidgetRecipeTitle;
    @BindView(R.id.recyclerview_widget_recipe_list)
    RecyclerView mRecyclerViewWidgetRecipeList;
    @BindView(R.id.progressbar_loading)
    ProgressBar mProgressBarLoading;

    public RecipeWidgetProviderConfigureActivity() {
        super();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the com.fjoglar.bakingapp.widget host to
        // cancel out of the com.fjoglar.bakingapp.widget placement if the user presses the back
        // button.
        setResult(RESULT_CANCELED);

        setContentView(R.layout.widget_recipe_provider_configure);

        ButterKnife.bind(this);

        setUpRecyclerView();

        // Find the com.fjoglar.bakingapp.widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app com.fjoglar.bakingapp.widget
        // ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initPresenter();
        mRecipeWidgetPresenter.subscribe();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mRecipeWidgetPresenter.unsubscribe();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void setPresenter(RecipeWidgetContract.Presenter presenter) {
        mRecipeWidgetPresenter = presenter;
    }

    @Override
    public void showRecipes(List<Recipe> recipes) {
        mTextViewWidgetRecipeTitle.setText(getText(R.string.widget_recipe_select_recipe));
        mRecipeWidgetAdapter.setRecipesList(recipes);
    }

    @Override
    public void showEmptyView() {
        mTextViewWidgetRecipeTitle.setText(getText(R.string.widget_recipe_empty_view));
    }

    @Override
    public void createWidget(Recipe recipe, List<Ingredient> ingredients) {
        final Context context = RecipeWidgetProviderConfigureActivity.this;

        // Save the values locally.
        saveTitlePref(context, mAppWidgetId, recipe.getName());
        saveIngredientsPref(context, mAppWidgetId, UiUtils.formatIngredientList(ingredients));

        // It is the responsibility of the configuration activity to update the app
        // com.fjoglar.bakingapp.widget
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        RecipeWidgetProvider.updateAppWidget(context, appWidgetManager, mAppWidgetId);

        // Make sure we pass back the original appWidgetId
        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }

    @Override
    public void showLoading() {
        mProgressBarLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        mProgressBarLoading.setVisibility(View.GONE);
    }

    @Override
    public void onWidgetRecipeClicked(Recipe recipe) {
        mRecipeWidgetPresenter.getIngredientsForRecipe(recipe);
    }

    // Write the prefix to the SharedPreferences object for this com.fjoglar.bakingapp.widget
    static void saveTitlePref(Context context, int appWidgetId, String text) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_TITLE_PREFIX_KEY + appWidgetId, text);
        prefs.apply();
    }

    // Read the prefix from the SharedPreferences object for this com.fjoglar.bakingapp.widget.
    // If there is no preference saved, get the default from a resource
    static String loadTitlePref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getString(PREF_TITLE_PREFIX_KEY + appWidgetId, null);
    }

    static void deleteTitlePref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_TITLE_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }

    // Write the prefix to the SharedPreferences object for this com.fjoglar.bakingapp.widget
    static void saveIngredientsPref(Context context, int appWidgetId, String text) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_INGREDIENTS_PREFIX_KEY + appWidgetId, text);
        prefs.apply();
    }

    // Read the prefix from the SharedPreferences object for this com.fjoglar.bakingapp.widget.
    // If there is no preference saved, get the default from a resource
    static String loadIngredientsPref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getString(PREF_INGREDIENTS_PREFIX_KEY + appWidgetId, null);
    }

    static void deleteIngredientsPref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_INGREDIENTS_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }

    private void setUpRecyclerView() {
        mRecipeWidgetAdapter = new RecipeWidgetAdapter(this);

        mRecyclerViewWidgetRecipeList.setHasFixedSize(true);
        mRecyclerViewWidgetRecipeList.setLayoutManager(new GridLayoutManager(this,
                getResources().getInteger(R.integer.recipes_column_number)));
        mRecyclerViewWidgetRecipeList.setAdapter(mRecipeWidgetAdapter);
    }

    private void initPresenter() {
        mRecipeWidgetPresenter = new RecipeWidgetPresenter(
                RecipesRepository.getInstance(
                        RecipesRemoteDataSource.getInstance(),
                        RecipesLocalDataSource.getInstance(
                                new ModelDataMapper(),
                                RecipeDb.getInstance(this))),
                this,
                SchedulerProvider.getInstance());
    }
}

