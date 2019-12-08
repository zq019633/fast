package com.android.fitz.fastreading.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.android.fitz.fastreading.R;
import com.android.fitz.fastreading.utils.CommonUtils;
import com.android.library.util.DensityUtil;
import com.android.library.util.ToastUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/8/3.
 * 自定义的转盘view
 */

public class TurnTableView extends View {
    private Context context = getContext();
    private Paint mCirclePaint;
    private List<Bitmap> mBitmaps;
    private List<List<Integer>> inLocationX = new ArrayList<>();
    private List<List<Integer>> inLocationY = new ArrayList<>();
    private List<List<Float>> outLocationX = new ArrayList<>();
    private List<List<Float>> outLocationY = new ArrayList<>();
    private int inSelected = -1;
    private int outSelected = -1;
    private List<Bitmap> result;
    private int completeCount;//已完成配对数量
    private List<Integer> pairs = new ArrayList<>();//专门用来存放配对的两个位置，inSelected和outSelected
    private boolean isDrawRect;//是否画红框，为了控制重绘
    private OnRefreshUIListener mOnRefreshUIListener;

    public TurnTableView(Context context) {
        super(context);
        init();
    }

    public TurnTableView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TurnTableView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 初始化工作
     */
    private void init() {
        mCirclePaint = new Paint();
        mCirclePaint.setColor(ContextCompat.getColor(getContext(), R.color.black));
        mCirclePaint.setStrokeWidth(DensityUtil.dip2px(getContext(), 4));
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawOutCircle(canvas);
        drawInCircle(canvas);
        drawOutImageView(canvas);
        drawInImageView(canvas);
        drawRect(canvas);
    }

    /**
     * 绘制外圆
     */
    private void drawOutCircle(Canvas canvas) {
        canvas.drawCircle(DensityUtil.dip2px(getContext(), 222), DensityUtil.dip2px(getContext(), 222), DensityUtil.dip2px(getContext(), 220), mCirclePaint);
    }

    /**
     * 绘制内圆
     *
     * @param canvas
     */
    private void drawInCircle(Canvas canvas) {
        mCirclePaint.reset();
        mCirclePaint.setColor(ContextCompat.getColor(getContext(), R.color.black));
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setStrokeWidth(DensityUtil.dip2px(getContext(), 2));
        mCirclePaint.setAntiAlias(true);
        canvas.drawCircle(DensityUtil.dip2px(getContext(), 222), DensityUtil.dip2px(getContext(), 222), DensityUtil.dip2px(getContext(), 88), mCirclePaint);
    }

