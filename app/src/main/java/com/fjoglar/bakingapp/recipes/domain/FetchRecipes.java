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

package com.fjoglar.bakingapp.recipes.domain;

import com.fjoglar.bakingapp.UseCase;
import com.fjoglar.bakingapp.data.source.RecipesDataSource;
import com.fjoglar.bakingapp.data.source.remote.jsonmodel.JsonRecipe;

import java.util.List;

import io.reactivex.Scheduler;

/**
 * This class is an implementation of {@link UseCase} that represents a use case for
 * fetch the list of recipes.
 */
public class FetchRecipes extends UseCase<List<JsonRecipe>, Void> {

    private final RecipesDataSource mRepository;

    public FetchRecipes(RecipesDataSource repository,
                        Scheduler threadExecutor,
                        Scheduler postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        mRepository = repository;
    }

    @Override
    public io.reactivex.Observable<List<JsonRecipe>> buildUseCaseObservable(Void unused) {
        return mRepository.fetchRecipes();
    }
}