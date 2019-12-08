package com.android.fitz.fastreading.ui.activity.train.game1;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

import com.android.fitz.fastreading.R;
import com.android.fitz.fastreading.base.AppApplication;
import com.android.fitz.fastreading.base.BaseActivity;
import com.android.fitz.fastreading.bean.TrainAnswerEntity;
import com.android.fitz.fastreading.constant.Constant;
import com.android.fitz.fastreading.constant.Preferences;
import com.android.fitz.fastreading.db.entity.AnimaPict;
import com.android.fitz.fastreading.db.greendao.AnimaPictDao;
import com.android.fitz.fastreading.db.greendao.DaoSession;
import com.android.fitz.fastreading.utils.CommonUtils;
import com.android.library.util.PreferencesUtils;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ExtendTrainAnswerPreviewActivity extends BaseActivity {

    @BindView(R.id.img)
    ImageView img;

    String ID;
    AnimaPictDao animaPict;

    @Override
    protected void initView() {
        final Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            ID = bundle.getString(Constant.ID);
        }


        DaoSession daoSession = ((AppApplication) getApplication()).getDaoSession();
        animaPict = daoSession.getAnimaPictDao();
        List<AnimaPict> list = animaPict.loadAll();

        int index = CommonUtils.getRandom(0, list.size() - 1);
        AnimaPict pict = list.get(index);

        TrainAnswerEntity answerEntity = new TrainAnswerEntity();
        // 正确答案id
        int iAnswer = CommonUtils.getRandom(0, 5);

        answerEntity.setIndex(index);
        answerEntity.setId(pict.getId());
        answerEntity.setiAnswer(iAnswer);
        answerEntity.setPict(pict.getUrl());

        List<Integer> ilist = new ArrayList<>();
        ilist.add(iAnswer);
        Log.e("iAnswer", iAnswer +"");
        Log.e("id", pict.getId() +"");
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            int pos = CommonUtils.getRandom(0, 7);
            if(!ilist.contains(pos)){
                ilist.add(pos);
                if(ilist.size() == 4){
                    break;
                }
            }
        }
        answerEntity.setiError(ilist);

        PreferencesUtils.putString(getApplicationContext(), Preferences.TRAIN_ANSWER, new Gson().toJson(answerEntity));

        Glide.with(this)
                .load(pict.getUrl())
                .into(img);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Bundle bundle1 = new Bundle();
                bundle1.putString(Constant.ID, ID);
                readyGoThenKill(ExtendTrainMoveGame3Activity.class, bundle);
            }
        }, 2000);
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_extend_train_answer_preview;
    }

    @Override
    protected void initInjector() {

    }


}
