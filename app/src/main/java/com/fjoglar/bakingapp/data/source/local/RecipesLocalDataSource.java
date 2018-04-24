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

package com.fjoglar.bakingapp.data.source.local;

import android.support.annotation.Nullable;

import com.fjoglar.bakingapp.data.model.Ingredient;
import com.fjoglar.bakingapp.data.model.Recipe;
import com.fjoglar.bakingapp.data.model.Step;
import com.fjoglar.bakingapp.data.model.mapper.ModelDataMapper;
import com.fjoglar.bakingapp.data.source.RecipesDataSource;
import com.fjoglar.bakingapp.data.source.RecipesRepository;
import com.fjoglar.bakingapp.data.source.local.db.RecipeDb;
import com.fjoglar.bakingapp.data.source.remote.jsonmodel.JsonIngredient;
import com.fjoglar.bakingapp.data.source.remote.jsonmodel.JsonRecipe;
import com.fjoglar.bakingapp.data.source.remote.jsonmodel.JsonStep;

import java.util.List;

import io.reactivex.Observable;

/**
 * Concrete implementation of a local data source.
 */
public class RecipesLocalDataSource implements RecipesDataSource {

    @Nullable
    private static RecipesLocalDataSource INSTANCE = null;

    private ModelDataMapper mModelDataMapper;
    private RecipeDb mRecipeDb;

    // Prevent direct instantiation.
    private RecipesLocalDataSource(ModelDataMapper modelDataMapper, RecipeDb recipeDb) {
        mModelDataMapper = modelDataMapper;
        mRecipeDb = recipeDb;
    }

    /**
     * Returns the single instance of this class, creating it if necessary.
     *
     * @return the {@link RecipesLocalDataSource} instance
     */
    public static RecipesLocalDataSource getInstance(ModelDataMapper modelDataMapper,
                                                     RecipeDb recipeDb) {
        if (INSTANCE == null) {
            INSTANCE = new RecipesLocalDataSource(modelDataMapper, recipeDb);
        }
        return INSTANCE;
    }

    /**
     * Used to force {@link #getInstance(ModelDataMapper, RecipeDb)} to create a new instance next
     * time it's called.
     */
    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public Observable<List<JsonRecipe>> fetchRecipes() {
        /**
         * Not required for the local data source because {@link RecipesRepository} delegates
         * this function to the remote data source.
         */
        return null;
    }

    @Override
    public Observable<List<Recipe>> getRecipes() {
        return Observable.fromCallable(() -> mRecipeDb.recipeDao().getAll());
    }

    @Override
    public Observable<Recipe> getRecipebyId(int recipeId) {
        return Observable.fromCallable(() -> mRecipeDb.recipeDao().getById(recipeId));
    }

    @Override
    public Observable<List<Ingredient>> getIngredientsByRecipeId(int recipeId) {
        return Observable.fromCallable(() -> mRecipeDb.ingredientDao().getByRecipeId(recipeId));
    }

    @Override
    public Observable<List<Step>> getStepsByRecipeId(int recipeId) {
        return Observable.fromCallable(() -> mRecipeDb.stepDao().getByRecipeId(recipeId));
    }

    @Override
    public Observable<Step> getStepById(int stepId) {
        return Observable.fromCallable(() -> mRecipeDb.stepDao().getById(stepId));
    }

    @Override
    public Observable<Boolean> updateRecipes(List<JsonRecipe> jsonRecipes) {
        return Observable.fromCallable(() -> {
            mRecipeDb.runInTransaction(() ->
                    deleteOldAndInsertNewRecipesTransaction(jsonRecipes));
            return true;
        });
    }

    private void deleteOldAndInsertNewRecipesTransaction(List<JsonRecipe> jsonRecipes) {
        // First delete all the current recipes in the DB.
        mRecipeDb.recipeDao().deleteAll();
        // Then insert the new downloaded recipes.
        for (JsonRecipe jsonRecipe : jsonRecipes) {
            mRecipeDb.recipeDao().insert(mModelDataMapper.transformRecipe(jsonRecipe));
            for (JsonIngredient jsonIngredient : jsonRecipe.getIngredients()) {
                mRecipeDb.ingredientDao().insert(
                        mModelDataMapper.transformIngredient(jsonIngredient, jsonRecipe.getId()));
            }
            for (JsonStep jsonStep : jsonRecipe.getSteps()) {
                mRecipeDb.stepDao().insert(
                        mModelDataMapper.transformStep(jsonStep, jsonRecipe.getId()));
            }
        }
    }
}