package com.android.fitz.fastreading.widget;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.fitz.fastreading.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/31.
 */

public class OtherSettingPopup {
    private PopupWindow mPopupWindow;
    private OnDismissListener mOnDismissListener;
    private View root;
    private OnConfirmListener mOnConfirmListener;
    private int mBackground = 1;
    private int mFont = 1;
    private int mTextColor = 1;
    private Context mContext;

    private TextView textView1, textView2, textView3, textView4, textView5, textView6, textView7, textView8, textView9, textView10, textView11, textView12;
    private List<TextView> mBackgroundTexts = new ArrayList<>();
    private List<TextView> mFontTexts = new ArrayList<>();
    private List<TextView> mColorTexts = new ArrayList<>();

    public void create(Context context, int width, int height, @Nullable int drawableId, int layoutId,
                       int background, int font, int textColor) {
        this.mContext = context;
        mBackground = background;
        mFont = font;
        mTextColor = textColor;
        root = LayoutInflater.from(context).inflate(layoutId, null);
        mPopupWindow = new PopupWindow(root, width, height, true);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setOutsideTouchable(true);
        // 实例化一个ColorDrawable颜色为半透明
//        ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置弹出窗体的背景
        if (drawableId != 0) {
            mPopupWindow.setBackgroundDrawable(ContextCompat.getDrawable(context, drawableId));
        } else {
            mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        }

        ImageView confirm = (ImageView) root.findViewById(R.id.text_confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnConfirmListener != null)
                    mOnConfirmListener.onConfirm(mBackground, mFont, mTextColor);
            }
        });

        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (mOnDismissListener != null) mOnDismissListener.onDismiss();
            }
        });

        textView1 = (TextView) root.findViewById(R.id.text1);
        textView2 = (TextView) root.findViewById(R.id.text2);
        textView3 = (TextView) root.findViewById(R.id.text3);
        textView4 = (TextView) root.findViewById(R.id.text4);
        textView5 = (TextView) root.findViewById(R.id.text5);
        textView6 = (TextView) root.findViewById(R.id.text6);
        textView7 = (TextView) root.findViewById(R.id.text7);
        textView8 = (TextView) root.findViewById(R.id.text8);
        textView9 = (TextView) root.findViewById(R.id.text9);
        textView10 = (TextView) root.findViewById(R.id.text10);
        textView11 = (TextView) root.findViewById(R.id.text11);
        textView12 = (TextView) root.findViewById(R.id.text12);

        mBackgroundTexts.add(textView1);
        mBackgroundTexts.add(textView2);
        mBackgroundTexts.add(textView3);
        mBackgroundTexts.add(textView4);
        mFontTexts.add(textView5);
        mFontTexts.add(textView6);
        mFontTexts.add(textView7);
        mFontTexts.add(textView8);
        mColorTexts.add(textView9);
        mColorTexts.add(textView10);
        mColorTexts.add(textView11);
        mColorTexts.add(textView12);

        for (int i = 0; i < mBackgroundTexts.size(); i++){
            mBackgroundTexts.get(i).setTextColor(ContextCompat.getColor(mContext, R.color.white));
            mFontTexts.get(i).setTextColor(ContextCompat.getColor(mContext, R.color.white));
            mColorTexts.get(i).setTextColor(ContextCompat.getColor(mContext, R.color.white));
            if (i + 1 == mBackground){
                mBackgroundTexts.get(i).setTextColor(ContextCompat.getColor(mContext, R.color.dp_blue));
            }
            if (i + 1 == mFont){
                mFontTexts.get(i).setTextColor(ContextCompat.getColor(mContext, R.color.dp_blue));
            }
            if (i + 1 == mTextColor){
                mColorTexts.get(i).setTextColor(ContextCompat.getColor(mContext, R.color.dp_blue));
            }
        }

        textView1.setOnClickListener(listener);
        textView2.setOnClickListener(listener);
        textView3.setOnClickListener(listener);
        textView4.setOnClickListener(listener);
        textView5.setOnClickListener(listener);
        textView6.setOnClickListener(listener);
        textView7.setOnClickListener(listener);
        textView8.setOnClickListener(listener);
        textView9.setOnClickListener(listener);
        textView10.setOnClickListener(listener);
        textView11.setOnClickListener(listener);
        textView12.setOnClickListener(listener);
    }

    public View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.text1:
                    mBackground = 1;
                    textView1.setTextColor(ContextCompat.getColor(mContext, R.color.dp_blue));
                    textView2.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                    textView3.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                    textView4.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                    break;
                case R.id.text2:
                    mBackground = 2;
                    textView1.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                    textView2.setTextColor(ContextCompat.getColor(mContext, R.color.dp_blue));
                    textView3.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                    textView4.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                    break;
                case R.id.text3:
                    mBackground = 3;
                    textView1.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                    textView2.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                    textView3.setTextColor(ContextCompat.getColor(mContext, R.color.dp_blue));
                    textView4.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                    break;
                case R.id.text4:
                    mBackground = 4;
                    textView1.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                    textView2.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                    textView3.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                    textView4.setTextColor(ContextCompat.getColor(mContext, R.color.dp_blue));
                    break;
                case R.id.text5:
                    mFont = 1;
                    textView5.setTextColor(ContextCompat.getColor(mContext, R.color.dp_blue));
                    textView6.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                    textView7.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                    textView8.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                    break;
                case R.id.text6:
                    mFont = 2;
                    textView5.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                    textView6.setTextColor(ContextCompat.getColor(mContext, R.color.dp_blue));
                    textView7.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                    textView8.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                    break;
                case R.id.text7:
                    mFont = 3;
                    textView5.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                    textView6.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                    textView7.setTextColor(ContextCompat.getColor(mContext, R.color.dp_blue));
                    textView8.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                    break;
                case R.id.text8:
                    mFont = 4;
                    textView5.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                    textView6.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                    textView7.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                    textView8.setTextColor(ContextCompat.getColor(mContext, R.color.dp_blue));
                    break;
                case R.id.text9:
                    mTextColor = 1;
                    textView9.setTextColor(ContextCompat.getColor(mContext, R.color.dp_blue));
                    textView10.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                    textView11.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                    textView12.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                    break;
                case R.id.text10:
                    mTextColor = 2;
                    textView9.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                    textView10.setTextColor(ContextCompat.getColor(mContext, R.color.dp_blue));
                    textView11.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                    textView12.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                    break;
                case R.id.text11:
                    mTextColor = 3;
                    textView9.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                    textView10.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                    textView11.setTextColor(ContextCompat.getColor(mContext, R.color.dp_blue));
                    textView12.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                    break;
                case R.id.text12:
                    mTextColor = 4;
                    textView9.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                    textView10.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                    textView11.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                    textView12.setTextColor(ContextCompat.getColor(mContext, R.color.dp_blue));
                    break;
            }
        }
    };

    public void showAtLocation(View parent, int gravity, int x, int y) {
        mPopupWindow.showAtLocation(parent, gravity, x, y);
    }

    public interface OnDismissListener {
        void onDismiss();
    }

    public interface OnConfirmListener {
        void onConfirm(int background, int font, int textColor);
    }

    public void dismiss() {
        if (mPopupWindow != null) mPopupWindow.dismiss();
    }

    public void setOnConfirmListener(OnConfirmListener onConfirmListener) {
        this.mOnConfirmListener = onConfirmListener;
    }

    public void setOnDismissListener(OnDismissListener onDismissListener) {
        this.mOnDismissListener = onDismissListener;
    }
}