    /**
     * 绘制一圈8个imageview
     */
    private void drawOutImageView(Canvas canvas) {
        if (mBitmaps != null && mBitmaps.size() == 8) {
            int radius = DensityUtil.dip2px(getContext(), 222);

            double angle = Math.toRadians(22.5);
            float x1 = (float) (200 * Math.sin(angle));
            float y1 = (float) (200 * Math.cos(angle));

            float x2 = (float) (200 * Math.cos(angle));
            float y2 = (float) (200 * Math.sin(angle));

            int width = 90;
            int height = 110;

            outLocationX.clear();
            outLocationY.clear();
            for (int i = 0; i < mBitmaps.size(); i++) {
                Bitmap bitmap = mBitmaps.get(i);
                if (i == 0) {
                    List<Float> locationX = new ArrayList<>();
                    locationX.add(x1 + radius - width / 2);
                    locationX.add(radius + x1 + width / 2);
                    outLocationX.add(locationX);
                    List<Float> locationY = new ArrayList<>();
                    locationY.add(radius - y1 - height / 2);
                    locationY.add(radius - y1 + height / 2);
                    outLocationY.add(locationY);
                    // 确定绘制图片的位置
                    RectF rectF = new RectF(x1 + radius - width / 2, radius - y1 - height / 2, radius + x1 + width / 2, radius - y1 + height / 2);
                    canvas.drawBitmap(bitmap, null, rectF, null);
                } else if (i == 1) {
                    List<Float> locationX = new ArrayList<>();
                    locationX.add(x2 + radius - width / 2);
                    locationX.add(radius + x2 + width / 2);
                    outLocationX.add(locationX);
                    List<Float> locationY = new ArrayList<>();
                    locationY.add(radius - y2 - height / 2);
                    locationY.add(radius - y2 + height / 2);
                    outLocationY.add(locationY);
                    RectF rectF = new RectF(x2 + radius - width / 2, radius - y2 - height / 2, radius + x2 + width / 2, radius - y2 + height / 2);
                    canvas.drawBitmap(bitmap, null, rectF, null);
                } else if (i == 2) {
                    List<Float> locationX = new ArrayList<>();
                    locationX.add(x2 + radius - width / 2);
                    locationX.add(radius + x2 + width / 2);
                    outLocationX.add(locationX);
                    List<Float> locationY = new ArrayList<>();
                    locationY.add(radius + y2 - height / 2);
                    locationY.add(radius + y2 + height / 2);
                    outLocationY.add(locationY);
                    RectF rectF = new RectF(x2 + radius - width / 2, radius + y2 - height / 2, radius + x2 + width / 2, radius + y2 + height / 2);
                    canvas.drawBitmap(bitmap, null, rectF, null);
                } else if (i == 3) {
                    List<Float> locationX = new ArrayList<>();
                    locationX.add(x1 + radius - width / 2);
                    locationX.add(radius + x1 + width / 2);
                    outLocationX.add(locationX);
                    List<Float> locationY = new ArrayList<>();
                    locationY.add(radius + y1 - height / 2);
                    locationY.add(radius + y1 + height / 2);
                    outLocationY.add(locationY);
                    RectF rectF = new RectF(x1 + radius - width / 2, radius + y1 - height / 2, radius + x1 + width / 2, radius + y1 + height / 2);
                    canvas.drawBitmap(bitmap, null, rectF, null);
                } else if (i == 4) {
                    List<Float> locationX = new ArrayList<>();
                    locationX.add(radius - x1 - width / 2);
                    locationX.add(radius - x1 + width / 2);
                    outLocationX.add(locationX);
                    List<Float> locationY = new ArrayList<>();
                    locationY.add(radius + y1 - height / 2);
                    locationY.add(radius + y1 + height / 2);
                    outLocationY.add(locationY);
                    RectF rectF = new RectF(radius - x1 - width / 2, radius + y1 - height / 2, radius - x1 + width / 2, radius + y1 + height / 2);
                    canvas.drawBitmap(bitmap, null, rectF, null);
                } else if (i == 5) {
                    List<Float> locationX = new ArrayList<>();
                    locationX.add(radius - x2 - width / 2);
                    locationX.add(radius - x2 + width / 2);
                    outLocationX.add(locationX);
                    List<Float> locationY = new ArrayList<>();
                    locationY.add(radius + y2 - height / 2);
                    locationY.add(radius + y2 + height / 2);
                    outLocationY.add(locationY);
                    RectF rectF = new RectF(radius - x2 - width / 2, radius + y2 - height / 2, radius - x2 + width / 2, radius + y2 + height / 2);
                    canvas.drawBitmap(bitmap, null, rectF, null);
                } else if (i == 6) {
                    List<Float> locationX = new ArrayList<>();
                    locationX.add(radius - x2 - width / 2);
                    locationX.add(radius - x2 + width / 2);
                    outLocationX.add(locationX);
                    List<Float> locationY = new ArrayList<>();
                    locationY.add(radius - y2 - height / 2);
                    locationY.add(radius - y2 + height / 2);
                    outLocationY.add(locationY);
                    RectF rectF = new RectF(radius - x2 - width / 2, radius - y2 - height / 2, radius - x2 + width / 2, radius - y2 + height / 2);
                    canvas.drawBitmap(bitmap, null, rectF, null);
                } else if (i == 7) {
                    List<Float> locationX = new ArrayList<>();
                    locationX.add(radius - x1 - width / 2);
                    locationX.add(radius - x1 + width / 2);
                    outLocationX.add(locationX);
                    List<Float> locationY = new ArrayList<>();
                    locationY.add(radius - y1 - height / 2);
                    locationY.add(radius - y1 + height / 2);
                    outLocationY.add(locationY);
                    RectF rectF = new RectF(radius - x1 - width / 2, radius - y1 - height / 2, radius - x1 + width / 2, radius - y1 + height / 2);
                    canvas.drawBitmap(bitmap, null, rectF, null);
                }
            }
        }
    }

