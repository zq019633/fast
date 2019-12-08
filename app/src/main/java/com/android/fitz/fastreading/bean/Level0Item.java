package com.android.fitz.fastreading.bean;

import android.graphics.drawable.Drawable;

import com.android.fitz.fastreading.adapter.ExtendTrainAdapter;
import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by luoxw on 2016/8/10.
 */
public class Level0Item extends AbstractExpandableItem<Level1Item> implements MultiItemEntity {
    public String title;
    public String subTitle;
    public int drawable;

    public Level0Item( String title, String subTitle, int drawable) {
        this.subTitle = subTitle;
        this.title = title;
        this.drawable = drawable;
    }

    @Override
    public int getItemType() {
        return ExtendTrainAdapter.TYPE_LEVEL_0;
    }

    @Override
    public int getLevel() {
        return 0;
    }
}
