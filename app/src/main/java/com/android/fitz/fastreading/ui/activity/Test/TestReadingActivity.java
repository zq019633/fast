package com.android.fitz.fastreading.ui.activity.Test;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.fitz.fastreading.R;
import com.android.fitz.fastreading.base.AppApplication;
import com.android.fitz.fastreading.base.BaseActivity;
import com.android.fitz.fastreading.constant.Constant;
import com.android.fitz.fastreading.db.entity.Test;
import com.android.fitz.fastreading.db.greendao.DaoSession;
import com.android.fitz.fastreading.db.greendao.TestDao;
import com.android.fitz.fastreading.widget.page.Config;
import com.android.fitz.fastreading.widget.page.PageFactory;
import com.android.fitz.fastreading.widget.page.PageWidget;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class TestReadingActivity extends BaseActivity {

    TestDao testDao;
    String id = "";
    @BindView(R.id.text_time)
    TextView mTimeText;
    //    @BindView(R.id.left)
//    LinearLayout mLeft;
//    @BindView(R.id.right)
//    LinearLayout mRight;
    @BindView(R.id.text_finish)
    TextView mFinishText;
    @BindView(R.id.fr)
    FrameLayout fr;

    DaoSession daoSession;
    Test mTest;
    private volatile boolean isRunning = true;
    private int totalTime;
    //    经过点数一共可以放18行
    private int mCount = 18;
    private List<TextView> textViews = new ArrayList<>();
    //    文章内容索引，表示到第几个字了
    private int mIndex;
    private int mLast;
    //文章的内容
    private String contents;
    private static final int FRONT = 10;
    private static final int AFTER = 11;
    /**
     * 保存每一页的起始索引和结束索引
     */
    private List<Integer> mPageIndexes = new ArrayList<>();
    //当前页码，从第0页开始
    private int page;
    //阅读的总字数
    private long words;
    //开始状态
    private static final int SRART = 1;
    //结束状态
    private static final int END = 2;
    //默认无状态
    private int status;
    private PageWidget pageWidget;
    private PageFactory pageFactory;
    private Config config = Config.getInstance();

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_test_reading;
    }

    @Override
    protected void initView() {
        Bundle bundle = getIntent().getExtras();
        daoSession = ((AppApplication) getApplication()).getDaoSession();
        testDao = daoSession.getTestDao();
        if (bundle != null) {
            id = bundle.getString(Constant.COMMON_KEY);

            getData();
        }

    }

    private void getData() {
        testDao
                .rx()
                .load(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Test>() {
                    @Override
                    public void call(Test test) {
                        mTest = test;
                        contents = test.getContent().trim();

                        fr.removeAllViews();
                        pageFactory = PageFactory.getInstance();
                        pageWidget = new PageWidget(TestReadingActivity.this);
                        pageFactory.setPageWidget(pageWidget);
                        pageFactory.setContents(contents);
                        fr.addView(pageWidget);
                        initListener();
                    }
                });
    }

    private void initListener() {
        pageWidget.setTouchListener(new PageWidget.TouchListener() {
            @Override
            public void center() {

            }

            @Override
            public Boolean prePage() {
                if (status == SRART) {
                    pageFactory.prePage();
                    if (pageFactory.isfirstPage()) {
                        return false;
                    }

                    return true;
                }
                return false;
            }

            @Override
            public Boolean nextPage() {
                if (status == SRART) {
                    pageFactory.nextPage();
                    if (pageFactory.islastPage()) {
                        return false;
                    }
                    return true;
                }
                return false;
            }

            @Override
            public void cancel() {

            }
        });
    }

    private void initLines(ViewGroup parent) {
        for (int i = 0; i < mCount; i++) {
            initLine(parent);
        }
    }

    /**
     * 初始化每一行c
     */
    private void initLine(ViewGroup parent) {
        TextView textView = new TextView(this);
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, DensityUtil.dip2px(this,
//                (float) Arith.div(317, 18)));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 23);
        textView.setFilters(new InputFilter[]{new InputFilter.LengthFilter(24)});
        textView.setGravity(Gravity.CENTER_VERTICAL);
        textView.setMaxLines(1);
        textView.setTextSize(12);
        textView.setTextColor(ContextCompat.getColor(this, R.color.black));
        parent.addView(textView, params);
        textViews.add(textView);
    }

    /**
     * 渲染数据（文章内容）
     */
    private void setText() {
        for (int i = 0; i < mCount * 2; i++) {
            mLast = mIndex + 24;
            if (mLast > contents.length() - 1) {
                mLast = contents.length() - 1;
            }
            String content = contents.substring(mIndex, mLast);
            if (content.contains("\r")) {
                if (content.indexOf("\r") == 0) {
                    content = content.substring(2);
                } else {
                    mLast = content.indexOf("\r") + mIndex;
                    content = contents.substring(mIndex, mLast);
                    mLast = mLast + 2;
                }
            }
            if (!TextUtils.isEmpty(content)) {
                textViews.get(i).setText(content);
                mIndex = mLast;
            }
            if (mLast == contents.length() - 1) return;
        }
    }

    /**
     * 结束
     */
    @OnClick(R.id.text_finish)
    public void finish(View view) {
        if (status == 0) {
            status = SRART;
            mFinishText.setBackgroundResource(R.drawable.ic_action_end);
            startTime();
        } else if (status == SRART) {
            status = END;
            isRunning = false;
            Bundle bundle = new Bundle();
            bundle.putString(Constant.COMMON_KEY, id);
            bundle.putString("words", String.valueOf(contents.length()));
            bundle.putString("time", String.valueOf(totalTime));
            readyGo(QuestionActivity.class, bundle);
            finish();
        }
    }

    /**
     * 计时线程
     */
    private final Thread mThread = new Thread() {
        @Override
        public void run() {
            while (isRunning) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                totalTime++;
                mHandler.sendEmptyMessage(START_TIME);
            }
        }
    };

    /**
     * 计时
     */
    private void startTime() {
        mThread.start();
    }

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == START_TIME) {
                mTimeText.setText(timeFormat());
            }
        }
    };
    private static final int START_TIME = 1000;

    /**
     * 格式化时间
     *
     * @return
     */
    private String timeFormat() {
        if (totalTime / 60 < 1) {
            if (totalTime < 10) return "00:00:0" + totalTime;
            return "00:00:" + totalTime;
        } else {
            if (totalTime / 60 < 60) {
                int minute = totalTime / 60;
                int second = totalTime % 60;
                String minutes = "";
                String secondes = "";
                if (minute < 10) {
                    minutes = "0" + minute;
                } else {
                    minutes = minute + "";
                }
                if (second < 10) {
                    secondes = "0" + second;
                } else {
                    secondes = second + "";
                }
                return "00:" + minutes + ":" + secondes;
            } else {
                return totalTime / 3600 + ":" + totalTime % 3600 / 60 + ":" + totalTime % 3600 / 60;
            }
        }
    }

    /**
     * 退出
     */
    @OnClick(R.id.image_back)
    public void back() {
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isRunning) isRunning = false;
    }

    @Override
    protected void initInjector() {

    }
}
