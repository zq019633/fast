package com.android.fitz.fastreading.ui.activity.Test;

import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.fitz.fastreading.R;
import com.android.fitz.fastreading.base.AppApplication;
import com.android.fitz.fastreading.base.BaseActivity;
import com.android.fitz.fastreading.constant.Constant;
import com.android.fitz.fastreading.db.entity.Question;
import com.android.fitz.fastreading.db.greendao.DaoSession;
import com.android.fitz.fastreading.db.greendao.QuestionDao;
import com.android.fitz.fastreading.utils.Arith;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;
import rx.functions.Action1;

public class QuestionActivity extends BaseActivity {
    DaoSession daoSession;
    QuestionDao questionDao;
    String id = "";
    private List<Question> mQuestions;
    //正确答案
    private String mAnswer = "";
    //用户选择的答案
    private String select = "";
    private List<TextView> textViews = new ArrayList<>();
    private String words = "";//字数
    private String time = "";//阅读时间(秒)
    //记录当前是第几题
    private int index = 1;
    private int correctNum;//答对的数量
    private int totalNum;//总共的题目数量

    @BindView(R.id.linearlayout_question)
    LinearLayout mQuestionLinearLayout;
    @BindView(R.id.text_question)
    TextView mQuestionText;
    @BindView(R.id.text_answer1)
    TextView mAnswer1Text;
    @BindView(R.id.text_answer2)
    TextView mAnswer2Text;
    @BindView(R.id.text_answer3)
    TextView mAnswer3Text;
    @BindView(R.id.text_answer4)
    TextView mAnswer4Text;
    @BindView(R.id.text_result)
    TextView mResultText;

    @BindView(R.id.linearlayout_result)
    LinearLayout mResultLinearLayout;
    @BindView(R.id.text_words)
    TextView mWordsText;
    @BindView(R.id.text_time)
    TextView mTimeText;
    @BindView(R.id.text_score)
    TextView mScoreText;
    @BindView(R.id.text_speed)
    TextView mSpeedText;

    @Override
    protected void initView() {
        textViews.add(mAnswer1Text);
        textViews.add(mAnswer2Text);
        textViews.add(mAnswer3Text);
        textViews.add(mAnswer4Text);
        Bundle bundle = getIntent().getExtras();
        daoSession = ((AppApplication) getApplication()).getDaoSession();
        questionDao = daoSession.getQuestionDao();
        if (bundle != null) {
            id = bundle.getString(Constant.COMMON_KEY);
            words = bundle.getString("words");
            time = bundle.getString("time");
            getQuestions();
        }
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_question;
    }

    /**
     * 获取文章对应的所有题目
     */
    private void getQuestions() {
        QueryBuilder<Question> db = daoSession.getQuestionDao().queryBuilder();
        db.where(QuestionDao.Properties.Train_id.eq(id))
                .rx()
                .list()
                .subscribe(new Action1<List<Question>>() {
                    @Override
                    public void call(List<Question> questions) {
                        if (questions != null && questions.size() > 0) totalNum = questions.size();
                        mQuestions = questions;
                        updateQuestion();
                    }
                });
    }

    /**
     * 永远显示问题数组的第一个问题对象
     */
    private void updateQuestion() {
        if (mQuestions != null && mQuestions.size() > 0) {
            Question question = mQuestions.get(0);
            if (question != null) {
                mAnswer = question.getAnswer();
                mQuestionText.setText(index + "." + question.getTitle());
                mAnswer1Text.setText("A、 " + question.getOp1());
                mAnswer2Text.setText("B、 " + question.getOp2());
                mAnswer3Text.setText("C、 " + question.getOp3());
                mAnswer4Text.setText("D、 " + question.getOp4());
                index++;
            }
        }
    }

    /**
     * 用户点击选择答案
     */
    @OnClick({R.id.text_answer1, R.id.text_answer2, R.id.text_answer3, R.id.text_answer4})
    public void select(View view) {
        switch (view.getId()) {
            case R.id.text_answer1:
                select = "1";
                mAnswer1Text.setTextColor(ContextCompat.getColor(this, R.color.dp_blue));
                break;
            case R.id.text_answer2:
                select = "2";
                mAnswer2Text.setTextColor(ContextCompat.getColor(this, R.color.dp_blue));
                break;
            case R.id.text_answer3:
                select = "3";
                mAnswer3Text.setTextColor(ContextCompat.getColor(this, R.color.dp_blue));
                break;
            case R.id.text_answer4:
                select = "4";
                mAnswer4Text.setTextColor(ContextCompat.getColor(this, R.color.dp_blue));
                break;
        }
        mAnswer1Text.setEnabled(false);
        mAnswer2Text.setEnabled(false);
        mAnswer3Text.setEnabled(false);
        mAnswer4Text.setEnabled(false);
        if (mAnswer.equals(select)) {
            mResultText.setText("恭喜你答对了！");
            correctNum++;
        } else {
            mResultText.setText("很遗憾答错了！正确答案是:" + textViews.get(Integer.valueOf(mAnswer) - 1).getText().toString());
        }
        mQuestions.remove(0);
        next();
    }

    /**
     * 下一题
     */
    private void next() {
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(HANDLER_NEXT);
            }
        };
        Timer timer = new Timer();
        timer.schedule(timerTask, 1000);
    }

    private static final int HANDLER_NEXT = 100;

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == HANDLER_NEXT) {
                if (mQuestions.size() > 0) {//跳到下一题
                    updateQuestion();
                    mAnswer1Text.setEnabled(true);
                    mAnswer2Text.setEnabled(true);
                    mAnswer3Text.setEnabled(true);
                    mAnswer4Text.setEnabled(true);
                    mAnswer1Text.setTextColor(ContextCompat.getColor(QuestionActivity.this, R.color.white));
                    mAnswer2Text.setTextColor(ContextCompat.getColor(QuestionActivity.this, R.color.white));
                    mAnswer3Text.setTextColor(ContextCompat.getColor(QuestionActivity.this, R.color.white));
                    mAnswer4Text.setTextColor(ContextCompat.getColor(QuestionActivity.this, R.color.white));
                    mResultText.setText("");
                } else {//没有下一题，跳到答题结果页面
                    mQuestionLinearLayout.setVisibility(View.INVISIBLE);
                    mResultLinearLayout.setVisibility(View.VISIBLE);
                    mWordsText.setText("文章字数 : " + words + "字");
                    mTimeText.setText("阅读时间 : " + time + "秒");
                    mScoreText.setText("答题成绩 : " + ((int) (Arith.div(correctNum, totalNum) * 100)));
                    if (time.equals("") || time.equals("0")){
                        mSpeedText.setText("阅读速度 : 0字/分");
                    }else {
                        mSpeedText.setText("阅读速度 : " + (int)Arith.div(Integer.valueOf(words), Arith.div(Integer.valueOf(time), 60))
                                + "字/分");
                    }
                }
            }
        }
    };

    @OnClick(R.id.text_back)
    public void back(View view){
        finish();
    }

    @Override
    protected void initInjector() {

    }
}
