package com.android.fitz.fastreading.presenter.impl;

import android.content.Context;
import android.text.TextUtils;

import com.android.fitz.fastreading.base.RetrofitService;
import com.android.fitz.fastreading.bean.DownLoadResult;
import com.android.fitz.fastreading.bean.UploadResult;
import com.android.fitz.fastreading.constant.AppConfig;
import com.android.fitz.fastreading.constant.Preferences;
import com.android.fitz.fastreading.injector.ContextLife;
import com.android.fitz.fastreading.presenter.IUpdatePresenter;
import com.android.fitz.fastreading.view.IUpdateView;
import com.android.library.base.BasePresenter;
import com.android.library.base.BaseSubscriber;
import com.android.library.util.ConvertUtils;
import com.android.library.util.PreferencesUtils;
import com.android.library.util.SDCardUtils;

import java.io.File;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by fitz on 2017/1/20.
 */

public class UpdatePresenterImpl extends BasePresenter<IUpdateView> implements IUpdatePresenter {

    private RetrofitService mService;
    private Context mContext;

    @Inject
    public UpdatePresenterImpl(RetrofitService service, @ContextLife("Application") Context context){
        this.mService = service;
        this.mContext = context;
    }

    @Override
    public void getData(String type) {
        mView.showProgress();
        String code = PreferencesUtils.getString(mContext, Preferences.GRANT_CODE_KEY);
        String date = PreferencesUtils.getLong(mContext, Preferences.UpdateTime) + "";

        mService.getCode("update", code, type, date)
                .compose(mView.<DownLoadResult>composeSchedulers())
                .compose(mView.<DownLoadResult>composeLifeCycle())
                .subscribe(new BaseSubscriber<DownLoadResult, IUpdateView>(mView){

                    @Override
                    public void onNext(DownLoadResult result) {
                        if(result.isOk()){
                            mView.downLoad(result.getFile(), result.getLasttime());
                        }
                    }

                    @Override
                    protected void onCompleted() {
                        //mView.hideProgress();
                    }
                });


//        String imagePath = SDCardUtils.getRootPath() + AppConfig.CACHE_PATH;
//        imagePath += "/anima/1.png";
//        File file = new File(imagePath);
//        if(file.exists()){
//            int i = 0;
//        }else{
//            int i = 1;
//        }
//
//        // create RequestBody instance from file
//        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//
//        MultipartBody.Part body =
//                MultipartBody.Part.createFormData("uploaded_file", file.getName(), requestFile);
//
//        mService.uploadFile(body)
//                .compose(mView.<UploadResult>composeSchedulers())
//                .compose(mView.<UploadResult>composeLifeCycle())
//                .subscribe(new BaseSubscriber<UploadResult, IUpdateView>(mView){
//
//                    @Override
//                    public void onNext(UploadResult result) {
//
//                    }
//
//                    @Override
//                    protected void onCompleted() {
//                        mView.hideProgress();
//                    }
//                });
    }
}
