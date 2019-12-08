package com.android.fitz.fastreading.db.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by fitz on 2017/07/21.
 */
@Entity
public class Question {

    /**
     * answer : 2
     * op1 : 一幅美丽的水墨画
     * op3 : 男高音唱着那粗犷嘹亮的歌儿
     * op2 : 珍珠撒在玉盆里
     * q_type : 2
     * op4 : 一首美妙的交响曲
     * id : 1
     * title : 以下比喻句没有在文中出现过
     * train_id : 2
     */
    @Id
    private String id;
    private String answer;
    private String op1;
    private String op3;
    private String op2;
    private String q_type;
    private String op4;
    private String title;
    private String train_id;

    @Generated(hash = 819417034)
    public Question(String id, String answer, String op1, String op3, String op2,
            String q_type, String op4, String title, String train_id) {
        this.id = id;
        this.answer = answer;
        this.op1 = op1;
        this.op3 = op3;
        this.op2 = op2;
        this.q_type = q_type;
        this.op4 = op4;
        this.title = title;
        this.train_id = train_id;
    }

    @Generated(hash = 1868476517)
    public Question() {
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getOp1() {
        return op1;
    }

    public void setOp1(String op1) {
        this.op1 = op1;
    }

    public String getOp3() {
        return op3;
    }

    public void setOp3(String op3) {
        this.op3 = op3;
    }

    public String getOp2() {
        return op2;
    }

    public void setOp2(String op2) {
        this.op2 = op2;
    }

    public String getQ_type() {
        return q_type;
    }

    public void setQ_type(String q_type) {
        this.q_type = q_type;
    }

    public String getOp4() {
        return op4;
    }

    public void setOp4(String op4) {
        this.op4 = op4;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTrain_id() {
        return train_id;
    }

    public void setTrain_id(String train_id) {
        this.train_id = train_id;
    }
}
