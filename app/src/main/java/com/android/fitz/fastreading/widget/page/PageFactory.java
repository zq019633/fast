package com.android.fitz.fastreading.widget.page;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.fitz.fastreading.R;
import com.android.fitz.fastreading.base.AppApplication;
import com.android.fitz.fastreading.widget.page.PageWidget;
import com.android.fitz.fastreading.widget.page.db.BookCatalogue;
import com.android.fitz.fastreading.widget.page.db.BookList;
import com.android.fitz.fastreading.widget.page.util.BitmapUtil;
import com.android.fitz.fastreading.widget.page.util.BookUtil;
import com.android.fitz.fastreading.widget.page.util.CommonUtil;
import com.android.fitz.fastreading.widget.page.util.FileUtils;
import com.android.fitz.fastreading.widget.page.util.TRPage;
import com.android.library.util.ToastUtils;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/20 0020.
 */
public class PageFactory {
    private static final String TAG = "PageFactory";
    private static PageFactory pageFactory;

    private Context mContext;
    private Config config;
    //当前的书本
//    private File book_file = null;
    // 默认背景颜色
    private int m_backColor = 0xffff9e85;
    //页面宽
    private int mWidth = 1200;
    //页面高
    private int mHeight = 600;
    //文字字体大小
    private float m_fontSize;
    //时间格式
    private SimpleDateFormat sdf;
    //时间
    private String date;
    //进度格式
    private DecimalFormat df;
    //电池边界宽度
    private float mBorderWidth;
    // 上下与边缘的距离
    private float marginHeight;
    // 左右与边缘的距离
    private float measureMarginWidth;
    // 左右与边缘的距离
    private float marginWidth;
    //状态栏距离底部高度
    private float statusMarginBottom;
    //行间距
    private float lineSpace;
    //段间距
    private float paragraphSpace;
    //字高度
    private float fontHeight;
    //字体
    private Typeface typeface;
    //文字画笔
    private Paint mPaint;
    //加载画笔
    private Paint waitPaint;
    //文字颜色
    private int m_textColor = Color.rgb(50, 65, 78);
    // 绘制内容的宽
    private float mVisibleHeight;
    // 绘制内容的宽
    private float mVisibleWidth;
    // 每页可以显示的行数
    private int mLineCount = 18;
    //电池画笔
    private Paint mBatterryPaint;
    //电池字体大小
    private float mBatterryFontSize;
    //背景图片
    private Bitmap m_book_bg = null;
    //当前显示的文字
//    private StringBuilder word = new StringBuilder();
    //当前总共的行
//    private Vector<String> m_lines = new Vector<>();
//    // 当前页起始位置
//    private long m_mbBufBegin = 0;
//    // 当前页终点位置
//    private long m_mbBufEnd = 0;
//    // 之前页起始位置
//    private long m_preBegin = 0;
//    // 之前页终点位置
//    private long m_preEnd = 0;
    // 图书总长度
//    private long m_mbBufLen = 0;
    private Intent batteryInfoIntent;
    //电池电量百分比
    private float mBatteryPercentage;
    //电池外边框
    private RectF rect1 = new RectF();
    //电池内边框
    private RectF rect2 = new RectF();
    //文件编码
//    private String m_strCharsetName = "GBK";
    //当前是否为第一页
    private boolean m_isfirstPage;
    //当前是否为最后一页
    private boolean m_islastPage;
    //书本widget
    private PageWidget mBookPageWidget;
    //    //书本所有段
//    List<String> allParagraph;
//    //书本所有行
//    List<String> allLines = new ArrayList<>();
    //现在的进度
    private float currentProgress;
    //目录
//    private List<BookCatalogue> directoryList = new ArrayList<>();
    //书本路径
    private String bookPath = "";
    //书本名字
    private String bookName = "";
    private BookList bookList;
    //书本章节
    private int currentCharter = 0;
    //当前电量
    private int level = 0;

    private PageEvent mPageEvent;

    private String contents;
    private List<List<String>> mContents = new ArrayList<>();
    private int mIndex;
    private int mLast;
    private int pageIndex;
    private int currentOrientataion = 2;//上一次翻页的方向，1是往前，2是往后

    private static Status mStatus = Status.OPENING;

    public enum Status {
        OPENING,
        FINISH,
        FAIL,
    }

    public static synchronized PageFactory getInstance() {
        return pageFactory;
    }

