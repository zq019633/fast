package com.android.fitz.fastreading.injector.component;

import android.content.Context;

import com.android.fitz.fastreading.injector.ContextLife;
import com.android.fitz.fastreading.injector.PerService;
import com.android.fitz.fastreading.injector.module.ServiceModule;

import dagger.Component;

/**
 * Created by yuyidong on 15/11/22.
 */
@PerService
@Component(dependencies = ApplicationComponent.class, modules = {ServiceModule.class})
public interface ServiceComponent {

    @ContextLife("Service")
    Context getServiceContext();

    @ContextLife("Application")
    Context getApplicationContext();

}
