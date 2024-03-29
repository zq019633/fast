package com.android.fitz.fastreading.injector.module;

import android.app.Activity;
import android.content.Context;

import com.android.fitz.fastreading.injector.ContextLife;
import com.android.fitz.fastreading.injector.PerActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created by yuyidong on 15/11/22.
 */
@Module
public class ActivityModule {
    private Activity mActivity;

    public ActivityModule(Activity activity) {
        mActivity = activity;
    }

    @Provides
    @PerActivity
    @ContextLife("Activity")
    public Context provideContext() {
        return mActivity;
    }

    @Provides
    @PerActivity
    public Activity provideActivity() {
        return mActivity;
    }

}
