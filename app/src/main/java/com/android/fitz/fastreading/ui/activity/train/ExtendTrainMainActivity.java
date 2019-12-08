package com.android.fitz.fastreading.ui.activity.train;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.fitz.fastreading.R;
import com.android.fitz.fastreading.base.AppApplication;
import com.android.fitz.fastreading.base.BaseActivity;
import com.android.fitz.fastreading.constant.Constant;
import com.android.fitz.fastreading.constant.Preferences;
import com.android.fitz.fastreading.db.entity.MenuView;
import com.android.fitz.fastreading.db.greendao.DaoSession;
import com.android.fitz.fastreading.db.greendao.MenuViewDao;
import com.android.library.util.ConvertUtils;
import com.android.library.util.PreferencesUtils;
import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class ExtendTrainMainActivity extends BaseActivity {

    @BindView(R.id.introduce)
    ImageView introduce;
    @BindView(R.id.settinglength)
    ImageButton settinglength;
    @BindView(R.id.setting)
    ImageButton setting;
    @BindView(R.id.settingmenu)
    LinearLayout settingmenu;
    @BindView(R.id.settinglengthmenu)
    LinearLayout settinglengthmenu;

    @BindView(R.id.container1)
    LinearLayout container1;
    @BindView(R.id.container2)
    LinearLayout container2;
    @BindView(R.id.container3)
    LinearLayout container3;
//    @BindView(R.id.rmenu1)
//    TextView rmenu1;
//    @BindView(R.id.rmenu2)
//    TextView rmenu2;
//    @BindView(R.id.rmenu3)
//    TextView rmenu3;
//    @BindView(R.id.rmenu4)
//    TextView rmenu4;
//    @BindView(R.id.lmenu1)
//    TextView lmenu1;
//    @BindView(R.id.lmenu2)
//    TextView lmenu2;
//    @BindView(R.id.lmenu3)
//    TextView lmenu3;
//    @BindView(R.id.lmenu4)
//    TextView lmenu4;
//    @BindView(R.id.rlmenu1)
//    TextView rlmenu1;
//    @BindView(R.id.rlmenu2)
//    TextView rlmenu2;
//    @BindView(R.id.rlmenu3)
//    TextView rlmenu3;
//    @BindView(R.id.rlmenu4)
//    TextView rlmenu4;
//    @BindView(R.id.llmenu1)
//    TextView llmenu1;
//    @BindView(R.id.llmenu2)
//    TextView llmenu2;
//    @BindView(R.id.llmenu3)
//    TextView llmenu3;
//    @BindView(R.id.llmenu4)
//    TextView llmenu4;
    @BindViews({ R.id.llmenu1, R.id.llmenu2, R.id.llmenu3, R.id.llmenu4, R.id.rlmenu1, R.id.rlmenu2, R.id.rlmenu3, R.id.rlmenu4 })
    List<TextView> settingLengthView;

    @BindViews({ R.id.lmenu1, R.id.lmenu2, R.id.lmenu3, R.id.lmenu4 })
    List<TextView> settingLeftArr;

    @BindViews({ R.id.rmenu1, R.id.rmenu2, R.id.rmenu3, R.id.rmenu4 })
    List<TextView> settingRightArr;

    String id;

    MenuViewDao viewDao;

    @Override
    protected void initView() {
        //settingLeftArr = new TextView[]{lmenu1, lmenu2, lmenu3, lmenu4};
        //settingRightArr = new TextView[]{rmenu1, rmenu2, rmenu3, rmenu4};
        //settingLengthArr = new TextView[]{llmenu1, llmenu2, llmenu3, llmenu4, rlmenu1, rlmenu2, rlmenu3, rlmenu4};

        DaoSession daoSession = ((AppApplication) getApplication()).getDaoSession();
        viewDao = daoSession.getMenuViewDao();

        viewDao.rx()
                .loadAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<MenuView>>() {
                    @Override
                    public void call(List<MenuView> lists) {
                        initMenu(lists);
                    }
                });

        initIntroduce();
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_extend_train_main;
    }

    @Override
    protected void initInjector() {

    }

    // 左侧菜单
    private void initMenu(List<MenuView> lists){
        container1.removeAllViews();
        container2.removeAllViews();
        container3.removeAllViews();
        for(MenuView item : lists){

            View  child = LayoutInflater.from(getApplicationContext()).inflate(R.layout.extend_train_menu_item, null);
            TextView txt = (TextView)child.findViewById(R.id.menu);
            txt.setOnClickListener(clickListener);
            txt.setTag(item);
            txt.setText(item.getTitle());

            if(item.getType().equals("1")){
                container1.addView(child);
            }else if(item.getType().equals("2")){
                container2.addView(child);
            }else if(item.getType().equals("3")){
                container3.addView(child);
            }
        }
        if(container1.getChildCount() > 0){
            container1.setVisibility(View.VISIBLE);
            View menu = container1.getChildAt(0).findViewById(R.id.menu);
            if(menu != null){
                menu.performClick();
            }
        }
    }


    private void initIntroduce() {
        // 【设置】左右
        int a = PreferencesUtils.getInt(getApplicationContext(), Preferences.SETTING_LEFT, 0);
        if(a == 0){
            PreferencesUtils.putInt(getApplicationContext(), Preferences.SETTING_LEFT, 2);
        }
        int b = PreferencesUtils.getInt(getApplicationContext(), Preferences.SETTING_RIGHT, 0);
        if(b == 0){
            PreferencesUtils.putInt(getApplicationContext(), Preferences.SETTING_RIGHT, 2);
        }
        // 【长度设置】
        int c = PreferencesUtils.getInt(getApplicationContext(), Preferences.SETTING_LENGTH, 0);
        if(c == 0){
            PreferencesUtils.putInt(getApplicationContext(), Preferences.SETTING_LENGTH, 2);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 【设置】隐藏
        settingmenu.setVisibility(View.GONE);
        // 【设定长度】隐藏
        settinglengthmenu.setVisibility(View.GONE);
        return super.onTouchEvent(event);
    }

    // GroupClick
    @OnClick({R.id.group1, R.id.group2, R.id.group3})
    public void onGroupClicked(View view) {
        container1.setVisibility(View.GONE);
        container2.setVisibility(View.GONE);
        container3.setVisibility(View.GONE);
        settinglength.setVisibility(View.GONE);
        switch (view.getId()) {
            case R.id.group1:
                container1.setVisibility(View.VISIBLE);
                break;
            case R.id.group2:
                container2.setVisibility(View.VISIBLE);
                break;
            case R.id.group3:
                container3.setVisibility(View.VISIBLE);
                settinglength.setVisibility(View.VISIBLE);
                break;
        }
        // 【设置】隐藏
        settingmenu.setVisibility(View.GONE);
        // 【设定长度】隐藏
        settinglengthmenu.setVisibility(View.GONE);
    }

    // 左侧菜单点击事件
    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            MenuView item = (MenuView) v.getTag();
            id = item.getId();
            Glide.with(ExtendTrainMainActivity.this)
                    .load(item.getPhoto())
                    .into(introduce);
            // 游戏类型
            //type = ConvertUtils.toInt(view.getTag());
            // 【设置】隐藏
            settingmenu.setVisibility(View.GONE);
            // 【设定长度】隐藏
            settinglengthmenu.setVisibility(View.GONE);
        }
    };

    // 训练模式【横向之字型运动... ; 跟住,别掉队... ; 数字训练...】
    @OnClick({R.id.back, R.id.settinglength, R.id.setting, R.id.play, R.id.settingmenu, R.id.settingsure, R.id.settingview, R.id.settinglengthsure, R.id.settinglengthview})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.settinglength:
                initSettingLength();
                break;
            case R.id.setting:
                initSetting();
                break;
            case R.id.play:
                playGame();
                break;
            case R.id.settingmenu:
                break;
            case R.id.settingsure:
                // 【参数设置】确定
                settingSure();
                break;
            case R.id.settinglengthsure:
                // 【参数设置】确定
                settingLengthSure();
                break;
            case R.id.settingview:
                // 【参数设置】
                break;
        }
        if (view.getId() != R.id.setting && view.getId() != R.id.settingview) {
            //【设置】隐藏
            settingmenu.setVisibility(View.GONE);
        }
        if (view.getId() != R.id.settinglength && view.getId() != R.id.settinglengthview) {
            //【设置】隐藏
            settinglengthmenu.setVisibility(View.GONE);
        }
    }

    // 进入游戏
    private void playGame(){
        Bundle bundle = new Bundle();
        bundle.putString(Constant.ID, id);
        readyGo(ReadyGoActivity.class, bundle);
    }

    // 【参数设置初始化】
    private void initSetting(){
        // 左侧
        for(TextView view : settingLeftArr){
            view.setSelected(false);
        }
        int index = 0;
        int left = PreferencesUtils.getInt(getApplicationContext(), Preferences.SETTING_LEFT);
        switch (left){
            case 1:
                index = 0;
                break;
            case 2:
                index = 1;
                break;
            case 3:
                index = 2;
                break;
            case 4:
                index = 3;
                break;
        }
        settingLeftArr.get(index).setSelected(true);
        // 右侧
        for(TextView view : settingRightArr){
            view.setSelected(false);
        }
        int right = PreferencesUtils.getInt(getApplicationContext(), Preferences.SETTING_RIGHT);
        switch (right){
            case 20:
                index = 0;
                break;
            case 40:
                index = 1;
                break;
            case 60:
                index = 2;
                break;
            case 120:
                index = 3;
                break;
        }
        settingRightArr.get(index).setSelected(true);

        settingmenu.setVisibility(View.VISIBLE);
    }

    // 【长度设置初始化】
    private void initSettingLength(){
        for(TextView view : settingLengthView){
            view.setSelected(false);
        }
        int index = PreferencesUtils.getInt(getApplicationContext(), Preferences.SETTING_LENGTH, 2) -1;
        settingLengthView.get(index).setSelected(true);

        settinglengthmenu.setVisibility(View.VISIBLE);
    }

    // 【参数设置】 左
    @OnClick({R.id.lmenu1, R.id.lmenu2, R.id.lmenu3, R.id.lmenu4})
    public void onLeftSettingClicked(View view) {
        for(TextView tv : settingLeftArr){
            tv.setSelected(false);
        }

        view.setSelected(true);
    }

    // 【参数设置】 右
    @OnClick({R.id.rmenu1, R.id.rmenu2, R.id.rmenu3, R.id.rmenu4})
    public void onRightSettingClicked(View view) {
        for(TextView tv : settingRightArr){
            tv.setSelected(false);
        }

        view.setSelected(true);
    }

    // 【长度设置】
    @OnClick({R.id.llmenu1, R.id.llmenu2, R.id.llmenu3, R.id.llmenu4, R.id.rlmenu1, R.id.rlmenu2, R.id.rlmenu3, R.id.rlmenu4})
    public void onSettingLegthRightViewClicked(View view) {
        for(TextView lv : settingLengthView){
            lv.setSelected(false);
        }

        view.setSelected(true);
    }

    // 【参数设置】 确认
    private void settingSure(){
        for(TextView view : settingLeftArr){
            if(view.isSelected()){
                PreferencesUtils.putInt(getApplicationContext(), Preferences.SETTING_LEFT, ConvertUtils.toInt(view.getTag()));
            }
        }
        // 右
        for(TextView view : settingRightArr){
            if(view.isSelected()){
                PreferencesUtils.putInt(getApplicationContext(), Preferences.SETTING_RIGHT, ConvertUtils.toInt(view.getTag()));
            }
        }
    }

    // 【长度设置】 确认
    private void settingLengthSure(){
        for(TextView view : settingLengthView){
            if(view.isSelected()){
                PreferencesUtils.putInt(getApplicationContext(), Preferences.SETTING_LENGTH, ConvertUtils.toInt(view.getTag()));
            }
        }
    }
}
