package com.android.fitz.fastreading.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.fitz.fastreading.R;
import com.android.fitz.fastreading.base.AppApplication;
import com.android.fitz.fastreading.base.BaseActivity;
import com.android.fitz.fastreading.constant.AppConfig;
import com.android.fitz.fastreading.db.entity.Article;
import com.android.fitz.fastreading.db.entity.Question;
import com.android.fitz.fastreading.db.greendao.AnimaPictDao;
import com.android.fitz.fastreading.db.greendao.ArticleDao;
import com.android.fitz.fastreading.db.greendao.DaoSession;
import com.android.fitz.fastreading.db.greendao.QuestionDao;
import com.android.fitz.fastreading.db.greendao.TestDao;
import com.android.fitz.fastreading.eventbus.DownProgressEvent;
import com.android.fitz.fastreading.presenter.impl.UpdatePresenterImpl;
import com.android.fitz.fastreading.service.UpdateService;
import com.android.fitz.fastreading.view.IUpdateView;
import com.android.library.util.FileUtils;
import com.android.library.util.SDCardUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

import butterknife.ButterKnife;
import butterknife.OnClick;
import zlc.season.rxdownload2.entity.DownloadEvent;

public class VersionUpdateActivity extends BaseActivity<UpdatePresenterImpl> implements IUpdateView {

    AnimaPictDao animDao;
    ArticleDao artDao;
    QuestionDao questionDao;
    TestDao testDao;

    MaterialDialog progress;

    @Override
    protected void initView() {
        DaoSession daoSession = ((AppApplication) getApplication()).getDaoSession();
        animDao = daoSession.getAnimaPictDao();
        artDao = daoSession.getArticleDao();
        testDao = daoSession.getTestDao();
        questionDao = daoSession.getQuestionDao();
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_version_update;
    }

    @Override
    protected boolean isBindEventBus() {
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void downLoadComplete(DownProgressEvent messageEvent) {
        if(messageEvent.getProgress() == 100 || messageEvent.getProgress() == -1) {
            hideProgress();
        }else{
            if(progress!= null && !progress.isCancelled()) {
                progress.setProgress(messageEvent.getProgress());
            }
        }
    }

    @Override
    protected void initInjector() {
        getActivityComponent().inject(this);
    }

    @OnClick({R.id.down, R.id.cover, R.id.back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.down:
                // 部分更新
                mPresenter.getData("1");
                break;
            case R.id.cover:
                coverData();
                break;
            case R.id.back:
                finish();
                break;
        }
    }

    @Override
    public void downLoad(String url, long UpdateTime) {
        Intent intent = new Intent(getApplicationContext(), UpdateService.class);
        intent.putExtra("url", url);
        intent.putExtra("updateTime", UpdateTime);
        startService(intent);
    }

    @Override
    public void showProgress() {
        progress = new MaterialDialog.Builder(this)
                .content("下载中,请稍后...")
                .progress(false, 100, false)
                .canceledOnTouchOutside(false)
                .show();
        progress.show();
    }

    @Override
    public void hideProgress() {
        if(progress != null && progress.isShowing()){
            progress.dismiss();
        }
    }

    private void coverData(){
        animDao.deleteAll();
        artDao.deleteAll();
        questionDao.deleteAll();
        testDao.deleteAll();

        String path = SDCardUtils.getRootPath() + AppConfig.CACHE_PATH;
        File file = new File(path);
        FileUtils.deleteAllFilesOfDir(file);

        // 完全覆盖
        mPresenter.getData("2");
    }
}
