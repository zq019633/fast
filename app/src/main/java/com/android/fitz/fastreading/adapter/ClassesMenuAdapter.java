package com.android.fitz.fastreading.adapter;

import com.android.fitz.fastreading.R;
import com.android.fitz.fastreading.bean.SelectItem;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by fitz on 2017/8/20.
 * title:  记录查询的适配器
 * createTime:2016/8/20
 */
public class ClassesMenuAdapter extends BaseQuickAdapter<SelectItem, BaseViewHolder> {

    public ClassesMenuAdapter(int layoutResId, List<SelectItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, SelectItem item) {
        holder.setText(R.id.name, item.getName());
        holder.getView(R.id.name).setSelected(item.isSelect());
    }
}
