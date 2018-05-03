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

import android.support.annotation.NonNull;
import android.util.Log;

import com.fjoglar.bakingapp.DefaultObserver;
import com.fjoglar.bakingapp.data.model.Ingredient;
import com.fjoglar.bakingapp.data.model.Recipe;
import com.fjoglar.bakingapp.data.source.RecipesDataSource;
import com.fjoglar.bakingapp.recipedetail.domain.GetIngredientsByRecipeId;
import com.fjoglar.bakingapp.recipes.domain.GetRecipes;
import com.fjoglar.bakingapp.util.schedulers.BaseSchedulerProvider;

import java.util.List;

/**
 * {@link RecipeWidgetContract.Presenter} that controls communication between views and models of
 * the presentation layer.
 */
public class RecipeWidgetPresenter implements RecipeWidgetContract.Presenter {

    private static final String TAG = RecipeWidgetPresenter.class.getSimpleName();

    @NonNull
    private final RecipesDataSource mRecipesRepository;

    @NonNull
    private final RecipeWidgetContract.View mRecipeWidgetView;

    @NonNull
    private final BaseSchedulerProvider mSchedulerProvider;

    private Recipe mRecipe;

    private final GetRecipes mGetRecipes;
    private final GetIngredientsByRecipeId mGetIngredientsByRecipeId;

    public RecipeWidgetPresenter(@NonNull RecipesDataSource repository,
                                 @NonNull RecipeWidgetContract.View recipeWidgetView,
                                 @NonNull BaseSchedulerProvider schedulerProvider) {
        mRecipesRepository = repository;
        mRecipeWidgetView = recipeWidgetView;
        mSchedulerProvider = schedulerProvider;

        mRecipeWidgetView.setPresenter(this);

        mGetRecipes = new GetRecipes(mRecipesRepository,
                mSchedulerProvider.io(),
                mSchedulerProvider.ui());
        mGetIngredientsByRecipeId = new GetIngredientsByRecipeId(mRecipesRepository,
                mSchedulerProvider.io(),
                mSchedulerProvider.ui());
    }

    @Override
    public void subscribe() {
        getRecipes();
    }

    @Override
    public void unsubscribe() {
        mGetRecipes.dispose();
        mGetIngredientsByRecipeId.dispose();
    }

    @Override
    public void getRecipes() {
        mRecipeWidgetView.showLoading();
        mGetRecipes.execute(new RecipesObserver(), null);
    }

    @Override
    public void getIngredientsForRecipe(Recipe recipe) {
        mRecipeWidgetView.showLoading();
        mRecipe = recipe;
        mGetIngredientsByRecipeId.execute(new IngredientsObserver(),
                GetIngredientsByRecipeId.Params.forRecipe(recipe.getId()));
    }

    private void showRecipes(List<Recipe> recipes) {
        if (recipes.isEmpty()) {
            mRecipeWidgetView.showEmptyView();
        } else {
            mRecipeWidgetView.showRecipes(recipes);
        }
    }

    private final class RecipesObserver extends DefaultObserver<List<Recipe>> {

        @Override
        public void onNext(List<Recipe> recipes) {
            showRecipes(recipes);
        }

        @Override
        public void onComplete() {
            mRecipeWidgetView.hideLoading();
        }

        @Override
        public void onError(Throwable e) {
            Log.e(TAG, e.toString());
        }
    }

    private final class IngredientsObserver extends DefaultObserver<List<Ingredient>> {

        @Override
        public void onNext(List<Ingredient> ingredients) {
            mRecipeWidgetView.createWidget(mRecipe, ingredients);
        }

        @Override
        public void onComplete() {
            mRecipeWidgetView.hideLoading();
        }

        @Override
        public void onError(Throwable e) {
            Log.e(TAG, e.toString());
        }
    }
}