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

package com.fjoglar.bakingapp.recipedetail;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.fjoglar.bakingapp.R;
import com.fjoglar.bakingapp.data.model.Step;
import com.fjoglar.bakingapp.stepdetail.StepDetailActivity;
import com.fjoglar.bakingapp.stepdetail.StepDetailFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Displays recipe details screen.
 */
public class RecipeDetailActivity extends AppCompatActivity implements
        RecipeDetailFragment.OnItemClickListener,
        StepDetailFragment.StepNavigationClickListener {

    @NonNull
    public static final String EXTRA_RECIPE_ID = "recipe_id";

    private boolean mTwoPane;

    // This idling resource will be used by Espresso to wait for and synchronize with
    // background threads.
    @Nullable
    CountingIdlingResource mIdlingResource = new CountingIdlingResource("GLOBAL");

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        if (intent == null || !intent.hasExtra(EXTRA_RECIPE_ID)) {
            closeOnError();
            return;
        }

        // Get the requested recipe
        int recipeId = intent.getIntExtra(EXTRA_RECIPE_ID, 0);

        RecipeDetailFragment recipeDetailFragment = (RecipeDetailFragment) getSupportFragmentManager()
                .findFragmentById(R.id.framelayout_recipe_detail_container);

        if (recipeDetailFragment == null) {
            // Create a new recipe detail fragment
            recipeDetailFragment = RecipeDetailFragment.newInstance(recipeId, mIdlingResource);

            // Add the fragment to its container using a FragmentManager and a Transaction
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.framelayout_recipe_detail_container, recipeDetailFragment)
                    .commit();
        }

        if (findViewById(R.id.linearlayout_recipe_detail_tablet_container) != null) {
            mTwoPane = true;

            StepDetailFragment stepDetailFragment = (StepDetailFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.framelayout_step_detail_container);

            if (stepDetailFragment == null) {
                // Create a new recipe detail fragment
                stepDetailFragment = StepDetailFragment.newInstance(recipeId, 0);

                // Add the fragment to its container using a FragmentManager and a Transaction
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.framelayout_step_detail_container, stepDetailFragment)
                        .commit();
            }
        } else {
            mTwoPane = false;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
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
    public void onItemClicked(Step step) {
        if (mTwoPane) {
            replaceStepFragment(step.getRecipeId(), step.getId());
        } else {
            Intent stepDetailActivityIntent = new Intent(this, StepDetailActivity.class);
            stepDetailActivityIntent.putExtra(StepDetailActivity.EXTRA_RECIPE_ID, step.getRecipeId());
            stepDetailActivityIntent.putExtra(StepDetailActivity.EXTRA_STEP_ID, step.getId());
            startActivity(stepDetailActivityIntent);
        }
    }

    @Override
    public void onNextStepClicked(Step step) {
        // Not used. Needed for tablet layout compatibility.
    }

    @Override
    public void onPreviousStepClicked(Step step) {
        // Not used. Needed for tablet layout compatibility.
    }

    private void closeOnError() {
        finish();
        Toast.makeText(getApplicationContext(), R.string.recipe_detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void replaceStepFragment(int recipeId, int stepId) {
        // Set the step to show
        StepDetailFragment stepDetailFragment = StepDetailFragment.newInstance(recipeId, stepId);

        // Replace the fragment to its container using a FragmentManager and a Transaction
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.framelayout_step_detail_container, stepDetailFragment)
                .commit();
    }

    @VisibleForTesting
    public CountingIdlingResource getCountingIdlingResource() {
        return mIdlingResource;
    }
}