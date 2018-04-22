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

/**
 * {@link StepDetailContract.Presenter} that controls communication between views and models of
 * the presentation layer.
 */
public class StepDetailPresenter implements StepDetailContract.Presenter {

    private static final String TAG = StepDetailPresenter.class.getSimpleName();

    @NonNull
    private final StepDetailContract.View mStepDetailView;

    @NonNull
    private final Step mStep;

    public StepDetailPresenter(@NonNull StepDetailContract.View stepDetailView,
                               @NonNull Step step) {
        mStepDetailView = stepDetailView;
        mStep = step;

        mStepDetailView.setPresenter(this);
    }

    @Override
    public void subscribe() {
        getStepDetail(mStep);
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
}
