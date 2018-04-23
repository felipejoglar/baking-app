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

import com.fjoglar.bakingapp.BasePresenter;
import com.fjoglar.bakingapp.BaseView;
import com.fjoglar.bakingapp.data.model.Step;

/**
 * This specifies the contract between the step detail view and the step detail presenter.
 */
public interface StepDetailContract {

    interface View extends BaseView<Presenter> {

        void showStepDetail(Step step);

        void showNextStepDetail(Step step);

        void showPreviousStepDetail(Step step);

        void showLoading();

        void hideLoading();
    }

    interface Presenter extends BasePresenter {

        void getStepDetail(int stepId);

        void getNextStepDetail();

        void getPreviousStepDetail();
    }
}
