package com.android.library.util;

import android.content.Context;

import com.github.johnpersano.supertoasts.SuperToast;

/**
 * Author:  Jact
 * Date:    2016/2/13.
 * Description:
 */
public class ToastUtils {

    /**
     * Long Toast
     * @param context
     */
    public static void LongToast(Context context, String msg){
        SuperToast superToast = new SuperToast(context);
        superToast.setText(msg);
        superToast.setDuration(SuperToast.Duration.LONG);
        superToast.show();
    }

    /**
     * Long Toast
     * @param context
     */
    public static void ShortToast(Context context, String msg){
        SuperToast superToast = new SuperToast(context);
        superToast.setText(msg);
        superToast.setDuration(SuperToast.Duration.SHORT);
        superToast.show();
    }

}
