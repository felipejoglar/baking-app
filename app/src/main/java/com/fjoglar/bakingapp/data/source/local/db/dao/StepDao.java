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

import com.fjoglar.bakingapp.data.model.Step;

import java.util.List;

import io.reactivex.Observable;

@Dao
public interface StepDao {

    @Insert
    public void insertAll(Step... steps);

    @Query("SELECT * FROM steps WHERE recipe_id = (:recipeId)")
    public Observable<List<Step>> getByRecipeId(int recipeId);

    @Query("SELECT * FROM steps WHERE id = (:stepId)")
    public Observable<Step> getById(int stepId);

    @Query("DELETE FROM steps")
    public void deleteAll();
}