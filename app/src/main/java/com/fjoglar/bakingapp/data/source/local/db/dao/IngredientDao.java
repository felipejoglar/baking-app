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

package com.fjoglar.bakingapp.data.source.local.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.fjoglar.bakingapp.data.model.Ingredient;

import java.util.List;

@Dao
public interface IngredientDao {

    @Insert
    void insertAll(Ingredient... ingredients);

    @Insert
    void insert(Ingredient ingredient);

    @Query("SELECT * FROM ingredients WHERE recipe_id = (:recipeId)")
    List<Ingredient> getByRecipeId(int recipeId);

    @Query("DELETE FROM ingredients")
    void deleteAll();
}
