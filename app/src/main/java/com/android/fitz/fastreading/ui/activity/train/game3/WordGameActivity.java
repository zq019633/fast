package com.android.fitz.fastreading.ui.activity.train.game3;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.fitz.fastreading.R;
import com.android.fitz.fastreading.base.AppApplication;
import com.android.fitz.fastreading.base.BaseActivity;
import com.android.fitz.fastreading.bean.MarginEntity;
import com.android.fitz.fastreading.constant.Constant;
import com.android.fitz.fastreading.constant.Preferences;
import com.android.fitz.fastreading.db.entity.MenuView;
import com.android.fitz.fastreading.db.entity.Words;
import com.android.fitz.fastreading.db.greendao.DaoSession;
import com.android.fitz.fastreading.db.greendao.MenuViewDao;
import com.android.fitz.fastreading.db.greendao.WordsDao;
import com.android.fitz.fastreading.ui.activity.train.game2.FollowMeEndActivity;
import com.android.fitz.fastreading.utils.CommonUtils;
import com.android.library.util.ConvertUtils;
import com.android.library.util.PreferencesUtils;
import com.android.library.util.ToastUtils;
import com.bumptech.glide.Glide;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Flowable;

public class WordGameActivity extends BaseActivity {

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
    // 显示长度
    final int maxLength = 11;

    List<Words> wList;
    List<String> wordList;

    @Override
    protected void initView() {
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            id = bundle.getString(Constant.ID);

            length = PreferencesUtils.getInt(getApplicationContext(), Preferences.SETTING_LENGTH) + 3;
            // 短文,中文
            if(id.equals("10") || id.equals("8")){
                //content.setTextSize(40);
                //content.setTextScaleX(2);
            }else{
                // 数字
                if(length > maxLength){
                    //content.setTextSize(40);
                    //content.setTextScaleX(3);
                }
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

    private void initData(){
        if(id.equals("10")){
            // 短文
            int count = timeCount * speed * length;
            wList = wordsDao.queryBuilder()
                    .where(WordsDao.Properties.View_train_id.eq(id))
                    .list();
            if(wList.size() == 0){
                ToastUtils.LongToast(getApplicationContext(), "没有该长度的字符.");
                readyGoThenKill(FollowMeEndActivity.class);
                return;
            }
            // 文章值
//            String content = "";
//            for(Words item : wList){
//                content += item.getContent();
//            }
//            while (content.length() < count) {
//                content += content;
//            }
//            // 短文切割
//            for(int i=0; i< timeCount * speed; i++) {
//                wordList.add(content.substring(i * length, (i + 1) * length));
//            }
            Random rd = new Random();
            Words wd = wList.get(rd.nextInt(wList.size()));
            String content = wd.getContent();

            do{
                content += content;
            }while (content.length() < timeCount * speed);

            int j = 0;
            for(int i=0; i<Integer.MAX_VALUE; i++) {
                if(i % (2 * speed) == 0){
                    wordList.add(content.substring(j * length, (j + 1) * length));
                    j++;
                }else{
                    wordList.add("");
                }
                if(wordList.size() > timeCount * speed){
                    break;
                }
            }
            Log.e("wrod", content);
        }else{
            // 中文，数字
            wList = wordsDao.queryBuilder()
                    .where(WordsDao.Properties.View_train_id.eq(id), WordsDao.Properties.Len.eq(length))
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
            // 短文不打乱
            Collections.shuffle(wordList);
        }

        Flowable.interval(1000/speed, TimeUnit.MILLISECONDS)
        //Flowable.interval(2, TimeUnit.SECONDS)
                .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
                .compose(this.<Long>composeLifeCycle())
                .take(timeCount * speed)
                //.take(timeCount / 2)
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
                        }else{
                            content.setVisibility(View.GONE);
                        }

                        //if(aLong == timeCount * speed - 1){
                        if(aLong == timeCount * speed -1){
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    readyGoThenKill(FollowMeEndActivity.class);
                                }
                            }, 1500);
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
        return R.layout.activity_word_game;
    }

    @Override
    protected void initInjector() {

    }

    @OnClick(R.id.home)
    public void onViewClicked() {
        finish();
    }
}
