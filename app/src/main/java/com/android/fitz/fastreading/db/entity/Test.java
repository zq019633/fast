package com.android.fitz.fastreading.db.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by fitz on 2017/07/21.
 */
@Entity
public class Test {

    /**
     * photo : 20170719163408709.jpg
     * speed : 01
     * words : 705
     * title : 三毛流浪记
     * content :
     * id : 5
     */

    private String photo;
    private String speed;
    private String words;
    private String title;
    private String content;
    @Id
    private String id;

    @Generated(hash = 1469084589)
    public Test(String photo, String speed, String words, String title,
            String content, String id) {
        this.photo = photo;
        this.speed = speed;
        this.words = words;
        this.title = title;
        this.content = content;
        this.id = id;
    }

    @Generated(hash = 372557997)
    public Test() {
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
