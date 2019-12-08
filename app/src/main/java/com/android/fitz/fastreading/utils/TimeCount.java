package com.android.fitz.fastreading.utils;

import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/8/5.
 */

public class TimeCount extends CountDownTimer {
    private TextView mText;
    private int totalTime;
    private OnFinishListener mOnFinishListener;

    public TimeCount(long millisInFuture, long countDownInterval,TextView tv) {
        super(millisInFuture, countDownInterval);
        this.mText = tv;
        totalTime = (int) millisInFuture;
    }

    @Override
    public void onTick(long millisInFuture) {
        totalTime = (int) (millisInFuture/1000);
        mText.setText(timeFormat());
    }

    @Override
    public void onFinish() {
        if (mOnFinishListener != null) mOnFinishListener.onFinish();
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

    public interface OnFinishListener{
        void onFinish();
    }

    public void setOnFinishiListener(OnFinishListener onFinishListener){
        this.mOnFinishListener = onFinishListener;
    }
}
