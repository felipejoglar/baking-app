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

package com.fjoglar.bakingapp;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

/**
 * Use cases are the entry points to the domain layer. This class represents a execution unit for
 * different use cases.
 * <p>
 * By convention each UseCase implementation will return the result using a
 * {@link DisposableObserver} that will execute its job in a background thread and post the result
 * in the UI thread (Main thread).
 *
 * @param <T>      the request type
 * @param <Params> Parameters (Optional) used to build/execute this use case.
 */
public abstract class UseCase<T, Params> {

    private final Scheduler mThreadExecutor;
    private final Scheduler mPostExecutionThread;
    private final CompositeDisposable mDisposables;

    public UseCase(Scheduler threadExecutor, Scheduler postExecutionThread) {
        mThreadExecutor = threadExecutor;
        mPostExecutionThread = postExecutionThread;
        mDisposables = new CompositeDisposable();
    }

    /**
     * Builds an {@link Observable} which will be used when executing the current {@link UseCase}.
     */
    public abstract Observable<T> buildUseCaseObservable(Params params);

    /**
     * Executes the current use case.
     *
     * @param observer {@link DisposableObserver} which will be listening to the observable build
     *                 by {@link #buildUseCaseObservable(Params params)} ()} method.
     * @param params   Parameters (Optional) used to build/execute this use case.
     */
    public void execute(DisposableObserver<T> observer, Params params) {

        final Observable<T> observable = this.buildUseCaseObservable(params)
                .subscribeOn(mThreadExecutor)
                .observeOn(mPostExecutionThread);
        addDisposable(observable.subscribeWith(observer));
    }

    /**
     * Dispose from current {@link CompositeDisposable}.
     */
    public void dispose() {
        if (!mDisposables.isDisposed()) {
            mDisposables.dispose();
        }
    }

    /**
     * Dispose from current {@link CompositeDisposable}.
     */
    private void addDisposable(Disposable disposable) {
        mDisposables.add(disposable);
    }
}