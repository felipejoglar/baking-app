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

import com.fjoglar.bakingapp.data.model.Ingredient;
import com.fjoglar.bakingapp.data.model.Recipe;
import com.fjoglar.bakingapp.data.model.Step;
import com.fjoglar.bakingapp.data.source.remote.jsonmodel.JsonRecipe;

import java.util.List;

import io.reactivex.Observable;

/**
 * Main entry point for accessing recipes data.
 */
public interface RecipesDataSource {

    Observable<List<JsonRecipe>> fetchRecipes();

    Observable<List<Recipe>> getRecipes();

    Observable<Recipe> getRecipebyId(int recipeId);

    Observable<List<Ingredient>> getIngredientsByRecipeId(int recipeId);

    Observable<List<Step>> getStepsByRecipeId(int recipeId);

    Observable<Step> getStepById(int stepId);

    void updateRecipes(List<JsonRecipe> jsonRecipes);
}