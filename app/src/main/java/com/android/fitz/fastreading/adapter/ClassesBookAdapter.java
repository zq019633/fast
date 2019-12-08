package com.android.fitz.fastreading.adapter;

import android.view.View;
import android.widget.ImageView;

import com.android.fitz.fastreading.R;
import com.android.fitz.fastreading.bean.BookEntity;
import com.android.fitz.fastreading.bean.SelectItem;
import com.android.fitz.fastreading.db.entity.Article;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by fitz on 2017/07/09.
 */

public class ClassesBookAdapter extends BaseQuickAdapter<List<Article>, BaseViewHolder> {

    public ClassesBookAdapter(int layoutResId, List<List<Article>> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, List<Article> list) {
        ImageView img1 = holder.getView(R.id.img1);
        ImageView img2 = holder.getView(R.id.img2);
        ImageView img3 = holder.getView(R.id.img3);
        ImageView img4 = holder.getView(R.id.img4);

        img1.setVisibility(View.INVISIBLE);
        img2.setVisibility(View.INVISIBLE);
        img3.setVisibility(View.INVISIBLE);
        img4.setVisibility(View.INVISIBLE);

        if(list.size() > 0){
            holder.addOnClickListener(R.id.img1);
            img1.setVisibility(View.VISIBLE);
            loadImage(list.get(0).getPhoto(), img1);
        }
        if(list.size() > 1){
            holder.addOnClickListener(R.id.img2);
            img2.setVisibility(View.VISIBLE);
            loadImage(list.get(1).getPhoto(), img2);
        }
        if(list.size() > 2){
            holder.addOnClickListener(R.id.img3);
            img3.setVisibility(View.VISIBLE);
            loadImage(list.get(2).getPhoto(), img3);
        }
        if(list.size() > 3){
            holder.addOnClickListener(R.id.img4);
            img4.setVisibility(View.VISIBLE);
            loadImage(list.get(3).getPhoto(), img4);
        }
    }

    private void loadImage(String url, ImageView img){
        Glide.with(mContext)
                .load(url)
                .into(img);
    }

}
