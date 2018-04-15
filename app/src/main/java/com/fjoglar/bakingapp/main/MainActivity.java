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

package com.fjoglar.bakingapp.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fjoglar.bakingapp.boilerplate.R;
import com.fjoglar.bakingapp.data.source.RecipesRepository;
import com.fjoglar.bakingapp.data.source.local.RecipesLocalDataSource;
import com.fjoglar.bakingapp.data.source.remote.RecipesRemoteDataSource;
import com.fjoglar.bakingapp.util.schedulers.SchedulerProvider;

public class MainActivity extends AppCompatActivity implements RecipesContract.View {

    private RecipesContract.Presenter mMainPresenter;

    private TextView mTxtWelcomeMessage;
    private ProgressBar mProgressLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTxtWelcomeMessage = (TextView) findViewById(R.id.txt_welcome_message);
        mProgressLoading = (ProgressBar) findViewById(R.id.progress_loading);

        mMainPresenter = new RecipesPresenter(
                RecipesRepository.getInstance(RecipesRemoteDataSource.getInstance(),
                        RecipesLocalDataSource.getInstance()),
                this,
                SchedulerProvider.getInstance());
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMainPresenter.subscribe();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMainPresenter.unsubscribe();
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
    public void setPresenter(@NonNull RecipesContract.Presenter presenter) {
        mMainPresenter = presenter;
    }

    @Override
    public void showWelcomeMessage(String message) {
        mTxtWelcomeMessage.setText(message);
    }

    @Override
    public void showLoading() {
        mProgressLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        mProgressLoading.setVisibility(View.GONE);
    }
}
