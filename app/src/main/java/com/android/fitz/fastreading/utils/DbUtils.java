package com.android.fitz.fastreading.utils;

import android.os.Environment;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.fitz.fastreading.bean.DownLoadDataEntity;
import com.android.fitz.fastreading.constant.AppConfig;
import com.android.fitz.fastreading.db.entity.AnimaPict;
import com.android.fitz.fastreading.db.entity.Article;
import com.android.fitz.fastreading.db.entity.MenuView;
import com.android.fitz.fastreading.db.entity.Question;
import com.android.fitz.fastreading.db.entity.Test;
import com.android.fitz.fastreading.db.entity.Words;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fitz on 2017/04/22.
 */

public class DbUtils {

    /***
     * 获取动物图片
     * @param entity
     * @return
     */
    public static List<AnimaPict> getAnimaPict(DownLoadDataEntity entity){
        String rootPath =  Environment.getExternalStorageDirectory().getAbsolutePath();
        List<AnimaPict> list = new ArrayList<>();
        for(DownLoadDataEntity.AnimaBean bean : entity.getAnima()) {
            AnimaPict anim = new AnimaPict();
            anim.setUrl(String.format("%s%s%s", rootPath, AppConfig.CACHE_ANIME_PATH, bean.getFile_name()));
            anim.setId(bean.getId());

            list.add(anim);
        }
        return list;
    }

    /***
     * 获取文章
     * @param entity
     * @return
     */
    public static List<Article> getArticle(DownLoadDataEntity entity){
        String rootPath =  Environment.getExternalStorageDirectory().getAbsolutePath();
        List<Article> list = new ArrayList<>();
        for(DownLoadDataEntity.TrainBean bean : entity.getTrain()) {
            Article article = new Article();
            article.setId(bean.getId());
            article.setTitle(bean.getTitle());
            article.setContent(bean.getContent());
            article.setPhoto(String.format("%s%s%s", rootPath, AppConfig.CACHE_ARTICLE_PATH , bean.getPhoto()));
            article.setSpeed(bean.getSpeed());
            article.setWords(bean.getWords());

            list.add(article);
        }
        return list;
    }

    /**
     * 获取测试题
     * @param entity
     * @return
     */
    public static List<Test> getTest(DownLoadDataEntity entity){
        List<Test> list = new ArrayList<>();
        String rootPath =  Environment.getExternalStorageDirectory().getAbsolutePath();
        for(DownLoadDataEntity.TestBean bean : entity.getTest()) {
            Test test = new Test();
            test.setId(bean.getId());
            test.setSpeed(bean.getSpeed());
            test.setContent(bean.getContent());
            test.setTitle(bean.getTitle());
            test.setWords(bean.getWords());
            test.setPhoto(String.format("%s%s%s", rootPath, AppConfig.CACHE_TEST_PATH , bean.getPhoto()));

            list.add(test);
        }
        return list;
    }

    /**
     * 获取问题
     * @param entity
     * @return
     */
    public static List<Question> getQuestion(DownLoadDataEntity entity){
        List<Question> list = new ArrayList<>();
        String rootPath =  Environment.getExternalStorageDirectory().getAbsolutePath();
        for(DownLoadDataEntity.QuestionBean bean : entity.getQuestion()) {
            Question question = new Question();
            question.setId(bean.getId());
            question.setTitle(bean.getTitle());
            question.setAnswer(bean.getAnswer());
            question.setTrain_id(bean.getTrain_id());
            question.setOp1(bean.getOp1());
            question.setOp2(bean.getOp2());
            question.setOp3(bean.getOp3());
            question.setOp4(bean.getOp4());
            question.setQ_type(bean.getQ_type());

            list.add(question);
        }
        return list;
    }


    /**
     * 获取菜单
     * @param entity
     * @return
     */
    public static List<MenuView> getMenuView(DownLoadDataEntity entity){
        List<MenuView> list = new ArrayList<>();
        String rootPath =  Environment.getExternalStorageDirectory().getAbsolutePath();
        for(DownLoadDataEntity.ViewBean bean : entity.getView()) {
            MenuView menu = new MenuView();
            menu.setId(bean.getId());
            menu.setType(bean.getType());
            menu.setRoute(bean.getRoute());
            menu.setV_desc(bean.getV_desc());
            menu.setPhoto(String.format("%s%s%s", rootPath, AppConfig.CACHE_VIEW_PATH , bean.getPhoto()));
            menu.setTitle(bean.getTitle());
            menu.setContent(bean.getContent());

            list.add(menu);
        }
        return list;
    }

    /**
     * 获取文字
     * @param entity
     * @return
     */
    public static List<Words> getWordsView(DownLoadDataEntity entity){
        List<Words> list = new ArrayList<>();
        for(DownLoadDataEntity.WordsBean bean : entity.getWords()) {
            Words word = new Words();
            word.setId(bean.getId());
            word.setLen(bean.getLen());
            word.setContent(bean.getContent());
            word.setView_train_id(bean.getView_train_id());

            list.add(word);
        }
        return list;
    }
}
