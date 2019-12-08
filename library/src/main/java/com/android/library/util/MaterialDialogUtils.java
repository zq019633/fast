package com.android.library.util;

import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.library.R;

/**
 * Created by fitz on 2017/1/4.
 */

public class MaterialDialogUtils {

    public static MaterialDialog.Builder Builder(FragmentActivity context, String content, @Nullable String title){
        MaterialDialog.Builder builder = new MaterialDialog.Builder(context);
        builder.content(content)
                .positiveColorRes(R.color.alpha_75_blue)
                .negativeColorRes(R.color.color_333333)
                .positiveText(R.string.sure)
                .negativeText(R.string.cancel);
        if(!StringUtils.isEmpty(title)){
            builder.title(title);
        }
        return builder;
    }

    public static MaterialDialog.Builder BuilderSingle(FragmentActivity context, String content, @Nullable String title){
        MaterialDialog.Builder builder = new MaterialDialog.Builder(context);
        builder.content(content)
                .negativeColorRes(R.color.color_333333)
                .positiveText(R.string.sure);
        if(!StringUtils.isEmpty(title)){
            builder.title(title);
        }
        return builder;
    }
}
