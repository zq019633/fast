package com.android.fitz.fastreading.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.fitz.fastreading.base.AppApplication;
import com.android.fitz.fastreading.bean.DownLoadDataEntity;
import com.android.fitz.fastreading.constant.AppConfig;
import com.android.fitz.fastreading.constant.Preferences;
import com.android.fitz.fastreading.db.entity.AnimaPict;
import com.android.fitz.fastreading.db.entity.Article;
import com.android.fitz.fastreading.db.entity.MenuView;
import com.android.fitz.fastreading.db.entity.Question;
import com.android.fitz.fastreading.db.entity.Test;
import com.android.fitz.fastreading.db.entity.Words;
import com.android.fitz.fastreading.db.greendao.AnimaPictDao;
import com.android.fitz.fastreading.db.greendao.ArticleDao;
import com.android.fitz.fastreading.db.greendao.DaoSession;
import com.android.fitz.fastreading.db.greendao.MenuViewDao;
import com.android.fitz.fastreading.db.greendao.QuestionDao;
import com.android.fitz.fastreading.db.greendao.TestDao;
import com.android.fitz.fastreading.db.greendao.WordsDao;
import com.android.fitz.fastreading.eventbus.DownProgressEvent;
import com.android.fitz.fastreading.utils.DbUtils;
import com.android.fitz.fastreading.utils.DirTraversal;
import com.android.fitz.fastreading.utils.ZipUtils;
import com.android.library.util.FileUtils;
import com.android.library.util.JSONHandlerUtils;
import com.android.library.util.PreferencesUtils;
import com.android.library.util.SDCardUtils;
import com.google.gson.Gson;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipException;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import zlc.season.rxdownload2.RxDownload;
import zlc.season.rxdownload2.entity.DownloadEvent;
import zlc.season.rxdownload2.entity.DownloadStatus;

public class UpdateService extends Service {

    String saveName = "cache.zip";
    Long updateTime;
    final String defaultPath = SDCardUtils.getRootPath() + AppConfig.SAVE_PATH;
    final String cachePath = SDCardUtils.getRootPath() + AppConfig.CACHE_PATH;
    String mDownloadUrl = "";
    private Disposable mDisposable;
    //NotifyUtils notify;
    String path = "";
    int iProgress = 0;
    int mTotal = 0;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent.getExtras() != null){
            mDownloadUrl = intent.getExtras().getString("url");
            updateTime = intent.getExtras().getLong("UpdateTime");

            startDownload();
        }else{
            stopSelf();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void startDownload() {
        path = String.format("%s/%s", defaultPath, saveName);
        if(!FileUtils.isFileExist(path)){
            boolean flag = FileUtils.makeDirs(path);
            int i  = 0;
        }else{
            FileUtils.deleteFile(path);
        }
        mTotal = 0;
        mDisposable = RxDownload.getInstance(getApplicationContext())
                //.download(mDownloadUrl)
                .download(mDownloadUrl, saveName, defaultPath)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DownloadStatus>() {
                    @Override
                    public void accept(DownloadStatus downloadStatus) throws Exception {
                        iProgress = getPercent(downloadStatus.getDownloadSize(), downloadStatus.getTotalSize());
                        if (iProgress > mTotal) {
                            mTotal = iProgress;
                            if(mTotal < 100) {
                                EventBus.getDefault().post(new DownProgressEvent(mTotal));
                            }
                        }
                        //Log.e("down", iProgress + "");
                    }
                }, new Consumer<Throwable>() {

                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        EventBus.getDefault().post(new DownProgressEvent(-1));
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        decodeZip();
                    }
                });
    }

    /// 进度
    public int getPercent(long downloadSize,long totalSize) {
        String percent;
        Double result;
        if (totalSize == 0L) {
            result = 0.0;
        } else {
            result = downloadSize * 1.0 / totalSize;
        }
        result = result * 100;
        return result.intValue();
    }

    /// 解析ZIP包
    public void decodeZip(){
        try {
            File file = DirTraversal.getFilePath(defaultPath + "/", saveName);
            if(file.exists()) {
                ZipUtils.upZipFile(file, cachePath);

                updateDB();
            }
        } catch (ZipException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 存储数据
    public void updateDB(){
        String pathString = cachePath +  "/data.json";
        String result = JSONHandlerUtils.readTxtFile(pathString);

        DownLoadDataEntity entity = new Gson().fromJson(result, DownLoadDataEntity.class);

        final List<AnimaPict> list = DbUtils.getAnimaPict(entity);
        final List<Article> artlist = DbUtils.getArticle(entity);
        final List<Test> testList = DbUtils.getTest(entity);
        final List<Question> questionList = DbUtils.getQuestion(entity);
        final List<MenuView> menuList = DbUtils.getMenuView(entity);
        final List<Words> wordsList =   DbUtils.getWordsView(entity);

        // 动物图片，文章，测试题，问题集
        DaoSession daoSession = ((AppApplication) getApplication()).getDaoSession();
        final AnimaPictDao animDao = daoSession.getAnimaPictDao();
        final ArticleDao artDao = daoSession.getArticleDao();
        final TestDao testDao = daoSession.getTestDao();
        final QuestionDao questionDao = daoSession.getQuestionDao();
        final MenuViewDao menuViewDao = daoSession.getMenuViewDao();
        final WordsDao wordsDao = daoSession.getWordsDao();

        animDao.rx()
                .insertOrReplaceInTx(list)
                .flatMap(new Func1<Iterable<AnimaPict>, Observable<Iterable<Article>>>() {
                    @Override
                    public Observable<Iterable<Article>> call(Iterable<AnimaPict> animaPicts) {
                        return artDao.rx().insertOrReplaceInTx(artlist);
                    }
                })
                .flatMap(new Func1<Iterable<Article>, Observable<Iterable<Test>>>() {
                    @Override
                    public Observable<Iterable<Test>> call(Iterable<Article> articles) {
                        return testDao.rx().insertOrReplaceInTx(testList);
                    }
                })
                .flatMap(new Func1<Iterable<Test>, Observable<Iterable<Question>>>() {
                    @Override
                    public Observable<Iterable<Question>> call(Iterable<Test> tests) {
                        return questionDao.rx().insertOrReplaceInTx(questionList);
                    }
                })
                .flatMap(new Func1<Iterable<Question>, Observable<Iterable<Words>>>() {
                    @Override
                    public Observable<Iterable<Words>> call(Iterable<Question> questions) {
                        return wordsDao.rx().insertOrReplaceInTx(wordsList);
                    }
                })
                .flatMap(new Func1<Iterable<Words>, Observable<Iterable<MenuView>>>() {
                    @Override
                    public Observable<Iterable<MenuView>> call(Iterable<Words> questions) {
                        return menuViewDao.rx().insertOrReplaceInTx(menuList);
                    }
                })
                .subscribeOn(rx.schedulers.Schedulers.io())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Iterable<MenuView>>() {
                    @Override
                    public void onCompleted() {
                        PreferencesUtils.putLong(getApplicationContext(), Preferences.UpdateTime, updateTime);

                        EventBus.getDefault().post(new DownProgressEvent(100));
                        stopSelf();
                    }

                    @Override
                    public void onError(Throwable e) {

                        EventBus.getDefault().post(new DownProgressEvent(100));
                        stopSelf();
                    }

                    @Override
                    public void onNext(Iterable<MenuView> menuViews) {

                    }
                });
    }

    @Override
    public void onDestroy() {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
