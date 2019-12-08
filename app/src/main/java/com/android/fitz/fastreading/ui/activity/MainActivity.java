package com.android.fitz.fastreading.ui.activity;

import android.os.Environment;
import android.util.Log;
import android.view.View;

import com.android.fitz.fastreading.R;
import com.android.fitz.fastreading.base.BaseActivity;
import com.android.fitz.fastreading.constant.AppConfig;
import com.android.fitz.fastreading.constant.Preferences;
import com.android.fitz.fastreading.eventbus.DownProgressEvent;
import com.android.fitz.fastreading.presenter.impl.MainPresenterImpl;
import com.android.fitz.fastreading.service.UpdateService;
import com.android.fitz.fastreading.ui.activity.Test.ReadTestMainActivity;
import com.android.fitz.fastreading.ui.activity.classes.ClassesMainActivity;
import com.android.fitz.fastreading.ui.activity.train.ExtendTrainMainActivity;
import com.android.fitz.fastreading.utils.DirTraversal;
import com.android.fitz.fastreading.utils.ZipUtils;
import com.android.fitz.fastreading.view.IEmptyView;
import com.android.library.util.PreferencesUtils;
import com.android.library.util.SDCardUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.util.zip.ZipException;

import butterknife.OnClick;
import zlc.season.rxdownload2.entity.DownloadEvent;

public class MainActivity extends BaseActivity<MainPresenterImpl> implements IEmptyView {

    @Override
    protected void initView() {
        // 速度
        int speed = PreferencesUtils.getInt(getApplicationContext(), Preferences.SETTING_LEFT, 0);
        if(speed == 0){
            PreferencesUtils.putInt(getApplicationContext(), Preferences.SETTING_LEFT, 2);
        }

        // 时间
        int time = PreferencesUtils.getInt(getApplicationContext(), Preferences.SETTING_RIGHT, 0);
        if(time == 0){
            PreferencesUtils.putInt(getApplicationContext(), Preferences.SETTING_RIGHT, 40);
        }
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initInjector() {
        getActivityComponent().inject(this);
    }

    @Override
    protected boolean isBindEventBus() {
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void downLoadComplete(DownProgressEvent messageEvent) {
        if(messageEvent.getProgress() == 100) {
            showMsg("下载完成.");
            Log.e("tag", 100 + "");
        }else if(messageEvent.getProgress() == -1){
            showMsg("下载失败,请重试.");
        }
    }

    @OnClick({R.id.update, R.id.classes, R.id.rede, R.id.train, R.id.quit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.update:
//                mPresenter.checkCode();
                break;
            case R.id.classes:
                readyGo(ClassesMainActivity.class);
                break;
            case R.id.rede:
                readyGo(ReadTestMainActivity.class);
                break;
            case R.id.train:
                readyGo(ExtendTrainMainActivity.class);
                break;
            case R.id.quit:
                quit();
                break;
        }
    }

    @Override
    public void onTaskFinish() {
        readyGo(VersionUpdateActivity.class);
    }

    private void quit() {
         finish();
    }
}
