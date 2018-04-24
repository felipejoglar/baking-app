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

package com.fjoglar.bakingapp.data.model.mapper;

import com.fjoglar.bakingapp.data.model.Ingredient;
import com.fjoglar.bakingapp.data.model.Recipe;
import com.fjoglar.bakingapp.data.model.Step;
import com.fjoglar.bakingapp.data.source.remote.jsonmodel.JsonIngredient;
import com.fjoglar.bakingapp.data.source.remote.jsonmodel.JsonRecipe;
import com.fjoglar.bakingapp.data.source.remote.jsonmodel.JsonStep;

/**
 * Mapper class used to transformRecipe {@link JsonRecipe}, {@link JsonIngredient} and {@link JsonStep}
 * (loaded from the network) to {@link Recipe}, {@link Ingredient} and {@link Step} (for offline
 * use).
 */
public class ModelDataMapper {

    public ModelDataMapper() {
    }

    /**
     * Transform a {@link JsonRecipe} into an {@link Recipe}.
     *
     * @param jsonRecipe Object to be transformed.
     * @return {@link Recipe}.
     */
    public Recipe transformRecipe(JsonRecipe jsonRecipe) {
        if (jsonRecipe == null) {
            throw new IllegalArgumentException("Cannot transformRecipe a null value");
        }
        final Recipe recipe = new Recipe();
        recipe.setId(jsonRecipe.getId());
        recipe.setName(jsonRecipe.getName());
        recipe.setServings(jsonRecipe.getServings());
        recipe.setImage(jsonRecipe.getImage());

        return recipe;
    }

    /**
     * Transform a {@link JsonIngredient} into an {@link Ingredient}.
     *
     * @param jsonIngredient Object to be transformed.
     * @param recipeId       id of the recipe to which this ingredient belongs to.
     * @return {@link Ingredient}.
     */
    public Ingredient transformIngredient(JsonIngredient jsonIngredient, int recipeId) {
        if (jsonIngredient == null) {
            throw new IllegalArgumentException("Cannot transformRecipe a null value");
        }
        final Ingredient ingredient = new Ingredient();
        ingredient.setQuantity(jsonIngredient.getQuantity());
        ingredient.setMeasure(jsonIngredient.getMeasure());
        ingredient.setIngredient(jsonIngredient.getIngredient());
        ingredient.setRecipeId(recipeId);

        return ingredient;
    }

    /**
     * Transform a {@link JsonStep} into an {@link Step}.
     *
     * @param jsonStep Object to be transformed.
     * @param recipeId id of the recipe to which this step belongs to.
     * @return {@link Step}.
     */
    public Step transformStep(JsonStep jsonStep, int recipeId) {
        if (jsonStep == null) {
            throw new IllegalArgumentException("Cannot transformRecipe a null value");
        }
        final Step step = new Step();
        step.setStepId(jsonStep.getId());
        step.setShortDescription(jsonStep.getShortDescription());
        step.setDescription(jsonStep.getDescription());
        step.setVideoUrl(jsonStep.getVideoURL());
        step.setThumbnailUrl(jsonStep.getThumbnailURL());
        step.setRecipeId(recipeId);

        return step;
    }
}
