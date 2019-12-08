package com.android.library.util;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ImageSpan;
import android.text.style.TextAppearanceSpan;

public class SpannableUtils {
	private static SpannableUtils spannableUtils = null;

	public static SpannableUtils getInstance() {
		if (spannableUtils == null) {
			spannableUtils = new SpannableUtils();
		}
		return spannableUtils;
	}
	
	/**
	 * 设置文字大小
	 * @param str
	 * @param size
	 * @param start
	 * @param end
	 * @return
	 */
	public SpannableString setSize(String str, int size, int start, int end){
		SpannableString sp = new SpannableString(str);
		sp.setSpan(new AbsoluteSizeSpan(size, true), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return sp;
	}
	
	/**
	 * 设置文字大小
	 * @param sp
	 * @param size
	 * @param start
	 * @param end
	 * @return
	 */
	public SpannableString setSize(SpannableString sp, int size, int start, int end){
		sp.setSpan(new AbsoluteSizeSpan(size, true), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return sp;
	}
	
	/**
	 * 设置文字颜色
	 * @param sp
	 * @param size
	 * @param start
	 * @param end
	 * @return
	 */
	public SpannableString setColor(Context context,SpannableString sp,int style, int start, int end){
		sp.setSpan(new TextAppearanceSpan(context, style), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return sp;
	}
	
	/**
	 * 设置文字颜色
	 * @param context
	 * @param str
	 * @param style
	 * @param start
	 * @param end
	 * @return
	 */
	public SpannableString setColor(Context context,String str,int style, int start, int end){
		SpannableString sp = new SpannableString(str);
		sp.setSpan(new TextAppearanceSpan(context, style), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return sp;
	}
	
	/**
	 * 设置Textcolor
	 * @param mContext
	 * @param drawable
	 */
	public ColorStateList setTextcolor(Context mContext, int drawable){
		XmlPullParser xrp = mContext.getResources().getXml(drawable);
		ColorStateList csl = null;
		try {
			csl = ColorStateList.createFromXml(mContext.getResources(), xrp);
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return csl;
	}
	
	// 图文混排
	public SpannableString setDrawable(SpannableString sp, Drawable drawable, int start, int end){
		//Drawable drawable = mContext.getResources().getDrawable(R.drawable.icon);   
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        sp.setSpan(new ImageSpan(drawable), start, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        
        return sp;
	}
}
