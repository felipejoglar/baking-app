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

package com.fjoglar.bakingapp.recipedetail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.fjoglar.bakingapp.R;
import com.fjoglar.bakingapp.data.model.Recipe;
import com.fjoglar.bakingapp.data.model.Step;
import com.fjoglar.bakingapp.stepdetail.StepDetailActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Main UI for the recipe detail screen.
 */
public class RecipeDetailFragment extends Fragment implements RecipeDetailContract.View,
        StepsAdapter.OnStepClickListener {

    @NonNull
    private static final String ARGUMENT_RECIPE = "recipe";

    private RecipeDetailContract.Presenter mRecipeDetailPresenter;
    private Recipe mRecipe;
    private IngredientsAdapter mIngredientsAdapter;
    private StepsAdapter mStepsAdapter;

    @BindView(R.id.recyclerview_recipe_detail_ingredients)
    RecyclerView mRecyclerViewRecipeDetailIngredients;
    @BindView(R.id.recyclerview_recipe_detail_steps)
    RecyclerView mRecyclerViewRecipeDetailSteps;
    @BindView(R.id.progressbar_loading)
    ProgressBar mProgressBarLoading;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the fragment
     */
    public RecipeDetailFragment() {
    }

    public static RecipeDetailFragment newInstance(@NonNull Recipe recipe) {
        Bundle arguments = new Bundle();
        arguments.putParcelable(ARGUMENT_RECIPE, recipe);

        RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
        recipeDetailFragment.setArguments(arguments);
        return recipeDetailFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(ARGUMENT_RECIPE)) {
            mRecipe = getArguments().getParcelable(ARGUMENT_RECIPE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recipe_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setUpIngredientsRecyclerView();
        setUpStepsRecyclerView();

        initPresenter();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mRecipeDetailPresenter.subscribe();
    }

    @Override
    public void onPause() {
        super.onPause();
        mRecipeDetailPresenter.unsubscribe();
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
    public void setPresenter(@NonNull RecipeDetailContract.Presenter presenter) {
        mRecipeDetailPresenter = presenter;
    }

    @Override
    public void showRecipeDetail(Recipe recipe) {
        mIngredientsAdapter.setIngredientsList(recipe.getIngredients());
        mStepsAdapter.setStepsList(recipe.getSteps());
    }

    @Override
    public void showTitle(String title) {
        // TODO: Update fragment title.
    }

    @Override
    public void showLoading() {
        mProgressBarLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        mProgressBarLoading.setVisibility(View.GONE);
    }

    @Override
    public void onStepClicked(Step step) {
        Intent stepDetailActivityIntent = new Intent(getActivity(), StepDetailActivity.class);
        stepDetailActivityIntent.putExtra(StepDetailActivity.EXTRA_STEP, step);
        startActivity(stepDetailActivityIntent);
    }

    private void initPresenter() {
        mRecipeDetailPresenter = new RecipeDetailPresenter(this, mRecipe);
    }

    private void setUpIngredientsRecyclerView() {
        mIngredientsAdapter = new IngredientsAdapter();

        mRecyclerViewRecipeDetailIngredients.setHasFixedSize(true);
        mRecyclerViewRecipeDetailIngredients.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerViewRecipeDetailIngredients.setNestedScrollingEnabled(false);
        mRecyclerViewRecipeDetailIngredients.addItemDecoration(
                new DividerItemDecoration(mRecyclerViewRecipeDetailIngredients.getContext(),
                        DividerItemDecoration.VERTICAL));
        mRecyclerViewRecipeDetailIngredients.setAdapter(mIngredientsAdapter);
    }

    private void setUpStepsRecyclerView() {
        mStepsAdapter = new StepsAdapter(this);

        mRecyclerViewRecipeDetailSteps.setHasFixedSize(true);
        mRecyclerViewRecipeDetailSteps.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerViewRecipeDetailSteps.setNestedScrollingEnabled(false);
        mRecyclerViewRecipeDetailSteps.addItemDecoration(
                new DividerItemDecoration(mRecyclerViewRecipeDetailSteps.getContext(),
                        DividerItemDecoration.VERTICAL));
        mRecyclerViewRecipeDetailSteps.setAdapter(mStepsAdapter);
    }
}