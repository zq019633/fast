package com.android.fitz.fastreading.bean;

/**
 * Created by fitz on 2017/07/08.
 */

public class ImageEntity {
    private String url;
    private int index;

    public ImageEntity(String url, int index){
        this.url = url;
        this.index = index;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
