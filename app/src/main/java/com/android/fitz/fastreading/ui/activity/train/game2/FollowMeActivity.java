package com.android.fitz.fastreading.ui.activity.train.game2;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.fitz.fastreading.R;
import com.android.fitz.fastreading.base.AppApplication;
import com.android.fitz.fastreading.base.BaseActivity;
import com.android.fitz.fastreading.bean.MarginEntity;
import com.android.fitz.fastreading.bean.TrainAnswerEntity;
import com.android.fitz.fastreading.constant.Constant;
import com.android.fitz.fastreading.constant.Preferences;
import com.android.fitz.fastreading.db.entity.AnimaPict;
import com.android.fitz.fastreading.db.entity.MenuView;
import com.android.fitz.fastreading.db.greendao.AnimaPictDao;
import com.android.fitz.fastreading.db.greendao.DaoSession;
import com.android.fitz.fastreading.db.greendao.MenuViewDao;
import com.android.fitz.fastreading.ui.activity.train.game1.ExtendTrainGameResultActivity;
import com.android.fitz.fastreading.ui.activity.train.game1.ExtendTrainMoveGame3Activity;
import com.android.fitz.fastreading.utils.CommonUtils;
import com.android.library.util.ConvertUtils;
import com.android.library.util.PreferencesUtils;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Flowable;

public class FollowMeActivity extends BaseActivity {

    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.container)
    RelativeLayout container;

    int conWidth;
    int conHeight;
    int width;
    int height;
    // 游戏Id
    String id;
    // 停留时间
    int speed;
    // 总时长
    int timeCount;
    // 选中图片出现的次数
    //int imgCount;
    int m = 5;
    int n = 6;

    SparseArray<MarginEntity> spArr;
    AnimaPictDao animaPictDao;
    MenuViewDao menuViewDao;

    //TrainAnswerEntity answerEntity;
    @Override
    protected void initView() {
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            id = bundle.getString(Constant.ID);
        }
        //answerEntity = new Gson().fromJson(PreferencesUtils.getString(getApplicationContext(), Preferences.TRAIN_ANSWER), TrainAnswerEntity.class);
        speed = PreferencesUtils.getInt(getApplicationContext(), Preferences.SETTING_LEFT);
        timeCount = PreferencesUtils.getInt(getApplicationContext(), Preferences.SETTING_RIGHT);
        // 图片出现次数1-5次
        //imgCount = answerEntity.getiAnswer();

        DaoSession daoSession = ((AppApplication) getApplication()).getDaoSession();
        animaPictDao = daoSession.getAnimaPictDao();
        menuViewDao = daoSession.getMenuViewDao();

        measureView();
        initData();
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_follow_me;
    }

    @Override
    protected void initInjector() {

    }

    // 测量图片和窗口的高宽
    private void measureView() {
        ViewTreeObserver imgvto = img.getViewTreeObserver();
        imgvto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                img.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                conWidth = container.getWidth();
                conHeight = container.getHeight();

                Log.e("conHeight", conHeight + "");

                height = conHeight / m;
                width = height * img.getWidth() / img.getHeight();

                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)img.getLayoutParams();
                params.width = width;
                params.height = height;

                spArr = new SparseArray<MarginEntity>();

                int iWidth = conWidth / n;
                int iHeight = conHeight / m;

                // 5*6
                int p = 0;
                for (int i = 1; i <= m; i++) {
                    for (int j = 1; j <= n; j++) {
                        MarginEntity item = new MarginEntity();
                        item.setLeft(iWidth * (j - 1));
                        item.setTop(iHeight * (i - 1));

                        spArr.put(p, item);
                        p++;
                    }
                }
            }

        });
    }

    private void initData(){
        final MenuView menuView = menuViewDao.load(id);
        final String[] arrRoute = menuView.getRoute().split(",");
        final List<AnimaPict> list = animaPictDao.loadAll();
        // 图片出现的数量
        int count = timeCount * speed;
        // 答案 index
        //AnimaPict pict = animaPictDao.load(answerEntity.getId());

        final List<AnimaPict> mList = new ArrayList<>();
        for(int i = 0; i<count; i++) {
            int position = CommonUtils.getRandom(0, list.size() - 1);
            mList.add(list.get(position));
        }


        Collections.shuffle(mList);

        Flowable.interval(1000/speed, TimeUnit.MILLISECONDS)
                //.subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
                .compose(this.<Long>composeLifeCycle())
                .take(mList.size())
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(Long aLong) {
                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)img.getLayoutParams();
                        int position = (int)(aLong % arrRoute.length);

                        int index = ConvertUtils.toInt(arrRoute[position]);
                        MarginEntity item = spArr.get(index - 1);

                        int top = item.getTop();
                        int left = item.getLeft() + (conWidth/n - width)/2;

                        //Log.e("top", top + "");

                        params.topMargin = top > 0 ? top : 0;
                        params.leftMargin = left > 0 ? left : 0;
                        img.setLayoutParams(params);

                        Glide.with(FollowMeActivity.this)
                                .load(mList.get(ConvertUtils.toInt(aLong)).getUrl())
                                .into(img);
                        if(aLong == mList.size() - 1){
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

    @OnClick(R.id.home)
    public void onViewClicked() {
        finish();
    }
}
