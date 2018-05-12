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
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
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
import com.fjoglar.bakingapp.data.model.mapper.ModelDataMapper;
import com.fjoglar.bakingapp.data.source.RecipesRepository;
import com.fjoglar.bakingapp.data.source.local.RecipesLocalDataSource;
import com.fjoglar.bakingapp.data.source.local.db.RecipeDb;
import com.fjoglar.bakingapp.data.source.remote.RecipesRemoteDataSource;
import com.fjoglar.bakingapp.util.schedulers.SchedulerProvider;
import com.fjoglar.bakingapp.util.ui.UiUtils;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Main UI for the step detail screen.
 */
public class StepDetailFragment extends Fragment implements StepDetailContract.View {

    @NonNull
    private static final String ARGUMENT_RECIPE_ID = "recipe_id";
    @NonNull
    private static final String ARGUMENT_STEP_ID = "step_id";

    private static final String SAVED_STATE_WINDOW = "window";
    private static final String SAVED_STATE_POSITION = "position";
    private static final String SAVED_STATE_AUTO_PLAY = "auto_play";

    private StepNavigationClickListener mStepNavigationClickListener;
    private StepDetailContract.Presenter mStepDetailPresenter;
    private SimpleExoPlayer mSimpleExoPlayer;
    private int mRecipeId;
    private int mStepId;

    private boolean mStartAutoPlay;
    private int mStartWindow;
    private long mStartPosition ;

    @BindView(R.id.simpleexoplayerview_step_detail_video)
    PlayerView mSimpleExoPlayerViewStepDetailVideo;
    @BindView(R.id.textview_step_detail_title)
    TextView mTextViewStepDetailTitle;
    @BindView(R.id.textview_step_detail_description)
    TextView mTextViewStepDetailDescription;
    @BindView(R.id.progressbar_loading)
    ProgressBar mProgressBarLoading;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the fragment
     */
    public StepDetailFragment() {
    }

    public static StepDetailFragment newInstance(int recipeId, int stepId) {
        Bundle arguments = new Bundle();
        arguments.putInt(ARGUMENT_RECIPE_ID, recipeId);
        arguments.putInt(ARGUMENT_STEP_ID, stepId);

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
        if (getArguments().containsKey(ARGUMENT_RECIPE_ID) &&
                getArguments().containsKey(ARGUMENT_STEP_ID)) {
            mRecipeId = getArguments().getInt(ARGUMENT_RECIPE_ID);
            mStepId = getArguments().getInt(ARGUMENT_STEP_ID);
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
        if (savedInstanceState != null) {
            mStartAutoPlay = savedInstanceState.getBoolean(SAVED_STATE_AUTO_PLAY);
            mStartWindow = savedInstanceState.getInt(SAVED_STATE_WINDOW);
            mStartPosition = savedInstanceState.getLong(SAVED_STATE_POSITION);
        } else {
            clearStartPosition();
        }
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
        initializePlayer();
    }

    @Override
    public void onPause() {
        super.onPause();
        mStepDetailPresenter.unsubscribe();
        releasePlayer();
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
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean(SAVED_STATE_AUTO_PLAY, mStartAutoPlay);
        outState.putInt(SAVED_STATE_WINDOW, mStartWindow);
        outState.putLong(SAVED_STATE_POSITION, mStartPosition);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void setPresenter(@NonNull StepDetailContract.Presenter presenter) {
        mStepDetailPresenter = presenter;
    }

    @Override
    public void showStepDetail(Step step) {
        mTextViewStepDetailTitle.setText(step.getShortDescription());
        mTextViewStepDetailDescription.setText(step.getDescription());
    }

    @Override
    public void loadVideo(String videoUrl) {
        Uri uri = Uri.parse(videoUrl);
        MediaSource mediaSource = buildMediaSource(uri);
        mSimpleExoPlayer.prepare(mediaSource, true, false);

        mSimpleExoPlayer.setPlayWhenReady(mStartAutoPlay);
        boolean haveStartPosition = mStartWindow != C.INDEX_UNSET;
        if (haveStartPosition) {
            mSimpleExoPlayer.seekTo(mStartWindow, mStartPosition);
        }
    }

    @Override
    public void showVideoPlaceholder(Step step) {
        mSimpleExoPlayerViewStepDetailVideo.setDefaultArtwork(BitmapFactory.decodeResource(
                getResources(), UiUtils.getImageResource(step.getRecipeId())));
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

    @OnClick(R.id.button_step_detail_navigate_next)
    public void onNextStepClicked() {
        mStepDetailPresenter.getNextStepDetail();
    }

    @OnClick(R.id.button_step_detail_navigate_previous)
    public void onPreviousStepClicked() {
        mStepDetailPresenter.getPreviousStepDetail();
    }

    private void initPresenter() {
        mStepDetailPresenter = new StepDetailPresenter(
                RecipesRepository.getInstance(
                        RecipesRemoteDataSource.getInstance(),
                        RecipesLocalDataSource.getInstance(
                                new ModelDataMapper(),
                                RecipeDb.getInstance(getContext()))),
                this,
                SchedulerProvider.getInstance(),
                mRecipeId,
                mStepId);
    }

    /**
     * Initialize ExoPlayer.
     */
    private void initializePlayer() {
        if (mSimpleExoPlayer == null) {
            mSimpleExoPlayer = ExoPlayerFactory.newSimpleInstance(
                    new DefaultRenderersFactory(getContext()),
                    new DefaultTrackSelector(), new DefaultLoadControl());

            mSimpleExoPlayerViewStepDetailVideo.setPlayer(mSimpleExoPlayer);
        }
    }

    /**
     * Release ExoPlayer.
     */
    private void releasePlayer() {
        if (mSimpleExoPlayer != null) {
            updateStartPosition();
            mSimpleExoPlayer.stop();
            mSimpleExoPlayer.release();
            mSimpleExoPlayer = null;
        }
    }

    private void updateStartPosition() {
        if (mSimpleExoPlayer != null) {
            mStartAutoPlay = mSimpleExoPlayer.getPlayWhenReady();
            mStartWindow = mSimpleExoPlayer.getCurrentWindowIndex();
            mStartPosition = Math.max(0, mSimpleExoPlayer.getContentPosition());
        }
    }

    private void clearStartPosition() {
        mStartAutoPlay = true;
        mStartWindow = C.INDEX_UNSET;
        mStartPosition = C.TIME_UNSET;
    }

    /**
     * Builds the MediaSource
     *
     * @param uri The URI of the video to play
     * @return The built MediaSource
     */
    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("baking_app")).
                createMediaSource(uri);
    }

    public interface StepNavigationClickListener {

        void onNextStepClicked(Step step);

        void onPreviousStepClicked(Step step);
    }
}