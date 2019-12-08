package com.android.fitz.fastreading.bean;

import com.android.fitz.fastreading.adapter.ExtendTrainAdapter;
import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by luoxw on 2016/8/10.
 */

public class Level1Item implements MultiItemEntity{
    private String title;
    private String subTitle;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }

    private int drawable;

    public Level1Item(String title, String subTitle, int drawable) {
        this.subTitle = subTitle;
        this.title = title;
        this.drawable = drawable;
    }

    @Override
    public int getItemType() {
        return ExtendTrainAdapter.TYPE_LEVEL_1;
    }
}