package com.android.fitz.fastreading.base;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.library.base.BaseAppCompatActivity;
import com.android.library.base.BasePresenter;
import com.android.library.base.IView;
import com.android.library.util.StringUtils;
import com.android.library.util.ToastUtils;
import com.android.library.util.netstatu.NetUtils;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.android.fitz.fastreading.ui.activity.MainActivity;
import com.android.fitz.fastreading.R;
import com.android.fitz.fastreading.constant.AppConfig;
import com.android.fitz.fastreading.constant.Constant;
import com.android.fitz.fastreading.injector.component.ActivityComponent;
import com.android.fitz.fastreading.injector.component.DaggerActivityComponent;
import com.android.fitz.fastreading.injector.module.ActivityModule;
import com.android.fitz.fastreading.widget.EmptyView;

import org.reactivestreams.Publisher;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.inject.Inject;

import butterknife.ButterKnife;
import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;


/**
 * Created by jact on 2016/2/12.
 */
public abstract class BaseActivity<T extends BasePresenter> extends BaseAppCompatActivity implements IView {
    protected MaterialDialog dialog;
    protected Toolbar mToolbar;
    protected EmptyView mEmptyView;
    private View notDataView;
    private View errorView;
    protected int page = 1;
    protected static final int PAGE_SIZE = 20;
    //private OnActionRefreshListener mOnActionRefreshListener;
    SwipeRefreshLayout.OnRefreshListener mOnRefreshListener;

    @Inject
    public T mPresenter;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        mToolbar = ButterKnife.findById(this, R.id.common_toolbar);
        if (null != mToolbar) {
            mToolbar.setTitle("");
            setSupportActionBar(mToolbar);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mEmptyView = ButterKnife.findById(this, R.id.empty);

        ButterKnife.bind(this);
    }

    @Override
    protected void attachView() {
        if (mPresenter != null)
            mPresenter.attachView(this);
    }

    protected ActivityComponent getActivityComponent() {
        Log.e("App", ((AppApplication) getApplication()).getApplicationComponent().getClass().getName());
        return DaggerActivityComponent.builder()
                .activityModule(new ActivityModule(this))
                .applicationComponent(((AppApplication) getApplication()).getApplicationComponent())
                .build();
    }

    /*@Override
    public <T> Observable.Transformer<T, T> applySchedulers() {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> observable) {
                return observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }*/

    @Override
    public <T> FlowableTransformer<T, T> composeSchedulers() {
        return new FlowableTransformer<T, T>() {
            @Override
            public Publisher<T> apply(Flowable<T> upstream) {
                return upstream.subscribeOn(io.reactivex.schedulers.Schedulers.io())
                        .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread());
            }
        };
    }

    public <T> LifecycleTransformer<T> composeLifeCycle() {
        return BaseActivity.this.bindUntilEvent(ActivityEvent.DESTROY);
    }

    @Override
    protected boolean isBindEventBus() {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onNetworkConnected(NetUtils.NetType type) {

    }

    @Override
    protected void onNetworkDisConnected() {

    }

    @Override
    protected boolean toggleOverridePendingTransition() {
        return true;
    }

    @Override
    protected TransitionMode getOverridePendingTransitionMode() {
        return TransitionMode.FADE;
    }

    @Override
    public void showNetError() {
        if (mEmptyView != null) {
            mEmptyView.showError("");
        }
    }

    @Override
    public void showLoading() {
        if(mEmptyView != null) {
            mEmptyView.showLoading();
        }
    }

    @Override
    public void hide() {
        if(mEmptyView != null) {
            mEmptyView.hide();
        }
    }

    @Override
    public void showMsg(String msg) {
       ToastUtils.ShortToast(getApplication(), msg);
    }

    @Override
    public void showException(Throwable ex) {
        //toggleShowError(true, msg, null);
        try {
            if (ex instanceof Exception) {
                if (ex instanceof UnknownHostException || ex instanceof ConnectException) {
                    showMsg(mContext.getString(R.string.common_error_service));
                } else if(ex instanceof SocketTimeoutException){
                    showMsg(mContext.getString(R.string.common_error_service_noresponse));
                }
            }
        } catch (Exception e) {

        }
    }

    @Override
    public void showEmpty() {
        if(mEmptyView != null) {
            mEmptyView.showEmpty();
        }
    }

    @Override
    public void showEmpty(String msg) {
        if(mEmptyView != null) {
            mEmptyView.showEmpty();
        }
    }

    @Override
    public void showProgress() {
        showDialog(getString(R.string.loading_default_text));
    }

    @Override
    public void showDialog(String content) {
        if(dialog == null){
            dialog = new MaterialDialog.Builder(this)
                    .progress(true, 0)
                    .content(content)
                    .canceledOnTouchOutside(false)
                    .build();
        }
        dialog.show();
    }

    @Override
    public void hideProgress() {
        if(dialog != null){
            dialog.dismiss();
        }
    }

    @Override
    public void onComplete() {

    }

    protected View getNodata(ViewGroup viewGroup){
        if(notDataView == null){
            notDataView = getLayoutInflater().inflate(R.layout.view_empty_empty_frame, viewGroup, false);
        }
        return notDataView;
    }

    protected View getErrorView(ViewGroup viewGroup){
        if(errorView == null){
            errorView = getLayoutInflater().inflate(R.layout.view_empty_error_frame, viewGroup, false);
            errorView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mOnRefreshListener != null){
                        mOnRefreshListener.onRefresh();
                    }
                }
            });
        }
        return errorView;
    }

    public void setOnActionRefreshListener(SwipeRefreshLayout.OnRefreshListener onRefreshListener){
        this.mOnRefreshListener = onRefreshListener;
    }

    @Override
    public void onTaskFinish() {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onResutCode(String code, String msg) {
//        if(StringUtils.isEquals(code, AppConfig.CODE_TOKEN_OUT)) {
//            Bundle bundle = new Bundle();
//            bundle.putBoolean(Constant.SIGN_OUT, true);
//            readyGo(MainActivity.class, bundle);
//        }
        Bundle bundle = new Bundle();
        bundle.putBoolean(Constant.SIGN_OUT, true);
        readyGo(MainActivity.class, bundle);
        if(!StringUtils.isEmpty(msg)) {
            showMsg(msg);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(dialog != null && dialog.isShowing()){
            dialog.dismiss();
        }
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        /*RefWatcher refWatcher = ((AppApplication)getApplication()).getRefWatcher(this);
        refWatcher.watch(this);*/
    }
}
