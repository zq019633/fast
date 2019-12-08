package com.android.fitz.fastreading.bean;

/**
 * Created by fitz on 2017/07/09.
 */

public class BookEntity {
    private int bookID;
    private String name;
    private String url;

    public BookEntity(int bookID, String name, String url){
        this.bookID = bookID;
        this.name = name;
        this.url = url;
    }

    public int getBookID() {
        return bookID;
    }

    public void setBookID(int bookID) {
        this.bookID = bookID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
