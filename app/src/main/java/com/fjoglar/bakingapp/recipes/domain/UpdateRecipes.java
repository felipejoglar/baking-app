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

import io.reactivex.Observable;
import io.reactivex.Scheduler;

/**
 * This class is an implementation of {@link UseCase} that represents a use case for
 * updating the list of recipes.
 */
public class UpdateRecipes extends UseCase<Boolean, UpdateRecipes.Params> {

    private final RecipesDataSource mRepository;

    public UpdateRecipes(RecipesDataSource repository,
                         Scheduler threadExecutor,
                         Scheduler postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        mRepository = repository;
    }

    @Override
    public Observable<Boolean> buildUseCaseObservable(Params params) {
        return mRepository.updateRecipes(params.jsonRecipes);
    }

    public static final class Params {

        private final List<JsonRecipe> jsonRecipes;

        private Params(List<JsonRecipe> jsonRecipes) {
            this.jsonRecipes = jsonRecipes;
        }

        public static Params withJsonRecipes(List<JsonRecipe> jsonRecipes) {
            return new Params(jsonRecipes);
        }
    }
}