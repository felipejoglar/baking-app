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

package com.fjoglar.bakingapp.stepdetail.domain;

import android.support.annotation.NonNull;

import com.fjoglar.bakingapp.UseCase;
import com.fjoglar.bakingapp.data.model.Step;
import com.fjoglar.bakingapp.data.source.RecipesDataSource;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

/**
 * This class is an implementation of {@link UseCase} that represents a use case for
 * getting a {@link Step} by its id.
 */
public class GetStepById extends UseCase<Step, GetStepById.Params> {

    private final RecipesDataSource mRepository;

    public GetStepById(RecipesDataSource repository,
                       Scheduler threadExecutor,
                       Scheduler postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        mRepository = repository;
    }

    @Override
    public Observable<Step> buildUseCaseObservable(Params params) {
        return mRepository.getStepById(params.stepId);
    }

    public static final class Params {

        private final int stepId;

        private Params(int stepId) {
            this.stepId = stepId;
        }

        @NonNull
        public static Params forStep(int stepId) {
            return new Params(stepId);
        }
    }
}