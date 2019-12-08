package com.android.fitz.fastreading.ui.activity.train.game1;

import android.os.Bundle;
import android.widget.TextView;

import com.android.fitz.fastreading.R;
import com.android.fitz.fastreading.base.BaseActivity;
import com.android.fitz.fastreading.constant.Constant;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ExtendTrainAnswerResultActivity extends BaseActivity {

    @BindView(R.id.result)
    TextView result;

    @Override
    protected void initView() {
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            // 正确答案
            int answer = bundle.getInt(Constant.ID);
            int ianswer = bundle.getInt(Constant.COMMON_KEY);
            if(answer == ianswer){
                result.setText(String.format("恭喜你答对了!答案是%s次", answer));
            }else{
                result.setText(String.format("很遗憾!你的回答错误。答案是%s次", answer));
            }
        }
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_extend_train_answer_result;
    }

    @Override
    protected void initInjector() {

    }

    @OnClick(R.id.back)
    public void onViewClicked() {
        finish();
    }
}
