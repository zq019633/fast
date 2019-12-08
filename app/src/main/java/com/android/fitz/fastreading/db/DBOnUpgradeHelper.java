package com.android.fitz.fastreading.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.android.fitz.fastreading.db.entity.Words;
import com.android.fitz.fastreading.db.greendao.AnimaPictDao;
import com.android.fitz.fastreading.db.greendao.ArticleDao;
import com.android.fitz.fastreading.db.greendao.DaoMaster;
import com.android.fitz.fastreading.db.greendao.MenuViewDao;
import com.android.fitz.fastreading.db.greendao.QuestionDao;
import com.android.fitz.fastreading.db.greendao.TestDao;
import com.android.fitz.fastreading.db.greendao.WordsDao;
import com.github.yuweiguocn.library.greendao.MigrationHelper;


/**
 * Created by Growth on 2016/3/3.
 */
public class DBOnUpgradeHelper extends DaoMaster.OpenHelper {
    public DBOnUpgradeHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        MigrationHelper.migrate(db, ArticleDao.class, AnimaPictDao.class, QuestionDao.class, TestDao.class, MenuViewDao.class, WordsDao.class);
    }
}
