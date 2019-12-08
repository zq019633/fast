package com.android.library.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.android.library.R;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

/**
 * Author:  ljo_h
 * Date:    2016/9/28
 * Description:
 */
public class XRecyclerView extends RecyclerView{
    private Context context;

    public XRecyclerView(Context context) {
        this(context, null);
    }

    public XRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;

        init(attrs);
    }

    private void init(AttributeSet attrs){
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.XRecyclerView);
        boolean isdriver = array.getBoolean(R.styleable.XRecyclerView_isdivider, false);
        int height = array.getDimensionPixelOffset(R.styleable.XRecyclerView_divider_height, 1);
        int background = array.getColor(R.styleable.XRecyclerView_divider_background, 1);
        int marginleft = array.getDimensionPixelOffset(R.styleable.XRecyclerView_divider_mrginleft, 0);
        int marginright = array.getDimensionPixelOffset(R.styleable.XRecyclerView_divider_mrginright, 0);

        if(isdriver){
            /*this.addItemDecoration(new DividerItemDecoration(context,
                    DividerItemDecoration.VERTICAL_LIST));*/
            this.addItemDecoration(new HorizontalDividerItemDecoration.Builder(context)
                    //.color(Color.RED)
                    .color(background)
                    .size(height)
                    //.sizeResId(R.dimen.recyclerview_driver)
                    //.marginResId(R.dimen.leftmargin, R.dimen.rightmargin)
                    .build());
        }
    }

}
