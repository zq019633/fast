package com.android.fitz.fastreading.injector.module;

import android.app.Service;
import android.content.Context;


import com.android.fitz.fastreading.injector.ContextLife;
import com.android.fitz.fastreading.injector.PerService;

import dagger.Module;
import dagger.Provides;

/**
 * Created by yuyidong on 15/11/22.
 */
@Module
public class ServiceModule {
    private Service mService;

    public ServiceModule(Service service) {
        mService = service;
    }

    @Provides
    @PerService
    @ContextLife("Service")
    public Context provideContext() {
        return mService;
    }
}
