package com.android.fitz.fastreading.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.android.fitz.fastreading.R;
import com.android.fitz.fastreading.bean.Level0Item;
import com.android.fitz.fastreading.bean.Level1Item;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

/**
 * Created by luoxw on 2016/8/9.
 */
public class ExtendTrainAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {
    private static final String TAG = ExtendTrainAdapter.class.getSimpleName();

    public static final int TYPE_LEVEL_0 = 0;
    public static final int TYPE_LEVEL_1 = 1;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public ExtendTrainAdapter(List<MultiItemEntity> data) {
        super(data);

        addItemType(TYPE_LEVEL_0, R.layout.train_menu_list_group_item);
        addItemType(TYPE_LEVEL_1, R.layout.train_menu_list_child_item);
    }

    @Override
    protected void convert(final BaseViewHolder holder, final MultiItemEntity item) {
        switch (holder.getItemViewType()) {
            case TYPE_LEVEL_0:
                final Level0Item lv0 = (Level0Item)item;
                Glide.with(mContext)
                        .load(lv0.drawable)
                        //.load(R.drawable.ic_know_training)
                        .into((ImageView) holder.getView(R.id.image));
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = holder.getAdapterPosition();

                        if (lv0.isExpanded()) {
                            // 关闭
                            collapse(pos);
                        } else {
                            // 打开
                            expand(pos);
                        }
                    }
                });
                break;
            case TYPE_LEVEL_1:
                final Level1Item lv1 = (Level1Item)item;
                holder.setText(R.id.name, lv1.getTitle())
                .addOnClickListener(R.id.name);
                break;
        }
    }
}
