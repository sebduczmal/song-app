package com.sebduczmal.songapp;


import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.lang.reflect.Proxy;

import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

public abstract class BasePresenter<View> {

    protected final CompositeDisposable disposables = new CompositeDisposable();

    @Nullable private View view;

    protected abstract Class viewClass();

    @CallSuper
    public void attachView(@NonNull View view) {
        this.view = view;
    }

    @NonNull
    protected View view() {
        return view == null ? emptyView() : view;
    }

    private View emptyView() {
        final Class viewClass = viewClass();
        return (View) Proxy.newProxyInstance(viewClass.getClassLoader(),
                new Class[]{viewClass}, (proxy, method, args) -> {
                    Timber.d("Empty view instance");
                    return null;
                });
    }

    @CallSuper
    public void detachView() {
        disposables.clear();
        view = null;
    }
}
