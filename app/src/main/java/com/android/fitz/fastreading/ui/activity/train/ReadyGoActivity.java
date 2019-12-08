package com.android.fitz.fastreading.ui.activity.train;

import android.app.Application;
import android.os.Bundle;
import android.widget.TextView;

import com.android.fitz.fastreading.R;
import com.android.fitz.fastreading.base.AppApplication;
import com.android.fitz.fastreading.base.BaseActivity;
import com.android.fitz.fastreading.constant.Constant;
import com.android.fitz.fastreading.db.entity.MenuView;
import com.android.fitz.fastreading.db.greendao.DaoSession;
import com.android.fitz.fastreading.db.greendao.MenuViewDao;
import com.android.fitz.fastreading.ui.activity.train.game1.ExtendTrainAnswerPreviewActivity;
import com.android.fitz.fastreading.ui.activity.train.game2.FollowMeActivity;

import com.android.fitz.fastreading.ui.activity.train.game3.EnglishWordActivity;
import com.android.fitz.fastreading.ui.activity.train.game3.ExtendImageGameActivity;

import com.android.fitz.fastreading.ui.activity.train.game2.LookForCompatriotActivity;
import com.android.fitz.fastreading.ui.activity.train.game3.WordGameActivity;


import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Flowable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class ReadyGoActivity extends BaseActivity {

    MenuViewDao viewDao;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.desc)
    TextView desc;

    String type;
    String id;

    @Override
    protected void initView() {
        DaoSession daoSession = ((AppApplication) getApplication()).getDaoSession();
        viewDao = daoSession.getMenuViewDao();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            id = bundle.getString(Constant.ID);

            viewDao.rx()
                    .load(id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<MenuView>() {
                        @Override
                        public void call(MenuView menu) {
                            desc.setText(menu.getV_desc());
                            type = menu.getType();
                        }
                    });
        }

        initTime();
    }

    private void initTime(){
        Flowable.interval(1, TimeUnit.SECONDS)
                .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
                .compose(this.<Long>composeLifeCycle())
                .take(4)
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(Long i) {
                        Bundle bundle = new Bundle();
                        bundle.putString(Constant.ID, id);
                        time.setText(3 - i + "");
                        if(i == 3){
                            switch (type){
                                case "1":
                                    readyGoThenKill(ExtendTrainAnswerPreviewActivity.class, bundle);
                                    break;
                                case "2":
                                    // 跟住别掉队
                                    if(id.equals("5")){
                                        readyGoThenKill(FollowMeActivity.class, bundle);
                                    }else if (id.equals("6")){
                                        readyGoThenKill(LookForCompatriotActivity.class,bundle);
                                    }
                                    break;
                                case "3":
                                    switch (id){
                                        case "11":
                                            readyGoThenKill(ExtendImageGameActivity.class);
                                            break;
                                        case "7":
                                        case "8":
                                        case "10":
                                            readyGoThenKill(WordGameActivity.class, bundle);
                                            break;
                                        case "9":
                                            readyGoThenKill(EnglishWordActivity.class,bundle);
                                            break;
                                        }
                                    break;
                            }
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
        return R.layout.activity_ready_go;
    }

    @Override
    protected void initInjector() {

    }
}
