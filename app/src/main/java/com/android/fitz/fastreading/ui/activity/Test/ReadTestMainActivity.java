package com.android.fitz.fastreading.ui.activity.Test;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.android.fitz.fastreading.R;
import com.android.fitz.fastreading.adapter.ClassesMenuAdapter;
import com.android.fitz.fastreading.adapter.ReadTestBookAdapter;
import com.android.fitz.fastreading.base.AppApplication;
import com.android.fitz.fastreading.base.BaseActivity;
import com.android.fitz.fastreading.bean.BookEntity;
import com.android.fitz.fastreading.constant.Constant;
import com.android.fitz.fastreading.db.entity.Article;
import com.android.fitz.fastreading.db.entity.Test;
import com.android.fitz.fastreading.db.greendao.ArticleDao;
import com.android.fitz.fastreading.db.greendao.DaoSession;
import com.android.fitz.fastreading.db.greendao.TestDao;
import com.android.library.widget.XRecyclerView;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class ReadTestMainActivity extends BaseActivity {

    @BindView(R.id.recyclerview)
    XRecyclerView mRecyclerView;

    ReadTestBookAdapter adapter;
    TestDao testDao;

    @Override
    protected void initView() {
        initAdapter();

        DaoSession daoSession = ((AppApplication) getApplication()).getDaoSession();
        testDao = daoSession.getTestDao();
        getBookData();
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_read_test_main;
    }

    @Override
    protected void initInjector() {

    }

    private void initAdapter() {
        //mSwipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ReadTestBookAdapter(R.layout.classes_book_list_item, new ArrayList<List<Test>>());
        adapter.openLoadAnimation();
        adapter.setOnItemChildClickListener(childClickListener);
        adapter.setEnableLoadMore(false);
        mRecyclerView.setAdapter(adapter);
    }

    BaseQuickAdapter.OnItemChildClickListener childClickListener = new BaseQuickAdapter.OnItemChildClickListener() {
        @Override
        public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
            List<Test> list = (List<Test>)adapter.getItem(position);
            Test test = null;
            switch (view.getId()){
                case R.id.img1:
                    test = list.get(0);
                    break;
                case R.id.img2:
                    test = list.get(1);
                    break;
                case R.id.img3:
                    test = list.get(2);
                    break;
                case R.id.img4:
                    test = list.get(3);
                    break;
            }
            Bundle bundle = new Bundle();
            bundle.putString(Constant.COMMON_KEY, test.getId());
            //readyGo(TestReadingActivity.class, bundle);
            readyGo(TestReadingActivity.class, bundle);
        }
    };

    @OnClick(R.id.back)
    public void onViewClicked() {
        finish();
    }

    public void getBookData() {
        testDao
                .rx()
                .loadAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Test>>() {
                    @Override
                    public void call(List<Test> testList) {
//                        for(int i =0; i <16; i ++){
//                            articlesList.add(articlesList.get(0));
//                        }

                        int count = testList.size();
                        int j = 4 - (count % 4);
                        for(int i = 0; i <j ; i++){
                            testList.add(new Test());
                        }

                        List<List<Test>> mList = new ArrayList<>();
                        List<Test> list = new ArrayList<Test>();
                        for (int i = 0; i < testList.size(); i++) {
                            Test item = testList.get(i);
                            if(i % 4 == 0){
                                list = new ArrayList<Test>();
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
                        adapter.setNewData(mList);
                    }
                });
    }

}
