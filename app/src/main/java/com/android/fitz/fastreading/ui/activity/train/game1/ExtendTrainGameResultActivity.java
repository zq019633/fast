package com.android.fitz.fastreading.ui.activity.train.game1;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.fitz.fastreading.R;
import com.android.fitz.fastreading.base.BaseActivity;
import com.android.fitz.fastreading.bean.TrainAnswerEntity;
import com.android.fitz.fastreading.constant.Constant;
import com.android.library.util.ConvertUtils;

import java.util.Collections;

import butterknife.BindView;
import butterknife.OnClick;

public class ExtendTrainGameResultActivity extends BaseActivity {
    TrainAnswerEntity answerEntity;

    @BindView(R.id.answer1)
    TextView answer1;
    @BindView(R.id.answer2)
    TextView answer2;
    @BindView(R.id.answer3)
    TextView answer3;
    @BindView(R.id.answer4)
    TextView answer4;
    @BindView(R.id.home)
    ImageButton home;

    @Override
    protected void initView() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            answerEntity = bundle.getParcelable(Constant.COMMON_KEY);
        }
        //answerEntity.getiError().add(answerEntity.getiAnswer());
        Collections.shuffle(answerEntity.getiError());

        int position = answerEntity.getiError().get(0);
        answer1.setText(position + "次");
        answer1.setTag(position);

        position = answerEntity.getiError().get(1);
        answer2.setText(position + "次");
        answer2.setTag(position);

        position = answerEntity.getiError().get(2);
        answer3.setText(position + "次");
        answer3.setTag(position);

        position = answerEntity.getiError().get(3);
        answer4.setText(position + "次");
        answer4.setTag(position);
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_extend_train_game_result;
    }

    @Override
    protected void initInjector() {

    }

    @OnClick({R.id.answer1, R.id.answer2, R.id.answer3, R.id.answer4, R.id.home})
    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.answer1:
//                break;
//            case R.id.answer2:
//                break;
//            case R.id.answer3:
//                break;
//            case R.id.answer4:
//                break;
//            case R.id.home:
//                finish();
//                break;
//        }
        if(view.getId() == R.id.home){
            finish();
            return;
        }
        answerResult(ConvertUtils.toInt(view.getTag()));
    }

    private void answerResult(int answer){
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.ID, answerEntity.getiAnswer());
        bundle.putInt(Constant.COMMON_KEY, answer);
        readyGoThenKill(ExtendTrainAnswerResultActivity.class, bundle);
    }
}
