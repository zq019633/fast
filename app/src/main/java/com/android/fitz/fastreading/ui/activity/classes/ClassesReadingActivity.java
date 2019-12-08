package com.android.fitz.fastreading.ui.activity.classes;

import android.animation.ValueAnimator;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.fitz.fastreading.R;
import com.android.fitz.fastreading.base.AppApplication;
import com.android.fitz.fastreading.base.BaseActivity;
import com.android.fitz.fastreading.constant.Constant;
import com.android.fitz.fastreading.constant.Preferences;
import com.android.fitz.fastreading.db.entity.Article;
import com.android.fitz.fastreading.db.greendao.ArticleDao;
import com.android.fitz.fastreading.db.greendao.DaoSession;
import com.android.fitz.fastreading.utils.Arith;
import com.android.fitz.fastreading.utils.CommonUtils;
import com.android.fitz.fastreading.utils.sputils.SPUtil;
import com.android.fitz.fastreading.widget.OtherSettingPopup;
import com.android.fitz.fastreading.widget.SettingPopup;
import com.android.library.util.ConvertUtils;
import com.android.library.util.DensityUtil;
import com.android.library.util.PreferencesUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class ClassesReadingActivity extends BaseActivity {
    ArticleDao articleDao;
    String id = "";
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.text_words)
    TextView mWordsText;
    @BindView(R.id.text_speed)
    TextView mSpeedText;
    @BindView(R.id.linearlayout_line)
    LinearLayout mLineLinearLayout;

    @BindView(R.id.image_start)
    ImageView mStartImage;
    @BindView(R.id.text_total_time)
    TextView mTotalTimeText;

    @BindView(R.id.relativelayout_canvas)
    RelativeLayout mCanvasRelativeLayout;
    @BindView(R.id.linearlayout_content)
    LinearLayout mContentLinearLayout;

    private List<TextView> textViews = new ArrayList<>();
    //    经过计算一共可以放13行
    private int mCount = 12;
    //    文章内容索引，表示到第几个字了
    private int mIndex;
    private int mLast;
    private boolean isStarting;
    private volatile int totalTime;
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == START_TIME) {
                mTotalTimeText.setText(timeFormat());
            }
        }
    };
    private static final int START_TIME = 100;
    private boolean isRunning = true;
    private int speed = 1000;
    //当前动画进行的值，0到1之间的浮点型
    private float value;
    private ValueAnimator valueAnimator;
    //文章的内容
    private String contents;
    private int bg = 1;
    private int font = 1;
    private int color = 1;
    private int cachedBg;
    private int cachedFont;
    private int cachedColor;
    private int cachedSpeed;

    private int[] mBackgrounds = new int[]{R.color.white,R.color.red,R.color.dp_blue,R.color.black};
    private int[] mTextColors = new int[]{R.color.white,R.color.red,R.color.dp_blue,R.color.black};

    //private String[] mFonts = new String[]{"fonts/arial.ttf","fonts/arial.ttf","fonts/arial.ttf", "fonts/simkai.ttf"};
    private String[] mFonts = new String[]{"fonts/arial.ttf","fonts/arial.ttf","fonts/arial.ttf", "fonts/xingkai.ttf"};
    private String type;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_classes_reading;
    }

    @Override
    protected void initView() {
        Bundle bundle = getIntent().getExtras();
        DaoSession daoSession = ((AppApplication) getApplication()).getDaoSession();
        articleDao = daoSession.getArticleDao();

        initLines();
        if (bundle != null) {
            id = bundle.getString(Constant.COMMON_KEY);
            type = PreferencesUtils.getString(getApplicationContext(), Preferences.SPEED_TYPE, "01");

            getData();
        }
        speed = ConvertUtils.toInt(CommonUtils.getSpeedList(type).get(0));
    }

    private void getData() {
        articleDao
                .rx()
                .load(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Article>() {
                    @Override
                    public void call(Article article) {
                        title.setText("题目:" + article.getTitle());
                        mWordsText.setText("字数:" + article.getWords() + "个");
                        //初始化从本地sp取保存的速度，如果没取到，就默认使用2000
//                        cachedSpeed = SPUtil.getInt(Constant.SP_SPEED_KEY,0);
//                        if (cachedSpeed != 0){
//                            speed = cachedSpeed;
//                        }
                        mSpeedText.setText("速度:" + speed + "字/分钟");
                        contents = article.getContent().trim();
                        setText();
                        //初始化从本地sp取保存的背景色，字体，字体颜色
                        cachedBg = SPUtil.getInt(Constant.SP_BG_KEY,0);
                        cachedFont = SPUtil.getInt(Constant.SP_FONT_KEY,0);
                        cachedColor = SPUtil.getInt(Constant.SP_COLOR_KEY,0);
                        if (cachedBg != 0){
                            bg = cachedBg;
                            mLineLinearLayout.setBackgroundColor(ContextCompat.getColor(ClassesReadingActivity.this,mBackgrounds[bg-1]));
                        }
                        if (cachedColor != 0){
                            color = cachedColor;
                            for (TextView textView : textViews){
                                textView.setTextColor(ContextCompat.getColor(ClassesReadingActivity.this,mTextColors[color-1]));
                            }
                        }
                        if (cachedFont != 0){
                            font = cachedFont;
                            Typeface typeface = Typeface.createFromAsset(getAssets(),mFonts[font-1]);
                            for (TextView textView : textViews){
                                textView.setTypeface(typeface);
                            }
                        }
                    }
                });
    }

    /**
     * 渲染数据（文章内容）
     */
    private void setText() {
        for (int i = 0; i < mCount; i++) {
            mLast = mIndex + 20;
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
        }
    }

    private void initLines() {
        for (int i = 0; i < mCount; i++) {
            initLine();
        }
    }

    /**
     * 初始化每一行
     */
    private void initLine() {
        TextView textView = new TextView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, DensityUtil.dip2px(this, 32));
        textView.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
        textView.setGravity(Gravity.CENTER_VERTICAL);
        textView.setTextSize(16);
        textView.setMaxLines(1);
        textView.setTextColor(ContextCompat.getColor(this, R.color.dp_blue));

        mLineLinearLayout.addView(textView, params);
        textViews.add(textView);

        View divider = new View(this);
        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(this, 2));
        divider.setBackgroundColor(ContextCompat.getColor(this, R.color.divider));
        mLineLinearLayout.addView(divider, params1);
    }

    /**
     * 开始或暂停阅读
     *
     * @param view
     */
    @OnClick(R.id.image_start)
    public void start(View view) {
        if (isStarting) {
            isStarting = false;
            mStartImage.setImageResource(R.drawable.ic_classes_reading);
            isRunning = false;
            stopCanvas();
        } else {
            isStarting = true;
            isRunning = true;
            mStartImage.setImageResource(R.drawable.ic_classes_stop);
            startTime();
            canvasMove();
        }
    }

    private void startTime() {
        new Thread() {
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
        }.start();
    }

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
     * 幕布开始滑动
     * 算法：一页14行，每行20个字，一页就是280字，假设速度为s（字/分）
     * 那么每秒的字数为s/60.一页的滑动总时间就是280/(s/60)=280*60/s
     */
    private void canvasMove() {
        valueAnimator = ValueAnimator.ofFloat(value, 1);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                value = (float) animation.getAnimatedValue();
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mCanvasRelativeLayout.getLayoutParams();
                params.height = (int) Arith.mul(value, DensityUtil.dip2px(ClassesReadingActivity.this,35 *mCount));
                mCanvasRelativeLayout.setLayoutParams(params);
                if (value == 1) {
                    if (contents.length() - 1 - mLast > 0) {
                        setText();
                        value = 0;
                        canvasMove();
                    } else {
                        isRunning = false;
                        stopCanvas();
                    }
                }
            }
        });
        long duration = (long) Arith.mul(Arith.div(16800000, speed), Arith.sub(1, value));
        valueAnimator.setDuration(duration).start();
    }

    /**
     * 暂时幕布滑动
     */
    private void stopCanvas() {
        if (valueAnimator != null) valueAnimator.cancel();
    }

    /**
     * 速度设置和其他设置
     *
     * @param view
     */
    @OnClick({R.id.image_speed_setting, R.id.image_other_setting})
    public void setting(View view) {
        //弹窗前先暂停当前阅读
        isStarting = false;
        mStartImage.setImageResource(R.drawable.ic_classes_reading);
        isRunning = false;
        stopCanvas();

        switch (view.getId()) {
            case R.id.image_speed_setting:
                final SettingPopup speedPopup = new SettingPopup();
                speedPopup.create(this, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
                        , R.drawable.shape_setting_popup_bg, R.layout.popup_speed_setting, String.valueOf(speed), type);
                speedPopup.showAtLocation(mContentLinearLayout, Gravity.CENTER, 0, 0);
                speedPopup.setOnConfirmListener(new SettingPopup.OnConfirmListener() {
                    @Override
                    public void onConfirm(int index) {
                        speed = ConvertUtils.toInt(CommonUtils.getSpeedList(type).get(index-1));
                        speedPopup.dismiss();
                        mSpeedText.setText("速度:" + speed + "字/分钟");
                        //把用户选择的速度存入sp
                        SPUtil.setValue(Constant.SP_SPEED_KEY,speed);
                    }
                });
                break;
            case R.id.image_other_setting:
                final OtherSettingPopup otherSettingPopup = new OtherSettingPopup();
                otherSettingPopup.create(this, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
                        , R.drawable.shape_setting_popup_bg, R.layout.popup_other_setting,bg,font,color);
                otherSettingPopup.showAtLocation(mContentLinearLayout, Gravity.CENTER, 0, 0);
                otherSettingPopup.setOnConfirmListener(new OtherSettingPopup.OnConfirmListener() {
                    @Override
                    public void onConfirm(int background, int font, int textColor) {
                        mLineLinearLayout.setBackgroundColor(ContextCompat.getColor(ClassesReadingActivity.this,mBackgrounds[background-1]));
                        Typeface typeface = Typeface.createFromAsset(getAssets(),mFonts[font-1]);
                        for (TextView textView : textViews){
                            textView.setTextColor(ContextCompat.getColor(ClassesReadingActivity.this,mTextColors[textColor-1]));
                            textView.setTypeface(typeface);
                        }

                        otherSettingPopup.dismiss();
                        bg = background;
                        ClassesReadingActivity.this.font = font;
                        ClassesReadingActivity.this.color = textColor;
                        SPUtil.setValue(Constant.SP_BG_KEY,background);
                        SPUtil.setValue(Constant.SP_FONT_KEY,font);
                        SPUtil.setValue(Constant.SP_COLOR_KEY,textColor);
                    }
                });
                break;
        }
    }

    /**
     * 返回目录
     */
    @OnClick(R.id.image_back)
    public void back() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (isRunning == true) isRunning = false;
        if (valueAnimator != null && valueAnimator.isRunning()) valueAnimator.cancel();
    }

    @Override
    protected void initInjector() {

    }
}
