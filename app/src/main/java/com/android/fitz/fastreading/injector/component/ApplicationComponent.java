package com.android.fitz.fastreading.injector.component;

import android.app.Application;
import android.content.Context;

import com.android.fitz.fastreading.base.AppApplication;
import com.android.fitz.fastreading.base.RetrofitService;
import com.android.fitz.fastreading.injector.ContextLife;
import com.android.fitz.fastreading.injector.module.ApplicationModule;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Component;

/**
 * Author:  ljo_h
 * Date:    2016/6/28
 * Description:
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    @ContextLife("Application")
    Context getContext();

    AppApplication injectApplication(AppApplication application);

    RetrofitService getRetrofitService();

    @Named("TokenID")
    String getTokenID();

    @Named("UserID")
    String getUserID();
}
