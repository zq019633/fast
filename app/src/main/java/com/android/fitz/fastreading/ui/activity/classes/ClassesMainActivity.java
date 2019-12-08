package com.android.fitz.fastreading.ui.activity.classes;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.android.fitz.fastreading.R;
import com.android.fitz.fastreading.adapter.ClassesBookAdapter;
import com.android.fitz.fastreading.adapter.ClassesMenuAdapter;
import com.android.fitz.fastreading.base.AppApplication;
import com.android.fitz.fastreading.base.BaseActivity;
import com.android.fitz.fastreading.bean.SelectItem;
import com.android.fitz.fastreading.constant.Constant;
import com.android.fitz.fastreading.constant.Preferences;
import com.android.fitz.fastreading.db.entity.Article;
import com.android.fitz.fastreading.db.greendao.ArticleDao;
import com.android.fitz.fastreading.db.greendao.DaoSession;
import com.android.library.util.PreferencesUtils;
import com.android.library.widget.XRecyclerView;
import com.android.library.widget.XSwipeRefreshLayout;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class ClassesMainActivity extends BaseActivity {

    @BindView(R.id.recyclerview)
    XRecyclerView mRecyclerView;
    @BindView(R.id.bookrecyclerview)
    XRecyclerView mBookRecyclerView;
    @BindView(R.id.swipeLayout)
    XSwipeRefreshLayout mSwipeRefreshLayout;

    ClassesMenuAdapter adapter;
    ClassesBookAdapter bAdapter;
    ArticleDao articleDao;

    @Override
    protected void initView() {
        mSwipeRefreshLayout.setEnabled(false);
        DaoSession daoSession = ((AppApplication) getApplication()).getDaoSession();
        articleDao = daoSession.getArticleDao();

        initMenuAdapter();
        initBookAdapter();

        getBookData(null);
        if(PreferencesUtils.getString(getApplicationContext(), Preferences.SPEED_TYPE, "").equals("")) {
            PreferencesUtils.putString(getApplicationContext(), Preferences.SPEED_TYPE, "01");
        }
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_classes_main;
    }

    @Override
    protected void initInjector() {

    }

    private void initMenuAdapter() {
        //mSwipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ClassesMenuAdapter(R.layout.classes_menu_list_item, getMenuData());
        adapter.openLoadAnimation();
        adapter.setOnItemClickListener(clickListener);
        adapter.setEnableLoadMore(false);
        mRecyclerView.setAdapter(adapter);
    }

    // 左侧菜单
    BaseQuickAdapter.OnItemClickListener clickListener = new BaseQuickAdapter.OnItemClickListener() {

        @Override
        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            List<SelectItem> list = adapter.getData();
            SelectItem item = (SelectItem) list.get(position);

            for(SelectItem citem : list){
                if(!item.getId().equals(citem.getId())){
                    citem.setSelect(false);
                }
            }
            item.setSelect(!item.isSelect());
            PreferencesUtils.putString(getApplicationContext(), Preferences.SPEED_TYPE, item.getId());

            adapter.notifyDataSetChanged();
            //adapter.setNewData(list);
            //getBookData(item);
        }
    };

    private void initBookAdapter(){
        mBookRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        bAdapter = new ClassesBookAdapter(R.layout.classes_book_list_item, new ArrayList<List<Article>>());
        bAdapter.openLoadAnimation();
        bAdapter.setOnItemChildClickListener(childClickListener);
        bAdapter.setEnableLoadMore(false);
        mBookRecyclerView.setAdapter(bAdapter);
    }

    // 书籍item
    BaseQuickAdapter.OnItemChildClickListener childClickListener = new BaseQuickAdapter.OnItemChildClickListener() {
        @Override
        public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
            List<Article> list = (List<Article>)adapter.getItem(position);
            Article article = null;
            switch (view.getId()){
                case R.id.img1:
                    article = list.get(0);
                    break;
                case R.id.img2:
                    article = list.get(1);
                    break;
                case R.id.img3:
                    article = list.get(2);
                    break;
                case R.id.img4:
                    article = list.get(3);
                    break;
            }
            Bundle bundle = new Bundle();
            bundle.putString(Constant.COMMON_KEY, article.getId());
            readyGo(ClassesReadingActivity.class, bundle);
        }
    };

    @OnClick(R.id.back)
    public void onViewClicked() {
        finish();
    }

    // 查询所有
    public void getBookData(SelectItem item) {
        rx.Observable observable = null;
        if(item == null || !item.isSelect()){
            observable = articleDao
                    .rx()
                    .loadAll();
        }else{
            observable = articleDao
                    .queryBuilder()
                    //.where(ArticleDao.Properties.Speed.eq(item.getId()))
                    .rx()
                    .list();
        }
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Article>>() {
                    @Override
                    public void call(List<Article> articlesList) {
//                        for(int i =0; i <6; i ++){
//                            articlesList.add(articlesList.get(0));
//                        }

                        int count = articlesList.size();
                        int j = 4 - (count % 4);
                        for(int i = 0; i <j ; i++){
                            articlesList.add(new Article());
                        }

                        List<List<Article>> mList = new ArrayList<>();
                        List<Article> list = new ArrayList<Article>();
                        for (int i = 0; i < articlesList.size(); i++) {
                            Article item = articlesList.get(i);
                            if(i % 4 == 0){
                                list = new ArrayList<Article>();
                            }
                            if(item.getId() != null){
                                list.add(item);
                            }
                            if(i % 4 == 0){
                                if(list.size() > 0) {
                                    mList.add(list);
                                }
                            }
                        }
                        bAdapter.setNewData(mList);
                    }
                });
    }

    /**
     * 获取菜单数据
     * @return
     */
    public List<SelectItem> getMenuData() {
        List<SelectItem> list = new ArrayList<>();
        String type = PreferencesUtils.getString(getApplicationContext(), Preferences.SPEED_TYPE, "01");

        list.add(new SelectItem("01", "1000字以下/分钟", type.equals("01")));
        list.add(new SelectItem("02", "1000-2000/分钟", type.equals("02")));
        list.add(new SelectItem("03", "2000-3000/分钟", type.equals("03")));
        list.add(new SelectItem("04", "3000-4000/分钟", type.equals("04")));
        list.add(new SelectItem("05", "4000-5000/分钟", type.equals("05")));
        list.add(new SelectItem("06", "5000-6500/分钟", type.equals("06")));
        list.add(new SelectItem("07", "6500-8000/分钟", type.equals("07")));
        list.add(new SelectItem("08", "8000-9500/分钟", type.equals("08")));
        list.add(new SelectItem("09", "9500-11000/分钟", type.equals("09")));
        list.add(new SelectItem("10", "11000-12500/分钟", type.equals("10")));
        list.add(new SelectItem("11", "12500-14000/分钟", type.equals("11")));
        list.add(new SelectItem("12", "14000-15000/分钟", type.equals("12")));
        list.add(new SelectItem("13", "15000-16000/分钟", type.equals("13")));
        list.add(new SelectItem("14", "16000-17000/分钟", type.equals("14")));

        return list;
    }


}
