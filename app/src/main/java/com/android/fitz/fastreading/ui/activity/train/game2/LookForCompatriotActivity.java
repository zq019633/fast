package com.android.fitz.fastreading.ui.activity.train.game2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.fitz.fastreading.R;
import com.android.fitz.fastreading.base.AppApplication;
import com.android.fitz.fastreading.base.BaseActivity;
import com.android.fitz.fastreading.constant.Preferences;
import com.android.fitz.fastreading.db.entity.AnimaPict;
import com.android.fitz.fastreading.db.greendao.AnimaPictDao;
import com.android.fitz.fastreading.db.greendao.DaoSession;
import com.android.fitz.fastreading.utils.CommonUtils;
import com.android.fitz.fastreading.utils.TimeCount;
import com.android.fitz.fastreading.widget.TurnTableView;
import com.android.library.util.PreferencesUtils;
import com.android.library.util.ToastUtils;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 视幅扩展——找同胞
 */
public class LookForCompatriotActivity extends BaseActivity {
    @BindView(R.id.image_time)
    ImageView mTimeImage;
    @BindView(R.id.image_skip)
    ImageView mSkipImage;
    @BindView(R.id.image_end)
    ImageView mEndImage;
    @BindView(R.id.turn_table_view)
    TurnTableView mTurnTableView;
    @BindView(R.id.text_time)
    TextView mTimeText;

    AnimaPictDao animaPictDao;
    // 总时长
    int timeCount;
    private TimeCount mTimerCount;
    private int correctNum;//答对的题目数
    private List<AnimaPict> list;
    private List<AnimaPict> mList;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_look_for_compatriot;
    }

    @Override
    protected void initView() {
        DaoSession daoSession = ((AppApplication) getApplication()).getDaoSession();
        animaPictDao = daoSession.getAnimaPictDao();

        timeCount = PreferencesUtils.getInt(getApplicationContext(), Preferences.SETTING_RIGHT);

        initData();
    }

    private void initData() {
        list = animaPictDao.loadAll();
        // 图片出现的数量
        final int count = 8;
        mList = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            int position = CommonUtils.getRandom(0, list.size() - 1);
            if(Collections.frequency(mList, list.get(position)) < 1) mList.add(list.get(position));
            if (mList.size() == count) break;
        }
        Collections.shuffle(mList);

        try {
            mTurnTableView.setBitmaps(getBitmaps(mList));
        } catch (Exception e) {
            e.printStackTrace();
        }

        mTurnTableView.setOnRefreshUIListener(new TurnTableView.OnRefreshUIListener() {
            @Override
            public void onRefreshUI() {
                correctNum++;
                mList.clear();
                for (int i = 0; i < list.size(); i++) {
                    int position = CommonUtils.getRandom(0, list.size() - 1);
                    if(Collections.frequency(mList, list.get(position)) < 1) mList.add(list.get(position));
                    if (mList.size() == count) break;
                }
                Collections.shuffle(mList);

                try {
                    mTurnTableView.setBitmaps(getBitmaps(mList));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        mTimerCount = new TimeCount(timeCount*1000,1000,mTimeText);
        mTimerCount.start();
        mTimerCount.setOnFinishiListener(new TimeCount.OnFinishListener() {
            @Override
            public void onFinish() {
                Bundle bundle = new Bundle();
                bundle.putInt("time",timeCount);
                bundle.putInt("correctNum",correctNum);
                readyGoThenKill(LookForCompatriotEndActivity.class,bundle);
            }
        });
    }

    private List<Bitmap> getBitmaps(List<AnimaPict> list){
        List<Bitmap> bitmaps = new ArrayList<>();
        for (AnimaPict animaPict : list){
            String url = animaPict.getUrl();
            Bitmap bitmap = BitmapFactory.decodeFile(url);
            bitmaps.add(bitmap);
        }
        return bitmaps;
    }

    /**
     * 结束
     */
    @OnClick(R.id.image_end)
    public void end(){
        finish();
    }

    /**
     * 快速跳过
     */
    @OnClick(R.id.image_skip)
    public void skip(){
        mList.clear();
        for (int i = 0; i < list.size(); i++) {
            int position = CommonUtils.getRandom(0, list.size() - 1);
            if(Collections.frequency(mList, list.get(position)) < 1) mList.add(list.get(position));
            if (mList.size() == 8) break;
        }
        Collections.shuffle(mList);

        try {
            mTurnTableView.setBitmaps(getBitmaps(mList));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTimerCount != null) mTimerCount.cancel();
    }

    @Override
    protected void initInjector() {

    }
}
