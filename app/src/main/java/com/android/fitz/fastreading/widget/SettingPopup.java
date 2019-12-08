package com.android.fitz.fastreading.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.fitz.fastreading.R;
import com.android.fitz.fastreading.utils.CommonUtils;
import com.android.fitz.fastreading.widget.page.util.CommonUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangyang on 2017/7/26.
 */

public class SettingPopup {
    private PopupWindow mPopupWindow;
    private OnDismissListener mOnDismissListener;
    private View root;
    private OnConfirmListener mOnConfirmListener;
    private int mIndex = 1;
    private Context mContext;
    private String mSpeed;
    private List<String> speedList;

    private TextView textView1,textView2,textView3,textView4,textView5,textView6,textView7,textView8,textView9;
    private List<TextView> textViews = new ArrayList<>();

    public void create(Context context,int width, int height, @Nullable int drawableId, int layoutId,String speed, String type) {
        this.mContext = context;
        this.mSpeed = speed;
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
                if (mOnConfirmListener != null) mOnConfirmListener.onConfirm(mIndex);
            }
        });

        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (mOnDismissListener != null) mOnDismissListener.onDismiss();
            }
        });
        speedList = new ArrayList<>();
        speedList.addAll(CommonUtils.getSpeedList(type));

        textView1 = (TextView) root.findViewById(R.id.text1);
        textView2 = (TextView) root.findViewById(R.id.text2);
        textView3 = (TextView) root.findViewById(R.id.text3);
        textView4 = (TextView) root.findViewById(R.id.text4);
        textView5 = (TextView) root.findViewById(R.id.text5);
        textView6 = (TextView) root.findViewById(R.id.text6);
        textView7 = (TextView) root.findViewById(R.id.text7);
        textView8 = (TextView) root.findViewById(R.id.text8);
        textView9 = (TextView) root.findViewById(R.id.text9);
        textViews.add(textView1);
        textViews.add(textView2);
        textViews.add(textView3);
        textViews.add(textView4);
        textViews.add(textView5);
        textViews.add(textView6);
        textViews.add(textView7);
        textViews.add(textView8);
        textViews.add(textView9);
        for (TextView textView : textViews){
            textView.setTextColor(ContextCompat.getColor(mContext,R.color.white));
            textView.setVisibility(View.GONE);
        }
        for(int i=0; i<speedList.size(); i++){
            TextView tv =textViews.get(i);
            tv.setText(speedList.get(i) + "字/分");
            tv.setTag(speedList.get(i));

            if(mSpeed.equals(speedList.get(i))){
                tv.setTextColor(ContextCompat.getColor(mContext,R.color.dp_blue));
            }
            tv.setOnClickListener(listener);
            tv.setVisibility(View.VISIBLE);
        }
    }

    public View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            for (TextView textView : textViews){
                textView.setTextColor(ContextCompat.getColor(mContext,R.color.white));
            }
            switch (v.getId()){
                case R.id.text1:
                    mIndex = 1;
                    textView1.setTextColor(ContextCompat.getColor(mContext,R.color.dp_blue));
                    break;
                case R.id.text2:
                    mIndex = 2;
                    textView2.setTextColor(ContextCompat.getColor(mContext,R.color.dp_blue));
                    break;
                case R.id.text3:
                    mIndex = 3;
                    textView3.setTextColor(ContextCompat.getColor(mContext,R.color.dp_blue));
                    break;
                case R.id.text4:
                    mIndex = 4;
                    textView4.setTextColor(ContextCompat.getColor(mContext,R.color.dp_blue));
                    break;
                case R.id.text5:
                    mIndex = 5;
                    textView5.setTextColor(ContextCompat.getColor(mContext,R.color.dp_blue));
                    break;
                case R.id.text6:
                    mIndex = 6;
                    textView6.setTextColor(ContextCompat.getColor(mContext,R.color.dp_blue));
                    break;
                case R.id.text7:
                    mIndex = 7;
                    textView7.setTextColor(ContextCompat.getColor(mContext,R.color.dp_blue));
                    break;
                case R.id.text8:
                    mIndex = 8;
                    textView8.setTextColor(ContextCompat.getColor(mContext,R.color.dp_blue));
                    break;
                case R.id.text9:
                    mIndex = 9;
                    textView9.setTextColor(ContextCompat.getColor(mContext,R.color.dp_blue));
                    break;
            }
        }
    };

    public void showAtLocation(View parent,int gravity,int x ,int y){
        mPopupWindow.showAtLocation(parent,gravity,x,y);
    }

    public interface OnDismissListener{
        void onDismiss();
    }

    public interface OnConfirmListener{
        void onConfirm(int index);
    }

    public void dismiss(){
        if (mPopupWindow != null) mPopupWindow.dismiss();
    }

    public void setOnConfirmListener(OnConfirmListener onConfirmListener){
        this.mOnConfirmListener = onConfirmListener;
    }

    public void setOnDismissListener(OnDismissListener onDismissListener){
        this.mOnDismissListener = onDismissListener;
    }
}