    /**
     * 绘制内圆2个imageview
     *
     * @param canvas
     */
    private void drawInImageView(Canvas canvas) {
        if (mBitmaps != null && mBitmaps.size() == 8) {
            if (!isDrawRect) {
                result = new ArrayList<>();
                for (int i = 0; i < 100; i++) {
                    int position = CommonUtils.getRandom(0, mBitmaps.size() - 1);
                    if (Collections.frequency(result, mBitmaps.get(position)) < 1)
                        result.add(mBitmaps.get(position));
                    if (result.size() == 2) break;
                }
            }

            int radius = DensityUtil.dip2px(getContext(), 222);
            int smallRadius = DensityUtil.dip2px(getContext(), 88);
            int width = 90;
            int height = 110;

            inLocationX.clear();
            inLocationY.clear();
            for (int i = 0; i < 2; i++) {
                Bitmap bitmap = result.get(i);
                if (i == 0) {
                    List<Integer> locationX = new ArrayList<>();
                    locationX.add(radius - smallRadius + 25);
                    locationX.add(radius - smallRadius + 25 + width);
                    inLocationX.add(locationX);
                    List<Integer> locationY = new ArrayList<>();
                    locationY.add(radius - smallRadius + 50);
                    locationY.add(radius - smallRadius + 50 + height);
                    inLocationY.add(locationY);
                    RectF rectF = new RectF(radius - smallRadius + 25, radius - smallRadius + 50, radius - smallRadius + 25 + width, radius - smallRadius + 50 + height);
                    canvas.drawBitmap(bitmap, null, rectF, null);
                } else if (i == 1) {
                    List<Integer> locationX = new ArrayList<>();
                    locationX.add(radius + smallRadius - width - 25);
                    locationX.add(radius + smallRadius - 25);
                    inLocationX.add(locationX);
                    List<Integer> locationY = new ArrayList<>();
                    locationY.add(radius - smallRadius + 50);
                    locationY.add(radius - smallRadius + 50 + height);
                    inLocationY.add(locationY);
                    RectF rectF = new RectF(radius + smallRadius - width - 25, radius - smallRadius + 50, radius + smallRadius - 25, radius - smallRadius + 50 + height);
                    canvas.drawBitmap(bitmap, null, rectF, null);
                }
            }
        }
    }

