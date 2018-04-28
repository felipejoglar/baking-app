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

package com.fjoglar.bakingapp.util.ui;

import android.graphics.Color;

import com.fjoglar.bakingapp.R;
import com.fjoglar.bakingapp.data.model.Ingredient;

/**
 * Provides different types of imagery and text formatting for UI presentation.
 */
public class UiUtils {

    /**
     * Gets the specified image in function of the recipe
     *
     * @param recipeId recipe to look for the image
     * @return the image resource id for the requested recipe
     */
    public static int getImageResource(int recipeId) {
        switch (recipeId) {
            case 1:
                return R.drawable.nutella_pie;
            case 2:
                return R.drawable.brownies;
            case 3:
                return R.drawable.yellow_cake;
            case 4:
                return R.drawable.cheesecake;
        }
        return 0;
    }

    /**
     * Format the ingredient in a single sentence to be shown in the UI
     *
     * @param ingredient the ingredient to show
     * @return the sentence for that ingredient
     */
    public static String formatIngredientText(Ingredient ingredient) {
        return new StringBuilder()
                .append("\u2022 ")
                .append(capitalizeFirstLetter(ingredient.getIngredient()))
                .append(": ")
                .append(ingredient.getQuantity())
                .append(" ")
                .append(ingredient.getMeasure()).toString();
    }

    /**
     * Selects a color for a step
     *
     * @param stepNumber number of the step
     * @return the color associated to that number
     */
    public static int getStepColor(int stepNumber) {
        int module = stepNumber % 10;
        switch (module) {
            case 0:
                return Color.parseColor("#F44336");
            case 1:
                return Color.parseColor("#9C27B0");
            case 2:
                return Color.parseColor("#3F51B5");
            case 3:
                return Color.parseColor("#03A9F4");
            case 4:
                return Color.parseColor("#009688");
            case 5:
                return Color.parseColor("#8BC34A");
            case 6:
                return Color.parseColor("#FFEB3B");
            case 7:
                return Color.parseColor("#FF9800");
            case 8:
                return Color.parseColor("#795548");
            case 9:
            default:
                return Color.parseColor("#607D8B");
        }
    }

    /**
     * Capitalizes first letter of a sentence.
     *
     * @param sentence the sentence to be capitalized
     * @return the capitalized sentence
     */
    private static String capitalizeFirstLetter(String sentence) {
        return sentence.substring(0, 1).toUpperCase() + sentence.substring(1).toLowerCase();
    }
}
