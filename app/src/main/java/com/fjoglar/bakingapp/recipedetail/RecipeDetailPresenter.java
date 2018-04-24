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
import android.util.Log;

import com.fjoglar.bakingapp.DefaultObserver;
import com.fjoglar.bakingapp.data.model.Ingredient;
import com.fjoglar.bakingapp.data.model.Recipe;
import com.fjoglar.bakingapp.data.model.Step;
import com.fjoglar.bakingapp.data.source.RecipesDataSource;
import com.fjoglar.bakingapp.recipedetail.domain.GetIngredientsByRecipeId;
import com.fjoglar.bakingapp.recipedetail.domain.GetRecipeById;
import com.fjoglar.bakingapp.recipedetail.domain.GetStepsByRecipeId;
import com.fjoglar.bakingapp.util.schedulers.BaseSchedulerProvider;

import java.util.List;

/**
 * {@link RecipeDetailContract.Presenter} that controls communication between views and models of
 * the presentation layer.
 */
public class RecipeDetailPresenter implements RecipeDetailContract.Presenter {

    private static final String TAG = RecipeDetailPresenter.class.getSimpleName();

    @NonNull
    private final RecipeDetailContract.View mRecipeDetailView;

    @NonNull
    private final RecipesDataSource mRecipesRepository;

    @NonNull
    private final BaseSchedulerProvider mSchedulerProvider;

    @NonNull
    private final int mRecipeId;

    private final GetRecipeById mGetRecipeById;
    private final GetIngredientsByRecipeId mGetIngredientsByRecipeId;
    private final GetStepsByRecipeId mGetStepsByRecipeId;

    public RecipeDetailPresenter(@NonNull RecipesDataSource repository,
                                 @NonNull RecipeDetailContract.View recipeDetailView,
                                 @NonNull BaseSchedulerProvider schedulerProvider,
                                 @NonNull int recipeId) {
        mRecipesRepository = repository;
        mRecipeDetailView = recipeDetailView;
        mSchedulerProvider = schedulerProvider;
        mRecipeId = recipeId;

        mRecipeDetailView.setPresenter(this);

        mGetRecipeById = new GetRecipeById(mRecipesRepository,
                mSchedulerProvider.io(),
                mSchedulerProvider.ui());
        mGetIngredientsByRecipeId = new GetIngredientsByRecipeId(mRecipesRepository,
                mSchedulerProvider.io(),
                mSchedulerProvider.ui());
        mGetStepsByRecipeId = new GetStepsByRecipeId(mRecipesRepository,
                mSchedulerProvider.io(),
                mSchedulerProvider.ui());
    }

    @Override
    public void subscribe() {
        getRecipeDetail(mRecipeId);
        getRecipeIngredients(mRecipeId);
        getRecipeSteps(mRecipeId);
    }

    @Override
    public void unsubscribe() {
        mGetRecipeById.dispose();
        mGetIngredientsByRecipeId.dispose();
        mGetStepsByRecipeId.dispose();
    }

    @Override
    public void getRecipeDetail(int recipeId) {
        mRecipeDetailView.showLoading();
        mGetRecipeById.execute(new GetRecipeDetailObserver(),
                GetRecipeById.Params.forRecipe(recipeId));
    }

    @Override
    public void getRecipeIngredients(int recipeId) {
        mRecipeDetailView.showLoading();
        mGetIngredientsByRecipeId.execute(new GetRecipeIngredientsObserver(),
                GetIngredientsByRecipeId.Params.forRecipe(recipeId));
    }

    @Override
    public void getRecipeSteps(int recipeId) {
        mRecipeDetailView.showLoading();
        mGetStepsByRecipeId.execute(new GetRecipeStepsObserver(),
                GetStepsByRecipeId.Params.forRecipe(recipeId));
    }

    private final class GetRecipeDetailObserver extends DefaultObserver<Recipe> {

        @Override
        public void onNext(Recipe recipe) {
            mRecipeDetailView.showRecipeDetail(recipe);
        }

        @Override
        public void onComplete() {
            mRecipeDetailView.hideLoading();
        }

        @Override
        public void onError(Throwable e) {
            Log.e(TAG, e.toString());
        }
    }

    private final class GetRecipeIngredientsObserver extends DefaultObserver<List<Ingredient>> {

        @Override
        public void onNext(List<Ingredient> ingredientList) {
            mRecipeDetailView.showRecipeDetailIngredients(ingredientList);
        }

        @Override
        public void onComplete() {
            mRecipeDetailView.hideLoading();
        }

        @Override
        public void onError(Throwable e) {
            Log.e(TAG, e.toString());
        }
    }

    private final class GetRecipeStepsObserver extends DefaultObserver<List<Step>> {

        @Override
        public void onNext(List<Step> stepList) {
            mRecipeDetailView.showRecipeDetailSteps(stepList);
        }

        @Override
        public void onComplete() {
            mRecipeDetailView.hideLoading();
        }

        @Override
        public void onError(Throwable e) {
            Log.e(TAG, e.toString());
        }
    }
}