    public static synchronized PageFactory createPageFactory(Context context) {
        if (pageFactory == null) {
            pageFactory = new PageFactory(context);
        }
        return pageFactory;
    }

    private PageFactory(Context context) {
        mContext = context.getApplicationContext();
        config = Config.getInstance();
        //获取屏幕宽高
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metric = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metric);
        //mWidth = metric.widthPixels;
        //mHeight = metric.heightPixels;
        //mWidth = 1200;
        //mHeight = 630;

        sdf = new SimpleDateFormat("HH:mm");//HH:mm为24小时制,hh:mm为12小时制
        date = sdf.format(new java.util.Date());
        df = new DecimalFormat("#0.0");

        marginWidth = mContext.getResources().getDimension(R.dimen.readingMarginWidth);
        marginHeight = 90;
        statusMarginBottom = mContext.getResources().getDimension(R.dimen.reading_status_margin_bottom);
        lineSpace = context.getResources().getDimension(R.dimen.reading_line_spacing);
        paragraphSpace = context.getResources().getDimension(R.dimen.reading_paragraph_spacing);
        mVisibleWidth = mWidth - marginWidth * 2;
        mVisibleHeight = mHeight - marginHeight * 2;

        typeface = config.getTypeface();
        m_fontSize = 16;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);// 画笔
        mPaint.setTextAlign(Paint.Align.LEFT);// 左对齐
        mPaint.setTextSize(m_fontSize);// 字体大小
        mPaint.setColor(m_textColor);// 字体颜色
        mPaint.setTypeface(typeface);
        mPaint.setSubpixelText(true);// 设置该项为true，将有助于文本在LCD屏幕上的显示效果

        waitPaint = new Paint(Paint.ANTI_ALIAS_FLAG);// 画笔
        waitPaint.setTextAlign(Paint.Align.LEFT);// 左对齐
        waitPaint.setTextSize(mContext.getResources().getDimension(R.dimen.reading_max_text_size));// 字体大小
        waitPaint.setColor(m_textColor);// 字体颜色
        waitPaint.setTypeface(typeface);
        waitPaint.setSubpixelText(true);// 设置该项为true，将有助于文本在LCD屏幕上的显示效果

        mBorderWidth = mContext.getResources().getDimension(R.dimen.reading_board_battery_border_width);
        mBatterryPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBatterryFontSize = CommonUtil.sp2px(context, 12);
        mBatterryPaint.setTextSize(mBatterryFontSize);
        mBatterryPaint.setTypeface(typeface);
        mBatterryPaint.setTextAlign(Paint.Align.LEFT);
        mBatterryPaint.setColor(m_textColor);
        batteryInfoIntent = context.getApplicationContext().registerReceiver(null,
                new IntentFilter(Intent.ACTION_BATTERY_CHANGED));//注册广播,随时获取到电池电量信息

        initBg(config.getDayOrNight());
        measureMarginWidth();
    }

    public void setContents(String contents) {
        mLast = 0;
        mIndex = 0;
        mContents.clear();
        this.contents = contents;
        while (true) {
            List<String> lines = new ArrayList<>();
            for (int i = 0; i < mLineCount; i++) {
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
                    lines.add(content);
                    mIndex = mLast;
                }
            }
            mContents.add(lines);
            if (mLast == contents.length() - 1) break;
        }

        currentPage();
    }

    private void measureMarginWidth() {
        float wordWidth = mPaint.measureText("\u3000");
        float width = mVisibleWidth % wordWidth;
        measureMarginWidth = marginWidth + width / 2;
    }

    //初始化背景
    private void initBg(Boolean isNight) {
        if (isNight) {
            //设置背景
//            setBgBitmap(BitmapUtil.decodeSampledBitmapFromResource(
//                    mContext.getResources(), R.drawable.main_bg, mWidth, mHeight));
            Bitmap bitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(bitmap);
            //canvas.drawColor(Color.BLACK);
            canvas.drawColor(Color.WHITE);
            setBgBitmap(bitmap);
            //设置字体颜色
            setM_textColor(Color.rgb(128, 128, 128));
            setBookPageBg(Color.WHITE);
        } else {
            //设置背景
            setBookBg(config.getBookBgType());
        }
    }

    private void drawStatus(Bitmap bitmap) {
        String status = "";
        switch (mStatus) {
            case OPENING:
                status = "正在打开书本...";
                break;
            case FAIL:
                status = "打开书本失败！";
                break;
        }

        Canvas c = new Canvas(bitmap);
        c.drawBitmap(getBgBitmap(), 0, 0, null);
        waitPaint.setColor(getTextColor());
        waitPaint.setTextAlign(Paint.Align.CENTER);

        Rect targetRect = new Rect(0, 0, mWidth, mHeight);
//        c.drawRect(targetRect, waitPaint);
        Paint.FontMetricsInt fontMetrics = waitPaint.getFontMetricsInt();
        // 转载请注明出处：http://blog.csdn.net/hursing
        int baseline = (targetRect.bottom + targetRect.top - fontMetrics.bottom - fontMetrics.top) / 2;
        // 下面这行是实现水平居中，drawText对应改为传入targetRect.centerX()
        waitPaint.setTextAlign(Paint.Align.CENTER);
        c.drawText(status, targetRect.centerX(), baseline, waitPaint);
//        c.drawText("正在打开书本...", mHeight / 2, 0, waitPaint);
        mBookPageWidget.postInvalidate();
    }

    public void onDraw(Bitmap bitmap, Boolean isNext) {
        Canvas c = new Canvas(bitmap);
        c.drawBitmap(getBgBitmap(), 0, 0, null);
        //c.drawColor(Color.WHITE);

//        word.setLength(0);
        mPaint.setTextSize(m_fontSize);
        mPaint.setColor(ContextCompat.getColor(AppApplication.getInstance(),R.color.black));
        mBatterryPaint.setColor(getTextColor());

        marginHeight = 80;
//        if (!isNext) pageIndex -= 2;
        if (mContents != null && mContents.size() >= pageIndex + 1) {
            float y = marginHeight;
            for (String strLine : mContents.get(pageIndex)) {
                y += m_fontSize + 8;
                c.drawText(strLine, 100, y, mPaint);
//                word.append(strLine);
                Log.e("1", strLine);
            }
            pageIndex++;
        }

        if (mContents != null && mContents.size() >= pageIndex + 1) {
            float y = marginHeight;
            for (String strLine : mContents.get(pageIndex)) {
                y += m_fontSize + 8;
                c.drawText(strLine, 670, y, mPaint);
                Log.e("2", strLine);
//                word.append(strLine);
            }
            pageIndex++;
        }

        //画进度及时间
        int dateWith = (int) (mBatterryPaint.measureText(date) + mBorderWidth);//时间宽度
        c.drawText(date, marginWidth, mHeight - statusMarginBottom, mBatterryPaint);
        // 画电池
        level = batteryInfoIntent.getIntExtra("level", 0);
        int scale = batteryInfoIntent.getIntExtra("scale", 100);
        mBatteryPercentage = (float) level / scale;
        float rect1Left = marginWidth + dateWith + statusMarginBottom;//电池外框left位置
        //画电池外框
        float width = CommonUtil.convertDpToPixel(mContext, 20) - mBorderWidth;
        float height = CommonUtil.convertDpToPixel(mContext, 10);
        rect1.set(rect1Left, mHeight - height - statusMarginBottom, rect1Left + width, mHeight - statusMarginBottom);
        rect2.set(rect1Left + mBorderWidth, mHeight - height + mBorderWidth - statusMarginBottom, rect1Left + width - mBorderWidth, mHeight - mBorderWidth - statusMarginBottom);
        c.save(Canvas.CLIP_SAVE_FLAG);
        c.clipRect(rect2, Region.Op.DIFFERENCE);
        c.drawRect(rect1, mBatterryPaint);
        c.restore();

        mBookPageWidget.postInvalidate();
    }

    //向前翻页
    public void prePage() {
        if (pageIndex == 2 || pageIndex == 0) {
            Log.e(TAG, "当前是第一页");
            if (!m_isfirstPage) {
                Toast.makeText(mContext, "当前是第一页", Toast.LENGTH_SHORT).show();
            }
            m_isfirstPage = true;
            return;
        } else {
            m_isfirstPage = false;
        }

        currentOrientataion = 1;

        pageIndex -= 4;
//        ToastUtils.ShortToast(mContext,pageIndex+"");
        onDraw(mBookPageWidget.getNextPage(), false);
//        onDraw(mBookPageWidget.getCurPage(), false);
    }

    //向后翻页
    public void nextPage() {
        if (mContents != null && mContents.size() - 1 <= pageIndex) {
            Log.e(TAG, "已经是最后一页了");
            if (!m_islastPage) {
                Toast.makeText(mContext, "已经是最后一页了", Toast.LENGTH_SHORT).show();
            }
            m_islastPage = true;
            return;
        } else {
            m_islastPage = false;
        }

        currentOrientataion = 2;
        pageIndex -= 2;
        onDraw(mBookPageWidget.getCurPage(), true);
        onDraw(mBookPageWidget.getNextPage(), true);
        Log.e("nextPage", "nextPagenext");
    }

    /**
     * 打开书本
     *
     * @throws IOException
     */
    public void openBook(BookList bookList) throws IOException {
        //清空数据
        currentCharter = 0;
//        m_mbBufLen = 0;
        initBg(config.getDayOrNight());

        this.bookList = bookList;
        bookPath = bookList.getBookpath();
        bookName = FileUtils.getFileName(bookPath);

        mStatus = Status.OPENING;
        drawStatus(mBookPageWidget.getCurPage());
        drawStatus(mBookPageWidget.getNextPage());

    }

    //绘制当前页面
    public void currentPage() {
        pageIndex = 0;
//        onDraw(mBookPageWidget.getCurPage(), true);
        onDraw(mBookPageWidget.getNextPage(), true);
    }

    //设置页面的背景
    public void setBookBg(int type) {
        Bitmap bitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        int color = 0;
        switch (type) {
            case Config.BOOK_BG_DEFAULT:
                canvas = null;
                bitmap.recycle();
                if (getBgBitmap() != null) {
                    getBgBitmap().recycle();
                }
//                bitmap = BitmapUtil.decodeSampledBitmapFromResource(
//                        mContext.getResources(), R.drawable.ic_book_bg, mWidth, mHeight);
                bitmap = BitmapUtil.drawableToBitmap(mWidth, mHeight, mContext.getResources().getDrawable(R.drawable.ic_book_bg));
                color = mContext.getResources().getColor(R.color.read_font_default);
                setBookPageBg(mContext.getResources().getColor(R.color.read_bg_default));
                break;
            case Config.BOOK_BG_1:
                canvas.drawColor(mContext.getResources().getColor(R.color.read_bg_1));
                color = mContext.getResources().getColor(R.color.read_font_1);
                setBookPageBg(mContext.getResources().getColor(R.color.read_bg_1));
                break;
            case Config.BOOK_BG_2:
                canvas.drawColor(mContext.getResources().getColor(R.color.read_bg_2));
                color = mContext.getResources().getColor(R.color.read_font_2);
                setBookPageBg(mContext.getResources().getColor(R.color.read_bg_2));
                break;
            case Config.BOOK_BG_3:
                canvas.drawColor(mContext.getResources().getColor(R.color.read_bg_3));
                color = mContext.getResources().getColor(R.color.read_font_3);
                if (mBookPageWidget != null) {
                    mBookPageWidget.setBgColor(mContext.getResources().getColor(R.color.read_bg_3));
                }
                break;
            case Config.BOOK_BG_4:
                canvas.drawColor(mContext.getResources().getColor(R.color.read_bg_4));
                color = mContext.getResources().getColor(R.color.read_font_4);
                setBookPageBg(mContext.getResources().getColor(R.color.read_bg_4));
                break;
        }

        setBgBitmap(bitmap);
        //设置字体颜色
        setM_textColor(color);
    }

    public void setBookPageBg(int color) {
        if (mBookPageWidget != null) {
            mBookPageWidget.setBgColor(color);
        }
    }

    public static Status getStatus() {
        return mStatus;
    }


    public String getBookPath() {
        return bookPath;
    }

    //是否是第一页
    public boolean isfirstPage() {
        return m_isfirstPage;
    }

    //是否是最后一页
    public boolean islastPage() {
        return m_islastPage;
    }

    //设置页面背景
    public void setBgBitmap(Bitmap BG) {
        m_book_bg = BG;
    }

    //设置页面背景
    public Bitmap getBgBitmap() {
        return m_book_bg;
    }

    //设置文字颜色
    public void setM_textColor(int m_textColor) {
        this.m_textColor = m_textColor;
    }

    //获取文字颜色
    public int getTextColor() {
        return this.m_textColor;
    }

    //获取文字大小
    public float getFontSize() {
        return this.m_fontSize;
    }

    public void setPageWidget(PageWidget mBookPageWidget) {
        this.mBookPageWidget = mBookPageWidget;
    }

    public void setPageEvent(PageEvent pageEvent) {
        this.mPageEvent = pageEvent;
    }

    public interface PageEvent {
        void changeProgress(float progress);
    }

}
