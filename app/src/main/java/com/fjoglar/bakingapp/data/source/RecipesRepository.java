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

package com.fjoglar.bakingapp.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.fjoglar.bakingapp.data.model.Ingredient;
import com.fjoglar.bakingapp.data.model.Recipe;
import com.fjoglar.bakingapp.data.model.Step;
import com.fjoglar.bakingapp.data.source.remote.jsonmodel.JsonRecipe;

import java.util.List;

import io.reactivex.Observable;

/**
 * Concrete implementation to load recipes from the data sources.
 */
public class RecipesRepository implements RecipesDataSource {

    @Nullable
    private static volatile RecipesRepository INSTANCE = null;

    @NonNull
    private final RecipesDataSource mRecipesRemoteDataSource;

    @NonNull
    private final RecipesDataSource mRecipesLocalDataSource;

    // Prevent direct instantiation.
    private RecipesRepository(@NonNull RecipesDataSource recipesRemoteDataSource,
                              @NonNull RecipesDataSource recipesLocalDataSource) {
        mRecipesRemoteDataSource = recipesRemoteDataSource;
        mRecipesLocalDataSource = recipesLocalDataSource;
    }

    /**
     * Returns the single instance of this class, creating it if necessary.
     *
     * @param recipesRemoteDataSource the backend data source
     * @return the {@link RecipesRepository} instance
     */
    public static RecipesRepository getInstance(@NonNull RecipesDataSource recipesRemoteDataSource,
                                                @NonNull RecipesDataSource recipesLocalDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new RecipesRepository(recipesRemoteDataSource, recipesLocalDataSource);
        }
        return INSTANCE;
    }

    /**
     * Used to force {@link #getInstance(RecipesDataSource, RecipesDataSource)} to create a new
     * instance next time it's called.
     */
    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public Observable<List<JsonRecipe>> fetchRecipes() {
        return mRecipesRemoteDataSource.fetchRecipes();
    }

    @Override
    public Observable<List<Recipe>> getRecipes() {
        return mRecipesLocalDataSource.getRecipes();
    }

    @Override
    public Observable<Recipe> getRecipebyId(int recipeId) {
        return mRecipesLocalDataSource.getRecipebyId(recipeId);
    }

    @Override
    public Observable<List<Ingredient>> getIngredientsByRecipeId(int recipeId) {
        return mRecipesLocalDataSource.getIngredientsByRecipeId(recipeId);
    }

    @Override
    public Observable<List<Step>> getStepsByRecipeId(int recipeId) {
        return mRecipesLocalDataSource.getStepsByRecipeId(recipeId);
    }

    @Override
    public Observable<Step> getStepById(int stepId) {
        return mRecipesLocalDataSource.getStepById(stepId);
    }

    @Override
    public void updateRecipes(List<JsonRecipe> jsonRecipes) {
        mRecipesLocalDataSource.updateRecipes(jsonRecipes);
    }
}