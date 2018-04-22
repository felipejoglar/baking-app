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

import com.fjoglar.bakingapp.data.model.Step;

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
    private final List<Step> mStepList;

    @NonNull
    private final int mStepIndex;

    public StepDetailPresenter(@NonNull StepDetailContract.View stepDetailView,
                               @NonNull List<Step> stepList,
                               @NonNull int stepIndex) {
        mStepDetailView = stepDetailView;
        mStepList = stepList;
        mStepIndex = stepIndex;

        mStepDetailView.setPresenter(this);
    }

    @Override
    public void subscribe() {
        getStepDetail(mStepList.get(mStepIndex));
    }

    @Override
    public void unsubscribe() {

    }

    @Override
    public void getStepDetail(Step step) {
        mStepDetailView.showLoading();
        mStepDetailView.showStepDetail(step);
        mStepDetailView.hideLoading();
    }

    @Override
    public void getNextStepDetail(int currentStepIndex) {
        if (currentStepIndex != (mStepList.size() - 1)) {
            mStepDetailView.showNextStepDetail(mStepList.get(currentStepIndex + 1));
        }
    }

    @Override
    public void getPreviousStepDetail(int currentStepIndex) {
        if (currentStepIndex != 0) {
            mStepDetailView.showPreviousStepDetail(mStepList.get(currentStepIndex - 1));
        }
    }
}
