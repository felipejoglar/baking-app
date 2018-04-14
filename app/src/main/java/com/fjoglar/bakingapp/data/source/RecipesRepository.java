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

import com.fjoglar.bakingapp.data.model.Recipe;

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

    // Prevent direct instantiation.
    private RecipesRepository(@NonNull RecipesDataSource recipesRemoteDataSource) {
        mRecipesRemoteDataSource = recipesRemoteDataSource;
    }

    /**
     * Returns the single instance of this class, creating it if necessary.
     *
     * @param recipesRemoteDataSource the backend data source
     * @return the {@link RecipesRepository} instance
     */
    public static RecipesRepository getInstance(@NonNull RecipesDataSource recipesRemoteDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new RecipesRepository(recipesRemoteDataSource);
        }
        return INSTANCE;
    }

    /**
     * Used to force {@link #getInstance(RecipesDataSource)} to create a new instance
     * next time it's called.
     */
    public static void destroyInstance() {
        INSTANCE = null;
    }

    /**
     * Gets recipes from remote data source (network).
     */
    @Override
    public Observable<List<Recipe>> getRecipes() {
        // TODO: in production it would be nice to get recipes from cache, local data source (SQLite)
        // or remote data source, whichever is available first.
        return mRecipesRemoteDataSource.getRecipes();
    }
}