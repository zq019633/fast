package com.android.fitz.fastreading.ui.activity.train.game2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.android.fitz.fastreading.R;
import com.android.fitz.fastreading.base.BaseActivity;
import com.android.fitz.fastreading.utils.Arith;

import butterknife.BindView;
import butterknife.OnClick;

public class LookForCompatriotEndActivity extends BaseActivity {
    @BindView(R.id.text_time)
    TextView mTimeText;
    @BindView(R.id.text_correct_num)
    TextView mCorrectNumText;
    @BindView(R.id.text_score)
    TextView mScoreText;

    private int time;
    private int correctNum;

    @Override
    protected void initView() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            time = bundle.getInt("time");
            correctNum = bundle.getInt("correctNum");
        }

        mTimeText.setText("总计用时: " + time + "秒");
        mCorrectNumText.setText("总计答对: " + correctNum + "个");
        double score = Arith.div(correctNum,time);
//        30个以上  优秀  30/40    >0.75个/秒
//        20-30个 良好     0.5 <=    <0.75
//        20个以下一般    <0.5
          if (score > 0.75) {
              mScoreText.setText("当前成绩: " + "优秀" + "!");
          }else if (score >= 0.5 && score <0.75){
              mScoreText.setText("当前成绩: " + "良好" + "!");
          }else {
              mScoreText.setText("当前成绩: " + "一般" + "!");
          }
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_look_for_compatriot_end;
    }

    @OnClick(R.id.text_back)
    public void back(){
        finish();
    }

    @Override
    protected void initInjector() {

    }
}
