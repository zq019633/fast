package com.android.fitz.fastreading.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.library.base.BasePresenter;
import com.android.library.base.IView;
import com.android.library.base.BaseAppCompatFragment;
import com.android.library.util.StringUtils;
import com.android.library.util.ToastUtils;
import com.android.library.util.netstatu.NetUtils;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.android.fitz.fastreading.ui.activity.MainActivity;
import com.android.fitz.fastreading.R;
import com.android.fitz.fastreading.constant.AppConfig;
import com.android.fitz.fastreading.constant.Constant;
import com.android.fitz.fastreading.injector.component.DaggerFragmentComponent;
import com.android.fitz.fastreading.injector.component.FragmentComponent;
import com.android.fitz.fastreading.injector.module.FragmentModule;
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
 * Created by jact on 2016/2/16.
 */
public abstract class BaseFragment<T extends BasePresenter> extends BaseAppCompatFragment implements IView {
    protected MaterialDialog dialog;
    protected FragmentComponent mFragmentComponent;
    protected Toolbar mToolbar;
    protected EmptyView mEmptyView;
    protected int page = 1;
    protected final static int PAGE_SIZE = 5;

    @Inject
    public T mPresenter;

    public View setContentView(LayoutInflater inflater, ViewGroup container, int layoutResID) {
        View rootView = inflater.inflate(layoutResID, container, false);
        ButterKnife.bind(this, rootView);
        if (null != getLoadingTargetView()) {

        }
        mToolbar = ButterKnife.findById(rootView, R.id.common_toolbar);
        if (null != mToolbar) {
            mToolbar.setTitle("");
            ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mEmptyView = ButterKnife.findById(rootView, R.id.empty);
        return rootView;
    }

    @Override
    protected void attachView() {
        if (mPresenter != null)
            mPresenter.attachView(this);
    }

    protected FragmentComponent getFragmentComponent() {
        return DaggerFragmentComponent.builder()
                .fragmentModule(new FragmentModule(this))
                .applicationComponent(((AppApplication) getActivity().getApplication()).getApplicationComponent())
                .build();
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    protected int getScreenHeight() {
        return getActivity().findViewById(android.R.id.content).getHeight();
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

    @Override
    public <T> LifecycleTransformer<T> composeLifeCycle() {
        return this.<T >bindUntilEvent(FragmentEvent.DESTROY);
    }

    /**
     * startActivity
     *
     * @param clazz
     */
    protected void readyGo(Class<?> clazz) {
        Intent intent = new Intent(getActivity(), clazz);
        startActivity(intent);
    }

    /**
     * startActivity with bundle
     *
     * @param clazz
     * @param bundle
     */
    protected void readyGo(Class<?> clazz, Bundle bundle) {
        Intent intent = new Intent(getActivity(), clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * startActivity then finish
     *
     * @param clazz
     */
    protected void readyGoThenKill(Class<?> clazz) {
        Intent intent = new Intent(getActivity(), clazz);
        startActivity(intent);
        getActivity().finish();
    }

    /**
     * startActivity with bundle then finish
     *
     * @param clazz
     * @param bundle
     */
    protected void readyGoThenKill(Class<?> clazz, Bundle bundle) {
        Intent intent = new Intent(getActivity(), clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
        getActivity().finish();
    }

    /**
     * startActivityForResult
     *
     * @param clazz
     * @param requestCode
     */
    protected void readyGoForResult(Class<?> clazz, int requestCode) {
        Intent intent = new Intent(getActivity(), clazz);
        startActivityForResult(intent, requestCode);
    }

    /**
     * startActivityForResult with bundle
     *
     * @param clazz
     * @param requestCode
     * @param bundle
     */
    protected void readyGoForResult(Class<?> clazz, int requestCode, Bundle bundle) {
        Intent intent = new Intent(getActivity(), clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    @Override
    protected boolean toggleOverridePendingTransition() {
        return false;
    }

    @Override
    protected TransitionMode getOverridePendingTransitionMode() {
        return null;
    }

    @Override
    protected boolean isBindEventBus() {
        return false;
    }

    @Override
    protected void onNetworkConnected(NetUtils.NetType type) {

    }

    @Override
    protected void onNetworkDisConnected() {

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
        if (mEmptyView != null) {
            mEmptyView.showEmpty();
        }
    }

    @Override
    public void showEmpty(String msg) {
        if (mEmptyView != null) {
            mEmptyView.showEmpty();
        }
    }

    @Override
    public void showNetError() {
        if (mEmptyView != null) {
            mEmptyView.showError("");
        }
    }

    @Override
    public void showLoading() {
        if (mEmptyView != null) {
            mEmptyView.showLoading();
        }
    }

    @Override
    public void hide() {
        if (mEmptyView != null) {
            mEmptyView.hide();
        }
    }

    @Override
    public void showMsg(String msg) {
        ToastUtils.LongToast(getActivity().getApplicationContext(), msg);
        //MyToast.ShortToast(getActivity().getApplicationContext(), msg);
    }

    @Override
    public void showProgress() {
        showDialog(getString(R.string.loading_default_text));
    }

    @Override
    public void showDialog(String content) {
        if (dialog == null) {
            dialog = new MaterialDialog.Builder(getActivity())
                    .progress(true, 0)
                    .content(content)
                    .canceledOnTouchOutside(false)
                    .build();
            dialog.show();
        }
    }

    @Override
    public void hideProgress() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @Override
    public void onComplete() {
        hideProgress();
    }

    @Override
    public void onTaskFinish() {

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
        showMsg(msg);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        /*RefWatcher refWatcher = ((AppApplication)getActivity().getApplication()).getRefWatcher(getActivity());
        refWatcher.watch(this);*/
    }

}
