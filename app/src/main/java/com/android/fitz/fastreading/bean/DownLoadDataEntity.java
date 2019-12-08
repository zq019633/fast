package com.android.fitz.fastreading.bean;

import java.util.List;

/**
 * Created by fitz on 2017/07/14.
 */

public class DownLoadDataEntity {

    private List<WordsBean> words;
    private List<AnimaBean> anima;
    private List<TestBean> test;
    private List<ViewBean> view;
    private List<QuestionBean> question;
    private List<TrainBean> train;

    public List<WordsBean> getWords() {
        return words;
    }

    public void setWords(List<WordsBean> words) {
        this.words = words;
    }

    public List<AnimaBean> getAnima() {
        return anima;
    }

    public void setAnima(List<AnimaBean> anima) {
        this.anima = anima;
    }

    public List<TestBean> getTest() {
        return test;
    }

    public void setTest(List<TestBean> test) {
        this.test = test;
    }

    public List<ViewBean> getView() {
        return view;
    }

    public void setView(List<ViewBean> view) {
        this.view = view;
    }

    public List<QuestionBean> getQuestion() {
        return question;
    }

    public void setQuestion(List<QuestionBean> question) {
        this.question = question;
    }

    public List<TrainBean> getTrain() {
        return train;
    }

    public void setTrain(List<TrainBean> train) {
        this.train = train;
    }

    public static class WordsBean {
        /**
         * len : 2
         * view_train_id : 7
         * content : 12
         * id : 7
         */

        private String len;
        private String view_train_id;
        private String content;
        private String id;

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

    public static class AnimaBean {
        /**
         * file_name : 1.png
         * id : 1
         */

        private String file_name;
        private String id;

        public String getFile_name() {
            return file_name;
        }

        public void setFile_name(String file_name) {
            this.file_name = file_name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    public static class TestBean {
        /**
         * photo : 20170712105118478.png
         * speed : 04
         * words : 15000
         * title : 我爱家乡的秋夜
         * content : 《我爱家乡的秋夜》
         * id : 2
         */

        private String photo;
        private String speed;
        private String words;
        private String title;
        private String content;
        private String id;

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

    public static class ViewBean {
        /**
         * type : 1
         * route : 2,11,14,23,26,23,14,11
         * v_desc : 游戏规则：
         * photo : 20170725111038455.png
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
        private String id;

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
    }

    public static class QuestionBean {
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

        private String answer;
        private String op1;
        private String op3;
        private String op2;
        private String q_type;
        private String op4;
        private String id;
        private String title;
        private String train_id;

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

    public static class TrainBean {
        /**
         * photo : 20170709133640071.png
         * speed : 01
         * words : 2500
         * title : 我爱家乡的秋夜
         * content : 《我爱家乡的秋夜》
         * id : 1
         */

        private String photo;
        private String speed;
        private String words;
        private String title;
        private String content;
        private String id;

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
}
