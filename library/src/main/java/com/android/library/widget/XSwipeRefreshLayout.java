package com.android.library.widget;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;

/**
 * Author:  ljo_h
 * Date:    2016/9/28
 * Description:
 */
public class XSwipeRefreshLayout extends SwipeRefreshLayout {
    private Context context;

    public XSwipeRefreshLayout(Context context) {
        this(context, null);
    }

    public XSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
    }


    public void refresh(final boolean isRefresh){
        this.post(new Runnable() {
            @Override
            public void run() {
                setRefreshing(isRefresh);
            }
        });
    }

}
