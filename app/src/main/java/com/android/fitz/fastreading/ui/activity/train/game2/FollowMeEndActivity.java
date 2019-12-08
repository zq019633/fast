package com.android.fitz.fastreading.ui.activity.train.game2;

import android.os.Bundle;

import com.android.fitz.fastreading.R;
import com.android.fitz.fastreading.base.BaseActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class FollowMeEndActivity extends BaseActivity {

    @Override
    protected void initView() {

    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_follow_me_end;
    }

    @Override
    protected void initInjector() {

    }

    @OnClick(R.id.back)
    public void onViewClicked() {
        finish();
    }
}
