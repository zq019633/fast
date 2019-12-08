/*
 * Copyright (c) 2015 [1076559197@qq.com | tchen0707@gmail.com]
 *
 * Licensed under the Apache License, Version 2.0 (the "License”);
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.library.base;

import com.trello.rxlifecycle2.LifecycleTransformer;

import io.reactivex.FlowableTransformer;

public interface IView {

    void showLoading();

    void hide();

    void showException(Throwable ex);

    void showNetError();

    void showMsg(String msg);

    void showEmpty(String msg);

    void showEmpty();

    void showDialog(String content);

    void showProgress();

    void hideProgress();

    void onComplete();

    void onTaskFinish();

    void onResutCode(String code, String msg);

    <T> FlowableTransformer<T, T> composeSchedulers();

    <T> LifecycleTransformer<T> composeLifeCycle();
}
