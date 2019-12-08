package com.android.library.base;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.exceptions.CompositeException;

/**
 * Created by fitz on 2016/12/24.
 */

public abstract class BaseListSubscriber<T, S extends IView> implements Subscriber<T>{

    S s;
    public boolean isLoad = false;

    @Override
    public void onSubscribe(Subscription s) {
        s.request(Long.MAX_VALUE);
    }

    public BaseListSubscriber(S s){
        this.s = s;
    }

    public BaseListSubscriber(S s, boolean isLoad){
        this.s = s;
        this.isLoad = isLoad;
    }

    @Override
    public void onComplete() {
        s.onComplete();
        onCompleted();
    }

    @Override
    public void onError(Throwable ex) {
        try {
            if (ex instanceof Exception) {
                if (ex instanceof UnknownHostException || ex instanceof ConnectException) {
                    if(isLoad)
                        s.showNetError();
                }
                if(ex instanceof CompositeException){
                    Throwable e = ((CompositeException) ex).getExceptions().get(0);
                    if (e instanceof UnknownHostException || e instanceof ConnectException) {
                        if(isLoad)
                            s.showNetError();
                    }
                }
            }
        } catch (Exception e) {

        }
        if(ex instanceof CompositeException) {
            Throwable e = ((CompositeException) ex).getExceptions().get(0);
            s.showException(e);
        }else {
            s.showException(ex);
        }
        s.onComplete();
    }

    public abstract void onCompleted();
}
