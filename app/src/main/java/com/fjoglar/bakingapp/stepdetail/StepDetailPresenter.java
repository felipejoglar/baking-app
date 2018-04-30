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

package com.fjoglar.bakingapp.stepdetail;

import android.support.annotation.NonNull;
import android.util.Log;

import com.fjoglar.bakingapp.DefaultObserver;
import com.fjoglar.bakingapp.data.model.Step;
import com.fjoglar.bakingapp.data.source.RecipesDataSource;
import com.fjoglar.bakingapp.recipedetail.domain.GetStepsByRecipeId;
import com.fjoglar.bakingapp.stepdetail.domain.GetStepById;
import com.fjoglar.bakingapp.util.schedulers.BaseSchedulerProvider;

import java.util.List;

/**
 * {@link StepDetailContract.Presenter} that controls communication between views and models of
 * the presentation layer.
 */
public class StepDetailPresenter implements StepDetailContract.Presenter {

    private static final String TAG = StepDetailPresenter.class.getSimpleName();

    @NonNull
    private final StepDetailContract.View mStepDetailView;

    @NonNull
    private final RecipesDataSource mRecipesRepository;

    @NonNull
    private final BaseSchedulerProvider mSchedulerProvider;

    private final int mRecipeId;
    private final int mStepId;

    @NonNull
    private List<Step> mStepList;

    private final GetStepsByRecipeId mGetStepsByRecipeId;
    private final GetStepById mGetStepById;

    public StepDetailPresenter(@NonNull RecipesDataSource repository,
                               @NonNull StepDetailContract.View stepDetailView,
                               @NonNull BaseSchedulerProvider schedulerProvider,
                               int recipeId,
                               int stepId) {
        mRecipesRepository = repository;
        mStepDetailView = stepDetailView;
        mSchedulerProvider = schedulerProvider;
        mRecipeId = recipeId;
        mStepId = stepId;

        mStepDetailView.setPresenter(this);

        mGetStepsByRecipeId = new GetStepsByRecipeId(mRecipesRepository,
                mSchedulerProvider.io(),
                mSchedulerProvider.ui());
        mGetStepById = new GetStepById(mRecipesRepository,
                mSchedulerProvider.io(),
                mSchedulerProvider.ui());
    }

    @Override
    public void subscribe() {
        mGetStepsByRecipeId.execute(new GetRecipeStepsObserver(),
                GetStepsByRecipeId.Params.forRecipe(mRecipeId));
        if (mStepId != 0) {
            getStepDetail(mStepId);
        }
    }

    @Override
    public void unsubscribe() {
        mGetStepsByRecipeId.dispose();
        mGetStepById.dispose();
    }

    @Override
    public void getStepDetail(int stepId) {
        mStepDetailView.showLoading();
        mGetStepById.execute(new GetStepDetailObserver(), GetStepById.Params.forStep(stepId));
    }

    @Override
    public void getNextStepDetail() {
        int currentStepIndex = getCurrentStepIndexInList(mStepList);
        if (currentStepIndex != (mStepList.size() - 1)) {
            mStepDetailView.showNextStepDetail(mStepList.get(currentStepIndex + 1));
        }
    }

    @Override
    public void getPreviousStepDetail() {
        int currentStepIndex = getCurrentStepIndexInList(mStepList);
        if (currentStepIndex != 0) {
            mStepDetailView.showPreviousStepDetail(mStepList.get(currentStepIndex - 1));
        }
    }

    private void loadVideo(Step step) {
        if (!step.getVideoUrl().isEmpty()) {
            mStepDetailView.loadVideo(step.getVideoUrl());
        } else if (!step.getThumbnailUrl().isEmpty()) {
            mStepDetailView.loadVideo(step.getThumbnailUrl());
        } else {
            mStepDetailView.showVideoPlaceholder(step);
        }
    }

    private int getCurrentStepIndexInList(List<Step> stepList) {
        for (Step step : stepList) {
            if (step.getId() == mStepId) {
                return mStepList.indexOf(step);
            }
        }
        return 0;
    }

    private final class GetStepDetailObserver extends DefaultObserver<Step> {

        @Override
        public void onNext(Step step) {
            mStepDetailView.showStepDetail(step);
            loadVideo(step);
        }

        @Override
        public void onComplete() {
            mStepDetailView.hideLoading();
        }

        @Override
        public void onError(Throwable e) {
            Log.e(TAG, e.toString());
        }
    }

    private final class GetRecipeStepsObserver extends DefaultObserver<List<Step>> {

        @Override
        public void onNext(List<Step> stepList) {
            mStepList = stepList;
            if (mStepId == 0) {
                mStepDetailView.showStepDetail(mStepList.get(0));
                loadVideo(mStepList.get(0));
            }
        }

        @Override
        public void onComplete() {

        }

        @Override
        public void onError(Throwable e) {
            Log.e(TAG, e.toString());
        }
    }
}
