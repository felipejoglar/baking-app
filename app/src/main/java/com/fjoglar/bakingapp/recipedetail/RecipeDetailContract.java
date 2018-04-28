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

import com.fjoglar.bakingapp.BasePresenter;
import com.fjoglar.bakingapp.BaseView;
import com.fjoglar.bakingapp.data.model.Ingredient;
import com.fjoglar.bakingapp.data.model.Recipe;
import com.fjoglar.bakingapp.data.model.Step;

import java.util.List;

/**
 * This specifies the contract between the recipe detail view and the recipe detail presenter.
 */
public interface RecipeDetailContract {

    interface View extends BaseView<Presenter> {

        void showRecipeDetail(Recipe recipe);

        void showRecipeDetailIngredients(List<Ingredient> ingredientList);

        void showRecipeDetailSteps(List<Step> stepList);

        void showTitle(String title, int servings);

        void showLoading();

        void hideLoading();
    }

    interface Presenter extends BasePresenter {

        void getRecipeDetail(int recipeId);

        void getRecipeIngredients(int recipeId);

        void getRecipeSteps(int recipeId);
    }
}
