package com.android.fitz.fastreading.db.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by fitz on 2017/09/03.
 */
@Entity
public class Words {

    /**
     * len : 2
     * view_train_id : 7
     * content : 12
     * id : 7
     */
    @Id(autoincrement = true)
    private Long WID;
    private String len;
    private String view_train_id;
    private String content;
    private String id;

    @Generated(hash = 121538544)
    public Words(Long WID, String len, String view_train_id, String content,
            String id) {
        this.WID = WID;
        this.len = len;
        this.view_train_id = view_train_id;
        this.content = content;
        this.id = id;
    }

    @Generated(hash = 796553661)
    public Words() {
    }

    public Long getWID() {
        return WID;
    }

    public void setWID(Long WID) {
        this.WID = WID;
    }

    public String getLen() {
        return len;
    }

    public void setLen(String len) {
        this.len = len;
    }

    public String getView_train_id() {
        return view_train_id;
    }

    public void setView_train_id(String view_train_id) {
        this.view_train_id = view_train_id;
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
