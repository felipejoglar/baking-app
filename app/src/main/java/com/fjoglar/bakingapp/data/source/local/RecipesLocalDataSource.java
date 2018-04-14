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

import com.fjoglar.bakingapp.data.model.Recipe;
import com.fjoglar.bakingapp.data.source.RecipesDataSource;
import com.fjoglar.bakingapp.data.source.RecipesRepository;

import java.util.List;

import io.reactivex.Observable;

/**
 * Concrete implementation of a local data source.
 */
public class RecipesLocalDataSource implements RecipesDataSource {

    @Nullable
    private static RecipesLocalDataSource INSTANCE = null;

    // Prevent direct instantiation.
    private RecipesLocalDataSource() {

    }

    /**
     * Returns the single instance of this class, creating it if necessary.
     *
     * @return the {@link RecipesLocalDataSource} instance
     */
    public static RecipesLocalDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RecipesLocalDataSource();
        }
        return INSTANCE;
    }

    /**
     * Used to force {@link #getInstance()} to create a new instance next time it's called.
     */
    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public Observable<List<Recipe>> getRecipes() {
        /**
         * Not required for the local data source because the {@link RecipesRepository} delegates
         * this function to the remote data source.
         */
        return null;
    }
}