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

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.fjoglar.bakingapp.R;
import com.fjoglar.bakingapp.data.model.Recipe;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Displays recipe details screen.
 */
public class RecipeDetailActivity extends AppCompatActivity {

    public static final String EXTRA_RECIPE = "recipe";

    @BindView(R.id.framelayout_recipe_detail_container)
    FrameLayout mFrameLayoutRecipeDetailContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        ButterKnife.bind(this);

        // Get the requested recipe
        Recipe recipe = getIntent().getParcelableExtra(EXTRA_RECIPE);

        RecipeDetailFragment recipeDetailFragment = (RecipeDetailFragment) getSupportFragmentManager()
                .findFragmentById(R.id.framelayout_recipe_detail_container);

        if (recipeDetailFragment == null) {
            // Create a new recipe detail fragment
            recipeDetailFragment = new RecipeDetailFragment();

            // Set the recipe to show
            recipeDetailFragment.setRecipe(recipe);

            // Add the fragment to its container using a FragmentManager and a Transaction
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.framelayout_recipe_detail_container, recipeDetailFragment)
                    .commit();
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
}
