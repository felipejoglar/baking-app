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

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fjoglar.bakingapp.R;
import com.fjoglar.bakingapp.data.model.Step;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import java.util.ArrayList;
import java.util.List;

/**
 * Main UI for the step detail screen.
 */
public class StepDetailFragment extends Fragment implements StepDetailContract.View {

    @NonNull
    private static final String ARGUMENT_STEP_LIST = "step_list";
    @NonNull
    private static final String ARGUMENT_STEP_INDEX = "step_index";

    private StepNavigationClickListener mStepNavigationClickListener;
    private StepDetailContract.Presenter mStepDetailPresenter;
    private List<Step> mStepList;
    private int mStepIndex;

    @BindView(R.id.textview_step_detail_description)
    TextView mTextViewStepDetailDescription;
    @BindView(R.id.progressbar_loading)
    ProgressBar mProgressBarLoading;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the fragment
     */
    public StepDetailFragment() {
    }

    public static StepDetailFragment newInstance(@NonNull List<Step> stepList, int stepIndex) {
        Bundle arguments = new Bundle();
        arguments.putParcelableArrayList(ARGUMENT_STEP_LIST,
                (ArrayList<? extends Parcelable>) stepList);
        arguments.putInt(ARGUMENT_STEP_INDEX, stepIndex);

        StepDetailFragment stepDetailFragment = new StepDetailFragment();
        stepDetailFragment.setArguments(arguments);
        return stepDetailFragment;
    }

    @Override
    public void onAttach(Context context) {

        super.onAttach(context);
        // This makes sure that the host activity has implemented the StepNavigationClickListener
        // interface. If not, it throws an exception
        try {
            mStepNavigationClickListener = (StepNavigationClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement StepNavigationClickListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(ARGUMENT_STEP_LIST) &&
                getArguments().containsKey(ARGUMENT_STEP_INDEX)) {
            mStepList = getArguments().getParcelableArrayList(ARGUMENT_STEP_LIST);
            mStepIndex = getArguments().getInt(ARGUMENT_STEP_INDEX);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_step_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initPresenter();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mStepDetailPresenter.subscribe();
    }

    @Override
    public void onPause() {
        super.onPause();
        mStepDetailPresenter.unsubscribe();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void setPresenter(@NonNull StepDetailContract.Presenter presenter) {
        mStepDetailPresenter = presenter;
    }

    @Override
    public void showStepDetail(Step step) {
        StringBuilder sb = new StringBuilder()
                .append(step.getId())
                .append(": ").append(step.getShortDescription())
                .append("\n\n- ").append(step.getDescription())
                .append("\n\n- ").append(step.getVideoURL())
                .append("\n- ").append(step.getThumbnailURL());

        mTextViewStepDetailDescription.setText(sb);
    }

    @Override
    public void showNextStepDetail(Step step) {
        mStepNavigationClickListener.onNextStepClicked(step);
    }

    @Override
    public void showPreviousStepDetail(Step step) {
        mStepNavigationClickListener.onPreviousStepClicked(step);
    }

    @Override
    public void showLoading() {
        mProgressBarLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        mProgressBarLoading.setVisibility(View.GONE);
    }

    @OnClick(R.id.button_step_fragment_navigate_next)
    public void onNextStepClicked() {
        mStepDetailPresenter.getNextStepDetail(mStepIndex);
    }

    @OnClick(R.id.button_step_fragment_navigate_previous)
    public void onPreviousStepClicked() {
        mStepDetailPresenter.getPreviousStepDetail(mStepIndex);
    }

    private void initPresenter() {
        mStepDetailPresenter = new StepDetailPresenter(this, mStepList, mStepIndex);
    }

    public interface StepNavigationClickListener {

        void onNextStepClicked(Step step);

        void onPreviousStepClicked(Step step);
    }
}