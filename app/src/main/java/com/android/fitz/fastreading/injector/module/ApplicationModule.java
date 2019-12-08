package com.android.fitz.fastreading.injector.module;

import android.app.Application;
import android.content.Context;

import com.android.library.util.AppUtils;
import com.android.library.util.EncryptUtils;
import com.android.library.util.ObjectUtils;
import com.android.library.util.PreferencesUtils;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.android.fitz.fastreading.base.AppApplication;
import com.android.fitz.fastreading.base.RetrofitService;
import com.android.fitz.fastreading.constant.AppConfig;
import com.android.fitz.fastreading.constant.Preferences;
import com.android.fitz.fastreading.injector.ContextLife;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Author:  ljo_h
 * Date:    2016/6/28
 * Description:
 */
@Module
public class
ApplicationModule {
    AppApplication mApplication;

    public ApplicationModule(AppApplication application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    @ContextLife("Application")
    public Context provideContext() {
        return mApplication.getApplicationContext();
    }

    @Provides
    @Singleton
    RetrofitService provideRetrofitService(){
        //手动创建一个OkHttpClient并设置超时时间
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();

        if(AppConfig.DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClientBuilder.addInterceptor(logging);
        }
        httpClientBuilder.connectTimeout(2, TimeUnit.MINUTES);

        //String baseUrl = "https://api.github.com";
        //String baseUrl = "http://121.40.30.66:9038";
        //String baseUrl = "http://192.168.1.63:8886";
        Retrofit retrofit = new Retrofit.Builder()
                .client(httpClientBuilder.build())
                .baseUrl(AppConfig.HTTP_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit.create(RetrofitService.class);
    }

    @Provides
    @Named("UserID")
    String provideUserID(){
        return PreferencesUtils.getString(mApplication.getApplicationContext(), Preferences.USERID);
    }

    @Provides
    @Named("TokenID")
    String provideTokenID(){
        return PreferencesUtils.getString(mApplication.getApplicationContext(), Preferences.TAKENID);
    }
}
