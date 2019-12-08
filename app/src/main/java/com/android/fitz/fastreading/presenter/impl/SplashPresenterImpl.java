package com.android.fitz.fastreading.presenter.impl;

import android.content.Context;

import com.android.fitz.fastreading.base.RetrofitService;
import com.android.fitz.fastreading.bean.CommonResult;
import com.android.fitz.fastreading.bean.DownLoadResult;
import com.android.fitz.fastreading.constant.Preferences;
import com.android.fitz.fastreading.injector.ContextLife;
import com.android.fitz.fastreading.presenter.ISplashPresenter;
import com.android.fitz.fastreading.presenter.IUpdatePresenter;
import com.android.fitz.fastreading.view.ISplashView;
import com.android.fitz.fastreading.view.IUpdateView;
import com.android.library.base.BasePresenter;
import com.android.library.base.BaseSubscriber;
import com.android.library.util.PreferencesUtils;

import javax.inject.Inject;

/**
 * Created by fitz on 2017/1/20.
 */

public class SplashPresenterImpl extends BasePresenter<ISplashView> implements ISplashPresenter {

    private RetrofitService mService;
    private Context mContext;

    @Inject
    public SplashPresenterImpl(RetrofitService service, @ContextLife("Application") Context context){
        this.mService = service;
        this.mContext = context;
    }

    @Override
    public void checkCode(String mac, final String verCode) {
        mView.showProgress();
        mService.checkCode("checkvercode", mac, verCode)
                .compose(mView.<CommonResult>composeSchedulers())
                .compose(mView.<CommonResult>composeLifeCycle())
                .subscribe(new BaseSubscriber<CommonResult, ISplashView>(mView){

                    @Override
                    public void onNext(CommonResult result) {
                        if(result.isOk()){
                            PreferencesUtils.putString(mContext, Preferences.GRANT_CODE_KEY, verCode);
                            mView.onTaskFinish();
                        }else{
                            mView.showMsg(result.getMsg());
                        }
                    }

                    @Override
                    protected void onCompleted() {
                        mView.hideProgress();
                    }
                });
    }
}
