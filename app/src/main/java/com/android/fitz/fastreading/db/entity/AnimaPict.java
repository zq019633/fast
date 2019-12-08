package com.android.fitz.fastreading.db.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by fitz on 2017/07/17.
 */

@Entity
public class AnimaPict {
    @Id
    private String id;
    private String url;
    @Generated(hash = 2109685413)
    public AnimaPict(String id, String url) {
        this.id = id;
        this.url = url;
    }
    @Generated(hash = 161589393)
    public AnimaPict() {
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getUrl() {
        return this.url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
}
