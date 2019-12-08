package com.android.fitz.fastreading.ui.activity.train.game3;

import android.media.Image;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
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
import com.android.fitz.fastreading.ui.activity.train.game2.FollowMeEndActivity;
import com.android.fitz.fastreading.utils.CommonUtils;
import com.android.library.util.ConvertUtils;
import com.android.library.util.PreferencesUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
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

public class ExtendImageGameActivity extends BaseActivity {

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
    // 长度
    int imgLength;

    int m = 5;
    int n = 6;
    final int iCnt = 1;

    SparseArray<MarginEntity> spArr;
    AnimaPictDao animaPictDao;
    MenuViewDao menuViewDao;
    List<ImageView> imgViewList;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_extend_image_game;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected void initView() {
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            id = bundle.getString(Constant.ID);
        }
        speed = PreferencesUtils.getInt(getApplicationContext(), Preferences.SETTING_LEFT);
        timeCount = PreferencesUtils.getInt(getApplicationContext(), Preferences.SETTING_RIGHT);
        imgLength = PreferencesUtils.getInt(getApplicationContext(), Preferences.SETTING_LENGTH) + 3;

        DaoSession daoSession = ((AppApplication) getApplication()).getDaoSession();
        animaPictDao = daoSession.getAnimaPictDao();
        menuViewDao = daoSession.getMenuViewDao();

        imgViewList = new ArrayList<>();
        for(int i=0; i<imgLength; i++){
            ImageView iv = new ImageView(ExtendImageGameActivity.this);
            container.addView(iv);

            imgViewList.add(iv);
        }

        measureView();
        initData();
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

                height = conHeight / m;
                width = height * img.getWidth() / img.getHeight();

                for(ImageView iv : imgViewList) {
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) iv.getLayoutParams();
                    params.width = width;
                    params.height = height;
                }

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
        //final String[] arrRoute = menuView.getRoute().split(",");
        // 0永远取不到.
        final int[] arrRoute = {0, 6, 7, 8, 9, 10, 12, 13, 14, 15, 16, 18, 19, 20, 21, 22};
        final List<AnimaPict> list = animaPictDao.loadAll();
        // 图片出现的数量
        int count = timeCount * speed * iCnt;

        // 所有图片组数
        final List<List<AnimaPict>> mList = new ArrayList<>();
        for(int i = 0; i<count; i++) {
            List<AnimaPict> iList = new ArrayList<>();
            for(int j=0 ;j< imgLength; j++) {
                int index = CommonUtils.getRandom(0, list.size() - 1);
                iList.add(list.get(index));
            }
            mList.add(iList);
        }

        Collections.shuffle(mList);

        Flowable.interval(1000/(speed * iCnt), TimeUnit.MILLISECONDS)
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
                        // 随机取3*5的 imgLength 的位置
                        List<Integer> positions = new ArrayList<Integer>();
                        for (int i = 0; i < Integer.MAX_VALUE; i++) {
                            int m = CommonUtils.getRandom(0, 14);
                            if (!positions.contains(arrRoute[m])) {
                                positions.add(arrRoute[m]);
                            }
                            if (positions.size() == imgLength) {
                                break;
                            }
                        }

                        int index = 0;
                        for (ImageView iv : imgViewList) {
                            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) iv.getLayoutParams();

                            MarginEntity item = spArr.get(positions.get(index));

                            // 图片比
                            int top = item.getTop();
                            int left = item.getLeft() + (conWidth / n - width) / 2;

                            params.topMargin = top > 0 ? top : 0;
                            params.leftMargin = left > 0 ? left : 0;
                            iv.setLayoutParams(params);

                            final int position = index;
                            Glide.with(ExtendImageGameActivity.this)
                                    .load(mList.get(ConvertUtils.toInt(aLong)).get(index).getUrl())
                                    .into(iv);

                            index++;
                        }
                        if(ConvertUtils.toInt(aLong) % (2 * speed * iCnt) == 0 && aLong >0) {
                            //Log.e("a", aLong + "");
                            container.setVisibility(View.VISIBLE);
                        }else{
                            container.setVisibility(View.GONE);
                        }
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
