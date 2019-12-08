package com.android.fitz.fastreading.view;

import com.android.library.base.IView;

/**
 * Created by fitz on 2017/1/20.
 */

public interface IUpdateView extends IView{
    void downLoad(String url, long updateTime);
}
