package com.android.fitz.fastreading.base;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.android.fitz.fastreading.db.DBOnUpgradeHelper;
import com.android.fitz.fastreading.db.GreenDaoContext;
import com.android.fitz.fastreading.db.greendao.DaoMaster;
import com.android.fitz.fastreading.db.greendao.DaoSession;
import com.android.fitz.fastreading.widget.page.Config;
import com.android.fitz.fastreading.widget.page.PageFactory;
import com.github.yuweiguocn.library.greendao.MigrationHelper;
import com.rey.material.app.ThemeManager;
import com.android.fitz.fastreading.constant.AppConfig;
import com.android.fitz.fastreading.injector.component.ApplicationComponent;
import com.android.fitz.fastreading.injector.component.DaggerApplicationComponent;
import com.android.fitz.fastreading.injector.module.ApplicationModule;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;


/**
 * Author:  jact
 * Date:    2016/3/16
 * Description:
 */
public class AppApplication extends Application {

    DBOnUpgradeHelper mHelper;
    private SQLiteDatabase db;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;
    private ApplicationComponent component;
    private static AppApplication sInstance;
    /*private RefWatcher refWatcher;

    public static RefWatcher getRefWatcher(Context context) {
        AppApplication application = (AppApplication) context
                .getApplicationContext();
        return application.refWatcher;
    }*/

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);

        // 安装tinker
        Beta.installTinker();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        init();
    }

    public static AppApplication getInstance(){
        return sInstance;
    }

    private void init(){
        Config.createConfig(this);
        PageFactory.createPageFactory(this);
        initDb();

        ThemeManager.init(this, 2, 0, null);

        this.component = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
        this.component.injectApplication(this);

        AppConfig.DEBUG = true;
        //enabledStrictMode();
        initUmeng();
        //refWatcher = LeakCanary.install(this);

        // 调试时，将第三个参数改为true
        Bugly.init(this, "36f7a74b09", false);
    }

    private void initDb(){
        //DaoManager.init(this);

        MigrationHelper.DEBUG = true;
        //mHelper = new DaoMaster.DevOpenHelper(new GreenDaoContext(getApplicationContext()), "hospital", null);
        //MigrationHelper.DEBUG = true; //如果你想查看日志信息，请将DEBUG设置为true
        mHelper = new DBOnUpgradeHelper(new GreenDaoContext(getApplicationContext()), "fastreading.db", null);
        db = mHelper.getWritableDatabase();
        // 注意：该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。
        mDaoMaster = new DaoMaster(db);
        mDaoSession = mDaoMaster.newSession();
    }

    public DaoSession getDaoSession() {
        return mDaoSession;
    }

    private void initUmeng(){
        /*UMShareAPI.get(this);

        PlatformConfig.setWeixin("wx17ca3a64c0aca959", "0280799a9db8795e6f3c75b5ddd0bbf5");
        PlatformConfig.setSinaWeibo("1896994260", "2dca405a943bfb7322a9dd8bf3e5098f");
        PlatformConfig.setQQZone("101371461", "9a070d99d148a1a45c918b9f2a58374e");

        Config.REDIRECT_URL = "http://www.crazpay.com/sina/callback";
        Config.DEBUG = true;*/
    }

    public ApplicationComponent getApplicationComponent() {
        return this.component;
    }

}
