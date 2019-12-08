package com.android.fitz.fastreading.ui.activity.train.game3;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.fitz.fastreading.R;
import com.android.fitz.fastreading.base.AppApplication;
import com.android.fitz.fastreading.base.BaseActivity;
import com.android.fitz.fastreading.constant.Constant;
import com.android.fitz.fastreading.constant.Preferences;
import com.android.fitz.fastreading.db.entity.MenuView;
import com.android.fitz.fastreading.db.entity.Words;
import com.android.fitz.fastreading.db.greendao.ArticleDao;
import com.android.fitz.fastreading.db.greendao.DaoSession;
import com.android.fitz.fastreading.db.greendao.MenuViewDao;
import com.android.fitz.fastreading.db.greendao.WordsDao;
import com.android.fitz.fastreading.ui.activity.train.game2.FollowMeEndActivity;
import com.android.library.util.ConvertUtils;
import com.android.library.util.PreferencesUtils;
import com.android.library.util.ToastUtils;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Flowable;

public class EnglishWordActivity extends BaseActivity {
    @BindView(R.id.content)
    TextView content;

    // 游戏Id
    String id;
    //MenuViewDao menuViewDao;
    WordsDao wordsDao;
    // 长度
    int length;
    // 停留时间
    int speed;
    // 总时长
    int timeCount;
    List<Words> wList;
    List<String> wordList;

    @Override
    protected void initView() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            id = bundle.getString(Constant.ID);

            length = PreferencesUtils.getInt(getApplicationContext(), Preferences.SETTING_LENGTH) + 3;
            if(length<=6) {
                length = length - 2;
            }
        }
        wordList = new ArrayList<>();

        speed = PreferencesUtils.getInt(getApplicationContext(), Preferences.SETTING_LEFT);
        timeCount = PreferencesUtils.getInt(getApplicationContext(), Preferences.SETTING_RIGHT);

        DaoSession daoSession = ((AppApplication) getApplication()).getDaoSession();
        //menuViewDao = daoSession.getMenuViewDao();
        wordsDao = daoSession.getWordsDao();

        initData();
    }

    private void initData() {
        //final MenuView menuView = menuViewDao.load(id);
        wList = wordsDao.queryBuilder()
                .where(WordsDao.Properties.View_train_id.eq(9), WordsDao.Properties.Len.eq(length))
                .list();
        if(wList.size() == 0){
            ToastUtils.LongToast(getApplicationContext(), "没有该长度的字符.");
            readyGoThenKill(FollowMeEndActivity.class);
            return;
        }
        // 字符串长度
        int count = timeCount * speed;

        do{
            for(Words item : wList){
                wordList.add(item.getContent());
            }
        }while (wordList.size() < count);

        Collections.shuffle(wordList);

        //Log.e("tag", speed + "");
        Flowable.interval(1000 / speed, TimeUnit.MILLISECONDS)
                .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
                .compose(this.<Long>composeLifeCycle())
                .take(timeCount * speed)
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(Long aLong) {
                        if(ConvertUtils.toInt(aLong) % (2 * speed) == 0) {
                            content.setText(wordList.get(ConvertUtils.toInt(aLong)));
                            content.setVisibility(View.VISIBLE);
                        }else {
                            content.setVisibility(View.GONE);
                        }
                        if (aLong == timeCount * speed - 1) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    readyGoThenKill(FollowMeEndActivity.class);
                                }
                            }, 1000);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_english_word;
    }

    @OnClick(R.id.home)
    public void onViewClicked() {
        finish();
    }

    @Override
    protected void initInjector() {

    }
}
