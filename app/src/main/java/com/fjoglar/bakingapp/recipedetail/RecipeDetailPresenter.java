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

import android.support.annotation.NonNull;

import com.fjoglar.bakingapp.data.model.Recipe;

/**
 * {@link RecipeDetailContract.Presenter} that controls communication between views and models of
 * the presentation layer.
 */
public class RecipeDetailPresenter implements RecipeDetailContract.Presenter {

    private static final String TAG = RecipeDetailPresenter.class.getSimpleName();

    @NonNull
    private final RecipeDetailContract.View mRecipeDetailView;

    @NonNull
    private final Recipe mRecipe;

    public RecipeDetailPresenter(@NonNull RecipeDetailContract.View recipeDetailView,
                                 @NonNull Recipe recipe) {
        mRecipeDetailView = recipeDetailView;
        mRecipe = recipe;

        mRecipeDetailView.setPresenter(this);
    }

    @Override
    public void subscribe() {
        getRecipeDetail(mRecipe);
    }

    @Override
    public void unsubscribe() {

    }

    @Override
    public void getRecipeDetail(Recipe recipe) {
        mRecipeDetailView.showLoading();
        mRecipeDetailView.showRecipeDetail(recipe);
        mRecipeDetailView.hideLoading();
    }
}
