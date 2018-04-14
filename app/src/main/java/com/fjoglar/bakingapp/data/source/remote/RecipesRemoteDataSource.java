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

package com.fjoglar.bakingapp.data.source.remote;

import android.support.annotation.Nullable;

import com.fjoglar.bakingapp.data.model.Recipe;
import com.fjoglar.bakingapp.data.source.RecipesDataSource;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Concrete implementation of a remote data source that gets the recipes from a JSON file
 * through the network.
 */
public class RecipesRemoteDataSource implements RecipesDataSource {

    private static final String RECIPES_BASE_URL = "https://d17h27t6h515a5.cloudfront.net";

    @Nullable
    private static RecipesRemoteDataSource INSTANCE = null;

    private RecipeApiService mRecipeApiService;

    // Prevent direct instantiation.
    private RecipesRemoteDataSource() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RECIPES_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        mRecipeApiService = retrofit.create(RecipeApiService.class);
    }

    /**
     * Returns the single instance of this class, creating it if necessary.
     *
     * @return the {@link RecipesRemoteDataSource} instance
     */
    public static RecipesRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RecipesRemoteDataSource();
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
        return mRecipeApiService.getRecipesFromJson();
    }
}