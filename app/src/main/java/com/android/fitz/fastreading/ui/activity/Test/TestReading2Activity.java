package com.android.fitz.fastreading.ui.activity.Test;

import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.fitz.fastreading.R;
import com.android.fitz.fastreading.base.BaseActivity;
import com.android.fitz.fastreading.widget.page.Config;
import com.android.fitz.fastreading.widget.page.PageFactory;
import com.android.fitz.fastreading.widget.page.PageWidget;
import com.android.fitz.fastreading.widget.page.db.BookList;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TestReading2Activity extends BaseActivity {

    @BindView(R.id.bookpage)
    PageWidget bookpage;

    private Config config;
    private WindowManager.LayoutParams lp;
    private BookList bookList;
    private PageFactory pageFactory;

    private Boolean isShow = false;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_test_reading2;
    }

    @Override
    protected void initInjector() {

    }


    @Override
    protected void initView() {
        config = Config.getInstance();
        pageFactory = PageFactory.getInstance();

        //bookList = (BookList) intent.getSerializableExtra(EXTRA_BOOK);
        bookpage.setPageMode(config.getPageMode());
        pageFactory.setPageWidget(bookpage);

        bookList = new BookList();
        bookList.setBegin(7196);
        bookList.setBookname("log2");
        bookList.setBookpath("/storage/emulated/0/Test/log2.txt");
        bookList.setId(1);
        try {
            pageFactory.openBook(bookList);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "打开电子书失败", Toast.LENGTH_SHORT).show();
        }

        initListener();
    }

    private void initListener(){
//        bookpage.setTouchListener(new PageWidget.TouchListener() {
//            @Override
//            public void center() {
//                if (isShow) {
//                    hideReadSetting();
//                } else {
//                    showReadSetting();
//                }
//            }
//
//            @Override
//            public Boolean prePage() {
//                if (isShow || isSpeaking){
//                    return false;
//                }
//
//                pageFactory.prePage();
//                if (pageFactory.isfirstPage()) {
//                    return false;
//                }
//
//                return true;
//            }
//
//            @Override
//            public Boolean nextPage() {
//                Log.e("setTouchListener", "nextPage");
//                if (isShow || isSpeaking){
//                    return false;
//                }
//
//                pageFactory.nextPage();
//                if (pageFactory.islastPage()) {
//                    return false;
//                }
//                return true;
//            }
//
//            @Override
//            public void cancel() {
//                pageFactory.cancelPage();
//            }
//        });
    }


}
