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

import android.support.annotation.NonNull;
import android.util.Log;

import com.fjoglar.bakingapp.DefaultObserver;
import com.fjoglar.bakingapp.data.model.Recipe;
import com.fjoglar.bakingapp.data.source.RecipesDataSource;
import com.fjoglar.bakingapp.data.source.remote.jsonmodel.JsonRecipe;
import com.fjoglar.bakingapp.recipes.domain.FetchRecipes;
import com.fjoglar.bakingapp.recipes.domain.GetRecipes;
import com.fjoglar.bakingapp.recipes.domain.UpdateRecipes;
import com.fjoglar.bakingapp.util.schedulers.BaseSchedulerProvider;

import java.util.List;

public class RecipesPresenter implements RecipesContract.Presenter {

    private static final String TAG = RecipesPresenter.class.getSimpleName();

    @NonNull
    private final RecipesDataSource mRecipesRepository;

    @NonNull
    private final RecipesContract.View mRecipesView;

    @NonNull
    private final BaseSchedulerProvider mSchedulerProvider;

    private final FetchRecipes mFetchRecipes;
    private final UpdateRecipes mUpdateRecipes;
    private final GetRecipes mGetRecipes;

    public RecipesPresenter(@NonNull RecipesDataSource repository,
                            @NonNull RecipesContract.View recipesView,
                            @NonNull BaseSchedulerProvider schedulerProvider) {
        mRecipesRepository = repository;
        mRecipesView = recipesView;
        mSchedulerProvider = schedulerProvider;

        mRecipesView.setPresenter(this);

        mGetRecipes = new GetRecipes(mRecipesRepository,
                mSchedulerProvider.io(),
                mSchedulerProvider.ui());
        mFetchRecipes = new FetchRecipes(mRecipesRepository,
                mSchedulerProvider.io(),
                mSchedulerProvider.ui());
        mUpdateRecipes = new UpdateRecipes(mRecipesRepository,
                mSchedulerProvider.io(),
                mSchedulerProvider.ui());
    }

    @Override
    public void subscribe() {
        fetchRecipes();
    }

    @Override
    public void unsubscribe() {
        mGetRecipes.dispose();
        mFetchRecipes.dispose();
        mUpdateRecipes.dispose();
    }

    @Override
    public void fetchRecipes() {
        mRecipesView.showLoading();
        mFetchRecipes.execute(new FetchRecipesObserver(), null);
    }

    @Override
    public void updateRecipes(List<JsonRecipe> jsonRecipeList) {
        mRecipesView.showLoading();
        mUpdateRecipes.execute(new UpdateRecipesObserver(),
                UpdateRecipes.Params.withJsonRecipes(jsonRecipeList));
    }

    @Override
    public void getRecipes() {
        mRecipesView.showLoading();
        mGetRecipes.execute(new RecipesObserver(), null);
    }

    private void showRecipes(List<Recipe> recipes) {
        if (recipes.isEmpty()) {
            mRecipesView.showEmptyView();
        } else {
            mRecipesView.showRecipes(recipes);
        }
    }

    private final class FetchRecipesObserver extends DefaultObserver<List<JsonRecipe>> {

        @Override
        public void onNext(List<JsonRecipe> jsonRecipeList) {
            updateRecipes(jsonRecipeList);
        }

        @Override
        public void onComplete() {
            mRecipesView.hideLoading();
        }

        @Override
        public void onError(Throwable e) {
            Log.e(TAG, e.toString());
        }
    }

    private final class UpdateRecipesObserver extends DefaultObserver<Boolean> {

        @Override
        public void onNext(Boolean areUpdated) {
            // TODO: if areUpdated is false we must show an error message.
            getRecipes();
        }

        @Override
        public void onComplete() {
            mRecipesView.hideLoading();
        }

        @Override
        public void onError(Throwable e) {
            Log.e(TAG, e.toString());
        }
    }

    private final class RecipesObserver extends DefaultObserver<List<Recipe>> {

        @Override
        public void onNext(List<Recipe> recipes) {
            showRecipes(recipes);
        }

        @Override
        public void onComplete() {
            mRecipesView.hideLoading();
        }

        @Override
        public void onError(Throwable e) {
            Log.e(TAG, e.toString());
        }
    }
}