package com.android.fitz.fastreading.db.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by fitz on 2017/07/25.
 */

@Entity
public class MenuView {

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getV_desc() {
        return v_desc;
    }

    public void setV_desc(String v_desc) {
        this.v_desc = v_desc;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
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

    /**
     * type : 1
     * route : 2,11,14,23,26
     * v_desc : 游戏规则：眼睛根据图片的移动轨迹进行运动，注意目标图片出现的次数，锻炼眼部对角向肌肉。
     * photo : 20170725011646947.png
     * title : 横向“之”字形运动
     * content : 无
     * id : 1
     */

    private String type;
    private String route;
    private String v_desc;
    private String photo;
    private String title;
    private String content;
    @Id
    private String id;
    @Generated(hash = 1261064576)
    public MenuView(String type, String route, String v_desc, String photo,
            String title, String content, String id) {
        this.type = type;
        this.route = route;
        this.v_desc = v_desc;
        this.photo = photo;
        this.title = title;
        this.content = content;
        this.id = id;
    }

    @Generated(hash = 376617101)
    public MenuView() {
    }
}
