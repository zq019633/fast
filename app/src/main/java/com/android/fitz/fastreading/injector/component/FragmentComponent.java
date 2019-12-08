package com.android.fitz.fastreading.injector.component;

import android.app.Activity;
import android.content.Context;

import com.android.fitz.fastreading.injector.ContextLife;
import com.android.fitz.fastreading.injector.PerFragment;
import com.android.fitz.fastreading.injector.module.FragmentModule;

import dagger.Component;

/**
 * Created by yuyidong on 15/11/22.
 */
@PerFragment
@Component(modules = FragmentModule.class, dependencies = ApplicationComponent.class)
public interface FragmentComponent {
    @ContextLife("Application")
    Context getContext();

    @ContextLife("Activity")
    Context getActivityContext();

    Activity getActivity();
}
