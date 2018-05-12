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

package com.fjoglar.bakingapp.recipes;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fjoglar.bakingapp.R;
import com.fjoglar.bakingapp.data.model.Recipe;
import com.fjoglar.bakingapp.data.model.mapper.ModelDataMapper;
import com.fjoglar.bakingapp.data.source.RecipesRepository;
import com.fjoglar.bakingapp.data.source.local.RecipesLocalDataSource;
import com.fjoglar.bakingapp.data.source.local.db.RecipeDb;
import com.fjoglar.bakingapp.data.source.remote.RecipesRemoteDataSource;
import com.fjoglar.bakingapp.recipedetail.RecipeDetailActivity;
import com.fjoglar.bakingapp.util.schedulers.SchedulerProvider;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipesActivity extends AppCompatActivity implements RecipesContract.View,
        RecipesAdapter.RecipeClickListener {

    private static final String SAVED_STATE_RECIPES_LIST = "recipes_list";

    private RecipesContract.Presenter mRecipesPresenter;
    private RecipesAdapter mRecipesAdapter;
    private List<Recipe> mRecipesList;
    private boolean mForceLoad;

    // This idling resource will be used by Espresso to wait for and synchronize with
    // background threads.
    @Nullable
    CountingIdlingResource mIdlingResource = new CountingIdlingResource("GLOBAL");

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.recyclerview_recipes)
    RecyclerView mRecyclerViewRecipes;
    @BindView(R.id.progressbar_loading)
    ProgressBar mProgressBarLoading;
    @BindView(R.id.textview_recipes_emptyview)
    TextView mTextViewRecipesEmptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        // By default we force the load of the data from the repository.
        mForceLoad = true;
        if (savedInstanceState != null) {
            // Restore value of members from saved state
            mRecipesList = savedInstanceState.getParcelableArrayList(SAVED_STATE_RECIPES_LIST);

            // As we get the list from saved state we don't need to load the data
            // from the repository.
            mForceLoad = false;
        }

        setUpRecyclerView();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initPresenter();
        // TODO: Maybe we need to pass mForceLoad to BasePresenter.subscribe() as method parameter
        // and add the logic there, in the Presenter.
        if (mForceLoad) {
            mRecipesPresenter.subscribe();
        } else if (mRecipesList == null || mRecipesList.isEmpty()) {
            showEmptyView();
        } else {
            showRecipes(mRecipesList);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mRecipesPresenter.unsubscribe();
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
    protected void onSaveInstanceState(Bundle outState) {
        if (mRecipesAdapter.getRecipesList() != null) {
            outState.putParcelableArrayList(SAVED_STATE_RECIPES_LIST,
                    new ArrayList<>(mRecipesAdapter.getRecipesList()));
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void setPresenter(@NonNull RecipesContract.Presenter presenter) {
        mRecipesPresenter = presenter;
    }

    @Override
    public void showRecipes(List<Recipe> recipes) {
        mTextViewRecipesEmptyView.setVisibility(View.GONE);
        mRecyclerViewRecipes.setVisibility(View.VISIBLE);
        mRecipesAdapter.setRecipesList(recipes);
    }

    @Override
    public void showEmptyView() {
        mRecyclerViewRecipes.setVisibility(View.GONE);
        mTextViewRecipesEmptyView.setVisibility(View.VISIBLE);
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
    public void onRecipeClicked(Recipe recipe) {
        Intent recipeDetailActivityIntent = new Intent(this, RecipeDetailActivity.class);
        recipeDetailActivityIntent.putExtra(RecipeDetailActivity.EXTRA_RECIPE_ID, recipe.getId());
        startActivity(recipeDetailActivityIntent);
    }

    private void setUpRecyclerView() {
        mRecipesAdapter = new RecipesAdapter(this);

        mRecyclerViewRecipes.setHasFixedSize(true);
        mRecyclerViewRecipes.setLayoutManager(new GridLayoutManager(this,
                getResources().getInteger(R.integer.recipes_column_number)));
        mRecyclerViewRecipes.setAdapter(mRecipesAdapter);
    }

    private void initPresenter() {
        mRecipesPresenter = new RecipesPresenter(
                RecipesRepository.getInstance(
                        RecipesRemoteDataSource.getInstance(),
                        RecipesLocalDataSource.getInstance(
                                new ModelDataMapper(),
                                RecipeDb.getInstance(this))),
                this,
                SchedulerProvider.getInstance(),
                mIdlingResource);
    }

    @VisibleForTesting
    public CountingIdlingResource getCountingIdlingResource() {
        return mIdlingResource;
    }
}