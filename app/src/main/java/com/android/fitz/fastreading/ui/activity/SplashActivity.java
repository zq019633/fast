package com.android.fitz.fastreading.ui.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.fitz.fastreading.R;
import com.android.fitz.fastreading.base.BaseActivity;
import com.android.fitz.fastreading.constant.Preferences;
import com.android.fitz.fastreading.presenter.impl.SplashPresenterImpl;
import com.android.fitz.fastreading.view.ISplashView;
import com.android.library.util.AppUtils;
import com.android.library.util.PreferencesUtils;
import com.android.library.util.StringUtils;
import com.meituan.android.walle.WalleChannelReader;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.UpgradeInfo;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class SplashActivity extends BaseActivity<SplashPresenterImpl> implements ISplashView{

    @BindView(R.id.code)
    EditText code;
    @BindView(R.id.sure)
    TextView sure;
    @BindView(R.id.container)
    LinearLayout container;

    @Override
    protected void initView() {
//        String codeKey = PreferencesUtils.getString(getApplicationContext(), Preferences.GRANT_CODE_KEY);
//        if(TextUtils.isEmpty(codeKey)){
//            container.setVisibility(View.VISIBLE);
//        }else{
            readyGoThenKill(MainActivity.class);
//        }
//        String channel = WalleChannelReader.getChannel(this.getApplicationContext());
//        sure.setText(channel);
        //readyGoThenKill(MainActivity.class);
//        loadUpgradeInfo();
    }

    private void loadUpgradeInfo() {

        /***** 获取升级信息 *****/
        UpgradeInfo upgradeInfo = Beta.getUpgradeInfo();

        if (upgradeInfo == null) {
            //sure.setText("无升级信息");
            //return;
        }

    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initInjector() {
        getActivityComponent().inject(this);
    }

    @Override
    public void onTaskFinish() {
        readyGoThenKill(MainActivity.class);
    }

    @OnClick(R.id.sure)
    public void onViewClicked() {
        String strCode = code.getText().toString();
        mPresenter.checkCode(AppUtils.getIMEI(getApplicationContext()), strCode);
    }

    @OnTextChanged(value = R.id.code, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void afterTextChanged(Editable s) {
        sure.setEnabled(!TextUtils.isEmpty(s.toString()));
    }
}
