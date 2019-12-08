package com.android.fitz.fastreading.eventbus;

/**
 * Created by fitz on 2017/07/13.
 */

public class DownProgressEvent {

    // -1出现异常，100下载完成
    private int progress;

    public DownProgressEvent(int progress){
        this.progress = progress;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}
