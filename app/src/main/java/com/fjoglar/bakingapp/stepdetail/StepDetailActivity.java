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

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.fjoglar.bakingapp.R;
import com.fjoglar.bakingapp.data.model.Step;

import java.util.List;

/**
 * Displays step details screen.
 */
public class StepDetailActivity extends AppCompatActivity
        implements StepDetailFragment.StepNavigationClickListener{

    @NonNull
    public static final String EXTRA_STEP_LIST = "step_list";
    @NonNull
    public static final String EXTRA_STEP_INDEX = "step_index";

    private List<Step> mStepList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);

        Intent intent = getIntent();
        if (intent == null || !intent.hasExtra(EXTRA_STEP_LIST) ||
                !intent.hasExtra(EXTRA_STEP_INDEX)) {
            closeOnError();
            return;
        }

        // Get the requested step
        mStepList = getIntent().getParcelableArrayListExtra(EXTRA_STEP_LIST);
        int stepIndex = getIntent().getIntExtra(EXTRA_STEP_INDEX, 0);

        StepDetailFragment stepDetailFragment = (StepDetailFragment) getSupportFragmentManager()
                .findFragmentById(R.id.framelayout_step_detail_container);

        if (stepDetailFragment == null) {
            // Create a new recipe detail fragment
            stepDetailFragment = StepDetailFragment.newInstance(mStepList, stepIndex);

            // Add the fragment to its container using a FragmentManager and a Transaction
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.framelayout_step_detail_container, stepDetailFragment)
                    .commit();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // When the home button is pressed, take the user back
        // to the ParentActivity
        if (item.getItemId() == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }

    public void onNextStepClicked(Step step) {
        replaceStepFragment(mStepList.indexOf(step));
    }

    public void onPreviousStepClicked(Step step) {
        replaceStepFragment(mStepList.indexOf(step));
    }

    private void replaceStepFragment(int stepIndex) {
        // Set the step to show
        StepDetailFragment stepDetailFragment = StepDetailFragment.newInstance(mStepList, stepIndex);

        // Add the fragment to its container using a FragmentManager and a Transaction
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.framelayout_step_detail_container, stepDetailFragment)
                .commit();
    }

    private void closeOnError() {
        finish();
        Toast.makeText(getApplicationContext(), R.string.recipe_detail_error_message, Toast.LENGTH_SHORT).show();
    }
}
