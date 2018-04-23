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

package com.fjoglar.bakingapp.data.source.local.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.fjoglar.bakingapp.data.model.Ingredient;
import com.fjoglar.bakingapp.data.model.Recipe;
import com.fjoglar.bakingapp.data.model.Step;
import com.fjoglar.bakingapp.data.source.local.db.dao.IngredientDao;
import com.fjoglar.bakingapp.data.source.local.db.dao.RecipeDao;
import com.fjoglar.bakingapp.data.source.local.db.dao.StepDao;

@Database(entities = {Recipe.class, Ingredient.class, Step.class}, version = 1, exportSchema = false)
public abstract class RecipeDb extends RoomDatabase {

    private static final String DB_NAME = "recipe.db";
    private static RecipeDb INSTANCE = null;

    /**
     * Returns the single instance of this class, creating it if necessary.
     *
     * @return the {@link RecipeDb} instance
     */
    public static RecipeDb getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = create(context);
        }
        return INSTANCE;
    }

    private static RecipeDb create(final Context context) {
        return Room.databaseBuilder(
                context,
                RecipeDb.class,
                DB_NAME).build();
    }

    /**
     * Used to force {@link #getInstance(Context context)} to create a new instance next
     * time it's called.
     */
    public static void destroyInstance() {
        if (INSTANCE.isOpen() && INSTANCE != null) {
            INSTANCE.close();
        }
        INSTANCE = null;
    }

    public abstract RecipeDao recipeDao();

    public abstract IngredientDao ingredientDao();

    public abstract StepDao stepDao();

}
