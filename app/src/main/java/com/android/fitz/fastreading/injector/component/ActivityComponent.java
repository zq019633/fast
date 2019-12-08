package com.android.fitz.fastreading.injector.component;

import android.app.Activity;
import android.content.Context;

import com.android.fitz.fastreading.injector.ContextLife;
import com.android.fitz.fastreading.injector.PerActivity;
import com.android.fitz.fastreading.injector.module.ActivityModule;
import com.android.fitz.fastreading.ui.activity.MainActivity;
import com.android.fitz.fastreading.ui.activity.SplashActivity;
import com.android.fitz.fastreading.ui.activity.VersionUpdateActivity;

import dagger.Component;

/**
 * Created by yuyidong on 15/11/22.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class})
public interface ActivityComponent {

    @ContextLife("Activity")
    Context getActivityContext();

    @ContextLife("Application")
    Context getApplicationContext();

    Activity getActivity();

    void inject(VersionUpdateActivity versionUpdateActivity);

    void inject(SplashActivity splashActivity);

    void inject(MainActivity mainActivity);
}
