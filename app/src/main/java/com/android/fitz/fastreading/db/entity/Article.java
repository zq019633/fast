package com.android.fitz.fastreading.db.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by fitz on 2017/4/18.
 */

@Entity
public class Article {
    @Id
    private String id;
    private String photo;
    private String speed;
    private String words;
    private String title;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    private String content;

    @Generated(hash = 2020122655)
    public Article(String id, String photo, String speed, String words,
            String title, String content) {
        this.id = id;
        this.photo = photo;
        this.speed = speed;
        this.words = words;
        this.title = title;
        this.content = content;
    }

    @Generated(hash = 742516792)
    public Article() {
    }
}