    public void setBitmaps(List<Bitmap> bitmaps) {
        this.mBitmaps = bitmaps;
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x;
        int y;

        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                x = (int) event.getX();
                y = (int) event.getY();
                if (x >= inLocationX.get(0).get(0) && x <= inLocationX.get(0).get(1) && y >= inLocationY.get(0).get(0) && y <= inLocationY.get(0).get(1)) {
                    inSelected = 0;
                } else if (x >= inLocationX.get(1).get(0) && x <= inLocationX.get(1).get(1) && y >= inLocationY.get(1).get(0) && y <= inLocationY.get(1).get(1)) {
                    inSelected = 1;
                } else if (x >= outLocationX.get(0).get(0) && x <= outLocationX.get(0).get(1) && y >= outLocationY.get(0).get(0) && y <= outLocationY.get(0).get(1)) {
                    outSelected = 0;
                } else if (x >= outLocationX.get(1).get(0) && x <= outLocationX.get(1).get(1) && y >= outLocationY.get(1).get(0) && y <= outLocationY.get(1).get(1)) {
                    outSelected = 1;
                } else if (x >= outLocationX.get(2).get(0) && x <= outLocationX.get(2).get(1) && y >= outLocationY.get(2).get(0) && y <= outLocationY.get(2).get(1)) {
                    outSelected = 2;
                } else if (x >= outLocationX.get(3).get(0) && x <= outLocationX.get(3).get(1) && y >= outLocationY.get(3).get(0) && y <= outLocationY.get(3).get(1)) {
                    outSelected = 3;
                } else if (x >= outLocationX.get(4).get(0) && x <= outLocationX.get(4).get(1) && y >= outLocationY.get(4).get(0) && y <= outLocationY.get(4).get(1)) {
                    outSelected = 4;
                } else if (x >= outLocationX.get(5).get(0) && x <= outLocationX.get(5).get(1) && y >= outLocationY.get(5).get(0) && y <= outLocationY.get(5).get(1)) {
                    outSelected = 5;
                } else if (x >= outLocationX.get(6).get(0) && x <= outLocationX.get(6).get(1) && y >= outLocationY.get(6).get(0) && y <= outLocationY.get(6).get(1)) {
                    outSelected = 6;
                } else if (x >= outLocationX.get(7).get(0) && x <= outLocationX.get(7).get(1) && y >= outLocationY.get(7).get(0) && y <= outLocationY.get(7).get(1)) {
                    outSelected = 7;
                }

                if (inSelected >= 0 && outSelected >= 0 && result.get(inSelected) == mBitmaps.get(outSelected)) {
                    if (completeCount == 0) {//完成第一个配对
                        completeCount++;
                        pairs.clear();
                        pairs.add(inSelected);
                        pairs.add(outSelected);
                        isDrawRect = true;
                        invalidate();
                    } else if (completeCount == 1) {//两个配对都完成了
                        completeCount++;
                        pairs.add(inSelected);
                        pairs.add(outSelected);
                        isDrawRect = true;
                        invalidate();

                        post(new Runnable() {
                            @Override
                            public void run() {
                                isDrawRect = false;
                                if (mOnRefreshUIListener != null) mOnRefreshUIListener.onRefreshUI();
                                inSelected = -1;
                                outSelected = -1;
                                result.clear();
                                completeCount = 0;//已完成配对数量
                                pairs.clear();//专门用来存放配对的两个位置，inSelected和outSelected
                                postInvalidate();
                            }
                        });
                    }
                }
                break;
        }
        return true;
    }

    private void drawRect(Canvas canvas) {
        if (inSelected >= 0 && outSelected >= 0) {
            Paint paint = new Paint();
            paint.setColor(ContextCompat.getColor(getContext(), R.color.red));
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(5);
            if (pairs.size() > 0) {
                canvas.drawRect(inLocationX.get(pairs.get(0)).get(0), inLocationY.get(pairs.get(0)).get(0), inLocationX.get(pairs.get(0)).get(1),
                        inLocationY.get(pairs.get(0)).get(1), paint);
                canvas.drawRect(outLocationX.get(pairs.get(1)).get(0), outLocationY.get(pairs.get(1)).get(0), outLocationX.get(pairs.get(1)).get(1),
                        outLocationY.get(pairs.get(1)).get(1), paint);
            }
            if (pairs.size() > 2){
                canvas.drawRect(inLocationX.get(pairs.get(2)).get(0), inLocationY.get(pairs.get(2)).get(0), inLocationX.get(pairs.get(2)).get(1),
                        inLocationY.get(pairs.get(2)).get(1), paint);
                canvas.drawRect(outLocationX.get(pairs.get(3)).get(0), outLocationY.get(pairs.get(3)).get(0), outLocationX.get(pairs.get(3)).get(1),
                        outLocationY.get(pairs.get(3)).get(1), paint);
            }
        }
    }

    public interface OnRefreshUIListener{
        void onRefreshUI();
    }

    public void setOnRefreshUIListener(OnRefreshUIListener onRefreshUIListener){
        this.mOnRefreshUIListener = onRefreshUIListener;
    }
}
