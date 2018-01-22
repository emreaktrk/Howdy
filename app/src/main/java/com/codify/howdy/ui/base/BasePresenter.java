package com.codify.howdy.ui.base;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.view.View;

import java.util.ArrayList;

import io.reactivex.disposables.Disposable;

public abstract class BasePresenter<V extends MvpView> implements MvpPresenter<V> {

    protected ArrayList<Disposable> mDisposables;
    private V mView;
    private View mRoot;

    @Override
    public void attachView(V view, View root) {
        mView = view;
        mRoot = root;

        mDisposables = new ArrayList<>();
    }

    @Override
    public void detachView() {
        mView = null;
        mRoot = null;

        for (Disposable disposable : mDisposables) {
            disposable.dispose();
        }
    }

    private boolean isViewAttached() {
        return mView != null;
    }

    private void assertViewAttached() {
        if (!isViewAttached()) {
            throw new ViewNotAttachedException();
        }
    }

    protected final <T extends View> T findViewById(@IdRes int id) {
        assertViewAttached();

        return mRoot.findViewById(id);
    }

    protected final <T extends View> T findViewById(@IdRes int id, Class<? extends T> clazz) {
        assertViewAttached();

        return clazz.cast(mRoot.findViewById(id));
    }

    protected final @NonNull Context getContext() {
        assertViewAttached();

        return mRoot.getContext();
    }

    protected static class ViewNotAttachedException extends RuntimeException {
        ViewNotAttachedException() {
            super("Make sure attach view first.");
        }
    }
}
